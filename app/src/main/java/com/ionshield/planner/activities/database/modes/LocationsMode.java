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
import com.ionshield.planner.database.DatabaseContract;

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
        return db.rawQuery("SELECT " + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations._ID + ", "
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.NAME + ", "
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_X + ", "
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_Y + ", "
                + DatabaseContract.Types.TABLE_NAME + "." + DatabaseContract.Types.NAME + " AS type FROM " + DatabaseContract.Locations.TABLE_NAME
                + " JOIN " + DatabaseContract.Types.TABLE_NAME + " ON " + DatabaseContract.Types. TABLE_NAME + "." + DatabaseContract.Types._ID + "="
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.TYPE_ID + ";", new String[]{});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.Locations.TABLE_NAME + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT " + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations._ID + ", "
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.NAME + ", "
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_X + ", "
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_Y + ", "
                + DatabaseContract.Types.TABLE_NAME + "." + DatabaseContract.Types.NAME + " AS type FROM " + DatabaseContract.Locations.TABLE_NAME
                + " JOIN " + DatabaseContract.Types.TABLE_NAME + " ON " + DatabaseContract.Types. TABLE_NAME + "." + DatabaseContract.Types._ID + "="
                + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.TYPE_ID
                + " WHERE " + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
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

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Locations._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.NAME));
        //int typeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.TYPE_ID));
        String type = cursor.getString(cursor.getColumnIndexOrThrow("type"));
        double coordinateX = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.COORDINATE_X));
        double coordinateY = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.COORDINATE_Y));

        idView.setText(String.valueOf(id));
        nameView.setText(name);
        typeIdView.setText(String.valueOf(type));
        coordinateXView.setText(String.valueOf(coordinateX));
        coordinateYView.setText(String.valueOf(coordinateY));
    }

    @Override
    public String getTableName() {
        return DatabaseContract.Locations.TABLE_NAME;
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
