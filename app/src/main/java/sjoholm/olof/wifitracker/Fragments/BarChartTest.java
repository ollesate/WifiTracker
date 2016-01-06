package sjoholm.olof.wifitracker.Fragments;


import android.content.Context;
import android.graphics.Color;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.WifiDataState;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;
import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class BarChartTest extends Fragment {

    private BarChart barChart;

    public BarChartTest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bar_chart_test, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        barChart = (HorizontalBarChart) view.findViewById(R.id.bar_chart);

        intializeBarChartSettings(barChart);

        ArrayList<WifiDataState> wifiData = getWifiStatusFromDatabase();

        initializeLegend(getColorSSIDPairs(wifiData));

        BarData barData = getBarData(wifiData);

        barChart.setData(barData);
    }

    private void initializeLegend(ArrayList<Pair<String, Integer>> pairs) {
        //Add the legend lables programatically
        Legend legend = barChart.getLegend();
        legend.setTextColor(Color.BLACK);
        int [] legendColors = new int[pairs.size()];
        String [] legendSSID = new String[pairs.size()];
        for(int i = 0; i < pairs.size(); i++){
            Pair<String, Integer> p = pairs.get(i);
            legendSSID[i] = p.first;
            legendColors[i] = p.second;
        }

        legend.setCustom(legendColors, legendSSID);
    }

    private ArrayList<Pair<String, Integer>> getColorSSIDPairs(ArrayList<WifiDataState> wifiData){
        ArrayList<Pair<String, Integer>> pairs = new ArrayList<>();

        int uniques = 0;
        for(WifiDataState wifiState : wifiData){
            if(!hasValue(wifiState.wifiName, pairs)){
                Pair<String, Integer> pair = new Pair<String, Integer>(wifiState.wifiName, getColor(uniques));
                pairs.add(pair);
                uniques++;
            }
        }

        return pairs;
    }

    private BarData getBarData(ArrayList<WifiDataState> wifiData) {

        int wifiColor = Color.RED;

        ArrayList<BarEntry> barEntries = new ArrayList<>();

        //Create the offset
        int offsetIndex = 0;
        long firstStateTimeMillis = wifiData.get(offsetIndex).timeMillis;
        float [] segmentValues = new float[wifiData.size()+1];
        long convertedMillisDay = (long) TimeUtil.fromTimeMillisToDayMillis(firstStateTimeMillis);
        segmentValues[offsetIndex] = TimeUtil.getHoursFromMillis(convertedMillisDay);

        //Add the offset color as transparent
        ArrayList<Integer> segmentColors = new ArrayList<>();
        segmentColors.add(Color.TRANSPARENT); //First value to make an offset


        for(int currentIndex = 0; currentIndex < wifiData.size(); currentIndex++){
            WifiDataState currentWifiStatus  = wifiData.get(currentIndex);
            int lastIndex = wifiData.size()-1;

            int shiftedIndex = currentIndex+1;
            int nextElement = currentIndex+1;
            long duration;
            if(currentIndex == lastIndex){ //Count from current time
                duration = TimeUtil.currentTimeMillis() - currentWifiStatus.timeMillis;
            }else{ //Calculate duration from next element
                duration = wifiData.get(nextElement).timeMillis - currentWifiStatus.timeMillis;
            }

            //Insert value
            segmentValues[shiftedIndex] = TimeUtil.getHoursFromMillis(duration);

            int statusColor = (currentWifiStatus.state == NetworkInfo.State.CONNECTED)
                    ? wifiColor
                    : Color.TRANSPARENT;
            segmentColors.add(statusColor);

        }

        barEntries.add(new BarEntry(segmentValues, 0)); //All data share one row

        ArrayList<String> categories = new ArrayList<>();
        categories.add("Srsly"); //Dummy data

        //Create dataset
        BarDataSet dataSet = new BarDataSet(barEntries, ""); //All values get inserted
        dataSet.setBarSpacePercent(60);   //Set the bar size
        dataSet.setColors(segmentColors); //All barDataColors get inserted

        BarData data = new BarData(categories, dataSet);
        data.setDrawValues(false);

        return data;
    }

    private void intializeBarChartSettings(BarChart mChart){
        // change the position of the y-labels
        YAxis yLabels = mChart.getAxisLeft();
        yLabels.setValueFormatter(new DigitalTimeFormatter());
        mChart.getAxisRight().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);
        // scaling can now only be done on x- and y-axis separately
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(false);
        // set the size
        mChart.setMinimumHeight(500);
        //Remove axis labels, instead legend is better
        XAxis xAxis = mChart.getXAxis();
        xAxis.setDrawLabels(false);

        mChart.setMarkerView(new BarSegmentMarkerView(getActivity()));

    }

    private boolean hasValue(String SSID, ArrayList<Pair<String, Integer>> arrayList){
        for(Pair<String, Integer> p : arrayList){
            if(p.first.equals(SSID)){
                return true;
            }
        }
        return false;
    }

    private int getColor(int i){
        int color = -1;
        switch (i){
            case 0:
                color = Color.RED;
                break;
            case 1:
                color = Color.BLUE;
                break;
            case 2:
                color = Color.GREEN;
                break;
            case 3:
                color = Color.CYAN;
                break;
            case 4:
                color = Color.YELLOW;
                break;
            case 5:
                color = Color.MAGENTA;
                break;
        }
        return color;
    }

    private ArrayList<WifiDataState> getWifiStatusFromDatabase(){
        WifiStatesDatabase database = new WifiStatesDatabase(getActivity());
        return database.getAllData();
    }

    private class DigitalTimeFormatter implements YAxisValueFormatter {

        @Override
        public String getFormattedValue(float value, YAxis yAxis) {
            //return TimeUtil.ToHoursAndMinutesDigital((long) (value));
            return TimeUtil.TimeInDigitalFromHours(value);
        }
    }

    public class BarSegmentMarkerView extends MarkerView {

        private TextView tvContent;
        private float segmentSize;

        public BarSegmentMarkerView(Context context) {
            super(context, R.layout.marker_view);
            tvContent = (TextView) findViewById(R.id.tvContent);
        }

        // callbacks everytime the MarkerView is redrawn, can be used to update the
        // content (user-interface)
        @Override
        public void refreshContent(Entry e, Highlight highlight) {

            float segmentValue;

            if (e instanceof BarEntry) {

                BarEntry be = (BarEntry) e;

                if(be.getVals() != null) {
                    segmentValue = be.getVals()[highlight.getStackIndex()];
                } else {
                    segmentValue = be.getVal();
                }

                float barTotalSize = barChart.getBarBounds(be).right - barChart.getBarBounds(be).left;
                float totalBarValueSum = be.getPositiveSum();
                float percentageOfTotal = segmentValue / totalBarValueSum;
                segmentSize = barTotalSize * percentageOfTotal;

            } else {
                segmentValue = e.getVal();
            }

            tvContent.setText(TimeUtil.TimeInTextFromHours(segmentValue));
        }

        @Override
        public int getXOffset(float xpos) {
            // this will center the marker-view horizontally
            return (int)(-getWidth() / 2 - segmentSize / 2);
        }

        @Override
        public int getYOffset(float ypos) {
            // this will cause the marker-view to be above the selected value
            return -getHeight();
        }
    }
}
