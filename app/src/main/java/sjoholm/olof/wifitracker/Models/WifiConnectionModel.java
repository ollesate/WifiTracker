package sjoholm.olof.wifitracker.Models;

import android.net.NetworkInfo;

import java.sql.Date;

import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2015-12-22.
 */
public class WifiConnectionModel {

    public Date date;
    public String wifiName;
    public long timeMillis;
    public long durationMillis;

    @Override
    public String toString() {
        return date + " | " + timeMillis + " | "+ wifiName + " | " + TimeUtil.TimeInTextFromHours(durationMillis);
    }
}
