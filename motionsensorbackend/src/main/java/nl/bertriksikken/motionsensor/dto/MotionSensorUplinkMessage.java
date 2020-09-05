package nl.bertriksikken.motionsensor.dto;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Data object containing the fields as parsed from the TTN uplink message.
 */
@JsonPropertyOrder({"status", "battery", "voltage", "temp", "time", "count"})
public final class MotionSensorUplinkMessage {
    
    @JsonProperty("status")
    private boolean occupied;
    @JsonProperty("battery")
    private double voltage;
    @JsonProperty("temp")
    private double temperature;
    @JsonProperty("time")
    private int time;
    @JsonProperty("count")
    private int count;

    MotionSensorUplinkMessage(boolean occupied, double voltage, double temperature, int time, int count) {
        this.occupied = occupied;
        this.voltage = voltage;
        this.temperature = temperature;
        this.time = time;
        this.count = count;
    }

    public static MotionSensorUplinkMessage decode(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        boolean occupied = (bb.get() != 0);
        double voltage = (25.0 + (bb.get() & 0x0F)) / 10.0;
        double temperature = bb.get() - 32;
        int time = bb.getShort();
        int count = bb.get() & 0xFF;
        count += (bb.get() & 0xFF) << 8;
        count += (bb.get() & 0xFF) << 16;
        return new MotionSensorUplinkMessage(occupied, voltage, temperature, time, count);
    }

    public boolean isOccupied() {
        return occupied;
    }

    public double getVoltage() {
        return voltage;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getTime() {
        return time;
    }

    public int getCount() {
        return count;
    }
    
}
