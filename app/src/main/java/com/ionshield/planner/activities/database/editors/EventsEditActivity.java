package com.ionshield.planner.activities.database.editors;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.database.DBC;
import com.ionshield.planner.database.DatabaseHelper;
import com.ionshield.planner.fragments.DatePickerFragment;
import com.ionshield.planner.fragments.DurationPickerFragment;
import com.ionshield.planner.fragments.TimePickerFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EventsEditActivity extends AppCompatActivity {
    private int id;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_edit);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", -1);

        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();

        if (id >= 0) {
            TextView title = findViewById(R.id.title);
            title.setText(R.string.edit);

            try {
                Cursor cursor = db.rawQuery("SELECT * FROM " + DBC.Events.TABLE_NAME + " WHERE " + DBC.Events._ID + "=?;", new String[]{String.valueOf(id)});

                cursor.moveToFirst();
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Events.NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Events.DESC));
                int itemId = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Events.ITEM_ID));
                long duration = cursor.getLong(cursor.getColumnIndexOrThrow(DBC.Events.DURATION));

                DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
                DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);

                boolean useDate = cursor.getInt(cursor.getColumnIndexOrThrow(DBC.Events.IS_DATE_USED)) != 0;
                int datetimeMinIndex = cursor.getColumnIndex(DBC.Events.DATETIME_MIN);
                int datetimeMaxIndex = cursor.getColumnIndex(DBC.Events.DATETIME_MAX);
                String timeMin = "";
                String timeMax = "";
                String dateMin = "";
                String dateMax = "";
                if (datetimeMinIndex >= 0) {
                    long millis = cursor.getLong(datetimeMinIndex);
                    if (millis != -1) {
                        timeMin = tf.format(new Date(millis));
                        if (useDate) {
                            dateMin = df.format(new Date(millis));
                        }
                    }
                }
                if (datetimeMaxIndex >= 0) {
                    long millis = cursor.getLong(datetimeMaxIndex);
                    if (millis != -1) {
                        timeMax = tf.format(new Date(millis));
                        if (useDate) {
                            dateMax = df.format(new Date(millis));
                        }
                    }
                }

                EditText nameField = findViewById(R.id.name_field);
                EditText descField = findViewById(R.id.desc_field);
                EditText itemIdField = findViewById(R.id.item_id_field);
                TextView timeMinView = findViewById(R.id.time_min_field);
                TextView timeMaxView = findViewById(R.id.time_max_field);
                TextView dateMinView = findViewById(R.id.date_min_field);
                TextView dateMaxView = findViewById(R.id.date_max_field);
                CheckBox checkBox = findViewById(R.id.use_date_checkbox);
                EditText durationField = findViewById(R.id.duration_field);

                nameField.setText(name);
                descField.setText(desc);
                itemIdField.setText(String.valueOf(itemId));
                timeMinView.setText(timeMin);
                timeMaxView.setText(timeMax);
                dateMinView.setText(dateMin);
                dateMaxView.setText(dateMax);
                checkBox.setChecked(useDate);
                durationField.setText(String.valueOf(duration));

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
        EditText itemIdField = findViewById(R.id.item_id_field);

        EditText timeMinField = findViewById(R.id.time_min_field);
        EditText dateMinField = findViewById(R.id.date_min_field);
        EditText timeMaxField = findViewById(R.id.time_max_field);
        EditText dateMaxField = findViewById(R.id.date_max_field);
        CheckBox checkBox = findViewById(R.id.use_date_checkbox);
        EditText durationField = findViewById(R.id.duration_field);

        String name = nameField.getText().toString();
        String desc = descField.getText().toString();
        String timeMinString = timeMinField.getText().toString();
        String dateMinString = dateMinField.getText().toString();
        String timeMaxString = timeMaxField.getText().toString();
        String dateMaxString = dateMaxField.getText().toString();
        boolean useDate = checkBox.isChecked();

        int itemId;
        long duration;
        Long datetimeMin = 0L;
        Long datetimeMax = 0L;

        try {
            itemId = Integer.parseInt(itemIdField.getText().toString());
            duration = Long.parseLong(durationField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        DateFormat tf = DateFormat.getTimeInstance(DateFormat.SHORT);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        try {
            if (useDate) {
                //Min
                if (dateMinString.length() == 0) {
                    if (timeMinString.length() == 0) {
                        datetimeMin = null;
                    }
                    else {
                        Toast.makeText(this, R.string.date_not_set_error_message, Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if (timeMinString.length() == 0) {
                        Toast.makeText(this, R.string.time_not_set_error_message, Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        Date timeMin = tf.parse(timeMinString);
                        Date dateMin = df.parse(dateMinString);
                        datetimeMin = timeMin.getTime() + dateMin.getTime();
                    }
                }
                //Max
                if (dateMaxString.length() == 0) {
                    if (timeMaxString.length() == 0) {
                        datetimeMax = null;
                    }
                    else {
                        Toast.makeText(this, R.string.date_not_set_error_message, Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    if (timeMaxString.length() == 0) {
                        Toast.makeText(this, R.string.date_not_set_error_message, Toast.LENGTH_LONG).show();
                        return;
                    }
                    else {
                        Date timeMax = tf.parse(timeMaxString);
                        Date dateMax = df.parse(dateMaxString);
                        datetimeMax = timeMax.getTime() + dateMax.getTime();
                    }
                }
            }
            else {
                //Min
                if (timeMinString.length() == 0) {
                    datetimeMin = null;
                }
                else {
                    Date timeMin = tf.parse(timeMinString);
                    datetimeMin = timeMin.getTime();
                }
                //Max
                if (timeMaxString.length() == 0) {
                    datetimeMax = null;
                }
                else {
                    Date timeMax = tf.parse(timeMaxString);
                    datetimeMax = timeMax.getTime();
                }
            }
        } catch (ParseException e) {
            Toast.makeText(this, R.string.datetime_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }


        try {
            if (id >= 0) {
                SQLiteStatement stmt = db.compileStatement("UPDATE " + DBC.Events.TABLE_NAME + " SET " + DBC.Events.NAME + "=?, " + DBC.Events.DESC + "=?, " + DBC.Events.ITEM_ID + "=?, " + DBC.Events.DATETIME_MIN + "=?, " + DBC.Events.DATETIME_MAX + "=?, " + DBC.Events.IS_DATE_USED + "=?, " + DBC.Events.DURATION + "=? WHERE " + BaseColumns._ID + "=?;");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, itemId);
                if (datetimeMin == null) {
                    stmt.bindDouble(4, -1);
                }
                else {
                    stmt.bindDouble(4, datetimeMin);
                }
                if (datetimeMax == null) {
                    stmt.bindLong(5, -1);
                }
                else {
                    stmt.bindLong(5, datetimeMax);
                }
                stmt.bindLong(6, useDate ? 1 : 0);
                stmt.bindLong(7, duration);
                stmt.bindLong(8, id);
                stmt.executeUpdateDelete();
            } else {
                SQLiteStatement stmt = db.compileStatement("INSERT INTO " + DBC.Events.TABLE_NAME + " (" + DBC.Events.NAME + ", " + DBC.Events.DESC + ", " + DBC.Events.ITEM_ID + ", " + DBC.Events.DATETIME_MIN + ", " + DBC.Events.DATETIME_MAX + ", " + DBC.Events.IS_DATE_USED + ", " + DBC.Events.DURATION + ") VALUES (?, ?, ?, ?, ?, ?, ?);");
                stmt.bindString(1, name);
                stmt.bindString(2, desc);
                stmt.bindLong(3, itemId);
                if (datetimeMin == null) {
                    stmt.bindLong(4, -1);
                }
                else {
                    stmt.bindLong(4, datetimeMin);
                }
                if (datetimeMax == null) {
                    stmt.bindLong(5, -1);
                }
                else {
                    stmt.bindLong(5, datetimeMax);
                }
                stmt.bindLong(6, useDate ? 1 : 0);
                stmt.bindLong(7, duration);
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
            EditText itemIdField = findViewById(R.id.item_id_field);
            itemIdField.setText(String.valueOf(data.getIntExtra("value", 0)));
        }
    }

    public void browseItems(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.ITEMS.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }

    public void pickMinTime(View view) {
        DialogFragment newFragment = new TimePickerFragment(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                try {
                    Date date = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH).parse(hour + ":" + minute);
                    EditText timeMinField = findViewById(R.id.time_min_field);
                    if (date != null) {
                        timeMinField.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        newFragment.show(getSupportFragmentManager(), "timeMinPicker");

    }

    public void pickMinDate(View view) {
        DialogFragment newFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                Date date = new GregorianCalendar(year, month, day).getTime();
                EditText dateMinField = findViewById(R.id.date_min_field);
                dateMinField.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(date));
            }
        });
        newFragment.show(getSupportFragmentManager(), "dateMinPicker");
    }

    public void pickMaxTime(View view) {
        DialogFragment newFragment = new TimePickerFragment(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                try {
                    Date date = DateFormat.getTimeInstance(DateFormat.SHORT, Locale.ENGLISH).parse(hour + ":" + minute);
                    EditText timeMaxField = findViewById(R.id.time_max_field);
                    if (date != null) {
                        timeMaxField.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(date));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        newFragment.show(getSupportFragmentManager(), "timeMaxPicker");

    }

    public void pickMaxDate(View view) {
        DialogFragment newFragment = new DatePickerFragment(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                Date date = new GregorianCalendar(year, month, day).getTime();
                EditText dateMaxField = findViewById(R.id.date_max_field);
                dateMaxField.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(date));
            }
        });
        newFragment.show(getSupportFragmentManager(), "dateMaxPicker");
    }

    public void pickDuration(View view) {
        DialogFragment newFragment = new DurationPickerFragment(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                EditText durationField = findViewById(R.id.duration_field);
                durationField.setText(String.valueOf(60 * hour + minute));
            }
        });
        newFragment.show(getSupportFragmentManager(), "timeMaxPicker");
    }



}