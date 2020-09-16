package nl.bertriksikken.motionsensorbackend;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import nl.bertriksikken.ttn.dto.TtnUplinkMessage;

public final class MotionSensorBackendTest {

    @Test
    public void test() throws JsonProcessingException {
        MotionSensorBackendConfig config = new MotionSensorBackendConfig();
        MotionSensorBackend backend = new MotionSensorBackend(config);

        ObjectMapper mapper = new ObjectMapper();
        TtnUplinkMessage uplink = mapper.readValue(
                "{\"app_id\":\"vleermuispir\",\"dev_id\":\"vleermuispir01\",\"hardware_serial\":\"58A0CB000011D8AB\","
                + "\"port\":102,\"counter\":802,\"payload_raw\":\"Afs4AAAxEQA=\",\"payload_fields\":{\"batt\":3.6,"
                + "\"bytes\":\"Afs4AAAxEQA=\",\"cap\":100,\"count\":4401,\"occupied\":true,\"port\":102,\"temp\":24,"
                + "\"time\":0},\"metadata\":{\"time\":\"2020-09-16T20:33:32.968596353Z\",\"frequency\":868.3,"
                + "\"modulation\":\"LORA\",\"data_rate\":\"SF7BW125\",\"airtime\":56576000,\"coding_rate\":\"4/5\","
                + "\"gateways\":[{\"gtw_id\":\"eui-58a0cbfffe802765\",\"timestamp\":1176808547,"
                + "\"time\":\"2020-09-16T20:33:33.016449928Z\",\"channel\":0,\"rssi\":-76,\"snr\":8.5,"
                + "\"rf_chain\":0}]}}",
                TtnUplinkMessage.class);

        backend.messageReceived("topic", uplink);
    }

}
