package nl.bertriksikken.motionsensorbackend;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.time.Instant;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public final class MotionEventWriterTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void testWriteCsv() throws IOException {
        MotionEventWriter writer = new MotionEventWriter(tempFolder.getRoot());
        
        Instant now = Instant.now();
        MotionEvent event1 = new MotionEvent(11, now, true, 3.6, 22.1, 1);
        MotionEvent event2 = new MotionEvent(12, now.plusSeconds(60), false, 3.5, 22.1, 2);

        writer.write("device", event1);
        File file = writer.write("device", event2);
        
        String result = new String(Files.readAllBytes(file.toPath()), Charset.forName("ASCII"));
        Assert.assertNotNull(result);
        System.out.println(result);
    }
    
}
