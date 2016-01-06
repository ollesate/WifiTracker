package sjoholm.olof.wifitracker.Utils;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import sjoholm.olof.wifitracker.Activities.MainActivity;
import sjoholm.olof.wifitracker.Fragments.MoreInfoFragment;

/**
 * Created by olof on 2016-01-01.
 */
public class DialogUtil {

    public static void showDialog(MainActivity activity, DialogFragment dialog){
        FragmentManager fm = activity.getSupportFragmentManager();
        dialog.show(fm, "fragment_edit_name");
    }

    public static void showMoreInfoDialog(MainActivity context){
        showDialog(context, new MoreInfoFragment());
    }

}
