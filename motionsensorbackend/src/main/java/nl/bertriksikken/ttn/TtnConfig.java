package nl.bertriksikken.ttn;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TtnConfig {

    @JsonProperty("mqtt_url")
    private String url = "tcp://eu.thethings.network";
    
    @JsonProperty("name")
    private String name = "vleermuispir";

    @JsonProperty("key")
    private String key = "ttn-account-v2.XF13zuLFPowflh4RIyfKXPcl0F9fmYfYr28vIXJXcPs";

    public String getUrl() {
        return url;
    }
    
    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }
    
}
