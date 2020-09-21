package nl.bertriksikken.motionsensor.dto;

import org.junit.Assert;
import org.junit.Test;

public final class MotionSensorUplinkMessageTest {

    @Test
    public void testDecode() throws DecodeException {
        byte[] data = new byte[] { 0x00, (byte)0xFB, 0x36, 0x41, 0x00, (byte)0x8C, 0x07, 0x00};
        MotionSensorUplinkMessage message = MotionSensorUplinkMessage.decode(data);

        Assert.assertEquals(false, message.isOccupied());
        Assert.assertEquals(3.6, message.getVoltage(), 0.1);
        Assert.assertEquals(22.0, message.getTemperature(), 0.1);
        Assert.assertEquals(65, message.getTime());
        Assert.assertEquals(1932, message.getCount());
        
        Assert.assertNotNull(message.toString());
    }
    
}
