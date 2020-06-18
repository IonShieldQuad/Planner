package com.ionshield.planner.activities.database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;

import androidx.arch.core.util.Function;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Mode;
import com.ionshield.planner.database.DatabaseContract;
import com.ionshield.planner.database.DatabaseHelper;

public class BaseAdapter extends CursorAdapter {
    private Function<Integer, Void> callback;
    private Mode mode;
    private boolean isSelector = false;

    public BaseAdapter(Context context, Cursor c, int flags, Mode mode, Function<Integer, Void> callback, boolean isSelector) {
        super(context, c, flags);
        this.callback = callback;
        this.mode = mode;
        this.isSelector = isSelector;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        if (isSelector) {
            return LayoutInflater.from(context).inflate(mode.getSelectLayoutPartRes(), viewGroup, false);
        }
        return LayoutInflater.from(context).inflate(mode.getEditLayoutPartRes(), viewGroup, false);
    }

    @Override
    public void bindView(final View view, final Context context, Cursor cursor) {
        mode.bindCursorData(view, context, cursor);

        final int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Items._ID));

        if (isSelector) {
            Button selectButton = view.findViewById(R.id.select_button);
            if (selectButton != null) {
                selectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View l) {
                        callback.apply(id);
                    }
                });
            }
        }
        else {
            Button deleteButton = view.findViewById(R.id.delete_button);
            Button editButton = view.findViewById(R.id.edit_button);
            if (deleteButton != null) {
                deleteButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View l) {
                        DatabaseHelper helper = new DatabaseHelper(context);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        helper.delete(db, mode.getTableName(), id);
                        if (callback != null) {
                            callback.apply(0);
                        }
                    }
                });
            }
            if (editButton != null) {
                editButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = mode.getEditorIntent(context, id);
                        context.startActivity(intent);
                        if (callback != null) {
                            callback.apply(0);
                        }
                    }
                });
            }
        }
    }
}
