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
import com.ionshield.planner.activities.database.editors.LinksEditActivity;
import com.ionshield.planner.database.DBC;

public class LinksMode implements Mode {
    @Override
    public int getTitleRes() {
        return R.string.links_title;
    }

    @Override
    public Intent getEditorIntent(Context context, Integer id) {
        Intent intent = new Intent(context, LinksEditActivity.class);
        if (id != null) {
            intent.putExtra("id", id);
        }
        return intent;
    }

    @Override
    public Cursor queryAll(SQLiteDatabase db) {
        return db.rawQuery("SELECT " + DBC.Links.TABLE_NAME + ".*, "
                + "f." + DBC.Nodes.COORDINATE_X + " AS from_x, "
                + "f." + DBC.Nodes.COORDINATE_Y + " AS from_y, "
                + "t." + DBC.Nodes.COORDINATE_X + " AS to_x, "
                + "t." + DBC.Nodes.COORDINATE_Y + " AS to_y, "
                + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.NAME + " AS type FROM " + DBC.Links.TABLE_NAME
                + " JOIN " + DBC.Nodes.TABLE_NAME + " f ON f." + DBC.Nodes._ID + "="
                + DBC.Links.TABLE_NAME + "." + DBC.Links.FROM_NODE_ID
                + " JOIN " + DBC.Nodes.TABLE_NAME + " t ON t." + DBC.Nodes._ID + "="
                + DBC.Links.TABLE_NAME + "." + DBC.Links.TO_NODE_ID
                + " JOIN " + DBC.LinkTypes.TABLE_NAME + " ON " + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes._ID + "="
                + DBC.Links.TABLE_NAME + "." + DBC.Links.LINK_TYPE_ID
                + ";", new String[]{});
    }

    @Override
    public Cursor querySearch(SQLiteDatabase db, String searchString) {
        return db.rawQuery("SELECT " + DBC.Links.TABLE_NAME + ".*, "
                + "f." + DBC.Nodes.COORDINATE_X + " AS from_x, "
                + "f." + DBC.Nodes.COORDINATE_Y + " AS from_y, "
                + "t." + DBC.Nodes.COORDINATE_X + " AS to_x, "
                + "t." + DBC.Nodes.COORDINATE_Y + " AS to_y, "
                + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes.NAME + " AS type FROM " + DBC.Links.TABLE_NAME
                + " JOIN " + DBC.Nodes.TABLE_NAME + " f ON f." + DBC.Nodes._ID + "="
                + DBC.Links.TABLE_NAME + "." + DBC.Links.FROM_NODE_ID
                + " JOIN " + DBC.Nodes.TABLE_NAME + " t ON t." + DBC.Nodes._ID + "="
                + DBC.Links.TABLE_NAME + "." + DBC.Links.TO_NODE_ID
                + " JOIN " + DBC.LinkTypes.TABLE_NAME + " ON " + DBC.LinkTypes.TABLE_NAME + "." + DBC.LinkTypes._ID + "="
                + DBC.Links.TABLE_NAME + "." + DBC.Links.LINK_TYPE_ID
                + " WHERE (type LIKE ? OR from_x LIKE ? OR from_y LIKE ? OR to_x LIKE ? OR to_y LIKE ?);",
                new String[]{"%" + searchString + "%", searchString + "%", searchString + "%", searchString + "%", searchString + "%"});
    }

    @Override
    public int getEditLayoutPartRes() {
        return R.layout.links_part;
    }

    @Override
    public int getSelectLayoutPartRes() {
        return R.layout.links_select_part;
    }

    @Override
    public void bindCursorData(View view, Context context, Cursor cursor) {
        TextView idView = view.findViewById(R.id.id);
        TextView fromXView = view.findViewById(R.id.from_coordinate_x);
        TextView fromYView = view.findViewById(R.id.from_coordinate_y);
        TextView toXView = view.findViewById(R.id.to_coordinate_x);
        TextView toYView = view.findViewById(R.id.to_coordinate_y);
        TextView linkTypeView = view.findViewById(R.id.type_id);
        TextView modifierView = view.findViewById(R.id.modifier);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Links._ID));
        double fromX = cursor.getDouble(cursor.getColumnIndexOrThrow("from_x"));
        double fromY = cursor.getDouble(cursor.getColumnIndexOrThrow("from_y"));
        double toX = cursor.getDouble(cursor.getColumnIndexOrThrow("to_x"));
        double toY = cursor.getDouble(cursor.getColumnIndexOrThrow("to_y"));
        String type = cursor.getString((cursor.getColumnIndexOrThrow("type")));
        double modifier = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Links.MODIFIER));

        idView.setText(String.valueOf(id));
        fromXView.setText(String.valueOf(fromX));
        fromYView.setText(String.valueOf(fromY));
        toXView.setText(String.valueOf(toX));
        toYView.setText(String.valueOf(toY));
        linkTypeView.setText(type);
        modifierView.setText(String.valueOf(modifier));
    }

    @Override
    public String getTableName() {
        return DBC.Links.TABLE_NAME;
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
