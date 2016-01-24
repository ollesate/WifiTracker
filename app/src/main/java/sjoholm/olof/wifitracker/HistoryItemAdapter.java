package sjoholm.olof.wifitracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import sjoholm.olof.wifitracker.Models.WifiConnectionModel;
import sjoholm.olof.wifitracker.Utils.TimeUtil;

/**
 * Created by olof on 2016-01-16.
 */
public class HistoryItemAdapter extends ArrayAdapter<WifiConnectionModel> {

    public HistoryItemAdapter(Context context, int resource) {
        super(context, resource);
    }

    public HistoryItemAdapter(Context context, int resource, List<WifiConnectionModel> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.wifi_row, null);
        }

        WifiConnectionModel model = getItem(position);

        if (model != null) {
            TextView tvWifi = (TextView) v.findViewById(R.id.tvWifiName);
            TextView tvTime = (TextView) v.findViewById(R.id.tvTimeOfDay);
            TextView tvDuration = (TextView) v.findViewById(R.id.tvDuration);

            if (tvWifi != null) {
                String trimmed = trimQuatationMarks(model.wifiName);
                tvWifi.setText(trimmed);
            }

            if (tvTime != null) {
                tvTime.setText(getTimeFromTimeMillis(model.timeMillis));
            }

            if (tvDuration != null) {
                tvDuration.setText(getDurationFromTimeMIllis(model.durationMillis));
            }
        }

        return v;
    }

    private String getTimeFromTimeMillis(long timeMillis){
        return TimeUtil.ToTimeInDigital(getContext(), timeMillis);
    }

    private String getDurationFromTimeMIllis(long durationMillis){
        return TimeUtil.DurationInHoursAndMinutes(durationMillis);
    }

    private String trimQuatationMarks(String text){
        return text.replaceAll("^\"|\"$", "");
    }

}
