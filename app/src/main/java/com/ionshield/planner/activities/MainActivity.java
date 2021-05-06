package com.ionshield.planner.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.DatabaseMenuActivity;
import com.ionshield.planner.activities.database.ListActivity;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.database.DBC;
import com.ionshield.planner.database.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private int planId = -1;
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        helper = new DatabaseHelper(this);
        db = helper.getReadableDatabase();
        //deleteDatabase("PlanningDatabase.db");
        updateText();
    }

    public void updateText() {
        TextView currentPlanTextView = findViewById(R.id.selected_plan_name);
        if (planId <= 0) {
            currentPlanTextView.setText(R.string.current_plan_none);
        }
        else {
            String planName = queryPlanName(planId);
            if (planName != null) {
                currentPlanTextView.setText(planName);
            }
            else {
                currentPlanTextView.setText(R.string.current_plan_none);
            }
        }
    }

    public String queryPlanName(int id) {
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM " + DBC.Plans.TABLE_NAME + " WHERE " + DBC.Plans._ID + "=?;", new String[]{String.valueOf(id)});

            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBC.Types.NAME));

            cursor.close();
            return name;
        }
        catch (SQLException e) {
            Toast.makeText(this, R.string.database_error_message, Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    public void useButtonClicked(View view) {
        Intent intent = new Intent(this, LocationFinderActivity.class);
        startActivity(intent);
    }

    public void databaseButtonClicked(View view) {
        Intent intent = new Intent(this, DatabaseMenuActivity.class);
        startActivity(intent);
    }

    public void pathButtonClicked(View view) {
        Intent intent = new Intent(this, CustomMapActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 1) {
                planId = data.getIntExtra("value", -1);
                updateText();
            }
        }
    }

    public void planSelectButtonClicked(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.PLANS.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }
    public void planListButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.PLANS.mode);
        startActivity(intent);
    }
}