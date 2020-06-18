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
import com.ionshield.planner.database.DatabaseContract;

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
        return db.rawQuery("SELECT " + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc._ID + ", "
                + DatabaseContract.Items.TABLE_NAME + "." + DatabaseContract.Items.NAME + " AS item, "
                + DatabaseContract.Types.TABLE_NAME + "." + DatabaseContract.Types.NAME + " AS type FROM " + DatabaseContract.ItemTypeAssoc.TABLE_NAME
                + " JOIN " + DatabaseContract.Items.TABLE_NAME + " ON " + DatabaseContract.Items.TABLE_NAME + "." + DatabaseContract.Items._ID + "="
                + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc.ITEM_ID
                + " JOIN " + DatabaseContract.Types.TABLE_NAME + " ON " + DatabaseContract.Types. TABLE_NAME + "." + DatabaseContract.Types._ID + "="
                + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc.TYPE_ID + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT " + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc._ID + ", "
                + DatabaseContract.Items.TABLE_NAME + "." + DatabaseContract.Items.NAME + " AS item, "
                + DatabaseContract.Types.TABLE_NAME + "." + DatabaseContract.Types.NAME + " AS type FROM " + DatabaseContract.ItemTypeAssoc.TABLE_NAME
                + " JOIN " + DatabaseContract.Items.TABLE_NAME + " ON " + DatabaseContract.Items.TABLE_NAME + "." + DatabaseContract.Items._ID + "="
                + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc.ITEM_ID
                + " JOIN " + DatabaseContract.Types.TABLE_NAME + " ON " + DatabaseContract.Types. TABLE_NAME + "." + DatabaseContract.Types._ID + "="
                + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc.TYPE_ID
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

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.ItemTypeAssoc._ID));
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
        return DatabaseContract.ItemTypeAssoc.TABLE_NAME;
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
