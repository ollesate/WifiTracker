package sjoholm.olof.wifitracker.Storage;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import sjoholm.olof.wifitracker.Models.WifiModel;

/**
 * Created by olof on 2015-12-22.
 */
public class Tables {

    static class EventTable extends Table {
        public final static String TABLE_NAME = "EventTable.Table";
        public final static String COLUMN_SSID = "EventTable.SSID";
        public final static String COLUMN_TIME = "EventTable.TIME";

        public final static String TABLE_CREATE = "Create Table " + TABLE_NAME + "(" +
                _ID + " Integer Primary key" + COMMA_SEP +
                COLUMN_SSID + TEXT_TYPE + COMMA_SEP +
                COLUMN_TIME + LONG_TYPE +
                ")";

        @Override
        String getTableName() {
            return TABLE_NAME;
        }

        @Override
        void createTable(SQLiteDatabase database) {
            TableBuilder builder = new TableBuilder(TABLE_NAME);
            builder.addColumn(new TableBuilder.Column(COLUMN_SSID).asInteger().asPrimaryKey());
            builder.addColumn(new TableBuilder.Column(COLUMN_TIME).asLong());
            database.execSQL(builder.build());
        }

        @Override
        String[] getProjection() {
            return new String[]{COLUMN_SSID, COLUMN_TIME};
        }

        @Override
        WifiModel modelFromCursor(Cursor cursor) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_SSID));
            Long time = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_TIME));
            return new WifiModel.Event(name, time);
        }

    }

}
