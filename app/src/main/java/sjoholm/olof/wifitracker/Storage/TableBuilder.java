package sjoholm.olof.wifitracker.Storage;

import java.util.ArrayList;

public class TableBuilder {
    private String tableName;
    private ArrayList<Column> columns = new ArrayList<>();

    public TableBuilder (String table) {
        tableName = table;
    }

    public TableBuilder addColumn(Column column) {
        return this;
    }

    public String build() {
        String build = "Create table " + tableName + "(";
        for (int i = 0; i < columns.size(); i++) {
            build += columns.get(i) + (i < columns.size()-1 ? ", " : "");
        }
        return build + ")";
    }

    public static class Column {
        protected String name;
        protected String type;
        protected boolean isKey;

        public Column(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name + " " + type + (isKey ? " Primary Key " : "");
        }

        public Column asPrimaryKey() {
            isKey = true;
            return this;
        }

        public Column asText() {
            type = "Text";
            return this;
        }

        public Column asLong() {
            type = "Long";
            return  this;
        }

        public Column asInteger() {
            type = "Integer";
            return this;
        }
    }
}