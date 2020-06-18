package com.ionshield.planner.activities.database;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.ItemsMode;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.activities.database.modes.TypesMode;

public class DatabaseMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_menu);
    }

    public void itemsButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.ITEMS.mode);
        startActivity(intent);
    }

    public void typesButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.TYPES.mode);
        startActivity(intent);
    }

    public void itemAssocButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.ITEM_ASSOC.mode);
        startActivity(intent);
    }

    public void locationsButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.LOCATIONS.mode);
        startActivity(intent);
    }

    public void eventsButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.EVENTS.mode);
        startActivity(intent);
    }

    public void plansButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.PLANS.mode);
        startActivity(intent);
    }

    public void eventAssocButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.EVENT_ASSOC.mode);
        startActivity(intent);
    }
}