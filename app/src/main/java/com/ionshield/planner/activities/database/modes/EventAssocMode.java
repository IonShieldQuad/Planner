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
import com.ionshield.planner.activities.database.editors.EventAssocEditActivity;
import com.ionshield.planner.activities.database.editors.PlansEditActivity;
import com.ionshield.planner.database.DatabaseContract;

public class EventAssocMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.event_assoc_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, EventAssocEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT " + DatabaseContract.EventPlanAssoc.TABLE_NAME + "." + DatabaseContract.EventPlanAssoc._ID + ", "
                + DatabaseContract.Events.TABLE_NAME + "." + DatabaseContract.Events.NAME + " AS event, "
                + DatabaseContract.Plans.TABLE_NAME + "." + DatabaseContract.Plans.NAME + " AS p FROM " + DatabaseContract.EventPlanAssoc.TABLE_NAME
                + " JOIN " + DatabaseContract.Events.TABLE_NAME + " ON " + DatabaseContract.Events.TABLE_NAME + "." + DatabaseContract.Events._ID + "="
                + DatabaseContract.EventPlanAssoc.TABLE_NAME + "." + DatabaseContract.EventPlanAssoc.EVENT_ID
                + " JOIN " + DatabaseContract.Plans.TABLE_NAME + " ON " + DatabaseContract.Plans. TABLE_NAME + "." + DatabaseContract.Plans._ID + "="
                + DatabaseContract.EventPlanAssoc.TABLE_NAME + "." + DatabaseContract.EventPlanAssoc.PLAN_ID + ";", new String[]{});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.EventPlanAssoc.TABLE_NAME + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT " + DatabaseContract.EventPlanAssoc.TABLE_NAME + "." + DatabaseContract.EventPlanAssoc._ID + ", "
                + DatabaseContract.Events.TABLE_NAME + "." + DatabaseContract.Events.NAME + " AS event, "
                + DatabaseContract.Plans.TABLE_NAME + "." + DatabaseContract.Plans.NAME + " AS p FROM " + DatabaseContract.EventPlanAssoc.TABLE_NAME
                + " JOIN " + DatabaseContract.Events.TABLE_NAME + " ON " + DatabaseContract.Events.TABLE_NAME + "." + DatabaseContract.Events._ID + "="
                + DatabaseContract.EventPlanAssoc.TABLE_NAME + "." + DatabaseContract.EventPlanAssoc.EVENT_ID
                + " JOIN " + DatabaseContract.Plans.TABLE_NAME + " ON " + DatabaseContract.Plans. TABLE_NAME + "." + DatabaseContract.Plans._ID + "="
                + DatabaseContract.EventPlanAssoc.TABLE_NAME + "." + DatabaseContract.EventPlanAssoc.PLAN_ID
                + " WHERE (event LIKE ? OR p LIKE ?);", new String[]{"%" + searchString + "%", "%" + searchString + "%"});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.EventPlanAssoc.TABLE_NAME + " WHERE (" + DatabaseContract.EventPlanAssoc.EVENT_ID + "=? OR " + DatabaseContract.EventPlanAssoc.PLAN_ID + "=?);", new String[]{searchString, searchString});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.event_assoc_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.event_assoc_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView eventIdView = view.findViewById(R.id.event_id);
        TextView planIdView = view.findViewById(R.id.plan_id);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.EventPlanAssoc._ID));
        //int eventId = cursor.getInt((cursor.getColumnIndexOrThrow(DatabaseContract.EventPlanAssoc.EVENT_ID)));
        //int planId = cursor.getInt((cursor.getColumnIndexOrThrow(DatabaseContract.EventPlanAssoc.PLAN_ID)));
        String event = cursor.getString((cursor.getColumnIndexOrThrow("event")));
        String plan = cursor.getString((cursor.getColumnIndexOrThrow("p")));

        idView.setText(String.valueOf(id));
        eventIdView.setText(String.valueOf(event));
        planIdView.setText(String.valueOf(plan));
    }

    @Override
    public String getTableName() {
        return DatabaseContract.EventPlanAssoc.TABLE_NAME;
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
