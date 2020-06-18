package com.ionshield.planner.activities.database.editors;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.database.DatabaseContract;
import com.ionshield.planner.database.DatabaseHelper;

public class EventAssocEditActivity extends AppCompatActivity {
    private EditText[] fields;
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_assoc_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        fields = new EditText[]{ findViewById(R.id.event_id_field),  findViewById(R.id.plan_id_field)};

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.EventPlanAssoc.TABLE_NAME + " WHERE " + DatabaseContract.EventPlanAssoc._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                int eventId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.EventPlanAssoc.EVENT_ID));
                int planId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.EventPlanAssoc.PLAN_ID));

                EditText eventIdField = (EditText) fields[0];
                EditText planIdField = (EditText) fields[1];

                eventIdField.setText(String.valueOf(eventId));
                planIdField.setText(String.valueOf(planId));

                cursor.close();
            }
            catch (SQLException e) {
                Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        db = helper.getWritableDatabase();
    }

    public void confirmButtonClicked(View view) {
        EditText eventIdField = findViewById(R.id.event_id_field);
        EditText planIdField = findViewById(R.id.plan_id_field);

        int eventId;
        int planId;

        try {
            eventId = Integer.parseInt(eventIdField.getText().toString());
            planId = Integer.parseInt(planIdField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DatabaseContract.EventPlanAssoc.TABLE_NAME + " SET " + DatabaseContract.EventPlanAssoc.EVENT_ID + "=?, " + DatabaseContract.EventPlanAssoc.PLAN_ID + "=? WHERE " + DatabaseContract.Items._ID + "=?;");
                stmt.bindLong(1, eventId);
                stmt.bindLong(2, planId);
                stmt.bindLong(3, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DatabaseContract.EventPlanAssoc.TABLE_NAME + " (" + DatabaseContract.EventPlanAssoc.EVENT_ID + ", " + DatabaseContract.EventPlanAssoc.PLAN_ID + ") VALUES (?, ?);");
                stmt.bindLong(1, eventId);
                stmt.bindLong(2, planId);
                stmt.executeInsert();
            }
            finish();
        }
        catch (SQLException e) {
            Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            fields[requestCode - 1].setText(String.valueOf(data.getIntExtra("value", 0)));
        }
    }

    public void browseEvents(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.EVENTS.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }

    public void browsePlans(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.PLANS.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 2);
    }
}