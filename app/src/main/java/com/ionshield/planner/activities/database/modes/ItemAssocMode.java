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
import com.ionshield.planner.activities.database.editors.ItemAssocEditActivity;
import com.ionshield.planner.database.DBC;

public class ItemAssocMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.item_assoc_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, ItemAssocEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT " + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc._ID + ", "
                + DBC.Items.TABLE_NAME + "." + DBC.Items.NAME + " AS item, "
                + DBC.Types.TABLE_NAME + "." + DBC.Types.NAME + " AS type FROM " + DBC.ItemTypeAssoc.TABLE_NAME
                + " JOIN " + DBC.Items.TABLE_NAME + " ON " + DBC.Items.TABLE_NAME + "." + DBC.Items._ID + "="
                + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc.ITEM_ID
                + " JOIN " + DBC.Types.TABLE_NAME + " ON " + DBC.Types. TABLE_NAME + "." + DBC.Types._ID + "="
                + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc.TYPE_ID + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT " + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc._ID + ", "
                + DBC.Items.TABLE_NAME + "." + DBC.Items.NAME + " AS item, "
                + DBC.Types.TABLE_NAME + "." + DBC.Types.NAME + " AS type FROM " + DBC.ItemTypeAssoc.TABLE_NAME
                + " JOIN " + DBC.Items.TABLE_NAME + " ON " + DBC.Items.TABLE_NAME + "." + DBC.Items._ID + "="
                + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc.ITEM_ID
                + " JOIN " + DBC.Types.TABLE_NAME + " ON " + DBC.Types. TABLE_NAME + "." + DBC.Types._ID + "="
                + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc.TYPE_ID
                + " WHERE (item LIKE ? OR type LIKE ?);", new String[]{"%" + searchString + "%", "%" + searchString + "%"});
        //return db.rawQuery("SELECT * FROM " + DatabaseContract.ItemTypeAssoc.TABLE_NAME + " WHERE (" + DatabaseContract.ItemTypeAssoc.ITEM_ID + "=? OR " + DatabaseContract.ItemTypeAssoc.TYPE_ID + "=?);", new String[]{searchString, searchString});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.item_assoc_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.item_assoc_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView itemIdView = view.findViewById(R.id.item_id);
        TextView typeIdView = view.findViewById(R.id.type_id);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.ItemTypeAssoc._ID));
        //int itemId = cursor.getInt((cursor.getColumnIndexOrThrow(DatabaseContract.ItemTypeAssoc.ITEM_ID)));
        //int typeId = cursor.getInt((cursor.getColumnIndexOrThrow(DatabaseContract.ItemTypeAssoc.TYPE_ID)));
        String item = cursor.getString((cursor.getColumnIndexOrThrow("item")));
        String type = cursor.getString((cursor.getColumnIndexOrThrow("type")));

        idView.setText(String.valueOf(id));
        itemIdView.setText(String.valueOf(item));
        typeIdView.setText(String.valueOf(type));
    }

    @Override
    public String getTableName() {
        return DBC.ItemTypeAssoc.TABLE_NAME;
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
