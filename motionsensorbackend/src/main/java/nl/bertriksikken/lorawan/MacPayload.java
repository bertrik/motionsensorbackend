package nl.bertriksikken.lorawan;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class MacPayload {
    
    private final long devAddr;
    private final byte fctrl;
    private final int fcnt;
    private final byte[] fopts;

    public MacPayload(long devAddr, byte fctrl, int fcnt, byte[] fopts) {
        this.devAddr = devAddr;
        this.fctrl = fctrl;
        this.fcnt = fcnt;
        this.fopts = fopts.clone();
    }

    public static MacPayload decode(byte[] data) {
        if (data.length < 7) {
            throw new IllegalArgumentException();
        }
        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        long devAddr = bb.getInt() & 0xFFFFFFFFL;
        byte fctrl = (byte) (bb.get() & 0xFF);
        int fcnt = bb.getShort() & 0xFFFF;
        byte[] fopts = new byte[data.length - 7];
        bb.get(fopts);
        return new MacPayload(devAddr, fctrl, fcnt, fopts);
    }

    public long getDevAddr() {
        return devAddr;
    }

    public byte getFctrl() {
        return fctrl;
    }

    public int getFcnt() {
        return fcnt;
    }

    public byte[] getFOpts() {
        return fopts.clone();
    }
    
}
