package sjoholm.olof.wifitracker.Storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.NicknameModel;

import static sjoholm.olof.wifitracker.Storage.SQLTypes.*;
/**
 * Created by olof on 2015-12-28.
 */
public class WifiNicknamesDatabase extends SQLiteOpenHelper{

    private static String TAG = WifiNicknamesDatabase.class.getSimpleName();

    private static final String TABLE_NAME = Tables.Nicknames.TABLE_NAME;
    private static final String TABLE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + Tables.Nicknames.COLUMN_WIFI_NAME + TEXT_TYPE +
                "FOREIGN KEY REFERENCES " + Tables.Wifis.TABLE_NAME + "(" + Tables.Wifis.COLUMN_WIFI_NAME + ")" + COMMA_SEP
            + Tables.Nicknames.COLUMN_WIFI_NICKNAME + TEXT_TYPE + "NOT NULL"
            + ");";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wifi_tracker_nicknames.db";

    public WifiNicknamesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(TABLE_DELETE);
        onCreate(sqLiteDatabase);
    }

    public void insert(){

    }

    public void update(){

    }

    public ArrayList<NicknameModel> getAll(){
        String sqlQuery =
                "Select " + Tables.Wifis.TABLE_NAME + "." + Tables.Wifis.COLUMN_WIFI_NAME + ", " +
                Tables.Nicknames.TABLE_NAME + "." + Tables.Nicknames.COLUMN_WIFI_NICKNAME +
                " From " + Tables.Wifis.TABLE_NAME +
                " Left Join " + Tables.Nicknames.COLUMN_WIFI_NICKNAME +
                " On " + Tables.Wifis.TABLE_NAME + "." + Tables.Wifis.COLUMN_WIFI_NAME + " = " +
                Tables.Nicknames.TABLE_NAME + " . " + Tables.Nicknames.COLUMN_WIFI_NICKNAME;

        Log.d(TAG, sqlQuery);

        Cursor cursor = getWritableDatabase().rawQuery(sqlQuery, new String[]{});
        return cursorToModels(cursor);
    }

    public ArrayList<NicknameModel> cursorToModels(Cursor cursor){
        ArrayList<NicknameModel> models = new ArrayList<>();

        cursor.moveToFirst();
        if(!cursor.isAfterLast()){
            models.add(cursorToModel(cursor));
            cursor.moveToNext();
        }

        return models;
    }

    public NicknameModel cursorToModel(Cursor cursor){
        NicknameModel model = new NicknameModel();

        model.nickName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.Wifis.COLUMN_WIFI_NAME)
        );

        model.wifiName = cursor.getString(
                cursor.getColumnIndexOrThrow(Tables.Nicknames.COLUMN_WIFI_NICKNAME)
        );

        return model;
    }


}
