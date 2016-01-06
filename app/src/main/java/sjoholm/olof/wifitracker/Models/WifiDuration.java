package sjoholm.olof.wifitracker.Models;

import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2016-01-04.
 */
public class WifiDuration {

    public String wifiName;
    public long durationMillis;

    @Override
    public String toString() {
        return wifiName + ": " + TimeUtil.TimeInTextFromHours(durationMillis);
    }
}
