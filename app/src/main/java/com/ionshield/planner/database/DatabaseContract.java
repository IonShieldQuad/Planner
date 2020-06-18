package com.ionshield.planner.database;

import android.provider.BaseColumns;

public abstract class DatabaseContract {

    public static abstract class Items implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String NAME = "name";
        public static final String DESC = "description";

        public static final String NAME_INDEX = TABLE_NAME + "_" + NAME + "_index";
    }

    public static abstract class Types implements BaseColumns {
        public static final String TABLE_NAME = "types";
        public static final String NAME = "name";
        public static final String DESC = "description";
        public static final String COLOR = "color";

        public static final String NAME_INDEX = TABLE_NAME + "_" + NAME + "_index";
    }

    public static abstract class ItemTypeAssoc implements BaseColumns {
        public static final String TABLE_NAME = "item_assoc";
        public static final String ITEM_ID = "item_id";
        public static final String TYPE_ID = "type_id";

        public static final String FK_ITEM = "fk_item";
        public static final String FK_TYPE = "fk_type";

        public static final String ITEM_ID_INDEX = TABLE_NAME + "_" + ITEM_ID + "_index";
        public static final String TYPE_ID_INDEX = TABLE_NAME + "_" + TYPE_ID + "_index";
    }

    public static abstract class Locations implements BaseColumns {
        public static final String TABLE_NAME = "locations";
        public static final String NAME = "name";
        public static final String DESC = "description";
        public static final String TYPE_ID = "type_id";
        public static final String COORDINATE_X = "coordinate_x";
        public static final String COORDINATE_Y = "coordinate_y";

        public static final String FK_TYPE = "fk_type";

        public static final String NAME_INDEX = TABLE_NAME + "_" + NAME + "_index";
        public static final String TYPE_ID_INDEX = TABLE_NAME + "_" + TYPE_ID + "_index";
        public static final String COORDINATE_X_INDEX = TABLE_NAME + "_" + COORDINATE_X + "_index";
        public static final String COORDINATE_Y_INDEX = TABLE_NAME + "_" + COORDINATE_Y + "_index";
    }

    public static abstract class Events implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String ITEM_ID = "item_id";
        public static final String NAME = "name";
        public static final String DESC = "description";
        public static final String DATETIME_MIN = "datetime_min";
        public static final String DATETIME_MAX = "datetime_max";
        public static final String IS_DATE_USED = "is_date_used";

        public static final String FK_ITEM = "fk_item";

        public static final String NAME_INDEX = TABLE_NAME + "_" + NAME + "_index";
        public static final String ITEM_ID_INDEX = TABLE_NAME + "_" + ITEM_ID + "_index";
        public static final String DATETIME_MIN_INDEX = TABLE_NAME + "_" + DATETIME_MIN + "_index";
        public static final String DATETIME_MAX_INDEX = TABLE_NAME + "_" + DATETIME_MAX + "_index";
    }

    public static abstract class Plans implements BaseColumns {
        public static final String TABLE_NAME = "plans";
        public static final String NAME = "name";
        public static final String DESC = "description";

        public static final String NAME_INDEX = TABLE_NAME + "_" + NAME + "_index";
    }

    public static abstract class EventPlanAssoc implements BaseColumns {
        public static final String TABLE_NAME = "event_assoc";
        public static final String EVENT_ID = "event_id";
        public static final String PLAN_ID = "plan_id";

        public static final String FK_EVENT = "fk_event";
        public static final String FK_PLAN = "fk_plan";

        public static final String EVENT_ID_INDEX = TABLE_NAME + "_" + EVENT_ID + "_index";
        public static final String PLAN_ID_INDEX = TABLE_NAME + "_" + PLAN_ID + "_index";
    }

    public static final String SQL_EXISTS_ITEMS =
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + Items.TABLE_NAME + "';";
    public static final String SQL_EXISTS_TYPES =
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + Types.TABLE_NAME + "';";
    public static final String SQL_EXISTS_ITEM_ASSOC =
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + ItemTypeAssoc.TABLE_NAME + "';";
    public static final String SQL_EXISTS_LOCATIONS =
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + Locations.TABLE_NAME + "';";
    public static final String SQL_EXISTS_EVENTS =
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + Events.TABLE_NAME + "';";
    public static final String SQL_EXISTS_PLANS =
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + Plans.TABLE_NAME + "';";
    public static final String SQL_EXISTS_EVENT_ASSOC =
            "SELECT count(*) FROM sqlite_master WHERE type='table' AND name='" + EventPlanAssoc.TABLE_NAME + "';";

    public static final String SQL_CREATE_ITEMS =
            "CREATE TABLE IF NOT EXISTS " + Items.TABLE_NAME + " (" +
                    Items._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Items.NAME + " TEXT NOT NULL," +
                    Items.DESC + " TEXT);";

    public static final String SQL_CREATE_ITEMS_INDEX_NAME =
            "CREATE INDEX IF NOT EXISTS " + Items.NAME_INDEX + " ON " + Items.TABLE_NAME + "(" + Items.NAME + ");";

    public static final String SQL_CREATE_TYPES_INDEX_NAME =
            "CREATE INDEX IF NOT EXISTS " + Types.NAME_INDEX + " ON " + Types.TABLE_NAME + "(" + Types.NAME + ");";

    public static final String SQL_CREATE_ITEM_ASSOC_INDEX_ITEM_ID =
            "CREATE INDEX IF NOT EXISTS " + ItemTypeAssoc.ITEM_ID_INDEX + " ON " + ItemTypeAssoc.TABLE_NAME + "(" + ItemTypeAssoc.ITEM_ID + ");";
    public static final String SQL_CREATE_ITEM_ASSOC_INDEX_TYPE_ID =
            "CREATE INDEX IF NOT EXISTS " + ItemTypeAssoc.TYPE_ID_INDEX + " ON " + ItemTypeAssoc.TABLE_NAME + "(" + ItemTypeAssoc.TYPE_ID + ");";

    public static final String SQL_CREATE_LOCATIONS_INDEX_NAME =
            "CREATE INDEX IF NOT EXISTS " + Locations.NAME_INDEX + " ON " + Locations.TABLE_NAME + "(" + Locations.NAME + ");";
    public static final String SQL_CREATE_LOCATIONS_INDEX_TYPE_ID =
            "CREATE INDEX IF NOT EXISTS " + Locations.TYPE_ID_INDEX + " ON " + Locations.TABLE_NAME + "(" + Locations.TYPE_ID + ");";
    public static final String SQL_CREATE_LOCATIONS_INDEX_COORDINATE_X =
            "CREATE INDEX IF NOT EXISTS " + Locations.COORDINATE_X_INDEX + " ON " + Locations.TABLE_NAME + "(" + Locations.COORDINATE_X + ");";
    public static final String SQL_CREATE_LOCATIONS_INDEX_COORDINATE_Y =
            "CREATE INDEX IF NOT EXISTS " + Locations.COORDINATE_Y_INDEX + " ON " + Locations.TABLE_NAME + "(" + Locations.COORDINATE_Y + ");";

    public static final String SQL_CREATE_EVENTS_INDEX_NAME =
            "CREATE INDEX IF NOT EXISTS " + Events.NAME_INDEX + " ON " + Events.TABLE_NAME + "(" + Events.NAME + ");";
    public static final String SQL_CREATE_EVENTS_INDEX_ITEM_ID =
            "CREATE INDEX IF NOT EXISTS " + Events.ITEM_ID_INDEX + " ON " + Events.TABLE_NAME + "(" + Events.ITEM_ID + ");";
    public static final String SQL_CREATE_EVENTS_INDEX_DATETIME_MIN =
            "CREATE INDEX IF NOT EXISTS " + Events.DATETIME_MIN_INDEX + " ON " + Events.TABLE_NAME + "(" + Events.DATETIME_MIN + ");";
    public static final String SQL_CREATE_EVENTS_INDEX_DATETIME_MAX =
            "CREATE INDEX IF NOT EXISTS " + Events.DATETIME_MAX_INDEX + " ON " + Events.TABLE_NAME + "(" + Events.DATETIME_MAX + ");";

    public static final String SQL_CREATE_PLANS_INDEX_NAME =
            "CREATE INDEX IF NOT EXISTS " + Plans.NAME_INDEX + " ON " + Plans.TABLE_NAME + "(" + Plans.NAME + ");";

    public static final String SQL_CREATE_EVENT_ASSOC_INDEX_EVENT_ID =
            "CREATE INDEX IF NOT EXISTS " + EventPlanAssoc.EVENT_ID_INDEX + " ON " + EventPlanAssoc.TABLE_NAME + "(" + EventPlanAssoc.EVENT_ID + ");";
    public static final String SQL_CREATE_EVENT_ASSOC_INDEX_PLAN_ID =
            "CREATE INDEX IF NOT EXISTS " + EventPlanAssoc.PLAN_ID_INDEX + " ON " + EventPlanAssoc.TABLE_NAME + "(" + EventPlanAssoc.PLAN_ID + ");";

    public static final String SQL_CREATE_TYPES =
            "CREATE TABLE IF NOT EXISTS " + Types.TABLE_NAME + " (" +
                    Types._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Types.NAME + " TEXT NOT NULL," +
                    Types.DESC + " TEXT," +
                    Types.COLOR + " INTEGER);";

    public static final String SQL_CREATE_ITEM_ASSOC =
            "CREATE TABLE IF NOT EXISTS " + ItemTypeAssoc.TABLE_NAME + " (" +
                    ItemTypeAssoc._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ItemTypeAssoc.ITEM_ID + " INTEGER NOT NULL," +
                    ItemTypeAssoc.TYPE_ID + " INTEGER NOT NULL," +
                    " CONSTRAINT " + ItemTypeAssoc.FK_ITEM +
                    " FOREIGN KEY(" + ItemTypeAssoc.ITEM_ID + ")" +
                    " REFERENCES " + Items.TABLE_NAME + "(" + Items._ID + ")" +
                    " ON DELETE CASCADE" +
                    " ON UPDATE CASCADE," +
                    " CONSTRAINT " + ItemTypeAssoc.FK_TYPE +
                    " FOREIGN KEY(" + ItemTypeAssoc.TYPE_ID + ")" +
                    " REFERENCES " + Types.TABLE_NAME + "(" + Types._ID + ")" +
                    " ON DELETE CASCADE" +
                    " ON UPDATE CASCADE);";

    public static final String SQL_CREATE_LOCATIONS =
            "CREATE TABLE IF NOT EXISTS " + Locations.TABLE_NAME + " (" +
                    Locations._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Locations.NAME + " TEXT NOT NULL," +
                    Locations.DESC + " TEXT," +
                    Locations.TYPE_ID + " INTEGER NOT NULL," +
                    Locations.COORDINATE_X + " REAL NOT NULL," +
                    Locations.COORDINATE_Y + " REAl NOT NULL," +
                    " CONSTRAINT " + Locations.FK_TYPE +
                    " FOREIGN KEY(" + Locations.TYPE_ID + ")" +
                    " REFERENCES " + Types.TABLE_NAME + "(" + Types._ID + ")" +
                    " ON DELETE CASCADE" +
                    " ON UPDATE CASCADE);";

    public static final String SQL_CREATE_EVENTS =
            "CREATE TABLE IF NOT EXISTS " + Events.TABLE_NAME + " (" +
                    Events._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Events.ITEM_ID + " INTEGER NOT NULL," +
                    Events.NAME + " TEXT," +
                    Events.DESC + " TEXT," +
                    Events.DATETIME_MIN + " INTEGER," +
                    Events.DATETIME_MAX + " INTEGER," +
                    Events.IS_DATE_USED + " INTEGER NOT NULL," +
                    " CONSTRAINT " + Events.FK_ITEM +
                    " FOREIGN KEY(" + Events.ITEM_ID + ")" +
                    " REFERENCES " + Items.TABLE_NAME + "(" + Items._ID + ")" +
                    " ON DELETE CASCADE" +
                    " ON UPDATE CASCADE);";

    public static final String SQL_CREATE_PLANS =
            "CREATE TABLE IF NOT EXISTS " + Plans.TABLE_NAME + " (" +
                    Plans._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    Plans.NAME + " TEXT NOT NULL," +
                    Plans.DESC + " TEXT);";

    public static final String SQL_CREATE_EVENT_ASSOC =
            "CREATE TABLE IF NOT EXISTS " + EventPlanAssoc.TABLE_NAME + " (" +
                    EventPlanAssoc._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    EventPlanAssoc.EVENT_ID + " INTEGER NOT NULL," +
                    EventPlanAssoc.PLAN_ID + " INTEGER NOT NULL," +
                    " CONSTRAINT " + EventPlanAssoc.FK_EVENT +
                    " FOREIGN KEY(" + EventPlanAssoc.EVENT_ID + ")" +
                    " REFERENCES " + Events.TABLE_NAME + "(" + Events._ID + ")" +
                    " ON DELETE CASCADE" +
                    " ON UPDATE CASCADE," +
                    " CONSTRAINT " + EventPlanAssoc.FK_PLAN +
                    " FOREIGN KEY(" + EventPlanAssoc.PLAN_ID + ")" +
                    " REFERENCES " + Plans.TABLE_NAME + "(" + Plans._ID + ")" +
                    " ON DELETE CASCADE" +
                    " ON UPDATE CASCADE);";
}
