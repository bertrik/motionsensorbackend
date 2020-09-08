package nl.bertriksikken.ttn.dto;

import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

public final class TtnDownlinkMessage {

    @JsonProperty("port")
    private int port;

    @JsonProperty("confirmed")
    private boolean confirmed;

    @JsonProperty("payload_raw")
    private byte[] rawPayload = new byte[0];

    public TtnDownlinkMessage(int port, boolean confirmed, byte[] rawPayload) {
        this.port = port;
        this.confirmed = confirmed;
        this.rawPayload = rawPayload.clone();
    }

    @Override
    public String toString() {
        return String.format(Locale.ROOT, "[port=%d,confirmed=%s,payload=%s]", port, confirmed, rawPayload);
    }
    
}
