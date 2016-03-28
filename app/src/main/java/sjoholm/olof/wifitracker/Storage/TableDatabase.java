package sjoholm.olof.wifitracker.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by olof on 2016-03-28.
 */
public abstract class TableDatabase extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "wifi_tracker.db";

    protected Table table;

    private static final int DB_VERSION = 1;

    public TableDatabase(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        table = initializeTable();
    }

    abstract Table initializeTable();

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        table.createTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop table if exists " + table.getTableName());
        table.createTable(sqLiteDatabase);
    }

    public void insert(ContentValues contentValues) {
        getWritableDatabase().insert(table.getTableName(), null, contentValues);
    }

    public void update(ContentValues contentValues){
        getWritableDatabase().update(table.getTableName(), contentValues, null, null);
    }

    public void update(ContentValues contentValues, int id){
        getWritableDatabase().update(table.getTableName(), contentValues, null, null);
    }

    public Cursor fetch(String[] projection) {
        return getReadableDatabase().query(table.getTableName(), projection, null, null, null, null, null);
    }
}
