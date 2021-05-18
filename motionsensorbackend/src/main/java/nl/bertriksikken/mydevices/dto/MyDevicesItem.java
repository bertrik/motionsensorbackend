package nl.bertriksikken.mydevices.dto;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class MyDevicesItem {
    
    @JsonProperty("channel")
    private final int channel;
    
    @JsonProperty("value")
    private final String value;
    
    @JsonProperty("type")
    private final String type;
    
    @JsonProperty("unit")
    private final String unit;
    
    public MyDevicesItem(int channel, String type, double value, String unit) {
        this(channel, type, String.format(Locale.ROOT, "%.1f", value), unit);
    }

    public MyDevicesItem(int channel, String type, String value, String unit) {
        this.channel = channel;
        this.value = value;
        this.type = type;
        this.unit = unit;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{%s[%d]=%s %s}", type, channel, value, unit);
    }
    
}
