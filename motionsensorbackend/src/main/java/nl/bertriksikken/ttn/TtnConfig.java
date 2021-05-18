package nl.bertriksikken.ttn;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TtnConfig {

    @JsonProperty("mqtt_url")
    private String mqttUrl = "tcp://eu1.cloud.thethings.network";

    @JsonProperty("identity_server_url")
    private String identityServerUrl = "https://eu1.cloud.thethings.network";

    @JsonProperty("identity_server_timeout")
    private int identityServerTimeout = 20;

    @JsonProperty("name")
    private String name = "vleermuispir";

    @JsonProperty("key")
    private String key = "NNSXS.M2OVRSGNZXVKOYKNF5L74D2UW5ZKFH7MHH664GA.34TWWXWGW35VCVAOWP4M4WIFVATPYDDUANWLK2ZMX2LXUQIDP6UA";

    public String getMqttUrl() {
        return mqttUrl;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getIdentityServerUrl() {
        return identityServerUrl;
    }

    public long getIdentityServerTimeout() {
        return identityServerTimeout;
    }

}
