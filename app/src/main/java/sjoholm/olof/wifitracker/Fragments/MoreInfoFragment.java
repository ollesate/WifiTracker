package sjoholm.olof.wifitracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInfoFragment extends DialogFragment {


    public MoreInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_more_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);
        adapter.addAll(getDatabaseData());

        ListView listViewWifiData;
        listViewWifiData = (ListView) view.findViewById(R.id.lvWifiDataMoreInfo);
        listViewWifiData.setAdapter(adapter);

    }

    private ArrayList getDatabaseData(){
        WifiStatesDatabase db = new WifiStatesDatabase(getContext());
        return db.getTodaysData();
    }
}
