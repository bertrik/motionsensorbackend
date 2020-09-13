package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

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

    private MotionSensorBackend(MotionSensorBackendConfig config) {
        TtnConfig ttnAppConfig = config.getTtnConfig();
        LOG.info("Adding MQTT listener for TTN application '{}'", ttnAppConfig.getName());

        mqttListener = new MqttListener(ttnAppConfig.getUrl(), ttnAppConfig.getName(), ttnAppConfig.getKey());
        mqttListener.setUplinkCallback((topic, message) -> messageReceived(mqttListener, topic, message));

        csvWriter = new MotionEventWriter(config.getStorageFolder());
    }

    private void messageReceived(MqttListener listener, String topic, TtnUplinkMessage uplink) {
        LOG.info("Received: '{}'", uplink);

        // decode JSON
        try {
            byte[] payload = uplink.getRawPayload();
            if (payload != null) {
                MotionSensorUplinkMessage message = MotionSensorUplinkMessage.decode(payload);
                Instant time = uplink.getTime().minusSeconds(60 * message.getTime()).truncatedTo(ChronoUnit.MINUTES);
                MotionEvent event = new MotionEvent(uplink.getCounter(), time, message.isOccupied(),
                        message.getVoltage(), message.getTemperature(), message.getCount());
                csvWriter.write(uplink.getHardwareSerial(), event);
            } else {
                LOG.warn("payload empty or too small");
            }
        } catch (Exception e) {
            LOG.warn("Caught Exception: '{}'", e.getMessage());
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
