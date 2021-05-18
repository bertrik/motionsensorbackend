package nl.bertriksikken.ttn;

import java.time.Instant;
import java.util.Arrays;
import java.util.Locale;

/**
 * Common class containing only the fields from the TTN upload message relevant
 * to us.
 */
public final class TtnUplinkMessage {

    private final Instant time;
    private final int counter;
    private final String deviceEui;
    private final byte[] rawPayload;
    private final int port;
    private double rssi = Double.NaN;
    private double snr = Double.NaN;
    private int sf = 0;

    public TtnUplinkMessage(Instant time, int counter, String deviceEui, byte[] rawPayload, int port) {
        this.time = time;
        this.counter = counter;
        this.deviceEui = deviceEui;
        this.rawPayload = rawPayload.clone();
        this.port = port;
    }
    
    public void setRadioParams(double rssi, double snr, int sf) {
        this.rssi = rssi;
        this.snr = snr;
        this.sf = sf;
    }

    public Instant getTime() {
        return time;
    }
    
    public int getCounter() {
        return counter;
    }
    
    public String getDeviceEui() {
        return deviceEui;
    }

    public byte[] getRawPayload() {
        return rawPayload.clone();
    }

    public int getPort() {
        return port;
    }

    public double getRSSI() {
        return rssi;
    }
    
    public double getSNR() {
        return snr;
    }

    public int getSF() {
        return sf;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "EUI %s, data %s, port %d, SF %d", deviceEui, Arrays.toString(rawPayload),
                port, sf);
    }
    
}
