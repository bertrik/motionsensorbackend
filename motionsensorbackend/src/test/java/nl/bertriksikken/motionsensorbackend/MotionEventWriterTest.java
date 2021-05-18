package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class MotionEventWriterTest {

    private MotionEventWriter writer;
    
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();
    
    @Before
    public void before() {
        writer = new MotionEventWriter(tempFolder.getRoot());
    }

    @Test
    public void testWriteMotion() throws IOException {
        // motion events
        Instant now = Instant.now();
        LoraParams loraParams1 = new LoraParams(now, 11, -50, 10, 7);
        MotionEvent event1 = new MotionEvent(loraParams1, true, 1, 0, 22.1, 3.6);
        Assert.assertNotNull(event1.toString());
        now = now.plusSeconds(60);
        LoraParams loraParams2 = new LoraParams(now, 12, -50, 10, 7);
        MotionEvent event2 = new MotionEvent(loraParams2, false, 2, 1, 22.1, 3.5);
        Assert.assertNotNull(event2.toString());

        File file = writer.write("device", event1);
        writer.write("device", event2);
        String result = new String(Files.readAllBytes(file.toPath()), Charset.forName("ASCII"));
        Assert.assertNotNull(result);
        System.out.println(result);
    }
    
    @Test
    public void testWriteHumidity() throws IOException {
        // temp/humidity events
        Instant now = Instant.now();
        LoraParams loraParams1 = new LoraParams(now, 16, -50, 10, 7);
        TempHumidityEvent event1 = new TempHumidityEvent(loraParams1, 50, 22.3, 3.5);
        Assert.assertNotNull(event1.toString());
        now = now.plusSeconds(10);
        LoraParams loraParams2 = new LoraParams(now, 17, -50, 10, 7);
        TempHumidityEvent event2 = new TempHumidityEvent(loraParams2, 51, 22.3, 3.6);
        Assert.assertNotNull(event1.toString());

        writer.write("device", event1);
        File file = writer.write("device", event2);
        
        String result = new String(Files.readAllBytes(file.toPath()), Charset.forName("ASCII"));
        Assert.assertNotNull(result);
        System.out.println(result);
    }
    
}
