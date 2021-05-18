package nl.bertriksikken.motionsensorbackend;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;

import nl.bertriksikken.mydevices.MyDevicesConfig;
import nl.bertriksikken.ttn.TtnConfig;

/**
 * Configuration class.
 */
@JsonAutoDetect(getterVisibility = Visibility.NONE)
public final class MotionSensorBackendConfig {

    @JsonProperty("ttn")
    private TtnConfig ttnConfig = new TtnConfig();

    @JsonProperty("mydevices")
    private MyDevicesConfig myDevicesConfig = new MyDevicesConfig();
    
    @JsonProperty("storage")
    private String storage = ".";

    public TtnConfig getTtnConfig() {
        return ttnConfig;
    }

    public MyDevicesConfig getMyDevicesConfig() {
        return myDevicesConfig;
    }
    
    public File getStorageFolder() {
        return new File(storage);
    }

}