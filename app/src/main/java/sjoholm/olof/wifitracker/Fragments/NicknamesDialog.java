package sjoholm.olof.wifitracker.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.NicknameModel;
import sjoholm.olof.wifitracker.Models.WifiConnectionModel;
import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.Tables;
import sjoholm.olof.wifitracker.Storage.WifiDatabase;
import sjoholm.olof.wifitracker.Storage.WifiNicknamesDatabase;

/**
 * A simple {@link Fragment} subclass.
 */
public class NicknamesDialog extends Fragment {

    private ListView listView;
    private NickNameAdapter adapter = new NickNameAdapter(getContext());
    private WifiDatabase database;


    public NicknamesDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_nicknames_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.lvNicknames);
    }

    @Override
    public void onResume() {
        super.onResume();

        database = new WifiDatabase(getContext());
        ArrayList<NicknameModel> data = database.getAllNicknames();


        adapter.addAll(data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NicknameModel model = adapter.getItem(i);
                createDialog(model);
            }
        });
    }

    private void createDialog(final NicknameModel model){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(model.nickName);
        builder.setMessage("Change the display name");
        final EditText input = new EditText(getContext());
        builder.setView(input);
        builder.setPositiveButton("ACCEPT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                changedNickname(model);
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }

    private void changedNickname(NicknameModel model){
        database.update(model);
    }

    private class NickNameAdapter extends ArrayAdapter<NicknameModel>{

        public NickNameAdapter(Context context) {
            super(context, R.layout.nickname_row);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = convertView;

            if (view == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                view = vi.inflate(R.layout.nickname_row, null);
            }

            NicknameModel model = getItem(position);

            if (model != null) {
                TextView tvWifi = (TextView) view.findViewById(R.id.tvWifiName);
                EditText edNickName = (EditText) view.findViewById(R.id.edNewName);

                if (tvWifi != null) {
                    tvWifi.setText(model.wifiName.replaceAll("^\"|\"$", ""));
                }

                if (edNickName != null) {
                    edNickName.setText(model.nickName == null ? "" : model.nickName);
                }
            }

            return view;

        }
    }

}
