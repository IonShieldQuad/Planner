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
import com.ionshield.planner.activities.database.editors.NodesEditActivity;
import com.ionshield.planner.database.DBC;

public class NodesMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.nodes_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, NodesEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME + " WHERE " + DBC.Nodes.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.nodes_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.nodes_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView nameView = view.findViewById(R.id.name);
        TextView coordinateXView = view.findViewById(R.id.coordinate_x);
        TextView coordinateYView = view.findViewById(R.id.coordinate_y);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Nodes._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Nodes.NAME));
        double coordinateX = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_X));
        double coordinateY = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_Y));

        idView.setText(String.valueOf(id));
        nameView.setText(name);
        coordinateXView.setText(String.valueOf(coordinateX));
        coordinateYView.setText(String.valueOf(coordinateY));
    }

    @Override
    public String getTableName() {
        return DBC.Nodes.TABLE_NAME;
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
