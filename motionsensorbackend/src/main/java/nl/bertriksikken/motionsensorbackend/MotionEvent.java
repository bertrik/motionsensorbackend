package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "seqnr", "time", "occupied", "battery", "temperature", "count" })
public final class MotionEvent {

    @JsonProperty("seqnr")
    private int sequenceNr;

    @JsonProperty("time")
    private OffsetDateTime time;

    @JsonProperty("occupied")
    private boolean occupied;

    @JsonProperty("battery")
    private double voltage;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("count")
    private int count;

    public MotionEvent(int sequenceNr, Instant instant, boolean occupied, double voltage, double temperature,
            int count) {
        this.sequenceNr = sequenceNr;
        this.time = OffsetDateTime.ofInstant(instant, ZoneId.of("UTC")).truncatedTo(ChronoUnit.MINUTES);
        this.occupied = occupied;
        this.voltage = voltage;
        this.temperature = temperature;
        this.count = count;
    }

    public OffsetDateTime getTime() {
        return time;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{time=%s,occupied=%s,voltage=%.2f,temperature=%.0f,count=%d}", time,
                occupied, voltage, temperature, count);
    }

}
