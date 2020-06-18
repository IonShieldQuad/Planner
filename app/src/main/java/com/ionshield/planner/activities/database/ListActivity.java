
package com.ionshield.planner.activities.database;

import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.util.Function;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Mode;
import com.ionshield.planner.database.DatabaseHelper;

public class ListActivity extends AppCompatActivity {
    private DatabaseHelper helper;
    private SQLiteDatabase db;
    private String searchString = "";
    private boolean isSelector = false;
    private Mode mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        isSelector = getIntent().getBooleanExtra("isSelector", false);
        mode = (Mode) getIntent().getSerializableExtra("mode");

        if (mode == null) throw new NullPointerException("Mode is null");

        TextView titleView = findViewById(R.id.title);
        titleView.setText(mode.getTitleRes());

        if (isSelector) {
            Button button = findViewById(R.id.button);
            button.setVisibility(View.GONE);
            button.setEnabled(false);
        }

        EditText searchField = findViewById(R.id.search_field);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                searchString = editable.toString();
                updateList(query(searchString));
            }
        });

        updateList(query(searchString));
    }

    private Cursor query(String searchString) {
        if (searchString == null || searchString.length() == 0) {
            return mode.queryAll(db);
        }
        return mode.querySearch(db, searchString);
    }

    private void updateList(Cursor cursor) {
        ListView listView = findViewById(R.id.list);

        CursorAdapter adapter;
        final Activity act = this;
        if (isSelector) {
            adapter = mode.getSelectorAdapter(this, cursor, 0, new Function<Integer, Void>() {
                @Override
                public Void apply(Integer input) {
                    Intent intent = new Intent();
                    intent.putExtra("value", input);
                    act.setResult(RESULT_OK, intent);
                    act.finish();
                    return null;
                }
            });
        }
        else {
            adapter = mode.getEditorAdapter(this, cursor, 0, new Function<Integer, Void>() {
                @Override
                public Void apply(Integer input) {
                    updateList(query(searchString));
                    return null;
                }
            });
        }
        listView.setAdapter(adapter);
    }

    public void buttonClicked(View view) {
        if(isSelector) return;
        Intent intent = mode.getEditorIntent(this, null);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateList(query(searchString));
    }
}