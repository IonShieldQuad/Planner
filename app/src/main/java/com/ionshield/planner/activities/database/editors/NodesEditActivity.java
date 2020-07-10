package com.ionshield.planner.activities.database.editors;

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

import androidx.appcompat.app.AppCompatActivity;

import com.ionshield.planner.R;
import com.ionshield.planner.database.DBC;
import com.ionshield.planner.database.DatabaseHelper;

public class NodesEditActivity extends AppCompatActivity {
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nodes_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME + " WHERE " + DBC.Nodes._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Nodes.NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Nodes.DESC));
                double coordinateX = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_X));
                double coordinateY = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_Y));

                EditText nameField = findViewById(R.id.name_field);
                EditText descField = findViewById(R.id.desc_field);
                EditText coordinateXField = findViewById(R.id.coordinate_x_field);
                EditText coordinateYField = findViewById(R.id.coordinate_y_field);

                nameField.setText(name);
                descField.setText(desc);
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
        EditText coordinateXField = findViewById(R.id.coordinate_x_field);
        EditText coordinateYField = findViewById(R.id.coordinate_y_field);

        int typeId;
        double coordinateX;
        double coordinateY;

        String name = nameField.getText().toString();
        String desc = descField.getText().toString();
        try {
            coordinateX = Double.parseDouble(coordinateXField.getText().toString());
            coordinateY = Double.parseDouble(coordinateYField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DBC.Nodes.TABLE_NAME + " SET " + DBC.Nodes.NAME + "=?, " + DBC.Nodes.DESC + "=?, " + DBC.Nodes.COORDINATE_X + "=?, " + DBC.Nodes.COORDINATE_Y + "=? WHERE " + DBC.Nodes._ID + "=?;");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindDouble(3, coordinateX);
                stmt.bindDouble(4, coordinateY);
                stmt.bindLong(5, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DBC.Nodes.TABLE_NAME + " (" + DBC.Nodes.NAME + ", " + DBC.Nodes.DESC + ", " + DBC.Nodes.COORDINATE_X + ", " + DBC.Nodes.COORDINATE_Y + ") VALUES (?, ?, ?, ?);");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindDouble(3, coordinateX);
                stmt.bindDouble(4, coordinateY);
                stmt.executeInsert();
            }
            finish();
        }
        catch (SQLException e) {
            Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
        }

    }

}