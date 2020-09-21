package nl.bertriksikken.motionsensor.dto;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Representation of TBHH100 sensor data, as uploaded over LoRaWAN.
 */
public final class HumiditySensorUplinkMessage {

    public static final int PORT = 103;

    @JsonProperty("status")
    private int status;
    @JsonProperty("battery")
    private double voltage;

    @JsonProperty("temp")
    private int temperature;
    @JsonProperty("rh")
    private int humidity;
    @JsonProperty("co2")
    private int co2;
    @JsonProperty("voc")
    private int voc;

    public HumiditySensorUplinkMessage(int status, double voltage, int temperature, int humidity, int co2, int voc) {
        this.status = status;
        this.voltage = voltage;
        this.temperature = temperature;
        this.humidity = humidity;
        this.co2 = co2;
        this.voc = voc;
    }

    public static HumiditySensorUplinkMessage decode(byte[] data) throws DecodeException {
        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        try {
            int status = bb.get() & 0xFF;
            double voltage = (25.0 + (bb.get() & 0x0F)) / 10.0;
            int temperature = (bb.get() & 0x7F) - 32;
            int humidity = bb.get() & 0x7F;
            int co2 = bb.getShort() & 0xFFFF;
            int voc = bb.getShort() & 0xFFFF;
            return new HumiditySensorUplinkMessage(status, voltage, temperature, humidity, co2, voc);
        } catch (BufferUnderflowException e) {
            throw new DecodeException(e);
        }
    }

    public int getStatus() {
        return status;
    }

    public double getVoltage() {
        return voltage;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getCo2() {
        return co2;
    }

    public int getVoc() {
        return voc;
    }
    
    public String toString() {
        return String.format(Locale.ROOT, "{status=%s,voltage=%f,temperature=%d,humidity=%d}", status, voltage,
                temperature, humidity);
    }

}
