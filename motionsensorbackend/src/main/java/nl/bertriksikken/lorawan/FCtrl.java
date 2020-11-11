package nl.bertriksikken.lorawan;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class FCtrl {

    private final Map<String, Object> elements = new HashMap<>();
    
    public static FCtrl decode(byte data) {
        FCtrl fctrl = new FCtrl();
        if ((data & 0x80) != 0) {
            fctrl.add("ADR", true);
        }
        if ((data & 0x40) != 0) {
            fctrl.add("ADRACKReq", true);
        }
        if ((data & 0x20) != 0) {
            fctrl.add("ACK", true);
        }
        if ((data & 0x10) != 0) {
            fctrl.add("FPending", true);
        }
        fctrl.add("FOptsLen", data & 0xF);

        return fctrl;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "%s", elements);
    }
    
    private void add(String key, Object value) {
        elements.put(key, value);
    }
    
}
