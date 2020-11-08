package nl.bertriksikken.lorawan;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class PhyPayload {
    
    private final int mhdr;
    private final byte[] macPayload;
    private final byte[] mic;

    PhyPayload(int mhdr, byte[] macPayload, byte[] mic) {
        this.mhdr = mhdr;
        this.macPayload = macPayload.clone();
        this.mic = mic.clone();
    }

    public static PhyPayload decode(byte[] data) {
        if (data.length < 5) {
            throw new IllegalArgumentException();
        }
        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        int mhdr = bb.get() & 0xFF;
        byte[] macPayload = new byte[data.length - 5];
        bb.get(macPayload);
        byte[] mic = new byte[4];
        bb.get(mic);
        return new PhyPayload(mhdr, macPayload, mic);
    }

    public int getMhdr() {
        return mhdr;
    }

    public byte[] getMacPayload() {
        return macPayload.clone();
    }

    public byte[] getMic() {
        return mic.clone();
    }
}
