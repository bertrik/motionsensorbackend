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
        MotionEvent event1 = new MotionEvent(now, 11, true, 1, 0, 22.1, 3.6);
        now = now.plusSeconds(60);
        MotionEvent event2 = new MotionEvent(now, 12, false, 2, 1, 22.1, 3.5);
        now = now.plusSeconds(10);

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
        TempHumidityEvent event3 = new TempHumidityEvent(now, 16, 22.3, 50, 3.5);
        now = now.plusSeconds(10);
        TempHumidityEvent event4 = new TempHumidityEvent(now, 17, 22.3, 51, 3.6);

        writer.write("device", event3);
        File file = writer.write("device", event4);
        
        String result = new String(Files.readAllBytes(file.toPath()), Charset.forName("ASCII"));
        Assert.assertNotNull(result);
        System.out.println(result);
    }
    
}
