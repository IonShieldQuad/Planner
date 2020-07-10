package com.ionshield.planner.activities.database.editors;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.database.DBC;
import com.ionshield.planner.database.DatabaseHelper;

public class LocationsEditActivity extends AppCompatActivity {
    private EditText[] fields;
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

        fields = new EditText[]{ findViewById(R.id.type_id_field),  findViewById(R.id.node_id_field)};

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DBC.Locations.TABLE_NAME + " WHERE " + DBC.Locations._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Locations.NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Locations.DESC));
                int typeId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Locations.TYPE_ID));
                int nodeId = 0;
                if (!cursor.isNull(cursor.getColumnIndexOrThrow(DBC.Locations.NODE_ID))) {
                    nodeId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Locations.NODE_ID));
                }

                EditText nameField = findViewById(R.id.name_field);
                EditText descField = findViewById(R.id.desc_field);
                EditText typeIdField = fields[0];
                EditText nodeIdField = fields[1];

                nameField.setText(name);
                descField.setText(desc);
                typeIdField.setText(String.valueOf(typeId));
                nodeIdField.setText(String.valueOf(nodeId));

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
        EditText nodeIdField = findViewById(R.id.node_id_field);

        int typeId;
        int nodeId;

        String name = nameField.getText().toString();
        String desc = descField.getText().toString();
        try {
            typeId = Integer.parseInt(typeIdField.getText().toString());
            nodeId = Integer.parseInt(nodeIdField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DBC.Locations.TABLE_NAME + " SET " + DBC.Locations.NAME + "=?, " + DBC.Locations.DESC + "=?, " + DBC.Locations.TYPE_ID + "=?, " + DBC.Locations.NODE_ID + "=? WHERE " + BaseColumns._ID + "=?;");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, typeId);
                stmt.bindLong(4, nodeId);
                stmt.bindLong(5, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DBC.Locations.TABLE_NAME + " (" + DBC.Locations.NAME + ", " + DBC.Locations.DESC + ", " + DBC.Locations.TYPE_ID + ", " + DBC.Locations.NODE_ID + ") VALUES (?, ?, ?, ?);");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, typeId);
                stmt.bindLong(4, nodeId);
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

    public void browseTypes(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.TYPES.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }

    public void browseNodes(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.NODES.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 2);
    }
}