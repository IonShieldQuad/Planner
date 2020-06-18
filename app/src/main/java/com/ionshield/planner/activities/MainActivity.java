package com.ionshield.planner.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.DatabaseMenuActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //deleteDatabase("PlanningDatabase.db");
    }

    public void useButtonClicked(View view) {
        Intent intent = new Intent(this,LocationFinderActivity.class);
        startActivity(intent);
    }

    public void databaseButtonClicked(View view) {
        Intent intent = new Intent(this, DatabaseMenuActivity.class);
        startActivity(intent);
    }
}