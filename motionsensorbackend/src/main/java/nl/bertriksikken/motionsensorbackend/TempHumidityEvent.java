package nl.bertriksikken.motionsensorbackend;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "date", "time", "fcnt", "rssi", "snr", "sf", "humidity", "temperature", "battery" })
public final class TempHumidityEvent extends BaseEvent {

    @JsonProperty("humidity")
    private final int humidity;
    @JsonProperty("temperature")
    private final double temperature;
    @JsonProperty("battery")
    private double battery;

    public TempHumidityEvent(LoraParams loraParams, int humidity, double temperature, double battery) {
        super("TBHV", loraParams);
        this.humidity = humidity;
        this.temperature = temperature;
        this.battery = battery;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{date=%s,time=%s,fnct=%d,humidity=%d,temp=%.0f,batt=%.1f}",
                getDate(), getTime(), getFcnt(), humidity, temperature, battery);
    }

    public int getHumidity() {
        return humidity;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getBattery() {
        return battery;
    }

}
