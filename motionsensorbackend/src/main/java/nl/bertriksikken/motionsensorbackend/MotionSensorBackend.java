package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.time.Instant;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import nl.bertriksikken.ttn.MqttListener;
import nl.bertriksikken.ttn.TtnConfig;
import nl.bertriksikken.ttn.dto.TtnDownlinkMessage;
import nl.bertriksikken.ttn.dto.TtnUplinkMessage;

public final class MotionSensorBackend {

    private static final Logger LOG = LoggerFactory.getLogger(MotionSensorBackend.class);
    private static final String CONFIG_FILE = "motionsensorbackend.yaml";

    private final MqttListener mqttListener;

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
    }

    private void messageReceived(MqttListener listener, String topic, TtnUplinkMessage uplink) {
        LOG.info("Received: '{}'", uplink);

        // decode JSON
        try {
            byte[] payload = uplink.getRawPayload();
            if ((payload == null) || payload.length < 4) {
                LOG.warn("payload empty or too small");
            } else {
                ByteBuffer bb = ByteBuffer.wrap(payload).order(ByteOrder.BIG_ENDIAN);
                long deviceTime = bb.getInt() & 0xFFFFFFFFL;
                long actual = Instant.now().getEpochSecond();
                long timeDiff = (actual - deviceTime);
                LOG.info("Received time: {}, Actual time: {}, difference {}", deviceTime, actual, timeDiff);
                if (Math.abs(timeDiff) > 30) {
                    // send a downlink to correct the offset
                    ByteBuffer dlPayload = ByteBuffer.allocate(8).order(ByteOrder.BIG_ENDIAN);
                    dlPayload.putInt((int) timeDiff);
                    dlPayload.putInt(uplink.getCounter());
                    TtnDownlinkMessage downlink = new TtnDownlinkMessage(uplink.getPort(), false, dlPayload.array());
                    listener.sendDownlink(uplink.getAppId(), uplink.getDevId(), downlink);
                }
            }
        } catch (MqttException e) {
            LOG.warn("Caught MqttException: '{}'", e.getMessage());
        } catch (JsonProcessingException e) {
            LOG.warn("Caught JsonProcessingException: '{}'", e.getMessage());
        }

    }

    /**
     * Starts the application.
     * 
     * @throws MqttException in case of a problem starting MQTT client
     */
    private void start() throws MqttException {
        LOG.info("Starting LoraLuftdatenForwarder application");

        // start sub-modules
        mqttListener.start();

        LOG.info("Started LoraLuftdatenForwarder application");
    }

    /**
     * Stops the application.
     * 
     * @throws MqttException
     */
    private void stop() {
        LOG.info("Stopping LoraLuftdatenForwarder application");

        mqttListener.stop();

        LOG.info("Stopped LoraLuftdatenForwarder application");
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
