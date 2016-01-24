package sjoholm.olof.wifitracker.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import sjoholm.olof.wifitracker.Activities.MainActivity;
import sjoholm.olof.wifitracker.R;

/**
 * Created by olof on 2015-12-27.
 */
public class Fragments {

    public static void showFragment(MainActivity activity, Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public static void addFragment(MainActivity activity, Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack("")
                .commit();
    }

    public static void showDataFragment(MainActivity activity){
        WifiDataFragment fragment = new WifiDataFragment();
        showFragment(activity, fragment);
    }

    public static void showNicknamesFragment(MainActivity activity){
        NicknamesDialog fragment = new NicknamesDialog();
        addFragment(activity, fragment);
    }

}
