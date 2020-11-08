package nl.bertriksikken.lorawan;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class MacPayload {
    
    private final int devAddr;
    private final int fctrl;
    private final int fcnt;
    private final byte[] fopts;

    public MacPayload(int devAddr, int fctrl, int fcnt, byte[] fopts) {
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
        int devAddr = bb.getInt() & 0xFFFFFFFF;
        int fctrl = bb.get() & 0xFF;
        int fcnt = bb.getShort() & 0xFFFF;
        byte[] fopts = new byte[data.length - 7];
        bb.get(fopts);
        return new MacPayload(devAddr, fctrl, fcnt, fopts);
    }

    public int getDevAddr() {
        return devAddr;
    }

    public int getFctrl() {
        return fctrl;
    }

    public int getFcnt() {
        return fcnt;
    }

    public byte[] getFOpts() {
        return fopts.clone();
    }
    
}
