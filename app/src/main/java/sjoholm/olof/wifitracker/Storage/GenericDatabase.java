package sjoholm.olof.wifitracker.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by olof on 2016-03-28.
 */
public class GenericDatabase {

    private SQLiteDatabase database;

    public GenericDatabase(SQLiteDatabase database) {
        this.database = database;
    }

    public void insert(String tableName, ContentValues contentValues) {
        database.insert(tableName, null, contentValues);
    }

    public void update(String tableName, ContentValues contentValues){
        database.update(tableName, contentValues, null, null);
    }

    public void update(String tableName, ContentValues contentValues, int id){
        database.update(tableName, contentValues, null, null);
    }

    public Cursor fetch(String tableName, String[] projection) {
        return database.query(tableName, projection, null, null, null, null, null);
    }

}
