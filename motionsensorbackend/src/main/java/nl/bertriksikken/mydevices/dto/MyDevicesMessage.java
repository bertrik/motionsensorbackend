package nl.bertriksikken.mydevices.dto;

import java.util.ArrayList;

import nl.bertriksikken.motionsensorbackend.BaseEvent;
import nl.bertriksikken.motionsensorbackend.MotionEvent;
import nl.bertriksikken.motionsensorbackend.TempHumidityEvent;

/**
 * See https://developers.mydevices.com/cayenne/docs/cayenne-mqtt-api/#cayenne-mqtt-api-supported-data-types
 */
public final class MyDevicesMessage extends ArrayList<MyDevicesItem> {

    private static final long serialVersionUID = 1L;

    private static MyDevicesMessage fromBaseEvent(BaseEvent event) {
        MyDevicesMessage message = new MyDevicesMessage();
        message.add(new MyDevicesItem(100, "rssi", event.getRssi(), "dbm"));
        message.add(new MyDevicesItem(101, "snr", event.getSnr(), "db"));
        message.add(new MyDevicesItem(102, "analog_sensor", event.getSf(), "null"));
        return message;
    }
    
    public static MyDevicesMessage fromMotionEvent(MotionEvent event) {
        MyDevicesMessage message = fromBaseEvent(event);
        message.add(new MyDevicesItem(1, "motion", (event.getOccupied() != 0) ? "1" : "0", "d"));
        message.add(new MyDevicesItem(2, "counter", Integer.toString(event.getCount()), "null"));
        message.add(new MyDevicesItem(3, "temp", event.getTemperature(), "c"));
        message.add(new MyDevicesItem(4, "voltage", event.getBattery(), "v"));
        message.add(new MyDevicesItem(5, "time", Integer.toString(event.getMinutes()), "min"));
        return message;
    }

    public static MyDevicesMessage fromHumidityEvent(TempHumidityEvent event) {
        MyDevicesMessage message = fromBaseEvent(event);
        message.add(new MyDevicesItem(2, "rel_hum", event.getHumidity(), "p"));
        message.add(new MyDevicesItem(3, "temp", event.getTemperature(), "c"));
        message.add(new MyDevicesItem(4, "voltage", event.getBattery(), "v"));
        return message;
    }

}
