package sjoholm.olof.wifitracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Models.WifiDurationSum;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Utils.DatabaseDataUtil;


/**
 * A simple {@link Fragment} subclass.
 */
public class DiagramFragment extends Fragment {


    private ListView listView;

    public DiagramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagram, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ArrayAdapter<WifiDurationSum> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, getWifiConnectionsFromToday());

        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }

    private ArrayList<WifiDurationSum> getWifiConnectionsFromToday(){
        Calendar calendar = Calendar.getInstance();
        return DatabaseDataUtil.getWifiData(getContext(), calendar.getTime());
    }

}
