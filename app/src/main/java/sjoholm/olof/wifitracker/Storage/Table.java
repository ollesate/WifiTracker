package sjoholm.olof.wifitracker.Storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.WifiModel;

/**
 * Created by olof on 2016-03-23.
 */
public abstract class Table implements BaseColumns, SQLTypes {
    abstract String getTableName();
    abstract void createTable(SQLiteDatabase database);
    abstract String[] getProjection();
    abstract WifiModel modelFromCursor(Cursor cursor);
}



