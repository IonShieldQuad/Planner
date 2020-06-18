package com.ionshield.planner.activities.database.editors;

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
import com.ionshield.planner.database.DatabaseContract;
import com.ionshield.planner.database.DatabaseHelper;

public class PlansEditActivity extends AppCompatActivity {
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plans_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.Plans.TABLE_NAME + " WHERE " + DatabaseContract.Plans._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Items.NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Items.DESC));

                EditText nameField = findViewById(R.id.name_field);
                EditText descField = findViewById(R.id.desc_field);

                nameField.setText(name);
                descField.setText(desc);

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

        String name = nameField.getText().toString();
        String desc = descField.getText().toString();


        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DatabaseContract.Plans.TABLE_NAME + " SET " + DatabaseContract.Plans.NAME + "=?, " + DatabaseContract.Plans.DESC + "=? WHERE " + DatabaseContract.Plans._ID + "=?;");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DatabaseContract.Plans.TABLE_NAME + " (" + DatabaseContract.Plans.NAME + ", " + DatabaseContract.Plans.DESC + ") VALUES (?, ?);");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.executeInsert();
            }
            finish();
        }
        catch (SQLException e) {
            Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
        }

    }
}