package sjoholm.olof.wifitracker;

import android.util.Log;

import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.WifiDataState;
import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2016-01-02.
 */
public class DayData {

    private ArrayList<String> hourCategories = new ArrayList<>();
    private float[] values = new float[24];

    public DayData() {
        for(int i = 0; i < 24; i++){
            String hour = (i < 10) ? "0" + i : i + "";
            hourCategories.add(hour);
        }
    }

    public ArrayList<String> getXCategories(){
        return hourCategories;
    }

    public ArrayList<BarEntry> getBarEntries(ArrayList<WifiDataState> wifiData){

        for (WifiDataState data :
                wifiData) {
            if(!data.wifiName.equals("<unknown ssid>"))
                insertIntoHourCategory(data);
        }

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        for(int index = 0; index < values.length; index++){
            barEntries.add(new BarEntry( values[index], index));
        }

        return barEntries;
    }

    private void insertIntoHourCategory(WifiDataState data){

        int hourIndexStart = (int) TimeUtil.getHoursFromMillis2(data.timeMillis);
        int hourIndexEnd = (int) TimeUtil.getHoursFromMillis2(data.timeMillis + data.durationMillis);

//        if(hourIndexStart == hourIndexEnd){
//
//            values[hourIndexStart] += TimeUtil.getHoursFromMillis2(data.durationMillis);
//        }
//        else if(hourIndexEnd > hourIndexStart){
//
//            long oneHour = 1 * TimeUtil.HOURS;
//            values[hourIndexStart] += TimeUtil.getHoursFromMillis2(oneHour);
//
//            long remainder = data.durationMillis - oneHour;
//            values[hourIndexEnd] += TimeUtil.getHoursFromMillis2(remainder);
//        }
//        else { //Should never happen
//            Log.d("Strange Error", "ooppsie");
//        }
        long startHour = data.timeMillis % TimeUtil.HOURS;
        long duration = data.durationMillis;
        int index = hourIndexStart;

        long firstVal = (duration > TimeUtil.HOURS - startHour) ? TimeUtil.HOURS - startHour : duration;
        values[index++] +=  TimeUtil.getHoursFromMillis2(firstVal);
        Log.d("DayData", index-1 + ": " +TimeUtil.getHoursFromMillis2(firstVal));


        if(duration-firstVal == 0)
            return;

        do{
            long hoursInMillisAtIndex = (duration > 1 * TimeUtil.HOURS) ? 1 * TimeUtil.HOURS : duration;
            values[index++] += TimeUtil.getHoursFromMillis2(hoursInMillisAtIndex);
            Log.d("DayData", index-1 + ": " +TimeUtil.getHoursFromMillis2(hoursInMillisAtIndex));
            duration-= 1 * TimeUtil.HOURS;
        }while(duration > 0);

    }

}
