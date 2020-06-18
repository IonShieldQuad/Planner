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
import com.ionshield.planner.activities.database.ListActivity;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.database.DatabaseContract;
import com.ionshield.planner.database.DatabaseHelper;

public class LocationsEditActivity extends AppCompatActivity {
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.Locations.TABLE_NAME + " WHERE " + DatabaseContract.Locations._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.DESC));
                int typeId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.TYPE_ID));
                double coordinateX = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.COORDINATE_X));
                double coordinateY = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseContract.Locations.COORDINATE_Y));

                EditText nameField = findViewById(R.id.name_field);
                EditText descField = findViewById(R.id.desc_field);
                EditText typeIdField = findViewById(R.id.type_id_field);
                EditText coordinateXField = findViewById(R.id.coordinate_x_field);
                EditText coordinateYField = findViewById(R.id.coordinate_y_field);


                nameField.setText(name);
                descField.setText(desc);
                typeIdField.setText(String.valueOf(typeId));
                coordinateXField.setText(String.valueOf(coordinateX));
                coordinateYField.setText(String.valueOf(coordinateY));

                cursor.close();
            }
            catch (SQLException e) {
                Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        db = helper.getWritableDatabase();
    }

    public void confirmButtonClicked(View view) {
        EditText nameField = findViewById(R.id.name_field);
        EditText descField = findViewById(R.id.desc_field);
        EditText typeIdField = findViewById(R.id.type_id_field);
        EditText coordinateXField = findViewById(R.id.coordinate_x_field);
        EditText coordinateYField = findViewById(R.id.coordinate_y_field);

        int typeId;
        double coordinateX;
        double coordinateY;

        String name = nameField.getText().toString();
        String desc = descField.getText().toString();
        try {
            typeId = Integer.parseInt(typeIdField.getText().toString());
            coordinateX = Double.parseDouble(coordinateXField.getText().toString());
            coordinateY = Double.parseDouble(coordinateYField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DatabaseContract.Locations.TABLE_NAME + " SET " + DatabaseContract.Locations.NAME + "=?, " + DatabaseContract.Locations.DESC + "=?, " + DatabaseContract.Locations.TYPE_ID + "=?, " + DatabaseContract.Locations.COORDINATE_X + "=?, " + DatabaseContract.Locations.COORDINATE_Y + "=? WHERE " + DatabaseContract.Items._ID + "=?;");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, typeId);
                stmt.bindDouble(4, coordinateX);
                stmt.bindDouble(5, coordinateY);
                stmt.bindLong(6, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DatabaseContract.Locations.TABLE_NAME + " (" + DatabaseContract.Locations.NAME + ", " + DatabaseContract.Locations.DESC + ", " + DatabaseContract.Locations.TYPE_ID + ", " + DatabaseContract.Locations.COORDINATE_X + ", " + DatabaseContract.Locations.COORDINATE_Y + ") VALUES (?, ?, ?, ?, ?);");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, typeId);
                stmt.bindDouble(4, coordinateX);
                stmt.bindDouble(5, coordinateY);
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
            EditText typeIdField = findViewById(R.id.type_id_field);
            typeIdField.setText(String.valueOf(data.getIntExtra("value", 0)));
        }
    }

    public void browseTypes(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.TYPES.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }
}