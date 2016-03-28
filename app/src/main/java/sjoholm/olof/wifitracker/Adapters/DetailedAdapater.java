package sjoholm.olof.wifitracker.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.WifiModel;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.ModelDatabase;

/**
 * Created by olof on 2016-03-15.
 */
public class DetailedAdapater extends ArrayAdapter<WifiModel.Detailed>{

    private ModelDatabase wifiDatabaseImpl;

    public DetailedAdapater(Context context, String wifiName) {
        super(context, android.R.layout.simple_list_item_1);

        wifiDatabaseImpl = new ModelDatabase();

        ArrayList<WifiModel.Detailed> wifiData;
        if (wifiName == null) {
            wifiData = wifiDatabaseImpl.getAllWifiConnections();
        } else {
            wifiData = wifiDatabaseImpl.getWifiConnections(wifiName);
        }
        addAll(wifiData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        }

        WifiModel.Detailed item = getItem(position);

        if (item != null) {
            TextView title = (TextView) convertView.findViewById(R.id.title);
            title.setText(item.wifiName);

            TextView start = (TextView) convertView.findViewById(R.id.start_time);
            start.setText(String.valueOf(item.startTime));

            TextView end = (TextView) convertView.findViewById(R.id.end_time);
            end.setText(String.valueOf(item.endTime));

            TextView duration = (TextView) convertView.findViewById(R.id.duration);
            duration.setText(String.valueOf(item.duration));
        }

        return convertView;
    }

}
