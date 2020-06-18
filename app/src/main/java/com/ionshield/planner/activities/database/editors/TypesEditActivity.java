package com.ionshield.planner.activities.database.editors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ionshield.planner.R;
import com.ionshield.planner.database.DatabaseContract;
import com.ionshield.planner.database.DatabaseHelper;

public class TypesEditActivity extends AppCompatActivity {
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_types_edit);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
        final TextView colorView = findViewById(R.id.color);

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseContract.Types.TABLE_NAME + " WHERE " + DatabaseContract.Types._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Types.NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.Types.DESC));
                int color = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.Types.COLOR));

                EditText nameField = findViewById(R.id.name_field);
                EditText descField = findViewById(R.id.desc_field);
                EditText colorField = findViewById(R.id.color_field);


                colorView.setBackgroundColor(color + 0xFF000000);
                nameField.setText(name);
                descField.setText(desc);
                colorField.setText(colorIntToHex(color));

                cursor.close();
            }
            catch (SQLException e) {
                Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
            }
        }

        db = helper.getWritableDatabase();

        final EditText colorField = findViewById(R.id.color_field);
        colorField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                Integer c = colorHexToInt(editable.toString());
                if (c == null) c = 0;
                colorView.setBackgroundColor(c + 0xFF000000);
            }
        });
    }

    public Integer colorHexToInt(String color) {
        String colorString = color.toUpperCase();
        char[] validChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        if (colorString.length() != 7 || !colorString.startsWith("#")) {
            return null;
        }
        colorString = colorString.substring(1);
        for (int i = 0; i < colorString.length(); i++) {
            boolean ok = false;
            char c = colorString.charAt(i);
            for (char validChar : validChars) {
                if (c == validChar) {
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                return null;
            }
        }
        return Integer.parseInt(colorString, 16);
    }

    public String colorIntToHex(int color) {
        StringBuilder colorString = new StringBuilder(Integer.toString(color, 16));
        while (colorString.length() < 6) {
            colorString.insert(0, "0");
        }
        colorString.insert(0, "#");
        return colorString.toString().toUpperCase();
    }

    public void confirmButtonClicked(View view) {
        EditText nameField = findViewById(R.id.name_field);
        EditText descField = findViewById(R.id.desc_field);
        EditText colorField = findViewById(R.id.color_field);

        String name = nameField.getText().toString();
        String desc = descField.getText().toString();
        String colorString = colorField.getText().toString().toUpperCase();

        Integer color = colorHexToInt(colorString);
        if (color == null) {
            Toast.makeText(this, R.string.color_error_message, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DatabaseContract.Types.TABLE_NAME + " SET " + DatabaseContract.Types.NAME + "=?, " + DatabaseContract.Types.DESC + "=?, " + DatabaseContract.Types.COLOR + "=? WHERE " + DatabaseContract.Types._ID + "=?;");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, color);
                stmt.bindLong(4, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DatabaseContract.Types.TABLE_NAME + " (" + DatabaseContract.Types.NAME + ", " + DatabaseContract.Types.DESC + ", " + DatabaseContract.Types.COLOR + ") VALUES (?, ?, ?);");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, color);
                stmt.executeInsert();
            }
            finish();
        }
        catch (SQLException e) {
            Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
        }

    }
}