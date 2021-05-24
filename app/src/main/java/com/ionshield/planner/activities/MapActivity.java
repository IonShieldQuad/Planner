package com.ionshield.planner.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.ionshield.planner.R;
import com.ionshield.planner.database.DBC;
import com.ionshield.planner.database.DatabaseHelper;

import java.util.HashMap;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private SQLiteDatabase mDatabase;
    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient fusedLocationClient;

    private boolean mEditEnabled = false;
    private Map<Marker, Integer> mMarkers = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mDatabase = new DatabaseHelper(this).getReadableDatabase();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_appbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            if (isEditEnabled()) {
                setEditEnabled(false);
                item.getIcon().setTint(0xffffffff);
            }
            else {
                setEditEnabled(true);
                item.getIcon().setTint(0xff22ff00);
            }

        }
        return true;
    }


    public void setCurrentButtonClicked(View view) {
    }

    public void toSelectedButtonClicked(View view) {
    }

    public boolean isEditEnabled() {
        return mEditEnabled;
    }

    public void setEditEnabled(boolean enable) {
        TextView title = (TextView) findViewById(R.id.title);


        //Toggle on
        if (enable && !mEditEnabled) {
            mEditEnabled = true;

            if (mMarkers != null) {
                for (Marker m : mMarkers.keySet()) {
                    m.setDraggable(true);
                }
            }

            if (title != null) {
                title.setText(R.string.edit_map_title);
            }
        }

        //Toggle off
        if (!enable && mEditEnabled) {
            mEditEnabled = false;

            if (mMarkers != null) {
                for (Marker m : mMarkers.keySet()) {
                    m.setDraggable(false);
                }
            }

            if (title != null) {
                title.setText(R.string.map_title);
            }
        }
    }

    public Cursor queryLocationNodes(SQLiteDatabase db) {
        return db.rawQuery(
                "SELECT " +
                        DBC.Nodes.TABLE_NAME + "." + DBC.Nodes._ID + ", " +
                        DBC.Locations.TABLE_NAME + "." + DBC.Locations.NAME + ", " +
                        DBC.Locations.TABLE_NAME + "." + DBC.Locations.DESC + ", " +
                        DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_X + " AS lat" + ", " +
                        DBC.Nodes.TABLE_NAME + "." + DBC.Nodes.COORDINATE_Y + " AS lng" + ", " +
                        DBC.Types.TABLE_NAME + "." + DBC.Types.COLOR + " AS " + DBC.Types.COLOR + " " +
                        " FROM " + DBC.Nodes.TABLE_NAME +
                        " LEFT JOIN " + DBC.Locations.TABLE_NAME +
                        " ON " + DBC.Nodes.TABLE_NAME + "." + DBC.Nodes._ID +
                        " = " + DBC.Locations.NODE_ID +
                        " LEFT JOIN " + DBC.Types.TABLE_NAME +
                        " ON " + DBC.Locations.TYPE_ID +
                        " = " + DBC.Types.TABLE_NAME + "." + DBC.Types._ID + ";",
                new String[]{});
    }

    public void updateMarker(SQLiteDatabase db, int id, LatLng coordinates) {
        Cursor c = db.rawQuery(
                "UPDATE " +
                        DBC.Nodes.TABLE_NAME + " " +
                        "SET " + DBC.Nodes.COORDINATE_X + "=?, " +
                        DBC.Nodes.COORDINATE_Y + "=? " +
                        "WHERE " + DBC.Nodes._ID + "=?",
        new String[]{String.valueOf(coordinates.latitude), String.valueOf(coordinates.longitude), String.valueOf(id)});
        c.close();
    }

    private void enableLocation(GoogleMap googleMap) {
        if (mGoogleMap == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        if (fusedLocationClient != null) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16f));
                            }
                        }
                    });



        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        googleMap.setMinZoomPreference(10);

        googleMap.setMapStyle(new MapStyleOptions(
                "[\n" +
                     "  {\n" +
                     "    \"featureType\": \"poi\",\n" +
                     "    \"stylers\": [\n" +
                     "      { \"visibility\": \"off\" }\n" +
                     "    ]\n" +
                     "  }\n" +
                     "]"
        ));

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        else {
            enableLocation(googleMap);
        }

        //Query nodes
        Cursor nodes = queryLocationNodes(mDatabase);
        //Place markers
        for (nodes.moveToFirst(); !nodes.isAfterLast(); nodes.moveToNext()) {
            int id = nodes.getInt(nodes.getColumnIndexOrThrow(DBC.Nodes._ID));
            String name = nodes.getString(nodes.getColumnIndexOrThrow(DBC.Locations.NAME));
            int color = nodes.getInt(nodes.getColumnIndexOrThrow(DBC.Types.COLOR));
            double lat = nodes.getDouble(nodes.getColumnIndexOrThrow("lat"));
            double lng = nodes.getDouble(nodes.getColumnIndexOrThrow("lng"));

            float opacity;
            if (name == null) {
                opacity = 0.2f;
            }
            else {
                opacity = 0.4f;
            }
            mMarkers.put(googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).alpha(opacity).title(name).flat(true)), id);
        }

        nodes.close();


        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                //TODO
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (mGoogleMap != null) {
                enableLocation(mGoogleMap);
            }
        }
    }
}