package sjoholm.olof.wifitracker.Utils;

import android.content.Intent;
import android.util.Log;

/**
 * Created by olof on 2015-12-22.
 */
public class IntentLoggerUtil {

    public static void LogActionInfo(String TAG, Intent intent){
        Log.d(TAG, "->Action Name: " + intent.getAction() + "<-");
    }

    public static void LogVerbose(String TAG, Intent intent){
        LogActionInfo(TAG, intent);


        if(intent.getExtras() != null) {
            int index = 0;
            for (String key : intent.getExtras().keySet()) {
                index++;
                Object value = intent.getExtras().get(key);
                Log.d(TAG, String.format(index + ": %s\t(%s)", key, value.getClass().getName()));
                Log.d(TAG, String.format("\t\t\t%s", value.toString()));
            }
        }

    }

    public static void LogSparse(String TAG, Intent intent){
        LogActionInfo(TAG, intent);

        if(intent.getExtras() != null) {
            int index = 0;
            for (String key : intent.getExtras().keySet()) {
                index++;
                Object value = intent.getExtras().get(key);
                Log.d(TAG, String.format(index + ": %s\t(%s)", key, value.getClass().getName()));
            }
        }

    }

}
