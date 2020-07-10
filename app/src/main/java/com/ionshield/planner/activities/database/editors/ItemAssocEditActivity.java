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

public class ItemAssocEditActivity extends AppCompatActivity {
    private EditText[] fields;
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_assoc_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        fields = new EditText[]{ findViewById(R.id.item_id_field),  findViewById(R.id.type_id_field)};

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DBC.ItemTypeAssoc.TABLE_NAME + " WHERE " + DBC.ItemTypeAssoc._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.ItemTypeAssoc.ITEM_ID));
                int typeId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.ItemTypeAssoc.TYPE_ID));

                EditText itemIdField = fields[0];
                EditText typeIdField = fields[1];

                itemIdField.setText(String.valueOf(itemId));
                typeIdField.setText(String.valueOf(typeId));

                cursor.close();
            }
            catch (SQLException e) {
                Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        db = helper.getWritableDatabase();
    }

    public void confirmButtonClicked(View view) {
        EditText itemIdField = findViewById(R.id.item_id_field);
        EditText typeIdField = findViewById(R.id.type_id_field);

        int itemId;
        int typeId;

        try {
            itemId = Integer.parseInt(itemIdField.getText().toString());
            typeId = Integer.parseInt(typeIdField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DBC.ItemTypeAssoc.TABLE_NAME + " SET " + DBC.ItemTypeAssoc.ITEM_ID + "=?, " + DBC.ItemTypeAssoc.TYPE_ID + "=? WHERE " + BaseColumns._ID + "=?;");
                stmt.bindLong(1, itemId);
                stmt.bindLong(2, typeId);
                stmt.bindLong(3, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DBC.ItemTypeAssoc.TABLE_NAME + " (" + DBC.ItemTypeAssoc.ITEM_ID + ", " + DBC.ItemTypeAssoc.TYPE_ID + ") VALUES (?, ?);");
                stmt.bindLong(1, itemId);
                stmt.bindLong(2, typeId);
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

    public void browseItems(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.ITEMS.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }

    public void browseTypes(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.TYPES.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 2);
    }
}