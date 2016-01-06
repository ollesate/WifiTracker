package sjoholm.olof.wifitracker.Storage;

import android.provider.BaseColumns;

/**
 * Created by olof on 2015-12-22.
 */
public class Tables {

    public Tables(){

    }

    public static abstract class WifiStatusChangedTable implements BaseColumns{
        public static final String TABLE_NAME = "WifiChanged";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_STATUS = "Status";
        public static final String COLUMN_WIFI_NAME = "WifiName";
        public static final String COLUMN_TIME_MILLIS = "TimeMillis";
        public static final String COLUMN_DURATION_MILLIS = "DurationMillis";
    }

    public static abstract class WifiNicknamesTable implements  BaseColumns{
        public static final String TABLE_NAME = "WifiNicknames";
        public static final String COLUMN_WIFI_NAME = "WifiName";
        public static final String COLUMN_WIFI_NICKNAME = "WifiNickname";
    }

}
