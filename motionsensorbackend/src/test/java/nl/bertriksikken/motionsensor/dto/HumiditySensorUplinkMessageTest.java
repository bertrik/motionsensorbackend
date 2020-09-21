package nl.bertriksikken.motionsensor.dto;

import org.junit.Assert;
import org.junit.Test;

public final class HumiditySensorUplinkMessageTest {

    /**
     * Verifies decoding of a raw message into individual fields.
     */
    @Test
    public void testDecode() throws DecodeException {
        byte[] data = new byte[] {0x08, 0x00, 50, 75, (byte)0xFF, (byte)0xFF, (byte)0xFF, (byte)0xFF};
        HumiditySensorUplinkMessage message = HumiditySensorUplinkMessage.decode(data);
        
        Assert.assertEquals(18, message.getTemperature());
        Assert.assertEquals(75, message.getHumidity());

        Assert.assertNotNull(message.toString());
    }

}
