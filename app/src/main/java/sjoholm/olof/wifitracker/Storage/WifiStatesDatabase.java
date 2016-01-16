package sjoholm.olof.wifitracker.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.NetworkInfo;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Models.WifiConnectionModel;
import sjoholm.olof.wifitracker.Models.WifiDuration;

/**
 * Created by olof on 2015-12-22.
 */
public class WifiStatesDatabase extends SQLiteOpenHelper {

    private static final String TAG = WifiStatesDatabase.class.getSimpleName();

    public static final int DATABASE_VERSION = 9;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String LONG_TYPE = " Long";

    private static final String DATABASE_NAME = "wifi_tracker.db";

    private static final String TABLE_CREATE = "CREATE TABLE " + Tables.WifiStatusChangedTable.TABLE_NAME +" ("
            + Tables.WifiStatusChangedTable._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
            + Tables.WifiStatusChangedTable.COLUMN_DATE + " DATE" + COMMA_SEP
            + Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME + TEXT_TYPE + COMMA_SEP
            + Tables.WifiStatusChangedTable.COLUMN_TIME_MILLIS + LONG_TYPE + COMMA_SEP
            + Tables.WifiStatusChangedTable.COLUMN_DURATION_MILLIS + LONG_TYPE
            + ")";

    private static final String TABLE_DELETE = "DROP TABLE IF EXISTS " + Tables.WifiStatusChangedTable.TABLE_NAME;

    public WifiStatesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Create table: " + TABLE_CREATE);
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TABLE_DELETE);
        onCreate(sqLiteDatabase);
    }

    private ContentValues toContentValues(WifiConnectionModel wifiConnectionModel){
        ContentValues values = new ContentValues();
        values.put(Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME, wifiConnectionModel.wifiName);
        values.put(Tables.WifiStatusChangedTable.COLUMN_DATE, wifiConnectionModel.date.toString());
        values.put(Tables.WifiStatusChangedTable.COLUMN_TIME_MILLIS, wifiConnectionModel.timeMillis);
        values.put(Tables.WifiStatusChangedTable.COLUMN_DURATION_MILLIS, wifiConnectionModel.durationMillis);
        return values;
    }

    public void insert(WifiConnectionModel insertDataState){

        SQLiteDatabase db = getWritableDatabase();
        db.insert(Tables.WifiStatusChangedTable.TABLE_NAME, null, toContentValues(insertDataState));

        Log.d(TAG, "Inserted : " + insertDataState.toString());
    }

    public void updatePreviousIfExists(WifiConnectionModel current){

        SQLiteDatabase db = getWritableDatabase();
        Cursor last = db.rawQuery("SELECT * FROM " + Tables.WifiStatusChangedTable.TABLE_NAME + " ORDER BY " + Tables.WifiStatusChangedTable._ID + " DESC LIMIT 1", new String[]{});

        if(last.getCount() == 1) {

            last.moveToFirst();

            int columnIndex = last.getColumnIndexOrThrow(Tables.WifiStatusChangedTable._ID);
            int id = last.getInt(columnIndex);

            WifiConnectionModel previous = cursorToModel(last);
            previous.durationMillis = current.timeMillis - previous.timeMillis; //Do the calculation

            update(previous, id);

        }
    }

    private void getMostRecent(){

    }

    public void update(WifiConnectionModel wifiConnectionModel, int id){
        SQLiteDatabase db = getWritableDatabase();
        db.update(
                Tables.WifiStatusChangedTable.TABLE_NAME,
                toContentValues(wifiConnectionModel),
                Tables.WifiStatusChangedTable._ID + " = " + String.valueOf(id),
                new String[]{});
    }

    public ArrayList<WifiConnectionModel> getAllData(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                Tables.WifiStatusChangedTable.TABLE_NAME,     //the table to query
                new String[]{},                             //the columns to return
                null,                                       //the columns for the where
                null,                                       //the values for the where clause
                null,                                       //dont group the rows
                null,                                       //dont filter by row groups
                null                                        //Sort order
        );

        return cursorToModels(c);
    }

    public ArrayList<WifiConnectionModel> getTodaysData(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * From " + Tables.WifiStatusChangedTable.TABLE_NAME + " where " + Tables.WifiStatusChangedTable.COLUMN_DATE + " >= date('now', 'start of day')";
        Cursor c = db.rawQuery(query, new String[]{});
        return cursorToModels(c);
    }

    public ArrayList<String> getTodaysWifis(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT DISTINCT " + Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME + " FROM " + Tables.WifiStatusChangedTable.TABLE_NAME;
        Cursor c = db.rawQuery(query, new String[]{});

        ArrayList<String> allWifiNames = new ArrayList<>();

        c.moveToFirst();

        while(!c.isAfterLast()){
            int index = c.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME);

            allWifiNames.add(c.getString(index));

            c.moveToNext();
        }

        return allWifiNames;
    }

    public ArrayList<WifiDuration> getTodaysWifiDuration(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select " + Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME + "" +
                ", total ( " + Tables.WifiStatusChangedTable.COLUMN_DURATION_MILLIS + ") as " + Tables.WifiStatusChangedTable.COLUMN_DURATION_MILLIS +
                ", " + Tables.WifiStatusChangedTable.COLUMN_TIME_MILLIS +
                " From " + Tables.WifiStatusChangedTable.TABLE_NAME +
                " where " + Tables.WifiStatusChangedTable.COLUMN_DATE + " >= date('now', 'start of day')" +
                " GROUP BY " + Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME +
                " order by " + Tables.WifiStatusChangedTable.COLUMN_TIME_MILLIS + " desc";




        ArrayList<WifiDuration> wifiList = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, new String[]{});
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {

            long firstElementTimeMillis = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_TIME_MILLIS));

            while (!cursor.isAfterLast()) {

                wifiList.add(curstorToModel(cursor));

                cursor.moveToNext();
            }

            if(wifiList.size() > 0){
                WifiDuration firstElement = wifiList.get(0);
                Log.d(TAG, "wifi " + firstElement.wifiName + ", time " + (Calendar.getInstance().getTime().getTime() - firstElementTimeMillis));
                firstElement.durationMillis += Calendar.getInstance().getTime().getTime() - firstElementTimeMillis; //Add time
            }

        }
        return wifiList;
    }



    private ArrayList<WifiConnectionModel> cursorToModels(Cursor cursor){
        ArrayList<WifiConnectionModel> items = new ArrayList<>();

        cursor.moveToFirst();

        while(!cursor.isAfterLast()){
            items.add(cursorToModel(cursor));
            cursor.moveToNext();
        }
        return items;
    }

    public void query(String sqlQuery){
        getReadableDatabase().execSQL(sqlQuery);
    }

    private WifiDuration curstorToModel(Cursor cursor){
        WifiDuration wifiDuration = new WifiDuration();

        wifiDuration.wifiName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME));
        wifiDuration.durationMillis = cursor.getLong(
                cursor.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_DURATION_MILLIS)
        );

        return wifiDuration;
    }

    public WifiConnectionModel cursorToModel(Cursor cursor){
        WifiConnectionModel wifiConnectionModel = new WifiConnectionModel();

        wifiConnectionModel.wifiName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME));
        wifiConnectionModel.date = Date.valueOf(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_DATE))
        );
        wifiConnectionModel.timeMillis = cursor.getLong(
                cursor.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_TIME_MILLIS)
        );
        wifiConnectionModel.durationMillis = cursor.getLong(
                cursor.getColumnIndexOrThrow(Tables.WifiStatusChangedTable.COLUMN_DURATION_MILLIS)
        );

        return wifiConnectionModel;
    }




}
