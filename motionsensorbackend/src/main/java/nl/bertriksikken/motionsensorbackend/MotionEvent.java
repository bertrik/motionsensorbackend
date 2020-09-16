package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "datetime", "seqnr", "occupied", "battery", "temperature", "count", "lastevent" })
public final class MotionEvent {

    @JsonProperty("datetime")
    private OffsetDateTime dateTime;

    @JsonProperty("seqnr")
    private int sequenceNr;

    @JsonProperty("occupied")
    private boolean occupied;

    @JsonProperty("battery")
    private double voltage;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("count")
    private int count;

    @JsonProperty("lastevent")
    private OffsetDateTime lastEvent;

    public MotionEvent(Instant instant, int sequenceNr, boolean occupied, double voltage, double temperature, int count,
            Instant lastEvent) {
        this.dateTime = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        this.sequenceNr = sequenceNr;
        this.occupied = occupied;
        this.voltage = voltage;
        this.temperature = temperature;
        this.count = count;
        this.lastEvent = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
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

    public OffsetDateTime getLastEvent() {
        return lastEvent;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{time=%s,occupied=%s,voltage=%.2f,temperature=%.0f,count=%d}", dateTime,
                occupied, voltage, temperature, count);
    }

}
