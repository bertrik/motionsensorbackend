package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import nl.bertriksikken.motionsensor.dto.DecodeException;
import nl.bertriksikken.motionsensor.dto.HumiditySensorUplinkMessage;
import nl.bertriksikken.motionsensor.dto.MotionSensorUplinkMessage;
import nl.bertriksikken.ttn.MqttListener;
import nl.bertriksikken.ttn.TtnConfig;
import nl.bertriksikken.ttn.dto.TtnUplinkMessage;

public final class MotionSensorBackend {

    private static final Logger LOG = LoggerFactory.getLogger(MotionSensorBackend.class);
    private static final String CONFIG_FILE = "motionsensorbackend.yaml";

    private final MqttListener mqttListener;
    private final MotionEventWriter csvWriter;

    public static void main(String[] args) throws IOException, MqttException {
        PropertyConfigurator.configure("log4j.properties");

        MotionSensorBackendConfig config = readConfig(new File(CONFIG_FILE));
        MotionSensorBackend app = new MotionSensorBackend(config);
        app.start();
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }

    MotionSensorBackend(MotionSensorBackendConfig config) {
        TtnConfig ttnAppConfig = config.getTtnConfig();
        LOG.info("Adding MQTT listener for TTN application '{}'", ttnAppConfig.getName());

        File storageFolder = config.getStorageFolder();
        mqttListener = new MqttListener(ttnAppConfig.getUrl(), ttnAppConfig.getName(), ttnAppConfig.getKey(),
                storageFolder);
        mqttListener.setUplinkCallback(this::messageReceived);
        csvWriter = new MotionEventWriter(storageFolder);
    }

    // package-private for testing
    void messageReceived(String topic, TtnUplinkMessage uplink) {
        int port = uplink.getPort();
        LOG.info("Received on port {}: '{}'", port, uplink);

        // decode JSON
        try {
            switch (port) {
            case MotionSensorUplinkMessage.PORT:
                handleMotionSensor(uplink);
                break;
            case HumiditySensorUplinkMessage.PORT:
                handleHumiditySensor(uplink);
                break;
            default:
                LOG.warn("Unhandled message on port: {}", port);
                break;
            }
        } catch (DecodeException e) {
            LOG.warn("Caught DecodeException: '{}'", e.getMessage());
        } catch (IOException e) {
            LOG.warn("Caught IOException: '{}'", e.getMessage());
        }
    }

    private void handleMotionSensor(TtnUplinkMessage uplink) throws IOException, DecodeException {
        byte[] payload = uplink.getRawPayload();
        if (payload != null) {
            MotionSensorUplinkMessage message = MotionSensorUplinkMessage.decode(payload);
            MotionEvent event = new MotionEvent(uplink.getTime(), uplink.getCounter(), message.isOccupied(),
                    message.getCount(), message.getTime(), message.getTemperature(), message.getVoltage());
            csvWriter.write(uplink.getHardwareSerial(), event);
        }
    }

    private void handleHumiditySensor(TtnUplinkMessage uplink) throws DecodeException, IOException {
        byte[] payload = uplink.getRawPayload();
        if (payload != null) {
            HumiditySensorUplinkMessage message = HumiditySensorUplinkMessage.decode(payload);
            TempHumidityEvent event = new TempHumidityEvent(uplink.getTime(), uplink.getCounter(),
                    message.getHumidity(), message.getTemperature(), message.getVoltage());
            csvWriter.write(uplink.getHardwareSerial(), event);
        }
    }

    /**
     * Starts the application.
     * 
     * @throws MqttException in case of a problem starting MQTT client
     */
    private void start() throws MqttException {
        LOG.info("Starting MotionSensorBackend application");

        // start sub-modules
        mqttListener.start();

        LOG.info("Started MotionSensorBackend application");
    }

    /**
     * Stops the application.
     * 
     * @throws MqttException
     */
    private void stop() {
        LOG.info("Stopping MotionSensorBackend application");

        mqttListener.stop();

        LOG.info("Stopped MotionSensorBackend application");
    }

    private static MotionSensorBackendConfig readConfig(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try (FileInputStream fis = new FileInputStream(file)) {
            return mapper.readValue(fis, MotionSensorBackendConfig.class);
        } catch (IOException e) {
            LOG.warn("Failed to load config {}, writing defaults", file.getAbsoluteFile());
            MotionSensorBackendConfig config = new MotionSensorBackendConfig();
            mapper.writeValue(file, config);
            return config;
        }
    }

}
