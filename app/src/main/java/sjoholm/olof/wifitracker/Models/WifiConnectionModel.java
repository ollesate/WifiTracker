package sjoholm.olof.wifitracker.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.sql.Date;

import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2015-12-22.
 */
public class WifiConnectionModel implements Serializable {

    public Date date;
    public String wifiName;
    public long timeMillis;
    public long durationMillis;

    @Override
    public String toString() {
        return date + " | " + timeMillis + " | "+ wifiName + " | " + TimeUtil.DurationInHoursAndMinutes(durationMillis);
    }

}
