package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import nl.bertriksikken.motionsensor.dto.DecodeException;
import nl.bertriksikken.motionsensor.dto.HumiditySensorUplinkMessage;
import nl.bertriksikken.motionsensor.dto.MotionSensorUplinkMessage;
import nl.bertriksikken.mydevices.IMyDevicesRestApi;
import nl.bertriksikken.mydevices.MyDevicesConfig;
import nl.bertriksikken.mydevices.MyDevicesHttpUploader;
import nl.bertriksikken.mydevices.dto.MyDevicesMessage;
import nl.bertriksikken.ttn.MqttListener;
import nl.bertriksikken.ttn.TtnConfig;
import nl.bertriksikken.ttn.TtnUplinkMessage;
import nl.bertriksikken.ttnv3.enddevice.EndDevice;
import nl.bertriksikken.ttnv3.enddevice.EndDeviceRegistry;
import nl.bertriksikken.ttnv3.enddevice.IEndDeviceRegistryRestApi;

public final class MotionSensorBackend {

    private static final Logger LOG = LoggerFactory.getLogger(MotionSensorBackend.class);
    private static final String CONFIG_FILE = "motionsensorbackend.yaml";

    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    private final MqttListener mqttListener;
    private final MotionEventWriter csvWriter;
    private final MyDevicesHttpUploader myDevicesUploader;
    private final EndDeviceRegistry endDeviceRegistry;

    public static void main(String[] args) throws IOException, MqttException {
        PropertyConfigurator.configure("log4j.properties");

        MotionSensorBackendConfig config = readConfig(new File(CONFIG_FILE));
        MotionSensorBackend app = new MotionSensorBackend(config);
        app.start();
        Runtime.getRuntime().addShutdownHook(new Thread(app::stop));
    }

    MotionSensorBackend(MotionSensorBackendConfig config) {
        TtnConfig ttnConfig = config.getTtnConfig();
        LOG.info("Adding MQTT listener for TTN application '{}'", ttnConfig.getName());

        File storageFolder = config.getStorageFolder();
        mqttListener = new MqttListener(ttnConfig.getMqttUrl(), ttnConfig.getName(), ttnConfig.getKey(), storageFolder);
        mqttListener.setUplinkCallback(this::messageReceived);
        csvWriter = new MotionEventWriter(storageFolder);

        MyDevicesConfig myDevicesConfig = config.getMyDevicesConfig();
        IMyDevicesRestApi myDevicesRestApi = MyDevicesHttpUploader.newRestClient(myDevicesConfig.getUrl(),
                Duration.ofSeconds(myDevicesConfig.getTimeoutSec()));
        myDevicesUploader = new MyDevicesHttpUploader(myDevicesRestApi);

        IEndDeviceRegistryRestApi restApi = EndDeviceRegistry.newRestClient(ttnConfig.getIdentityServerUrl(),
                Duration.ofSeconds(ttnConfig.getIdentityServerTimeout()));
        String appName = ttnConfig.getName();
        endDeviceRegistry = new EndDeviceRegistry(restApi, appName, ttnConfig.getKey());
    }

    // package-private for testing
    void messageReceived(TtnUplinkMessage uplink) {
        LOG.info("Received '{}'", uplink);

        // decode JSON
        int port = uplink.getPort();
        try {
            switch (port) {
            case MotionSensorUplinkMessage.PORT:
                handleMotionSensor(uplink);
                break;
            case HumiditySensorUplinkMessage.PORT:
                handleHumiditySensor(uplink);
                break;
            default:
                LOG.warn("Unhandled message");
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
            LoraParams loraParams = new LoraParams(uplink.getTime(), uplink.getCounter(), uplink.getRSSI(),
                    uplink.getSNR(), uplink.getSF());
            MotionSensorUplinkMessage message = MotionSensorUplinkMessage.decode(payload);
            MotionEvent event = new MotionEvent(loraParams, message.isOccupied(), message.getCount(), message.getTime(),
                    message.getTemperature(), message.getVoltage());

            // write to CSV
            csvWriter.write(uplink.getDeviceEui(), event);

            // schedule for upload to mydevices
            MyDevicesMessage myDevicesMessage = MyDevicesMessage.fromMotionEvent(event);
            myDevicesUploader.scheduleUpload(uplink.getDeviceEui(), myDevicesMessage);
        }
    }

    private void handleHumiditySensor(TtnUplinkMessage uplink) throws DecodeException, IOException {
        byte[] payload = uplink.getRawPayload();
        if (payload != null) {
            LoraParams loraParams = new LoraParams(uplink.getTime(), uplink.getCounter(), uplink.getRSSI(),
                    uplink.getSNR(), uplink.getSF());
            HumiditySensorUplinkMessage message = HumiditySensorUplinkMessage.decode(payload);
            TempHumidityEvent event = new TempHumidityEvent(loraParams, message.getHumidity(), message.getTemperature(),
                    message.getVoltage());

            // write to CSV
            csvWriter.write(uplink.getDeviceEui(), event);

            // schedule for upload to mydevices
            MyDevicesMessage myDevicesMessage = MyDevicesMessage.fromHumidityEvent(event);
            myDevicesUploader.scheduleUpload(uplink.getDeviceEui(), myDevicesMessage);
        }
    }

    /**
     * Starts the application.
     * 
     * @throws MqttException in case of a problem starting MQTT client
     */
    private void start() throws MqttException {
        LOG.info("Starting MotionSensorBackend application");

        // schedule task to update attributes
        executor.scheduleAtFixedRate(this::updateAttributes, 0, 60, TimeUnit.MINUTES);

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
        executor.shutdown();

        LOG.info("Stopped MotionSensorBackend application");
    }

    // retrieves application attributes and notifies each interested component
    private void updateAttributes() {
        // fetch all attributes
        Map<String, AttributeMap> attributes = new HashMap<>();
        LOG.info("Fetching TTNv3 application attributes");
        try {
            List<EndDevice> devices = endDeviceRegistry.listEndDevices();
            devices.forEach(d -> attributes.put(d.getIds().getDevEui(), new AttributeMap(d.getAttributes())));
        } catch (IOException e) {
            LOG.warn("Error getting opensense map ids for {}", e.getMessage());
        }
        LOG.info("Fetching TTNv3 application attributes done");

        // notify all uploaders
        myDevicesUploader.processAttributes(attributes);
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
