package sjoholm.olof.wifitracker.Fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.HistoryItemAdapter;
import sjoholm.olof.wifitracker.Models.WifiConnectionModel;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryDialogFragment extends DialogFragment {

    public static String EXTRA_LISTVIEW_DATA = "listviewData";

    public HistoryDialogFragment() {
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

        HistoryItemAdapter adapter = new HistoryItemAdapter(getContext(), android.R.layout.simple_list_item_1);

        Bundle bundle = getArguments();
        if(bundle.containsKey(EXTRA_LISTVIEW_DATA)){
            ArrayList<WifiConnectionModel> list = (ArrayList< WifiConnectionModel> )bundle.get(EXTRA_LISTVIEW_DATA);
            adapter.clear();
            adapter.addAll(list);
        }

        ListView listViewWifiData;
        listViewWifiData = (ListView) view.findViewById(R.id.lvWifiDataMoreInfo);
        listViewWifiData.setAdapter(adapter);

    }

    private ArrayList getDataFromExtra(){
        WifiDatabase db = new WifiDatabase(getContext());
        return db.getTodaysConnections();
    }
}
