package com.ionshield.planner.activities.database.editors;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ionshield.planner.R;
import com.ionshield.planner.database.DBC;
import com.ionshield.planner.database.DatabaseHelper;

public class LinkTypesEditActivity extends AppCompatActivity {
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_types_edit);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
        final TextView colorView = findViewById(R.id.color);

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DBC.LinkTypes.TABLE_NAME + " WHERE " + DBC.LinkTypes._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.LinkTypes.NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DBC.LinkTypes.DESC));
                int color = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.LinkTypes.COLOR));
                int width = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.LinkTypes.WIDTH));
                double speed = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.LinkTypes.SPEED));
                boolean enabled = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.LinkTypes.ENABLED)) != 0;

                EditText nameField = findViewById(R.id.name_field);
                EditText descField = findViewById(R.id.desc_field);
                EditText colorField = findViewById(R.id.color_field);
                EditText widthField = findViewById(R.id.width_field);
                EditText speedField = findViewById(R.id.speed_field);
                CheckBox isEnabledCheckbox = findViewById(R.id.is_enabled_checkbox);

                colorView.setBackgroundColor(color + 0xFF000000);
                nameField.setText(name);
                descField.setText(desc);
                colorField.setText(colorIntToHex(color));
                widthField.setText(String.valueOf(width));
                speedField.setText(String.valueOf(speed));
                isEnabledCheckbox.setChecked(enabled);

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
        final EditText widthField = findViewById(R.id.width_field);
        widthField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int width = Integer.parseInt(editable.toString());
                    int w = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, width, getResources().getDisplayMetrics());
                    colorView.getLayoutParams().height = w;
                }
                catch (NumberFormatException e) {
                    colorView.getLayoutParams().height = 0;
                }
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
        EditText widthField = findViewById(R.id.width_field);
        EditText speedField = findViewById(R.id.speed_field);
        CheckBox isEnabledCheckBox = findViewById(R.id.is_enabled_checkbox);

        String name = nameField.getText().toString();
        String desc = descField.getText().toString();
        String colorString = colorField.getText().toString().toUpperCase();
        int width;
        double speed;
        boolean enabled = isEnabledCheckBox.isChecked();

        try {
            width = Integer.parseInt(widthField.getText().toString());
            speed = Double.parseDouble(speedField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_SHORT).show();
            return;
        }

        Integer color = colorHexToInt(colorString);
        if (color == null) {
            Toast.makeText(this, R.string.color_error_message, Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DBC.LinkTypes.TABLE_NAME + " SET " + DBC.LinkTypes.NAME + "=?, " + DBC.LinkTypes.DESC + "=?, " + DBC.LinkTypes.COLOR + "=?, " + DBC.LinkTypes.WIDTH + "=?, " + DBC.LinkTypes.SPEED + "=?, " + DBC.LinkTypes.ENABLED +"=? WHERE " + BaseColumns._ID + "=?;");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, color);
                stmt.bindLong(4, width);
                stmt.bindDouble(5, speed);
                stmt.bindLong(6, enabled ? 1 : 0);
                stmt.bindLong(7, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DBC.LinkTypes.TABLE_NAME + " (" + DBC.LinkTypes.NAME + ", " + DBC.LinkTypes.DESC + ", " + DBC.LinkTypes.COLOR + ", "+ DBC.LinkTypes.WIDTH + ", " + DBC.LinkTypes.SPEED + ", " + DBC.LinkTypes.ENABLED + ") VALUES (?, ?, ?, ?, ?, ?);");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, color);
                stmt.bindLong(4, width);
                stmt.bindDouble(5, speed);
                stmt.bindLong(6, enabled ? 1 : 0);
                stmt.executeInsert();
            }
            finish();
        }
        catch (SQLException e) {
            Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
        }

    }
}