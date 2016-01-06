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

    private static Context context;

    public static final void setUp(Context context){
        TimeUtil.context = context;
    }


    public static String TimeInTextFromHours(long timeMillis){
        int minutes = 0, hour = 0;

        if(timeMillis > 1 * MINUTES){
            minutes = (int)((timeMillis / MINUTES)%60);
        }
        if(timeMillis > 1 * HOURS){
            hour = (int)((timeMillis / HOURS)%60);
        }

        String hourText = (hour > 0) ? hour +"h " : "";
        return hourText + minutes + "min";
    }

    public static String TimeInDigitalFromHours(float hours){
        int h = (int)hours;
        int min = (int)((hours-h)*60);

        String zeroFillerMinute = (min >= 10) ? "" : "0";
        String zeroFillerHour = (h >= 10) ? "" : "0";

        return zeroFillerHour + h + ":" + zeroFillerMinute + min;
    }

    public static String TimeInTextFromHours(float hours){
        int h = (int)hours;
        int min = (int)((hours-h)*60);

        String hourText = (h > 0) ? h +"h " : "";
        return hourText + min + "min";
    }

    public static String ToHoursAndMinutesDigital(long timeMillis){
        int minutes = 0, hour = 0;

        if(timeMillis > 1 * MINUTES){
            minutes = (int)((timeMillis / (float)MINUTES)%60);
        }
        if(timeMillis > 1 * HOURS){
            hour = (int)((timeMillis / (float)HOURS)%24);
        }

        String zeroFillerMinute = (minutes >= 10) ? "" : "0";
        String zeroFillerHour = (hour >= 10) ? "" : "0";

        return zeroFillerHour + hour + ":" + zeroFillerMinute + minutes;
    }

    public static String ToDateAndTime(long timeMillis){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeMillis);
        return String.format("%d-%d-%d : %d:%d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND));
    }

    public static float fromTimeMillisToDayMillis(long timeMillis){
        Calendar time = Calendar.getInstance();
        time.setTimeInMillis(timeMillis);
        float dayMillis = time.get(Calendar.HOUR) * HOURS
                + time.get(Calendar.MINUTE) * MINUTES
                + time.get(Calendar.SECOND) * SECONDS;
        return dayMillis;
    }

    public static float getHoursFromMillis(long timeMillis){
        return (float)timeMillis / HOURS;
    }

    public static float getHoursFromMillis2(long timeMillis){
        float hours = (float)timeMillis / HOURS;
        float betterYet = hours % 24;
        return betterYet;
    }

    public static long currentTimeMillis(){
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        return date.getTime();
    }

    public static String ToTimeInDigital(long timeInMillis){
        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(context);
        Calendar calendar  = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        return dateFormat.format(calendar.getTime());
    }

}
