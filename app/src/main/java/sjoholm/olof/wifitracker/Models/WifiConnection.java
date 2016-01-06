package sjoholm.olof.wifitracker.Models;

import android.net.NetworkInfo;

import java.sql.Date;

import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2015-12-22.
 */
public class WifiConnection {

    public Date date;
    public String wifiName;
    public NetworkInfo.State state;
    public long timeMillis;
    public long durationMillis;

    public WifiConnection(){}; //Empty constructor

    @Override
    public String toString() {
        return date + " | " + TimeUtil.ToTimeInDigital(timeMillis) + " | "+ wifiName + " | " + TimeUtil.TimeInTextFromHours(durationMillis);
    }
}
