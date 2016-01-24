package sjoholm.olof.wifitracker.Utils;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import java.io.Serializable;
import java.util.ArrayList;

import sjoholm.olof.wifitracker.Activities.MainActivity;
import sjoholm.olof.wifitracker.Fragments.HistoryDialogFragment;
import sjoholm.olof.wifitracker.Models.WifiConnectionModel;

/**
 * Created by olof on 2016-01-01.
 */
public class DialogUtil {

    public static void showDialog(MainActivity activity, DialogFragment dialog){
        FragmentManager fm = activity.getSupportFragmentManager();
        dialog.show(fm, "fragment_edit_name");
    }

    public static void showMoreInfoDialog(MainActivity context, ArrayList<WifiConnectionModel> data){
        HistoryDialogFragment fragment = new HistoryDialogFragment();
        Bundle extras = new Bundle();
        extras.putSerializable(HistoryDialogFragment.EXTRA_LISTVIEW_DATA, (Serializable) data);
        fragment.setArguments(extras);
        showDialog(context, fragment);
    }

}
