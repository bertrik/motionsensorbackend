package nl.bertriksikken.lorawan;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public enum EMacCommand {
    
    ResetConf(0x01, "ResetConf"),
    LinkCheckAns(0x02, "LinkCheckAns"),
    LinkAdrReq(0x03, "LinkAdrReq"),
    DutyCycleReq(0x04, "DutyCycleReq"),
    RXParamSetup(0x05, "RXParamSetup"),
    DevStatusReq(0x06, "DevStatusReq"),
    NewChannelReq(0x07, "NewChannelReq"),
    // a lot more
    Unknown(-1, "Unknown");

    private static final Map<Integer, EMacCommand> commands = new HashMap<>();
    static {
        Stream.of(values()).forEach(e -> commands.put(e.cid, e));
    }
    
    private final int cid;
    private final String name;

    EMacCommand(int cid, String name) {
        this.cid = cid;
        this.name = name;
    }

    static EMacCommand get(int cid) {
        return commands.getOrDefault(cid, Unknown);
    }
    
    public int getCid() {
        return cid;
    }

    public String getName() {
        return name;
    }
    
}
