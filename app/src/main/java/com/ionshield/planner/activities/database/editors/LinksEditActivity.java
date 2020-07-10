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

public class LinksEditActivity extends AppCompatActivity {
    private EditText[] fields;
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_links_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        fields = new EditText[]{ findViewById(R.id.from_node_id_field), findViewById(R.id.to_node_id_field), findViewById(R.id.link_type_id_field)};

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DBC.Links.TABLE_NAME + " WHERE " + DBC.Links._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                int fromNodeId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Links.FROM_NODE_ID));
                int toNodeId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Links.TO_NODE_ID));
                int linkTypeId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Links.LINK_TYPE_ID));
                double modifier = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Links.MODIFIER));

                EditText fromNodeIdField = fields[0];
                EditText toNodeIdField = fields[1];
                EditText linkTypeIdField = fields[2];
                EditText modifierField = findViewById(R.id.modifier_field);

                fromNodeIdField.setText(String.valueOf(fromNodeId));
                toNodeIdField.setText(String.valueOf(toNodeId));
                linkTypeIdField.setText(String.valueOf(linkTypeId));
                modifierField.setText(String.valueOf(modifier));

                cursor.close();
            }
            catch (SQLException e) {
                Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        db = helper.getWritableDatabase();
    }

    public void confirmButtonClicked(View view) {
        EditText fromNodeIdField = fields[0];
        EditText toNodeIdField = fields[1];
        EditText linkTypeIdField = fields[2];
        EditText modifierField = findViewById(R.id.modifier_field);

        int fromNodeId;
        int toNodeId;
        int linkTypeId;
        double modifier;

        try {
            fromNodeId = Integer.parseInt(fromNodeIdField.getText().toString());
            toNodeId = Integer.parseInt(toNodeIdField.getText().toString());
            linkTypeId = Integer.parseInt(linkTypeIdField.getText().toString());
            modifier = Double.parseDouble(modifierField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DBC.Links.TABLE_NAME + " SET " + DBC.Links.FROM_NODE_ID + "=?, " + DBC.Links.TO_NODE_ID + "=?, " + DBC.Links.LINK_TYPE_ID + "=?, " + DBC.Links.MODIFIER +  "=? WHERE " + BaseColumns._ID + "=?;");
                stmt.bindLong(1, fromNodeId);
                stmt.bindLong(2, toNodeId);
                stmt.bindLong(3, linkTypeId);
                stmt.bindDouble(4, modifier);
                stmt.bindLong(5, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DBC.Links.TABLE_NAME + " (" + DBC.Links.FROM_NODE_ID + ", " + DBC.Links.TO_NODE_ID + ", " + DBC.Links.LINK_TYPE_ID + ", " + DBC.Links.MODIFIER + ") VALUES (?, ?, ?, ?);");
                stmt.bindLong(1, fromNodeId);
                stmt.bindLong(2, toNodeId);
                stmt.bindLong(3, linkTypeId);
                stmt.bindDouble(4, modifier);
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



    public void browseFromNodes(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.NODES.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }

    public void browseToNodes(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.NODES.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 2);
    }

    public void browseLinkTypes(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.LINK_TYPES.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 3);
    }
}