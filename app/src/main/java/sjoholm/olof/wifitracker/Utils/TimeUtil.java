package sjoholm.olof.wifitracker.Utils;

import android.content.Context;
import android.util.Log;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by olof on 2015-12-27.
 */
public class TimeUtil {

    public static final int SECONDS = 1000;
    public static final int MINUTES = SECONDS * 60;
    public static final int HOURS = MINUTES * 60;

    public static String DurationInHoursAndMinutes(long durationMillis){
        int minutes = 0, hour = 0;

        if(durationMillis > 1 * MINUTES){
            minutes = (int)((durationMillis / MINUTES)%60);
        }
        if(durationMillis > 1 * HOURS){
            hour = (int)((durationMillis / HOURS)%60);
        }

        String hourText = (hour > 0) ? hour +"h " : "";
        return hourText + minutes + "min";
    }

    public static String ToTimeInDigital(Context context, long timeInMillis){
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
        Calendar calendar  = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return dateFormat.format(calendar.getTime());
    }

}
