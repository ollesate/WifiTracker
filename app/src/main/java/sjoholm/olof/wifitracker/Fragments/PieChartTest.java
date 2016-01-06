package sjoholm.olof.wifitracker.Fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Models.WifiDurationSum;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Utils.TimeUtil;
import sjoholm.olof.wifitracker.Utils.DatabaseDataUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartTest extends Fragment {

    private PieChart pieChart;

    public PieChartTest() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pie_chart_test, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pieChart = (PieChart) view.findViewById(R.id.pie_chart);
        initilizePieChart(pieChart);
    }

    private void initilizePieChart(PieChart mChart){
        mChart.setDescription("");

        mChart.setCenterText(generateCenterText());
        mChart.setCenterTextSize(10f);

        // radius of the center hole in percent of maximum radius
        mChart.setHoleRadius(45f);
        mChart.setTransparentCircleRadius(50f);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);

        mChart.setData(getPieData());
        mChart.setOnClickListener(onChartClick);
    }

    private SpannableString generateCenterText() {
        SpannableString s = new SpannableString("Wifis\nConnection Time");
        s.setSpan(new RelativeSizeSpan(2f), 0, 6, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 6, s.length(), 0);
        return s;
    }

    protected PieData getPieData() {


        ArrayList<WifiDurationSum> data = DatabaseDataUtil.getWifiData(getActivity(), Calendar.getInstance().getTime());

        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<Entry> entries1 = new ArrayList<>();

        for(int i = 0; i < data.size(); i++){
            WifiDurationSum d = data.get(i);
            xVals.add(d.wifiName);
            Entry entry = new Entry(TimeUtil.getHoursFromMillis(d.durationConnectedInMillis), i);
            entries1.add(entry);
        }

        PieDataSet ds1 = new PieDataSet(entries1, "Connected Wifis from today");
        ds1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        ds1.setSliceSpace(2f);
        ds1.setValueTextColor(Color.WHITE);
        ds1.setValueTextSize(12f);

        PieData d = new PieData(xVals, ds1);
        return d;
    }

    private View.OnClickListener onChartClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.d("TAG", "CLICK");
        }
    };
}
