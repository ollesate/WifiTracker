package sjoholm.olof.wifitracker.Utils;

import android.content.Context;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;
import sjoholm.olof.wifitracker.Models.WifiDurationSum;
import sjoholm.olof.wifitracker.Models.WifiDataState;

/**
 * Created by olof on 2015-12-27.
 */
public class DatabaseDataUtil {

    private static final String TAG = DatabaseDataUtil.class.getSimpleName();

    public static ArrayList<WifiDurationSum> getWifiData(Context context, Date date){
        WifiStatesDatabase database = new WifiStatesDatabase(context);

        ArrayList<WifiDataState> wifiDataStates = database.getAllData();

        ArrayList<WifiDurationSum> sorted = sumAndSortConnectionData(wifiDataStates);

        return sorted;
    }

    public static ArrayList<WifiDurationSum> getConnectedWifis(Date from, Date to){  //TODO
        return null;
    }

    private static ArrayList<WifiDurationSum> sumAndSortConnectionData(ArrayList<WifiDataState> wifiDataStates){
        Map<String, WifiDurationSum> map = new HashMap();
        Calendar timeZero = Calendar.getInstance();
        timeZero.setTimeInMillis(timeZero.getTime().getTime());

            //Sätt tiden till kl 0:00
        timeZero.set(Calendar.SECOND, 0);
        timeZero.set(Calendar.MINUTE, 0);
        timeZero.set(Calendar.HOUR, 0);

        Long currentTimeMillis = Calendar.getInstance().getTime().getTime();

        for(int i = 0; i < wifiDataStates.size(); i++) {
            WifiDataState w = wifiDataStates.get(i);

            if(w.state == NetworkInfo.State.DISCONNECTED){
                continue; //No need to calculate disconnected time this way
            }

            long duration;
            int lastElement = wifiDataStates.size()-1;
            if(i == lastElement){
                duration = currentTimeMillis - w.timeMillis;
            }else{
                duration = wifiDataStates.get(i+1).timeMillis - w.timeMillis;
            }


            if (map.containsKey(w.wifiName)){ //Already exists? Add duration

                map.get(w.wifiName).durationConnectedInMillis += duration;

            }else{ //Does not exist? Create new and set duration

                map.put(
                        w.wifiName,
                        new WifiDurationSum(w.wifiName, duration));

            }

        }

        ArrayList<WifiDurationSum> result = new ArrayList<>(map.values());
        return result;
    }

    //getWifiSorted per HOUR
    //getSortedWifis(int year, int month, int day, int hour)
    //getSortedWifis(int year, int month, int day)
    //getSortedWifis(int year, int month)
    //getSortedWifis(int year)


}
