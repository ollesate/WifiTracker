package sjoholm.olof.wifitracker.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

/**
 * Created by olof on 2015-12-21.
 */
public class WifiReciever extends BroadcastReceiver{

    private static final String TAG = WifiReciever.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

    }

}
