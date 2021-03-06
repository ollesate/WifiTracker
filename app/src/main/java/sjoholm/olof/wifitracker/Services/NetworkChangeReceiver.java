package sjoholm.olof.wifitracker.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import java.sql.Date;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Storage.WifiDatabase;
import sjoholm.olof.wifitracker.Models.WifiConnectionModel;

/**
 * Created by olof on 2015-12-22.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    private static final String TAG = NetworkChangeReceiver.class.getSimpleName();
    private static final int NETWORK_TYPE_WIFI = 1;
    private static final int NETWORK_TYPE_SATELLITE = 0;

    @Override
    public void onReceive(Context context, Intent intent) {

        WifiDatabase db = new WifiDatabase(context);

        if(intent.getExtras().getInt("networkType") == NETWORK_TYPE_WIFI
                && !intent.getExtras().containsKey("otherNetwork")) { //endast om otherNetwork inte finns, för det sållar bort dubletter

            WifiConnectionModel wifiConnectionModel = getWifiEventFromExtra(intent.getExtras());

            if(disconnected(intent.getExtras())){
                db.updatePreviousIfExists(wifiConnectionModel);
            }else{
                db.insert(wifiConnectionModel);
            }

        }

    }

    private WifiConnectionModel getWifiEventFromExtra(Bundle bundle){
        WifiConnectionModel wifiConnectionModel = new WifiConnectionModel();

        NetworkInfo networkInfo = (NetworkInfo) bundle.get(WifiManager.EXTRA_NETWORK_INFO);

        wifiConnectionModel.date = new Date(Calendar.getInstance().getTimeInMillis()); //Hämtar stundens datum/tid
        wifiConnectionModel.wifiName = bundle.getString("extraInfo"); //Ger namnet på SSID
        wifiConnectionModel.timeMillis = Calendar.getInstance().getTimeInMillis();

        return wifiConnectionModel;
    }

    private boolean disconnected(Bundle bundle){
        NetworkInfo networkInfo = (NetworkInfo) bundle.get(WifiManager.EXTRA_NETWORK_INFO);
        return networkInfo.getState() == NetworkInfo.State.DISCONNECTED;
    }
}
