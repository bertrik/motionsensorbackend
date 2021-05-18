package nl.bertriksikken.motionsensorbackend;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Representation of a motion event that can be serialized to CSV.
 */
@JsonPropertyOrder({ "date", "time", "fcnt", "rssi", "snr", "sf", "occupied", "count", "minutes", "temp", "battery" })
public final class MotionEvent extends BaseEvent {

    @JsonProperty("occupied")
    private int occupied;

    @JsonProperty("count")
    private int count;

    @JsonProperty("minutes")
    private int minutes;

    @JsonProperty("temp")
    private double temperature;

    @JsonProperty("battery")
    private double battery;

    public MotionEvent(LoraParams loraParams, boolean occupied, int count, int minutes, double temperature,
            double battery) {
        super("TBMS", loraParams);
        this.occupied = occupied ? 1 : 0;
        this.count = count;
        this.minutes = minutes;
        this.temperature = temperature;
        this.battery = battery;
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

    public double getBattery() {
        return battery;
    }

    public int getMinutes() {
        return minutes;
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{date=%s,time=%s,fcnt=%d,occ=%s,count=%d,minutes=%d,temp=%.0f,batt=%.1f}",
                getDate(), getTime(), getFcnt(), occupied, count, minutes, temperature, battery);
    }

}
