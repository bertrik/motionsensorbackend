package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "date", "time", "seqnr", "humidity", "temperature", "battery" })
public final class TempHumidityEvent extends BaseEvent {

    @JsonProperty("humidity")
    private final int humidity;
    @JsonProperty("temperature")
    private final double temperature;
    @JsonProperty("battery")
    private double battery;

    public TempHumidityEvent(Instant instant, int sequenceNr, double temperature, int humidity, double battery) {
        super("TBHV", instant, sequenceNr);
        this.humidity = humidity;
        this.temperature = temperature;
        this.battery = battery;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{date=%s,time=%s,seqnr=%d,humidity=%d,temp=%.0f,batt=%.1f}",
                getDate(), getTime(), getSequenceNr(), humidity, temperature, battery);
    }

}
