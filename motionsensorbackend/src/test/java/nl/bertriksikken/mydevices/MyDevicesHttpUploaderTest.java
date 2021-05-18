package nl.bertriksikken.mydevices;

import java.time.Duration;
import java.time.Instant;

import nl.bertriksikken.motionsensor.dto.DecodeException;
import nl.bertriksikken.motionsensor.dto.MotionSensorUplinkMessage;
import nl.bertriksikken.motionsensorbackend.LoraParams;
import nl.bertriksikken.motionsensorbackend.MotionEvent;
import nl.bertriksikken.mydevices.dto.MyDevicesMessage;

public final class MyDevicesHttpUploaderTest {

    public static void main(String[] args) throws DecodeException {
        MyDevicesHttpUploaderTest test = new MyDevicesHttpUploaderTest();
        test.testMotionUpload();
    }

    public void testMotionUpload() throws DecodeException {
        byte[] raw = new byte[] { 0x01, 0x0B, 0x36, 0x00, 0x00, 0x16, 0x0D, 0x00 };
        MotionSensorUplinkMessage uplink = MotionSensorUplinkMessage.decode(raw);
        LoraParams loraParams = new LoraParams(Instant.now(), 123, -50, 10, 7);
        MotionEvent event = new MotionEvent(loraParams, uplink.isOccupied(), uplink.getCount(), uplink.getTime(),
                uplink.getTemperature(), uplink.getVoltage());
        MyDevicesMessage message = MyDevicesMessage.fromMotionEvent(event);

        String url = "https://api.mydevices.com";
        IMyDevicesRestApi restApi = MyDevicesHttpUploader.newRestClient(url, Duration.ofSeconds(10));
        MyDevicesHttpUploader uploader = new MyDevicesHttpUploader(restApi);

        MyDevicesCredentials credentials = new MyDevicesCredentials("1b3d0d60-9b7e-11e7-a1da-536ee79fd847",
                "f865f85afcb51f0a3c732d0c2910d93d8cd3b59e", "1514be30-b5ae-11eb-8779-7d56e82df461");
        uploader.uploadMeasurement("pirbertrik", credentials, message);
    }

}
