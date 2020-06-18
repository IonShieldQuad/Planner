package com.ionshield.planner.database;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.provider.BaseColumns;


public class DatabaseHelper extends SQLiteOpenHelper {

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }


    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PlanningDatabase.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseContract.SQL_CREATE_ITEMS);
        db.execSQL(DatabaseContract.SQL_CREATE_TYPES);
        db.execSQL(DatabaseContract.SQL_CREATE_ITEM_ASSOC);
        db.execSQL(DatabaseContract.SQL_CREATE_LOCATIONS);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENTS);
        db.execSQL(DatabaseContract.SQL_CREATE_PLANS);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENT_ASSOC);

        db.execSQL(DatabaseContract.SQL_CREATE_ITEMS_INDEX_NAME);
        db.execSQL(DatabaseContract.SQL_CREATE_TYPES_INDEX_NAME);
        db.execSQL(DatabaseContract.SQL_CREATE_ITEM_ASSOC_INDEX_ITEM_ID);
        db.execSQL(DatabaseContract.SQL_CREATE_ITEM_ASSOC_INDEX_TYPE_ID);
        db.execSQL(DatabaseContract.SQL_CREATE_LOCATIONS_INDEX_NAME);
        db.execSQL(DatabaseContract.SQL_CREATE_LOCATIONS_INDEX_TYPE_ID);
        db.execSQL(DatabaseContract.SQL_CREATE_LOCATIONS_INDEX_COORDINATE_X);
        db.execSQL(DatabaseContract.SQL_CREATE_LOCATIONS_INDEX_COORDINATE_Y);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENTS_INDEX_NAME);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENTS_INDEX_ITEM_ID);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENTS_INDEX_DATETIME_MIN);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENTS_INDEX_DATETIME_MAX);
        db.execSQL(DatabaseContract.SQL_CREATE_PLANS_INDEX_NAME);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENT_ASSOC_INDEX_EVENT_ID);
        db.execSQL(DatabaseContract.SQL_CREATE_EVENT_ASSOC_INDEX_PLAN_ID);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void delete(SQLiteDatabase db, String table, int id) {
        SQLiteStatement stmt = db.compileStatement("DELETE FROM " + table + " WHERE "+ BaseColumns._ID + "=?;");
        stmt.bindLong(1, id);
        stmt.execute();
    }

    public void truncate(SQLiteDatabase db, String table) {
        String sql = "DELETE FROM " + table + ";";
        db.execSQL(sql);
    }

}
