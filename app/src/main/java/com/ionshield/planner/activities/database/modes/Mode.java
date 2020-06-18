package com.ionshield.planner.activities.database.modes;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.CursorAdapter;

import androidx.arch.core.util.Function;

import java.io.Serializable;

public interface Mode extends Serializable {
    int getTitleRes();
    Intent getEditorIntent(Context context, Integer id);
    Cursor queryAll(SQLiteDatabase db);
    Cursor querySearch(SQLiteDatabase db, String searchString);
    int getEditLayoutPartRes();
    int getSelectLayoutPartRes();
    void bindCursorData(final View view, final Context context, Cursor cursor);
    String getTableName();
    CursorAdapter getEditorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback);
    CursorAdapter getSelectorAdapter(Context context, Cursor cursor, int flags, Function<Integer, Void> callback);
}
