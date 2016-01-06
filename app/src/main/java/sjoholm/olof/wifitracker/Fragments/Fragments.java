package sjoholm.olof.wifitracker.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import sjoholm.olof.wifitracker.Activities.MainActivity;
import sjoholm.olof.wifitracker.R;

/**
 * Created by olof on 2015-12-27.
 */
public class Fragments {

    public static void showDiagramFragment(MainActivity activity){
        Fragment fragment = new DiagramFragment();
        showFragment(activity, fragment);
    }

    public static void showPieChartFragment(MainActivity activity){
        Fragment fragment = new BarChartTest();
        showFragment(activity, fragment);
    }

    public static void showFragment(MainActivity activity, Fragment fragment){
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    public static void showDataFragment(MainActivity activity){
        SummedDataFragment fragment = new SummedDataFragment();
        showFragment(activity, fragment);
    }

}
