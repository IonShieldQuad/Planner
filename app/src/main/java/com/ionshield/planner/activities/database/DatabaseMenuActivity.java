package com.ionshield.planner.activities.database;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Modes;

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

    public void nodesButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.NODES.mode);
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

    public void linkTypesButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.LINK_TYPES.mode);
        startActivity(intent);
    }

    public void linksButtonClicked(View view) {
        Intent intent = new Intent(this, ListActivity.class);
        intent.putExtra("mode", Modes.LINKS.mode);
        startActivity(intent);
    }
}