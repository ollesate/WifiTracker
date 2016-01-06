package sjoholm.olof.wifitracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Activities.MainActivity;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;
import sjoholm.olof.wifitracker.Utils.DialogUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class SummedDataFragment extends Fragment {

    private ListView listViewWifiData;
    private Button buttonMoreInfo;
    private ArrayAdapter adapter;

    public SummedDataFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summed_data, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1);

        listViewWifiData = (ListView) view.findViewById(R.id.lvWifis);

        buttonMoreInfo = (Button) view.findViewById(R.id.bMoreInfo);
        buttonMoreInfo.setOnClickListener(moreInfoClick);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView(){
        adapter.clear();
        adapter.addAll(getDataFromDatabase());
        listViewWifiData.setAdapter(adapter);
    }

    private ArrayList getDataFromDatabase(){
        WifiStatesDatabase db = new WifiStatesDatabase(getContext());
        return db.getTodaysWifiDuration();
    }

    private View.OnClickListener moreInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            DialogUtil.showMoreInfoDialog((MainActivity)getActivity());
        }
    };
}
