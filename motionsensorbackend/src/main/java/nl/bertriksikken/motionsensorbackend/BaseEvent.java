package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
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

    @JsonProperty("seqnr")
    private final int sequenceNr;

    protected BaseEvent(String eventType, Instant instant, int sequenceNr) {
        this.eventType = eventType;
        this.date = LocalDate.ofInstant(instant, ZoneOffset.UTC);
        this.time = LocalTime.ofInstant(instant, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        this.sequenceNr = sequenceNr;
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

    public int getSequenceNr() {
        return sequenceNr;
    }

}
