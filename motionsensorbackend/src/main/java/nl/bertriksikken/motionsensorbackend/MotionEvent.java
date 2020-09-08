package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class MotionEvent {

    @JsonProperty("time")
    private Instant time;

    @JsonProperty("occupied")
    private boolean occupied;

    @JsonProperty("battery")
    private double voltage;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("count")
    private int count;

    public MotionEvent(Instant time, boolean occupied, double voltage, double temperature, int count) {
        this.time = time;
        this.occupied = occupied;
        this.voltage = voltage;
        this.temperature = temperature;
        this.count = count;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{time=%s,occupied=%s,voltage=%.2f,temperature=%.0f,count=%d}", time, occupied,
                voltage, temperature, count);
    }

}
