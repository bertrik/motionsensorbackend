package nl.bertriksikken.ttn.dto;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Unit tests related to TTN uplink messages.
 */
public final class TtnUplinkMessageTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Before
    public void before() {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Reads an example message and verifies parsing.
     * 
     * @throws IOException
     */
    @Test
    public void testDecode() throws IOException {
        // decode JSON
        try (InputStream is = this.getClass().getResourceAsStream("/ttn_mqtt_message.json")) {
            TtnUplinkMessage message = mapper.readValue(is, TtnUplinkMessage.class);
            Assert.assertNotNull(message);
            Assert.assertNotNull(message.appId);
            Assert.assertNotNull(message.devId);
            Assert.assertNotNull(message.hardwareSerial);
            Assert.assertNotNull(message.port);
            Assert.assertNotNull(message.counter);
            Assert.assertNotNull(message.rawPayload);

            System.out.println(message.toString());
        }
    }

    @Test
    public void testDecodeMetaData() throws JsonMappingException, JsonProcessingException {
        String data = "{\"app_id\":\"vleermuispir\",\"dev_id\":\"vleermuispir01\","
                + "\"hardware_serial\":\"58A0CB000011D8AB\",\"port\":1,\"counter\":0,\"payload_raw\":\"Afs4AQAxEAA=\","
                + "\"payload_fields\":{\"batt\":3.6,\"bytes\":\"Afs4AQAxEAA=\",\"cap\":100,\"count\":4145,"
                + "\"occupied\":true,\"port\":1,\"temp\":24,\"time\":1},"
                + "\"metadata\":{\"time\":\"2020-09-08T18:43:08.739292079Z\"}}\"";
        ObjectMapper mapper = new ObjectMapper();
        TtnUplinkMessage message = mapper.readValue(data, TtnUplinkMessage.class);
        Assert.assertNotNull(message);
        Assert.assertNotNull(message.getTime());
    }
}
