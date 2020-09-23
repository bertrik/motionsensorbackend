package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "datetime", "seqnr", "occupied", "battery", "temperature", "count", "lastevent" })
public final class MotionEvent extends BaseEvent {

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
        super("TBMS", instant, sequenceNr);
        this.occupied = occupied;
        this.voltage = voltage;
        this.temperature = temperature;
        this.count = count;
        this.lastEvent = OffsetDateTime.ofInstant(lastEvent, ZoneOffset.UTC).truncatedTo(ChronoUnit.MINUTES);
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
        return String.format(Locale.ROOT, "{time=%s,seq=%d,occupied=%s,voltage=%.2f,temperature=%.0f,count=%d}",
                getDateTime(), getSequenceNr(), occupied, voltage, temperature, count);
    }

}
