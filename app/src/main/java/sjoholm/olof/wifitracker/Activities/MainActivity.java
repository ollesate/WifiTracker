package sjoholm.olof.wifitracker.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sjoholm.olof.wifitracker.Fragments.Fragments;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;
import sjoholm.olof.wifitracker.Utils.TimeUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TimeUtil.setUp(this);
        Fragments.showDataFragment(this);
    }

}
