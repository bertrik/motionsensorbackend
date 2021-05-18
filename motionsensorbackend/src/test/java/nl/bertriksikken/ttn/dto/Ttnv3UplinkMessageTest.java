package nl.bertriksikken.ttn.dto;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import nl.bertriksikken.ttn.TtnUplinkMessage;

public final class Ttnv3UplinkMessageTest {

    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Reads an example message and verifies parsing.
     */
    @Test
    public void testDecode() throws IOException {
        // decode JSON
        try (InputStream is = this.getClass().getResourceAsStream("/ttnv3_mqtt_message.json")) {
            Ttnv3UplinkMessage message = mapper.readValue(is, Ttnv3UplinkMessage.class);
            Assert.assertNotNull(message);
            TtnUplinkMessage uplink = message.toTtnUplinkMessage();
            Assert.assertNotNull(uplink.toString());
            Assert.assertEquals("2021-02-04T22:12:45.761243856Z", uplink.getTime().toString());
            Assert.assertEquals(3779, uplink.getCounter());
            Assert.assertEquals("58A0CB000011D34E", uplink.getDeviceEui());
            Assert.assertEquals(8, uplink.getRawPayload().length);
            Assert.assertEquals(102, uplink.getPort());
            Assert.assertEquals(-42.0, uplink.getRSSI(), 0.1);
            Assert.assertEquals(10.0, uplink.getSNR(), 0.1);
            Assert.assertEquals(7, uplink.getSF());
        }
    }    
}
