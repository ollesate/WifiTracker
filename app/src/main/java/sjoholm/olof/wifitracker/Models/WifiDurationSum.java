package sjoholm.olof.wifitracker.Models;

import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2015-12-27.
 */
public class WifiDurationSum {

    public long durationConnectedInMillis;
    public String wifiName;

    public WifiDurationSum(String wifiName, long durationConnectedInMillis){
        this.wifiName = wifiName;
        this.durationConnectedInMillis = durationConnectedInMillis;
    }

    @Override
    public String toString() {
        return wifiName + " " + TimeUtil.TimeInTextFromHours(durationConnectedInMillis);
    }
}
