package sjoholm.olof.wifitracker.Testing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.CustomBarChart;
import sjoholm.olof.wifitracker.Models.WifiDataState;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;

/**
 * Created by olof on 2016-01-02.
 */
public class DiagramDialog extends DialogFragment{

    CustomBarChart barChart;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_diagram, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        barChart = (CustomBarChart) view.findViewById(R.id.bar_chart);
        getData();
    }

    private void getData(){
        WifiStatesDatabase db = new WifiStatesDatabase(getContext());
        ArrayList<String> wifiNames =  db.getTodaysWifis();
        ArrayList<WifiDataState> wifiData = db.getTodaysData();

        barChart.setStackedBarCategories(wifiNames);

        barChart.addColumnData(createDummyData(wifiData.size()), wifiData.get(0).wifiName);
        barChart.addColumnData(createDummyData(wifiData.size()), wifiData.get(0).wifiName);
        barChart.addColumnData(createDummyData(wifiData.size()), wifiData.get(0).wifiName);

        barChart.initialize();


    }

    private ArrayList<Float> createDummyData(int size){
        ArrayList<Float> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            double val = Math.floor(Math.random() * 100.0);
            data.add((float)val);
            Log.d("Data", (float) val +"");
        }
        return data;
    }
}
