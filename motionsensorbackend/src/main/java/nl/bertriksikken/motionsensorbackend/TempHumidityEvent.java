package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "date", "time", "seqnr", "humidity", "temperature", "battery"})
public final class TempHumidityEvent extends BaseEvent {

    @JsonProperty("humidity")
    private final int humidity;
    @JsonProperty("temperature")
    private final double temperature;
    @JsonProperty("battery")
    private double voltage;

    public TempHumidityEvent(Instant instant, int sequenceNr, double temperature, int humidity, double voltage) {
        super("TBHV", instant, sequenceNr);
        this.humidity = humidity;
        this.temperature = temperature;
        this.voltage = voltage;
    }
    
}
