package sjoholm.olof.wifitracker.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import sjoholm.olof.wifitracker.Models.NicknameModel;
import sjoholm.olof.wifitracker.Models.WifiConnectionModel;
import sjoholm.olof.wifitracker.Models.WifiDurationModel;

/**
 * Created by olof on 2015-12-22.
 */
public class WifiDatabase extends SQLiteOpenHelper {

    private static final String TAG = WifiDatabase.class.getSimpleName();
    private SQLiteDatabase db = getReadableDatabase();

    public static final int DATABASE_VERSION = 10;

    private static final String TEXT_TYPE = " TEXT_TYPE";
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
            + Tables.Nicknames.COLUMN_WIFI_NAME + SQLTypes.TEXT_TYPE + COMMA_SEP
            + Tables.Nicknames.COLUMN_WIFI_NICKNAME + SQLTypes.TEXT_TYPE + "NOT NULL" + COMMA_SEP
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

    public void update(Table table, ContentValues contentValues, int id) {
        db.update(table.getTableName(), contentValues, table._ID+ " = " + String.valueOf(id), null);
    }

    public void updateLastInserted(Table table, ContentValues contentValues) {
        String[] projection = contentValues.keySet().toArray(new String[]{});
        Cursor c = db.query(table.getTableName(), projection, null, null, null, null, table._ID, "1");
        if (c.getCount() == 1) {
            c.moveToFirst();
            update(table, contentValues, c.getInt(c.getColumnIndexOrThrow(table._ID)));
        }
    }

    public ArrayList<WifiConnectionModel> getTodaysConnections(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from " + Tables.Wifis.TABLE_NAME +
                " where " + Tables.Wifis.COLUMN_DATE + " >= date('now', 'start of time')";

        Cursor c = db.rawQuery(query, new String[]{});

        ArrayList<WifiConnectionModel> allData = cursorToModels(c);

        if(allData.size() > 0){
            WifiConnectionModel lastElement = allData.get(allData.size()-1);
            if(lastElement.durationMillis == 0)
                lastElement.durationMillis = Calendar.getInstance().getTime().getTime() - lastElement.timeMillis; //Add time
        }


        return allData;
    }

    protected Cursor getData(String table, String[] projection) {
        return db.query(table, projection, null, null, null, null, null);
    }

    public ArrayList<WifiConnectionModel> getTodaysConnectionsOfSort(String wifiName){
        SQLiteDatabase db = getReadableDatabase();
        String query = "Select * from " + Tables.Wifis.TABLE_NAME +
                " where " + Tables.Wifis.COLUMN_DATE + " >= date('now', 'start of time')" +
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
                " where " + Tables.Wifis.COLUMN_DATE + " >= date('now', 'start of time')" +
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

    public void query(String sqlQuery){
        getReadableDatabase().execSQL(sqlQuery);
    }

    //Throw away
    public ArrayList<NicknameModel> getAllNicknames() {
        return null;
    }
}
