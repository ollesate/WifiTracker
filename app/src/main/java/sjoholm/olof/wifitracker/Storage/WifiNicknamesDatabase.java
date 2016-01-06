package sjoholm.olof.wifitracker.Storage;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by olof on 2015-12-28.
 */
public class WifiNicknamesDatabase extends SQLiteOpenHelper{

    private static final String COMMA_SEP = ", ";

    private static final String TABLE_NAME = Tables.WifiNicknamesTable.TABLE_NAME;
    private static final String TABLE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    private static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + Tables.WifiNicknamesTable.COLUMN_WIFI_NAME + SQLTypes.TEXT +
                " FOREIGN KEY REFERENCES " + Tables.WifiStatusChangedTable.TABLE_NAME + "(" + Tables.WifiStatusChangedTable.COLUMN_WIFI_NAME + ")" + COMMA_SEP
            + Tables.WifiNicknamesTable.COLUMN_WIFI_NICKNAME + SQLTypes.TEXT + "NOT NULL"
            + ");";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "wifi_tracker.db";

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

    public void getAll(){

    }

    public void cursorToModels(Cursor cursor){

    }

    public void cursorToModel(Cursor cursor){

    }


}
