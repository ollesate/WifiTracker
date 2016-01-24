package sjoholm.olof.wifitracker.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Models.NicknameModel;
import sjoholm.olof.wifitracker.Models.WifiConnectionModel;
import sjoholm.olof.wifitracker.Models.WifiDurationModel;

import static sjoholm.olof.wifitracker.Storage.SQLTypes.COMMA_SEP;
import static sjoholm.olof.wifitracker.Storage.SQLTypes.TEXT;

/**
 * Created by olof on 2015-12-22.
 */
public class WifiDatabase extends SQLiteOpenHelper {

    private static final String TAG = WifiDatabase.class.getSimpleName();

    public static final int DATABASE_VERSION = 10;

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ", ";
    private static final String LONG_TYPE = " Long";

    public static final String DATABASE_NAME = "wifi_tracker.db";

    private static final String TABLE_CREATE = "CREATE TABLE " + Tables.Wifis.TABLE_NAME +" ("
            + Tables.Wifis._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
            + Tables.Wifis.COLUMN_DATE + " DATE" + COMMA_SEP
            + Tables.Wifis.COLUMN_WIFI_NAME + TEXT_TYPE + COMMA_SEP
            + Tables.Wifis.COLUMN_TIME_MILLIS + LONG_TYPE + COMMA_SEP
            + Tables.Wifis.COLUMN_DURATION_MILLIS + LONG_TYPE
            + ")";

    private static final String TABLE_DELETE2 = "DROP TABLE IF EXISTS " + Tables.Nicknames.TABLE_NAME;
    private static final String TABLE_CREATE2 = "CREATE TABLE " + Tables.Nicknames.TABLE_NAME + "("
            + Tables.Nicknames._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP
            + Tables.Nicknames.COLUMN_WIFI_NAME + TEXT + COMMA_SEP
            + Tables.Nicknames.COLUMN_WIFI_NICKNAME + TEXT + "NOT NULL" + COMMA_SEP
            + "FOREIGN KEY (" + Tables.Nicknames.COLUMN_WIFI_NAME + ") REFERENCES "
                + Tables.Wifis.TABLE_NAME + "(" + Tables.Wifis.COLUMN_WIFI_NAME + ")"
            + ")";

    private static final String TABLE_DELETE = "DROP TABLE IF EXISTS " + Tables.Wifis.TABLE_NAME;

