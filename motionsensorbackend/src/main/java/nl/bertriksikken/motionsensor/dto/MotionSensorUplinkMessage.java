package nl.bertriksikken.motionsensor.dto;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of TBMS100 sensor data, as uploaded over LoRaWAN.
 */
public final class MotionSensorUplinkMessage {

    public static final int PORT = 102;

    @JsonProperty("status")
    private boolean occupied;
    @JsonProperty("battery")
    private double voltage;
    @JsonProperty("temp")
    private int temperature;
    @JsonProperty("time")
    private int time;
    @JsonProperty("count")
    private int count;

    MotionSensorUplinkMessage(boolean occupied, double voltage, int temperature, int time, int count) {
        this.occupied = occupied;
        this.voltage = voltage;
        this.temperature = temperature;
        this.time = time;
        this.count = count;
    }

    public static MotionSensorUplinkMessage decode(byte[] data) throws DecodeException {
        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        try {
            boolean occupied = (bb.get() != 0);
            double voltage = (25.0 + (bb.get() & 0x0F)) / 10.0;
            int temperature = bb.get() - 32;
            int time = bb.getShort();
            int count = bb.get() & 0xFF;
            count += (bb.get() & 0xFF) << 8;
            count += (bb.get() & 0xFF) << 16;
            return new MotionSensorUplinkMessage(occupied, voltage, temperature, time, count);
        } catch (BufferUnderflowException e) {
            throw new DecodeException(e);
        }
    }

    public boolean isOccupied() {
        return occupied;
    }

    public double getVoltage() {
        return voltage;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }

    public String toString() {
        return String.format(Locale.ROOT, "{occupied=%s,voltage=%f,temperature=%d,count=%d,time=%d}", occupied, voltage,
                temperature, count, time);
    }

}
