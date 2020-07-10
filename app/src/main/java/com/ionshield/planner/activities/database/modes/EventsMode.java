package com.ionshield.planner.activities.database.modes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.arch.core.util.Function;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.BaseAdapter;
import com.ionshield.planner.activities.database.editors.EventsEditActivity;
import com.ionshield.planner.database.DBC;

import java.text.DateFormat;
import java.util.Date;

public class EventsMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.events_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, EventsEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT " + DBC.Events.TABLE_NAME + "." + DBC.Events._ID + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.NAME + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.DATETIME_MIN + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.DATETIME_MAX + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.IS_DATE_USED + ", "
                + DBC.Items.TABLE_NAME + "." + DBC.Items.NAME + " AS item FROM " + DBC.Events.TABLE_NAME
                + " JOIN " + DBC.Items.TABLE_NAME + " ON " + DBC.Items.TABLE_NAME + "." + DBC.Items._ID + "="
                + DBC.Events.TABLE_NAME + "." + DBC.Events.ITEM_ID + ";", new String[]{});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.Events.TABLE_NAME + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT " + DBC.Events.TABLE_NAME + "." + DBC.Events._ID + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.NAME + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.DATETIME_MIN + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.DATETIME_MAX + ", "
                + DBC.Events.TABLE_NAME + "." + DBC.Events.IS_DATE_USED + ", "
                + DBC.Items.TABLE_NAME + "." + DBC.Items.NAME + " AS item FROM " + DBC.Events.TABLE_NAME
                + " JOIN " + DBC.Items.TABLE_NAME + " ON " + DBC.Items.TABLE_NAME + "." + DBC.Items._ID + "="
                + DBC.Events.TABLE_NAME + "." + DBC.Events.ITEM_ID
                + " WHERE " + DBC.Events.TABLE_NAME + "." + DBC.Events.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.Events.TABLE_NAME + " WHERE " + DatabaseContract.Events.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.events_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.events_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView nameView = view.findViewById(R.id.name);
        TextView itemIdView = view.findViewById(R.id.item_id);
        TextView datetimeMinView = view.findViewById(R.id.datetime_min);
        TextView datetimeMaxView = view.findViewById(R.id.datetime_max);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Events._ID));
        String name = cursor.getString((cursor.getColumnIndexOrThrow(DBC.Events.NAME)));
        //int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Events.ITEM_ID));
        String item = cursor.getString(cursor.getColumnIndexOrThrow("item"));

        DateFormat df;
        boolean useDate = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Events.IS_DATE_USED)) != 0;
        int datetimeMinIndex = cursor.getColumnIndex(DBC.Events.DATETIME_MIN);
        int datetimeMaxIndex = cursor.getColumnIndex(DBC.Events.DATETIME_MAX);
        if (useDate) {
            df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
        }
        else {
            df = DateFormat.getTimeInstance(DateFormat.SHORT);
        }
        String datetimeMin = "";
        String datetimeMax = "";
        if (datetimeMinIndex >= 0) {
            long millis = cursor.getLong(datetimeMinIndex);
            if (millis != -1) datetimeMin = df.format(new Date(millis));
        }
        if (datetimeMaxIndex >= 0) {
            long millis = cursor.getLong(datetimeMaxIndex);
            if (millis != -1) datetimeMax = df.format(new Date(millis));
        }

        idView.setText(String.valueOf(id));
        nameView.setText(name);
        itemIdView.setText(String.valueOf(item));
        datetimeMinView.setText(datetimeMin);
        datetimeMaxView.setText(datetimeMax);
    }

    @Override
    public String getTableName() {
        return DBC.Events.TABLE_NAME;
    }

    @Override
    public CursorAdapter getEditorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback) {
        return new BaseAdapter(context, cursor, flags, this, callback, false);
    }

    @Override
    public CursorAdapter getSelectorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback) {
        return new BaseAdapter(context, cursor, flags, this, callback, true);
    }
}