    public WifiDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d(TAG, "Create table: " + TABLE_CREATE2);
        sqLiteDatabase.execSQL(TABLE_CREATE2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TABLE_DELETE2);
        onCreate(sqLiteDatabase);
    }

    private ContentValues toContentValues(WifiConnectionModel wifiConnectionModel){
        ContentValues values = new ContentValues();
        values.put(Tables.Wifis.COLUMN_WIFI_NAME, wifiConnectionModel.wifiName);
        values.put(Tables.Wifis.COLUMN_DATE, wifiConnectionModel.date.toString());
        values.put(Tables.Wifis.COLUMN_TIME_MILLIS, wifiConnectionModel.timeMillis);
        values.put(Tables.Wifis.COLUMN_DURATION_MILLIS, wifiConnectionModel.durationMillis);
        return values;
    }

    public void insert(WifiConnectionModel insertDataState){

        SQLiteDatabase db = getWritableDatabase();
        db.insert(Tables.Wifis.TABLE_NAME, null, toContentValues(insertDataState));

        Log.d(TAG, "Inserted : " + insertDataState.toString());
    }

    public void updatePreviousIfExists(WifiConnectionModel current){

        SQLiteDatabase db = getWritableDatabase();
        Cursor last = db.rawQuery("SELECT * FROM " + Tables.Wifis.TABLE_NAME + " ORDER BY " + Tables.Wifis._ID + " DESC LIMIT 1", new String[]{});

        if(last.getCount() == 1) {

            last.moveToFirst();

            int columnIndex = last.getColumnIndexOrThrow(Tables.Wifis._ID);
            int id = last.getInt(columnIndex);

            WifiConnectionModel previous = cursorToModel(last);
            previous.durationMillis = current.timeMillis - previous.timeMillis; //Do the calculation

            update(previous, id);

        }
    }

    public void update(WifiConnectionModel wifiConnectionModel, int id){
        SQLiteDatabase db = getWritableDatabase();
        db.update(
                Tables.Wifis.TABLE_NAME,
                toContentValues(wifiConnectionModel),
                Tables.Wifis._ID + " = " + String.valueOf(id),
                new String[]{});
    }

    public ArrayList<WifiConnectionModel> getAllData(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(
                Tables.Wifis.TABLE_NAME,     //the table to query
                new String[]{},                             //the columns to return
                null,                                       //the columns for the where
                null,                                       //the values for the where clause
                null,                                       //dont group the rows
                null,                                       //dont filter by row groups
                null                                        //Sort order
        );

        return cursorToModels(c);
    }

    public ArrayList<WifiConnectionModel> getTodaysConnections(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from " + Tables.Wifis.TABLE_NAME +
                " where " + Tables.Wifis.COLUMN_DATE + " >= date('now', 'start of day')";

        Cursor c = db.rawQuery(query, new String[]{});

        ArrayList<WifiConnectionModel> allData = cursorToModels(c);

        if(allData.size() > 0){
            WifiConnectionModel lastElement = allData.get(allData.size()-1);
            if(lastElement.durationMillis == 0)
                lastElement.durationMillis = Calendar.getInstance().getTime().getTime() - lastElement.timeMillis; //Add time
        }

        return allData;
    }

    public ArrayList<WifiConnectionModel> getTodaysConnectionsOfSort(String wifiName){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from " + Tables.Wifis.TABLE_NAME +
                " where " + Tables.Wifis.COLUMN_DATE + " >= date('now', 'start of day')" +
                " AND " + Tables.Wifis.COLUMN_WIFI_NAME + " = '" + wifiName +"'";

        Cursor c = db.rawQuery(query, new String[]{});

        ArrayList<WifiConnectionModel> allData = cursorToModels(c);

        if(allData.size() > 0){
            WifiConnectionModel lastElement = allData.get(allData.size()-1);
            lastElement.durationMillis = Calendar.getInstance().getTime().getTime() - lastElement.timeMillis; //Add time
        }

        return allData;
    }

    public ArrayList<String> getTodaysWifiNames(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT DISTINCT " + Tables.Wifis.COLUMN_WIFI_NAME + " FROM " + Tables.Wifis.TABLE_NAME;
        Cursor c = db.rawQuery(query, new String[]{});

        ArrayList<String> allWifiNames = new ArrayList<>();

        c.moveToFirst();

        while(!c.isAfterLast()){
            int index = c.getColumnIndexOrThrow(Tables.Wifis.COLUMN_WIFI_NAME);

            allWifiNames.add(c.getString(index));

            c.moveToNext();
        }

        return allWifiNames;
    }

    public ArrayList<WifiDurationModel> getTodaysWifiDuration(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select " + Tables.Wifis.COLUMN_WIFI_NAME + "" +
                ", total ( " + Tables.Wifis.COLUMN_DURATION_MILLIS + ") as " + Tables.Wifis.COLUMN_DURATION_MILLIS +
                ", " + Tables.Wifis.COLUMN_TIME_MILLIS +
                " From " + Tables.Wifis.TABLE_NAME +
                " where " + Tables.Wifis.COLUMN_DATE + " >= date('now', 'start of day')" +
                " GROUP BY " + Tables.Wifis.COLUMN_WIFI_NAME +
                " order by " + Tables.Wifis.COLUMN_TIME_MILLIS + " desc";

        ArrayList<WifiDurationModel> wifiList = new ArrayList<>();

        Cursor cursor = db.rawQuery(query, new String[]{});
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {

            long firstElementTimeMillis = cursor.getLong(
                    cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_TIME_MILLIS));

            while (!cursor.isAfterLast()) {

                wifiList.add(curstorToModel(cursor));

                cursor.moveToNext();
            }

            if(wifiList.size() > 0){
                WifiDurationModel firstElement = wifiList.get(0);
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

    private WifiDurationModel curstorToModel(Cursor cursor){
        WifiDurationModel wifiDuration = new WifiDurationModel();

        wifiDuration.wifiName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_WIFI_NAME));
        wifiDuration.durationMillis = cursor.getLong(
                cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_DURATION_MILLIS)
        );

        return wifiDuration;
    }

    private WifiConnectionModel cursorToModel(Cursor cursor){
        WifiConnectionModel wifiConnectionModel = new WifiConnectionModel();

        wifiConnectionModel.wifiName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_WIFI_NAME));
        wifiConnectionModel.date = Date.valueOf(
                cursor.getString(
                        cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_DATE))
        );
        wifiConnectionModel.timeMillis = cursor.getLong(
                cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_TIME_MILLIS)
        );
        wifiConnectionModel.durationMillis = cursor.getLong(
                cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_DURATION_MILLIS)
        );

        return wifiConnectionModel;
    }

    public void query(String sqlQuery){
        getReadableDatabase().execSQL(sqlQuery);
    }

    public ArrayList<NicknameModel> getAllNicknames() {
        String sqlQuery =
                "Select " + Tables.Wifis.TABLE_NAME + "." + Tables.Wifis.COLUMN_WIFI_NAME + ", " +
                        Tables.Nicknames.TABLE_NAME + "." + Tables.Nicknames.COLUMN_WIFI_NICKNAME +
                        " From " + Tables.Wifis.TABLE_NAME +
                        " Left Join " + Tables.Nicknames.TABLE_NAME +
                        " On " + Tables.Wifis.TABLE_NAME + "." + Tables.Wifis.COLUMN_WIFI_NAME + " = " +
                        Tables.Nicknames.TABLE_NAME + "." + Tables.Nicknames.COLUMN_WIFI_NICKNAME;

        Log.d(TAG, sqlQuery);

        Cursor cursor = getWritableDatabase().rawQuery(sqlQuery, new String[]{});
        return cursorToNicknameModels(cursor);
    }

    public ArrayList<NicknameModel> cursorToNicknameModels(Cursor cursor){
        ArrayList<NicknameModel> models = new ArrayList<>();

        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            models.add(cursorToNicknameModel(cursor));
            cursor.moveToNext();
        }

        return models;
    }

    public NicknameModel cursorToNicknameModel(Cursor cursor){
        NicknameModel model = new NicknameModel();

        model.wifiName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_WIFI_NAME)
        );

        model.nickName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.Nicknames.COLUMN_WIFI_NICKNAME)
        );

        return model;
    }

    public void update(NicknameModel model) {
//        SQLiteDatabase db = getWritableDatabase();
//        db.update(
//                Tables.Wifis.TABLE_NAME,
//                toContentValues(model),
//                Tables.Nicknames.COLUMN_WIFI_NAME + " = " + model.wifiName,
//                new String[]{});
        //db.insertWithOnConflict(SQLiteDatabase.CONFLICT_REPLACE)

    }

    public ContentValues toContentValues(NicknameModel model){

        return null;
    }
}
