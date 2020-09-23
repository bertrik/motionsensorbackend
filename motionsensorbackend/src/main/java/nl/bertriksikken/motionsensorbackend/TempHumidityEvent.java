package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "datetime", "seqnr", "temperature", "humidity", "battery"})
public final class TempHumidityEvent extends BaseEvent {

    @JsonProperty("temperature")
    private final double temperature;
    @JsonProperty("humidity")
    private final int humidity;
    @JsonProperty("battery")
    private double voltage;

    public TempHumidityEvent(Instant instant, int sequenceNr, double temperature, int humidity, double voltage) {
        super("TBHV", instant, sequenceNr);
        this.temperature = temperature;
        this.humidity = humidity;
        this.voltage = voltage;
    }
    
}
