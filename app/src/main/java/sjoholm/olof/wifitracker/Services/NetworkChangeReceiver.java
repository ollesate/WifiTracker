package sjoholm.olof.wifitracker.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import java.sql.Date;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Storage.WifiStatesDatabase;
import sjoholm.olof.wifitracker.Models.WifiDataState;

/**
 * Created by olof on 2015-12-22.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkChangeReceiver.class.getSimpleName();
    private static final int NETWORK_TYPE_WIFI = 1;
    private static final int NETWORK_TYPE_SATELLITE = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        WifiStatesDatabase db = new WifiStatesDatabase(context);

        if(intent.getExtras().getInt("networkType") == NETWORK_TYPE_WIFI
                && !intent.getExtras().containsKey("otherNetwork")) { //endast om otherNetwork inte finns, för det sållar bort dubletter

            WifiDataState wifiDataState = getWifiEventFromExtra(intent.getExtras());

            if(disconnected(intent.getExtras())){
                db.updatePreviousIfExists(wifiDataState);
            }else{
                db.insert(wifiDataState);
            }

        }

    }

    private WifiDataState getWifiEventFromExtra(Bundle bundle){
        WifiDataState wifiDataState = new WifiDataState();

        NetworkInfo networkInfo = (NetworkInfo) bundle.get(WifiManager.EXTRA_NETWORK_INFO);

        wifiDataState.state = networkInfo.getState();
        wifiDataState.date = new Date(Calendar.getInstance().getTimeInMillis()); //Hämtar stundens datum/tid
        wifiDataState.wifiName = bundle.getString("extraInfo"); //Ger namnet på SSID
        wifiDataState.timeMillis = Calendar.getInstance().getTimeInMillis();

        return wifiDataState;
    }

    private boolean disconnected(Bundle bundle){
        NetworkInfo networkInfo = (NetworkInfo) bundle.get(WifiManager.EXTRA_NETWORK_INFO);
        return networkInfo.getState() == NetworkInfo.State.DISCONNECTED;
    }
}
