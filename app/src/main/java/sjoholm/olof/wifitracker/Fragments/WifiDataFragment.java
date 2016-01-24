package sjoholm.olof.wifitracker.Fragments;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Activities.MainActivity;
import sjoholm.olof.wifitracker.Models.WifiConnectionModel;
import sjoholm.olof.wifitracker.Models.WifiDurationModel;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiDatabase;
import sjoholm.olof.wifitracker.Utils.DialogUtil;
import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class WifiDataFragment extends Fragment {

    private ListView listViewWifiData;
    private Button buttonMoreInfo;
    private ArrayAdapter adapter;
    private ArrayList<WifiDurationModel> wifiData;
    private WifiDatabase database;

    public WifiDataFragment() {
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

        adapter = new CustomAdapter(getContext());

        listViewWifiData = (ListView) view.findViewById(R.id.lvWifis);
        listViewWifiData.setOnItemClickListener(itemClick);

        buttonMoreInfo = (Button) view.findViewById(R.id.bMoreInfo);
        buttonMoreInfo.setOnClickListener(moreInfoClick);

    }

    @Override
    public void onResume() {
        super.onResume();
        database = new WifiDatabase(getContext());
        updateListView(database.getTodaysWifiDuration());
    }

    private void updateListView(ArrayList<WifiDurationModel> wifiData){
        this.wifiData = wifiData;
        adapter.clear();
        adapter.addAll(wifiData);
        listViewWifiData.setAdapter(adapter);
    }

    private View.OnClickListener moreInfoClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ArrayList<WifiConnectionModel> data = database.getTodaysConnections();
            DialogUtil.showMoreInfoDialog((MainActivity)getActivity(), data);
        }
    };

    private class CustomAdapter extends ArrayAdapter<WifiDurationModel>{

        public CustomAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = LayoutInflater.from(getContext());
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }

            WifiDurationModel model = getItem(position);

            if (model != null) {
                TextView tvWifi = (TextView) v.findViewById(android.R.id.text1);

                if (tvWifi != null) {
                    tvWifi.setText(getSpannableString(model.wifiName
                            , TimeUtil.DurationInHoursAndMinutes(model.durationMillis)));
                }
            }

            return v;
        }

        private SpannableString getSpannableString(String wifiName, String timeText){
            wifiName = wifiName.replaceAll("^\"|\"$", "");
            SpannableString styledString = new SpannableString(wifiName + ": " + timeText);
            styledString.setSpan(new StyleSpan(Typeface.BOLD), 0, wifiName.length(), 0); //Bold
            return styledString;
        }
    }

    private AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            String wifiName = wifiData.get(i).wifiName;
            ArrayList<WifiConnectionModel> data = database.getTodaysConnectionsOfSort(wifiName);
            DialogUtil.showMoreInfoDialog((MainActivity)getActivity(), data);
        }
    };

}
