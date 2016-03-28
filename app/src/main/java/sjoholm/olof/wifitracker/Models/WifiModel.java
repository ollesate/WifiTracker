package sjoholm.olof.wifitracker.Models;

import java.sql.Time;

/**
 * Created by olof on 2016-03-15.
 */
public class WifiModel {

    public static class Event extends WifiModel{
        public final long time;
        public final String wifiName;

        public Event(String wifiName, long time){
            this.time = time;
            this.wifiName = wifiName;
        }
    }

    public class Detailed {
        public final long startTime;
        public final long endTime;
        public final String wifiName;
        public final long duration;

        public Detailed(String wifiName, long startTime, long endTime) {
            this.wifiName = wifiName;
            this.startTime = startTime;
            this.endTime = endTime;
            duration = endTime - startTime;
        }
    }

    public class Duration {
        public final long time;
        public final long duration;
        public final String wifiName;

        public Duration(String wifiName, long time, long duration) {
            this.wifiName = wifiName;
            this.time = time;
            this.duration = duration;
        }
    }

}
