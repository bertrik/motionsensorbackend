package nl.bertriksikken.motionsensorbackend;

import org.junit.Assert;
import org.junit.Test;

public final class MotionSensorBackendTest {

    /**
     * Verifies that the constructor can be called with a default config.
     */
    @Test
    public void testConstructor() {
        MotionSensorBackendConfig config = new MotionSensorBackendConfig();
        MotionSensorBackend backend = new MotionSensorBackend(config);
        Assert.assertNotNull(backend);
    }
    
}
