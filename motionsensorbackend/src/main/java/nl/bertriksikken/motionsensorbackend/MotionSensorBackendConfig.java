package nl.bertriksikken.motionsensorbackend;

import com.fasterxml.jackson.annotation.JsonProperty;

import nl.bertriksikken.ttn.TtnConfig;

/**
 * Configuration class.
 */
public final class MotionSensorBackendConfig {

    @JsonProperty("ttn")
    private TtnConfig ttnConfig = new TtnConfig();

    public TtnConfig getTtnConfig() {
        return ttnConfig;
    }

}