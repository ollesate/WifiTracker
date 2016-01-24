package sjoholm.olof.wifitracker.Models;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.widget.TextView;

import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2016-01-04.
 */
public class WifiDurationModel {

    public String wifiName;
    public long durationMillis;

    @Override
    public String toString() {
        return wifiName.replaceAll("^\"|\"$", "") + ": " + TimeUtil.DurationInHoursAndMinutes(durationMillis);
    }

    private String getSpannableString(String wifiName, String timeText){
        SpannableString styledString = new SpannableString(wifiName + ": " + timeText);
        styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, wifiName.length(), 0); //Bold
        return null;
    }
}
