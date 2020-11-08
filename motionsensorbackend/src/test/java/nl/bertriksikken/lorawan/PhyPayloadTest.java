package nl.bertriksikken.lorawan;

import java.util.Base64;

import org.junit.Assert;
import org.junit.Test;

public final class PhyPayloadTest {

    @Test
    public void testDecode() {
        // decode PHY payload
        String base64 = "YGohASaFGQADUf8AAV5zA3Q=";
        byte[] data = Base64.getDecoder().decode(base64);
        PhyPayload phyPayload = PhyPayload.decode(data);
        Assert.assertEquals(0x60, phyPayload.getMhdr());
        Assert.assertNotNull(phyPayload.getMacPayload());
        Assert.assertArrayEquals(new byte[] {0x5E, 0x73, 0x03, 0x74}, phyPayload.getMic());
        
        // decode MAC payload
        MacPayload macPayload = MacPayload.decode(phyPayload.getMacPayload());
        Assert.assertEquals(0x2601216A, macPayload.getDevAddr());
        Assert.assertEquals(0x85, macPayload.getFctrl());
        Assert.assertEquals(25, macPayload.getFcnt());

        // decode FOPTS
        FOpts fopts = FOpts.decode(macPayload.getFctrl(), macPayload.getFOpts());
        System.out.println(fopts);
    }
    
}
