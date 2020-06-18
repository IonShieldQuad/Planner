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
import com.ionshield.planner.activities.database.editors.ItemsEditActivity;
import com.ionshield.planner.database.DatabaseContract;

public class ItemsMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.items_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, ItemsEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public CursorAdapter getEditorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback) {
        return new BaseAdapter(context, cursor, flags, this, callback, false);
    }

    @Override
    public CursorAdapter getSelectorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback) {
        return new BaseAdapter(context, cursor, flags, this, callback, true);
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + DatabaseContract.Items.TABLE_NAME + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT * FROM " + DatabaseContract.Items.TABLE_NAME + " WHERE " + DatabaseContract.Items.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.items_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.items_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView nameView = view.findViewById(R.id.name);
        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Items._ID));
        String name = cursor.getString((cursor.getColumnIndexOrThrow(DatabaseContract.Items.NAME)));
        idView.setText(String.valueOf(id));
        nameView.setText(name);
    }

    @Override
    public String getTableName() {
        return DatabaseContract.Items.TABLE_NAME;
    }
}
