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
import com.ionshield.planner.activities.database.editors.LocationsEditActivity;
import com.ionshield.planner.database.DBC;

public class LocationsMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.locations_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, LocationsEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT " + DBC.Locations.TABLE_NAME + "." + DBC.Locations._ID + ", "
                + DBC.Locations.TABLE_NAME + "." + DBC.Locations.NAME + ", "
                + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " AS x, "
                + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " AS y, "
                + DBC.Types.TABLE_NAME + "." + DBC.Types.NAME + " AS type FROM " + DBC.Locations.TABLE_NAME
                + " JOIN " + DBC.Types.TABLE_NAME + " ON " + DBC.Types.TABLE_NAME + "." + DBC.Types._ID + "="
                + DBC.Locations.TABLE_NAME + "." + DBC.Locations.TYPE_ID
                + " LEFT JOIN " + DBC.Nodes.TABLE_NAME + " ON " + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes._ID + "="
                + DBC.Locations.TABLE_NAME + "." + DBC.Locations.NODE_ID
                + ";", new String[]{});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.Locations.TABLE_NAME + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT " + DBC.Locations.TABLE_NAME + "." + DBC.Locations._ID + ", "
                + DBC.Locations.TABLE_NAME + "." + DBC.Locations.NAME + ", "
                + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " AS x, "
                + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " AS y, "
                + DBC.Types.TABLE_NAME + "." + DBC.Types.NAME + " AS type FROM " + DBC.Locations.TABLE_NAME
                + " JOIN " + DBC.Types.TABLE_NAME + " ON " + DBC.Types.TABLE_NAME + "." + DBC.Types._ID + "="
                + DBC.Locations.TABLE_NAME + "." + DBC.Locations.TYPE_ID
                + " LEFT JOIN " + DBC.Nodes.TABLE_NAME + " ON " + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes._ID + "="
                + DBC.Locations.TABLE_NAME + "." + DBC.Locations.NODE_ID
                + " WHERE " + DBC.Locations.TABLE_NAME + "." + DBC.Locations.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.Locations.TABLE_NAME + " WHERE " + DatabaseContract.Locations.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.locations_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.locations_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView nameView = view.findViewById(R.id.name);
        TextView typeIdView = view.findViewById(R.id.type_id);
        TextView coordinateXView = view.findViewById(R.id.coordinate_x);
        TextView coordinateYView = view.findViewById(R.id.coordinate_y);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Locations._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Locations.NAME));
        //int typeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.TYPE_ID));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
        Double coordinateX = null;
        Double coordinateY = null;
        if (!cursor.isNull(cursor.getColumnIndexOrThrow("x"))) {
            coordinateX = cursor.getDouble(cursor.getColumnIndexOrThrow("x"));
        }
        if (!cursor.isNull(cursor.getColumnIndexOrThrow("y"))) {
            coordinateY = cursor.getDouble(cursor.getColumnIndexOrThrow("y"));
        }

        idView.setText(String.valueOf(id));
        nameView.setText(name);
        typeIdView.setText(String.valueOf(type));
        coordinateXView.setText(coordinateX == null ? "" : String.valueOf(coordinateX));
        coordinateYView.setText(coordinateY == null ? "" : String.valueOf(coordinateY));
    }

    @Override
    public String getTableName() {
        return DBC.Locations.TABLE_NAME;
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
