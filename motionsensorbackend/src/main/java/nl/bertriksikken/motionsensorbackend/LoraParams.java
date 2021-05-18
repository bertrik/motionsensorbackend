package nl.bertriksikken.motionsensorbackend;

import java.time.Instant;

public final class LoraParams {

    final Instant time;
    final int fcnt;
    final double rssi;
    final double snr;
    final int sf;

    public LoraParams(Instant time, int fcnt, double rssi, double snr, int sf) {
        this.time = time;
        this.fcnt = fcnt;
        this.rssi = rssi;
        this.snr = snr;
        this.sf = sf;
    }

}
