package com.ionshield.planner.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.database.DatabaseContract;
import com.ionshield.planner.database.DatabaseHelper;

public class LocationFinderActivity extends AppCompatActivity {
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_finder);

        DatabaseHelper helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
    }

    public void findButtonClicked(View view) {
        EditText eventIdField = findViewById(R.id.event_id_field);
        EditText coordinateXField = findViewById(R.id.coordinate_x_field);
        EditText coordinateYField = findViewById(R.id.coordinate_y_field);

        int eventId;
        double coordinateX;
        double coordinateY;

        try {
            eventId = Integer.parseInt(eventIdField.getText().toString());
            coordinateX = Double.parseDouble(coordinateXField.getText().toString());
            coordinateY = Double.parseDouble(coordinateYField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        Cursor cursor = db.rawQuery("SELECT " + DatabaseContract.Locations.TABLE_NAME +".*, MIN("
                        + "(" + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_X + " - " + "?)" + " * "
                        + "(" + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_X + " - " + "?)" + " + "
                        + "(" + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_Y + " - " + "?)" + " * "
                        + "(" + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.COORDINATE_Y + " - " + "?)" + ")"
                        + " FROM " + DatabaseContract.Locations.TABLE_NAME + ", "
                        + DatabaseContract.Events.TABLE_NAME + ", "
                        + DatabaseContract.ItemTypeAssoc.TABLE_NAME
                        + " WHERE " + DatabaseContract.Events.TABLE_NAME + "." + DatabaseContract.Events._ID + "=?"
                        + " AND "+ DatabaseContract.Events.TABLE_NAME + "." + DatabaseContract.Events.ITEM_ID + "="
                        + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc.ITEM_ID
                        + " AND " + DatabaseContract.Locations.TABLE_NAME + "." + DatabaseContract.Locations.TYPE_ID + "="
                        + DatabaseContract.ItemTypeAssoc.TABLE_NAME + "." + DatabaseContract.ItemTypeAssoc.TYPE_ID + ";",
                new String[]{String.valueOf(coordinateX), String.valueOf(coordinateX), String.valueOf(coordinateY), String.valueOf(coordinateY), String.valueOf(eventId)});
        cursor.moveToFirst();
        if (cursor.getCount() == 0 || cursor.isNull(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.NAME))) {
            Toast.makeText(this, R.string.no_locations_found_message, Toast.LENGTH_LONG).show();
        }
        else {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.NAME));
            double x = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.COORDINATE_X));
            double y = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.COORDINATE_Y));
            Toast.makeText(this, getString(R.string.location_found_message, name, x, y), Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    public void browseEvents(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.EVENTS.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ((EditText)findViewById(R.id.event_id_field)).setText(String.valueOf(data.getIntExtra("value", 0)));
        }
    }
}