package sjoholm.olof.wifitracker.Storage;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;

import sjoholm.olof.wifitracker.Models.WifiModel;

public class ModelDatabase extends WifiDatabase{

    public ModelDatabase(Context context) {
        super(context);
    }

    public ArrayList<WifiModel.Detailed> getDetailedData() {
        Cursor c = getData(detailedFactory.getTable(), detailedFactory.getProjection());
        return detailedFactory.getModelsFromCursor(c);
    }

    private final ModelFactory detailedFactory = new ModelFactory<WifiModel.Detailed>() {
        @Override
        public WifiModel.Detailed fromCursor(Cursor c) {
            return null;
        }

        @Override
        public String getTable() {
            return null;
        }

        @Override
        public String[] getProjection() {
            return new String[0];
        }
    };
}

abstract class ModelFactory<WifiModel> {
    abstract WifiModel fromCursor(Cursor c);
    abstract String getTable();
    abstract String[] getProjection();

    public ArrayList<WifiModel> getModelsFromCursor(Cursor c) {
        c.moveToFirst();
        ArrayList<WifiModel> models = new ArrayList<>();
        while (!c.isAfterLast()) {
            WifiModel model = fromCursor(c);
            models.add(model);
        }
        return models;
    }
}


