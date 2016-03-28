package sjoholm.olof.wifitracker.Storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.WifiModel;

/**
 * Created by olof on 2016-03-28.
 */
public class EventDatabase extends TableDatabase {

    public EventDatabase(Context context) {
        super(context);
    }

    @Override
    Table initializeTable() {
        return new Tables.EventTable();
    }

    public ArrayList<WifiModel.Event> getEvents() {
        Cursor c = fetch(table.getProjection());
        c.moveToFirst();

        ArrayList<WifiModel.Event> events = new ArrayList<>();
        if(!c.isAfterLast()) {
            events.add((WifiModel.Event) table.modelFromCursor(c));
        }

        return events;
    }

    public void add(WifiModel.Event event) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Tables.EventTable.COLUMN_SSID, event.wifiName);
        contentValues.put(Tables.EventTable.COLUMN_TIME, event.time);
        insert(contentValues);
    }
}
