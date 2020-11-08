package nl.bertriksikken.lorawan;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public final class FOpts {
    
    private List<MacCommand> macCommands = new ArrayList<>();

    private void add(MacCommand macCommand) {
        macCommands.add(macCommand);
    }
    
    public static FOpts decode(int fctrl, byte[] data) {
        int foptsLen = fctrl & 0xF;
        if (foptsLen != data.length) {
            throw new IllegalArgumentException("FOptsLen mismatch");
        }

        ByteBuffer bb = ByteBuffer.wrap(data).order(ByteOrder.LITTLE_ENDIAN);
        FOpts fOpts = new FOpts();
        while (bb.hasRemaining()) {
            int cid = bb.get() & 0xFF;
            EMacCommand command = EMacCommand.get(cid);
            MacCommand macCommand = decode(command, bb);
            fOpts.add(macCommand);
        }
        return fOpts;
    }

    private static MacCommand decode(EMacCommand macCommand, ByteBuffer bb) {
        MacCommand fOpt = new MacCommand(macCommand);
        switch (macCommand) {
        case LinkAdrReq:
            int dataRateTxPower = bb.get() & 0xFF;
            fOpt.add("DataRate", (dataRateTxPower >> 4) & 0xF);
            fOpt.add("TXPower", dataRateTxPower & 0xF);
            int chMask = bb.getShort() & 0xFFFF;
            fOpt.add("ChMask", chMask);
            int redundancy = bb.get();
            fOpt.add("Redundancy", redundancy);
            break;
        default:
            throw new IllegalArgumentException("Unhandled command " + macCommand);
        }
        return fOpt;
    }
    
    @Override
    public String toString() {
        return String.format(Locale.ROOT, "{fopts=%s}", macCommands);
    }
    
    public static class MacCommand {
        private final EMacCommand macCommand;
        private final Map<String, Object> props = new LinkedHashMap<>();

        MacCommand(EMacCommand macCommand) {
            this.macCommand = macCommand;
        }

        public void add(String property, Object value) {
            props.put(property, value);
        }

        @Override
        public String toString() {
            return String.format(Locale.ROOT, "{cmd=%s: %s}", macCommand, props);
        }
    }

}
