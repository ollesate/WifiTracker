package sjoholm.olof.wifitracker.Testing;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.R;
import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;

/**
 * Created by olof on 2016-01-01.
 */
public class DatabaseDialog extends DialogFragment{

    private ListView listView;

    public DatabaseDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_database, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        listView = (ListView) view.findViewById(R.id.listView2);
        ArrayList databaseItems = getDatabaseItems();
        listView.setAdapter(getFilledAdapter(databaseItems));
    }

    private ArrayAdapter getFilledAdapter(ArrayList items){
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, items);
        return adapter;
    }

    private ArrayList getDatabaseItems(){
        WifiStatesDatabase db = new WifiStatesDatabase(getContext());
        return db.getAllData();
    }

}
