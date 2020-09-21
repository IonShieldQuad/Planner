package com.ionshield.planner.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.ionshield.planner.R;
import com.ionshield.planner.activities.database.modes.Modes;
import com.ionshield.planner.database.DBC;
import com.ionshield.planner.database.DatabaseHelper;
import com.ionshield.planner.fragments.CustomMapFragment;

import java.util.HashMap;
import java.util.Map;

public class CustomMapActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);

        mDatabase = new DatabaseHelper(this).getReadableDatabase();

        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.root, new CustomMapFragment())
                .commit();*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            }
        }
        return true;
    }

    public void findButtonClicked(View view) {
        CustomMapFragment mapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        if (mapFragment == null) return;

        if (mapFragment.getCurrentNode() <= 0) {
            Toast.makeText(this, R.string.no_current_position_message, Toast.LENGTH_SHORT).show();
            return;
        }
        int currentNode = mapFragment.getCurrentNode();

        EditText eventIdField = findViewById(R.id.event_id_field);
        //EditText heuristicField = findViewById(R.id.heuristic_field);
        //EditText maxDistanceField = findViewById(R.id.max_distance_field);

        int eventId;
        double heuristic;
        double maxDistance;

        try {
            eventId = Integer.parseInt(eventIdField.getText().toString());
            //heuristic = Double.parseDouble(heuristicField.getText().toString());
            //maxDistance = Double.parseDouble(maxDistanceField.getText().toString());
            //maxDistance = Math.max(0, maxDistance);
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        heuristic = Double.parseDouble(sp.getString("heuristic", "0"));
        maxDistance = Double.parseDouble(sp.getString("max_distance", "1000"));
        maxDistance = Math.max(0, maxDistance);

        Cursor cursor = mDatabase.rawQuery("SELECT * FROM " + DBC.Nodes.TABLE_NAME
                + " WHERE " + BaseColumns._ID + "=?",
                new String[]{String.valueOf(currentNode)});

        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            Toast.makeText(this, R.string.no_locations_found_message, Toast.LENGTH_LONG).show();
            cursor.close();
            return;
        }

        double x = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_X));
        double y = cursor.getDouble(cursor.getColumnIndexOrThrow(DBC.Nodes.COORDINATE_Y));

        cursor.close();


        cursor = mDatabase.rawQuery("SELECT " + DBC.Nodes.TABLE_NAME + ".* FROM " + DBC.Nodes.TABLE_NAME + ", "
                        + DBC.Locations.TABLE_NAME + ", "
                        + DBC.Events.TABLE_NAME + ", "
                        + DBC.ItemTypeAssoc.TABLE_NAME + ""
                        + " WHERE ("
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " * "
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " + "
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + " * "
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + ") <= (? * ?)"
                        + " AND " + DBC.Events.TABLE_NAME + "." + DBC.Events._ID + "=?"
                        + " AND "+ DBC.Events.TABLE_NAME + "." + DBC.Events.ITEM_ID + "="
                        + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc.ITEM_ID
                        + " AND " + DBC.Locations.TABLE_NAME + "." + DBC.Locations.TYPE_ID + "="
                        + DBC.ItemTypeAssoc.TABLE_NAME + "." + DBC.ItemTypeAssoc.TYPE_ID
                        + " AND " + DBC.Locations.NODE_ID + "="
                        + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes._ID
                        + " ORDER BY ("
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " * "
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " - " + "?)" + " + "
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + " * "
                        + "(" + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " - " + "?)" + ") ASC;",
                new String[]{String.valueOf(x), String.valueOf(x),
                        String.valueOf(y), String.valueOf(y),
                        String.valueOf(maxDistance), String.valueOf(maxDistance),
                        String.valueOf(eventId),
                        String.valueOf(x), String.valueOf(x),
                        String.valueOf(y), String.valueOf(y)});
        cursor.moveToFirst();
        if (cursor.isAfterLast()) {
            Toast.makeText(this, R.string.no_locations_found_message, Toast.LENGTH_LONG).show();
            cursor.close();
            return;
        }

        Map<Integer, Double> targets = new HashMap<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(BaseColumns._ID));
            targets.put(id, heuristic);
        }

        cursor.close();

        mapFragment.plotPath(currentNode, targets);
    }

    public void setCurrentButtonClicked(View view) {
        CustomMapFragment mapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        if (mapFragment == null) return;

        int selectedNode = mapFragment.getSelectedNode();
        mapFragment.setCurrentNode(selectedNode);
    }

    public void toSelectedButtonClicked(View view) {
        CustomMapFragment mapFragment = (CustomMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);

        if (mapFragment == null) return;

        if (mapFragment.getCurrentNode() <= 0) {
            Toast.makeText(this, R.string.no_current_position_message, Toast.LENGTH_SHORT).show();
            return;
        }
        int currentNode = mapFragment.getCurrentNode();

        if (mapFragment.getSelectedNode() <= 0) {
            Toast.makeText(this, R.string.no_selected_node_message, Toast.LENGTH_SHORT).show();
            return;
        }
        int selectedNode = mapFragment.getSelectedNode();

        //EditText heuristicField = findViewById(R.id.heuristic_field);

        double heuristic;

        try {
            //heuristic = Double.parseDouble(heuristicField.getText().toString());
        }
        catch (NumberFormatException e) {
            Toast.makeText(this, R.string.number_format_error_message, Toast.LENGTH_LONG).show();
            return;
        }

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        heuristic = Double.parseDouble(sp.getString("heuristic", "0"));

        Map<Integer, Double> targets = new HashMap<>();
        targets.put(selectedNode, heuristic);
        mapFragment.plotPath(currentNode, targets);

    }

    public void browseEvents(View view) {
        Intent intent = new Intent(this, com.ionshield.planner.activities.database.ListActivity.class);
        intent.putExtra("mode", Modes.EVENTS.mode);
        intent.putExtra("isSelector", true);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ((EditText)findViewById(R.id.event_id_field)).setText(String.valueOf(data.getIntExtra("value", 0)));
        }
    }
}