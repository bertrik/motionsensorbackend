package nl.bertriksikken.motionsensorbackend;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseEvent {

    @JsonIgnore
    private final String eventType;

    @JsonProperty("date")
    private final LocalDate date;

    @JsonProperty("time")
    private final LocalTime time;

    @JsonProperty("fcnt")
    private final int fcnt;

    @JsonProperty("rssi")
    private double rssi;

    @JsonProperty("snr")
    private double snr;

    @JsonProperty("sf")
    private int sf;

    protected BaseEvent(String eventType, LoraParams loraParams) {
        this.eventType = eventType;
        this.date = LocalDate.ofInstant(loraParams.time, ZoneOffset.UTC);
        this.time = LocalTime.ofInstant(loraParams.time, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        this.fcnt = loraParams.fcnt;
        this.rssi = loraParams.rssi;
        this.snr = loraParams.snr;
        this.sf = loraParams.sf;
    }

    public String getEventType() {
        return eventType;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public int getFcnt() {
        return fcnt;
    }
    
    public double getRssi() {
        return rssi;
    }
    
    public double getSnr() {
        return snr;
    }
    
    public int getSf() {
        return sf;
    }
    
}
