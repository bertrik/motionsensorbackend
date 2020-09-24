package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "date", "time", "seqnr", "occupied", "count", "minutes", "temperature", "battery"})
public final class MotionEvent extends BaseEvent {

    @JsonProperty("occupied")
    private int occupied;

    @JsonProperty("count")
    private int count;

    @JsonProperty("minutes")
    private int minutes;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("battery")
    private double voltage;

    public MotionEvent(Instant instant, int sequenceNr, boolean occupied, int count, int minutes, double temperature,
            double voltage) {
        super("TBMS", instant, sequenceNr);
        this.occupied = occupied ? 1 : 0;
        this.count = count;
        this.minutes = minutes;
        this.temperature = temperature;
        this.voltage = voltage;
    }

    public int getOccupied() {
        return occupied;
    }

    public int getCount() {
        return count;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getVoltage() {
        return voltage;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{date=%s,time=%s,seqnr=%d,occ=%s,count=%d,minutes=%d,temp=%.0f,batt=%.1f}",
                getDate(), getTime(), getSequenceNr(), occupied, count, minutes, temperature, voltage);
    }

}
