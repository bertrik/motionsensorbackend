package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseEvent {
    
    @JsonIgnore
    private final String eventType;
    
    @JsonProperty("datetime")
    private final OffsetDateTime dateTime;

    @JsonProperty("seqnr")
    private final int sequenceNr;

    protected BaseEvent(String eventType, Instant instant, int sequenceNr) {
        this.eventType = eventType;
        this.dateTime = OffsetDateTime.ofInstant(instant, ZoneOffset.UTC).truncatedTo(ChronoUnit.SECONDS);
        this.sequenceNr = sequenceNr;
    }

    public String getEventType() {
        return eventType;
    }

    public OffsetDateTime getDateTime() {
        return dateTime;
    }

    public int getSequenceNr() {
        return sequenceNr;
    }
    
}
