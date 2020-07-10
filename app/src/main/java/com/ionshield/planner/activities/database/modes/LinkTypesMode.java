package com.ionshield.planner.activities.database.modes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CursorAdapter;
import android.widget.TextView;

import androidx.arch.core.util.Function;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.BaseAdapter;
import com.ionshield.planner.activities.database.editors.LinkTypesEditActivity;
import com.ionshield.planner.database.DBC;

public class LinkTypesMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.link_types_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, LinkTypesEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT * FROM " + DBC.LinkTypes.TABLE_NAME + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT * FROM " + DBC.LinkTypes.TABLE_NAME + " WHERE " + DBC.LinkTypes.NAME + " LIKE ?;", new String[]{"%" + searchString + "%"});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.link_types_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.link_types_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView colorView = view.findViewById(R.id.color);
        TextView nameView = view.findViewById(R.id.name);
        TextView speedView = view.findViewById(R.id.speed);
        CheckBox isEnabledCheckBox = view.findViewById(R.id.is_enabled_checkbox);

        int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Types._ID));
        String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Types.NAME));
        int color = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Types.COLOR));
        int width = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.LinkTypes.WIDTH));
        double speed = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.LinkTypes.SPEED));
        boolean enabled = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.LinkTypes.ENABLED)) != 0;

        idView.setText(String.valueOf(id));
        colorView.setBackgroundColor(color + 0xFF000000);
        nameView.setText(name);
        colorView.getLayoutParams().height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, view.getResources().getDisplayMetrics());
        speedView.setText(String.valueOf(speed));
        isEnabledCheckBox.setChecked(enabled);
    }

    @Override
    public String getTableName() {
        return DBC.LinkTypes.TABLE_NAME;
    }

    @Override
    public CursorAdapter getEditorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback) {
        return new BaseAdapter(context, cursor, flags,this, callback, false);
    }

    @Override
    public CursorAdapter getSelectorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback) {
        return new BaseAdapter(context, cursor, flags,this, callback, true);
    }
}
