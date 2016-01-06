package sjoholm.olof.wifitracker.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Fragments.Fragments;
import sjoholm.olof.wifitracker.Models.WifiDataState;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.Tables;
import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;
import sjoholm.olof.wifitracker.Testing.DatabaseDialog;
import sjoholm.olof.wifitracker.Utils.DialogUtil;
import sjoholm.olof.wifitracker.Utils.TimeUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Fragments.showPieChartFragment(this);
        //DialogUtil.showDialog(this, new DatabaseDialog());
        //DialogUtil.showDialog(this, new DiagramDialog());
        //netMillenium();
        //Fragments.showFragment(this, new DiagramDialog());
//        DialogUtil.showDialog(this, new DatabaseDialog());
//
//        Calendar calendar = Calendar.getInstance();
//        //Time time =
//        DateFormat dateFormat = android.text.format.DateFormat.getTimeFormat(this);
//        String test = dateFormat.format(calendar.getTime());
//        //Log.d("Works? ;D", test);
        //Time 18:24
        //

        TimeUtil.initialize(this);
        WifiStatesDatabase db = new WifiStatesDatabase(this);
        db.getTodaysWifiDuration();

        Fragments.showDataFragment(this);
    }

    public void netMillenium(){
        WifiStatesDatabase wifiStatesDatabase = new WifiStatesDatabase(this);
        ArrayList<WifiDataState> wifiData = wifiStatesDatabase.getAllData();

        for(int currentIndex = 0; currentIndex < wifiData.size()-1; currentIndex++){

            int nextIndex = currentIndex + 1;

            calculateDuration(wifiData.get(currentIndex), wifiData.get(nextIndex));

        }
//
//        for(WifiDataState data : wifiData){
//            if(!data.wifiName.equals("<unknown ssid>"))
//                Log.d("Operation RECREATE", "wifiName: " + data.wifiName + ", dur:" + TimeUtil.TimeInTextFromHours(data.durationMillis));
//        }

        wifiStatesDatabase.query("DROP TABLE IF EXISTS " + Tables.WifiStatusChangedTable.TABLE_NAME);
        //wifiStatesDatabase.query("ALTER TABLE " + Tables.WifiStatusChangedTable.TABLE_NAME + " ADD " + Tables.WifiStatusChangedTable.COLUMN_DURATION_MILLIS + SQLTypes.LONG);
        for(WifiDataState data : wifiData){
            wifiStatesDatabase.insert(data);
        }

        logIt();
    }

    private void logIt(){
        WifiStatesDatabase wifiStatesDatabase = new WifiStatesDatabase(this);
        ArrayList<WifiDataState> wifiData = wifiStatesDatabase.getAllData();
        for(WifiDataState data : wifiData){
            if(!data.wifiName.equals("<unknown ssid>"))
                Log.d("Operation RECREATE", "wifiName: " + data.wifiName + ", dur:" + TimeUtil.TimeInTextFromHours(data.durationMillis));
        }
    }

    private void calculateDuration(WifiDataState curr, WifiDataState next){
        curr.durationMillis = next.timeMillis - curr.timeMillis;
    }


}
