package sjoholm.olof.wifitracker;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.sql.Date;
import java.util.ArrayList;

import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;

/**
 * Created by olof on 2016-01-01.
 */
public class CustomBarChart extends BarChart {

    private StackedBarColumn legendList;
    private StackedBarCategories categories;
    private ArrayList<StackedBarColumn> columns = new ArrayList<>();

    public CustomBarChart(Context context) {
        super(context);
    }

    public CustomBarChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomBarChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setLegend(){
        Legend legend = this.getLegend();
        legend.setTextColor(Color.BLACK); //Färg

    }

    public void displayDays(int day){

    }

    public void displayWeeks(int week){

    }

    public void displayMonths(int day){

    }

    public void displayYears(int years){

    }

    private void setBarData(/*Parameters*/){
        //Legends
        //X-axis: Years, Months, Weeks, Days
        //Limit nr of X-axis (eg. 7 for week, 12 for months, not all days, not all years)
    }

    private void getDatabaseData(Date from, Date to){

    }

    public void setStackedBarCategories(ArrayList<String> wifiNames) {
        categories  = new StackedBarCategories();
        categories.titles = wifiNames;
    }

    public void initialize() {
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        Log.d("Column Size", columns.size() +"" );
        for(int i = 0; i < columns.size(); i++){
            StackedBarColumn column = columns.get(i);
            BarEntry barEntry = new BarEntry(column.getValues(), i, "bob");
            barEntries.add(barEntry);
        }

        ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();

        float range = 100;
        for (int i = 0; i < 5; i++) {
            yVals.add(new BarEntry((float) (Math.random() * range), i));
        }


        WifiStatesDatabase db = new WifiStatesDatabase(getContext());

        DayData dayData = new DayData();
        ArrayList<BarEntry> dayEntries = dayData.getBarEntries(db.getTodaysData());

        BarDataSet dataSet = new BarDataSet(dayEntries, "MyLabel :)");

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.CYAN);
        colors.add(Color.GREEN);

        dataSet.setColors(colors);
//        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
//        dataSets.add(dataSet);


        ArrayList<String> xCats = new ArrayList<String>();
        for(int i = 0; i < 24; i++){
            String hour = (i < 10) ? "0" + i : i + "";
            xCats.add(hour);
        }




        BarData barData = new BarData(dayData.getXCategories(), dataSet);
        setData(barData);

        setDrawValueAboveBar(true);

        setDescription("");

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        setMaxVisibleValueCount(60);


        setDrawGridBackground(false);

        YAxis yr = getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(true);
        yr.setDrawLabels(false);

        XAxis xl = getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);
        xl.setTextColor(Color.MAGENTA);
        xl.setSpaceBetweenLabels(1);

        Legend l = getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);
        l.setCustom(colors, categories.titles);


//        setDoubleTapToZoomEnabled(false);
//
//        setDragEnabled(false);
//
//        setScaleEnabled(false);

        setTouchEnabled(false);
//        yr.setInverted(true);

        // mChart.setDrawYLabels(false);

        //setData(5, 100);
    }

    private void setData(int count, float range) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < count; i++) {
            xVals.add("val " + i);
            yVals1.add(new BarEntry((float) (Math.random() * range), i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet 1");
        set1.setColors(new int[]{
                Color.GREEN,
                Color.RED,
                Color.CYAN
        });

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, set1);
        data.setValueTextSize(10f);

        setData(data);
    }



    public void addColumnData(ArrayList<Float> dummyData, String wifiName) {

        StackedBarColumn column = new StackedBarColumn(categories);
        for(float f : dummyData) {
            String wifi = (Math.random() > 0.5) ? categories.titles.get(0) : categories.titles.get(1);
            column.addValue(wifi, f);
        }
        columns.add(column);
    }

    private class StackedBarCategories{

        public ArrayList<String> titles = new ArrayList<>();

        private int findIndexOf(String titleFind){

            for(int i = 0; i < titles.size(); i++){
                String currentTitle = titles.get(i);
                if(currentTitle.equals(titleFind)){
                    return i;
                }
            }
            return -1;
        }

        private boolean isValidIndex(int i){
            return i != -1;
        }

        public int size(){
            return titles.size();
        }

    }

    private class StackedBarColumn {

        public ArrayList<Float> barDataValue = new ArrayList<>();

        private StackedBarCategories categories;

        public StackedBarColumn(StackedBarCategories stackedBarCategories) {
            this.categories = stackedBarCategories;

            for(int i = 0; i < stackedBarCategories.size(); i++){//Create correct nr of columns
                barDataValue.add(0.0f);
            }
        }

        public void addValue(String categoryName, float value){

            int index = categories.findIndexOf(categoryName);

            if(categories.isValidIndex(index)){
                barDataValue.set(index, value);
            }else{
                //NO SUCH THING EXISTS
                Log.d("Error", "NO SUCH THING");
            }

        }

        public BarEntry getBarEntry(int index){
            float [] values = new float[barDataValue.size()];
            for(int i = 0; i < barDataValue.size(); i++){
                values[i] = barDataValue.get(i);
            }
            return new BarEntry(values, index);
        }

        public float [] getValues(){
            Log.d("Get Values", "");
            Log.d("Size", barDataValue.size() +"");
            float [] values = new float[barDataValue.size()];
            for(int i = 0; i < barDataValue.size(); i++){
                values[i] = barDataValue.get(i);
                Log.d("Values", values[i] + "");
            }
            Log.d("Values", "return");
            return values;
        }

    }

}
