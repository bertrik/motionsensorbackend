package nl.bertriksikken.mydevices.dto;

import java.time.Instant;

import org.junit.Assert;
import org.junit.Test;

import nl.bertriksikken.motionsensorbackend.LoraParams;
import nl.bertriksikken.motionsensorbackend.MotionEvent;
import nl.bertriksikken.motionsensorbackend.TempHumidityEvent;

public final class MyDevicesMessageTest {

    @Test
    public void testMotionEvent() {
        LoraParams loraParams = new LoraParams(Instant.now(), 123, -50, 10, 7);
        MotionEvent event = new MotionEvent(loraParams, false, 456, 42, 21.3, 3.6);
        MyDevicesMessage message = MyDevicesMessage.fromMotionEvent(event);
        Assert.assertEquals(
                "[{motion[1]=0 d}, {counter[2]=456 null}, {temp[3]=21.3 c}, {voltage[4]=3.6 v}, {time[5]=42 min}]",
                message.toString());
    }

    @Test
    public void testTempHumidityEvent() {
        LoraParams loraParams = new LoraParams(Instant.now(), 123, -50, 10, 7);
        TempHumidityEvent event = new TempHumidityEvent(loraParams, 50, 21.3, 3.6);
        MyDevicesMessage message = MyDevicesMessage.fromHumidityEvent(event);
        Assert.assertEquals("[{rel_hum[2]=50.0 p}, {temp[3]=21.3 c}, {voltage[4]=3.6 v}]", message.toString());
    }

}
