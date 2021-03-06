package com.ionshield.planner.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.ionshield.planner.R;
import com.ionshield.planner.database.PathRepository;
import com.ionshield.planner.database.entities.compound.NodeWithLocations;
import com.ionshield.planner.fragments.edit.LocationEditDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final String ARG_PATH_DATA = "path_data";
    public static final int POLYLINE_BASE_COLOR = 0x804060FF;
    public static final int POLYLINE_SELECTED_COLOR = 0xC0FF6020;

    private GoogleMap mGoogleMap;
    private FusedLocationProviderClient fusedLocationClient;

    private MapFragmentViewModel mViewModel;
    private final Map<Marker, NodeWithLocations> mMarkers = new HashMap<>();
    private Marker mSelectedMarker;

    private List<LatLng> points;
    private Polyline selectedPolyline;

    public static MapFragment newInstance(List<Double> data) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();

        if (data != null) {
            args.putSerializable(ARG_PATH_DATA, new ArrayList<>(data));
        }

        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.map_fragment, container, false);

        if (getArguments() != null && getArguments().getSerializable(ARG_PATH_DATA) != null) {
            List<Double> data = (List<Double>) getArguments().getSerializable(ARG_PATH_DATA);
            if (data != null) {
                points = new ArrayList<>();
                for (int i = 0; i < data.size() - 1 ; i += 2) {
                    points.add(new LatLng(data.get(i), data.get(i + 1)));
                }
            }
        }





        mViewModel = new ViewModelProvider(requireActivity()).get(MapFragmentViewModel.class);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }


        MaterialToolbar appBar = root.findViewById(R.id.app_bar);
        if (points == null) {
            appBar.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.action_mode) {
                    mViewModel.setMode(mViewModel.getMode().getValue() != null && !mViewModel.getMode().getValue());
                    return true;
                }
                if (item.getItemId() == R.id.action_add) {
                    if (mGoogleMap != null) {
                        mViewModel.addNode(mGoogleMap.getCameraPosition().target);
                        return true;
                    }
                }
                if (item.getItemId() == R.id.action_edit) {
                    if (mSelectedMarker != null) {
                        NodeWithLocations node = mMarkers.get(mSelectedMarker);
                        if (node != null) {
                            long locationId = node.locations == null || node.locations.isEmpty() ? 0 : node.locations.get(0).locationId;
                            if (getActivity() != null) {
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                LocationEditDialog dialog = LocationEditDialog.newInstance(locationId, node.node.nodeId);
                                fragmentManager.beginTransaction()
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .add(android.R.id.content, dialog)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }
                    }
                }
                if (item.getItemId() == R.id.action_delete) {
                    if (mSelectedMarker != null) {
                        NodeWithLocations node = mMarkers.get(mSelectedMarker);
                        if (node != null) {
                            mViewModel.deleteNode(node.node);
                        }
                    }
                }
                return false;
            });

            mViewModel.getMode().observe(getViewLifecycleOwner(), mode -> {
                appBar.setTitle(mode ? R.string.edit_map_title : R.string.map_title);
                appBar.getMenu().clear();
                appBar.inflateMenu(mode ? R.menu.map_appbar_menu_edit : R.menu.map_appbar_menu);

                for (Marker m : mMarkers.keySet()) {
                    m.setDraggable(mode);
                }
            });

        }
        else {
            appBar.setTitle(R.string.map_title);
            appBar.getMenu().clear();
            appBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
            appBar.setNavigationOnClickListener(l -> getParentFragmentManager().popBackStack());
        }





        return root;
    }





    private void enableLocation(GoogleMap googleMap) {
        if (mGoogleMap == null) {
            return;
        }
        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        if (mViewModel.getCameraPosition().getValue() == null) {
            if (fusedLocationClient != null) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(requireActivity(), location -> {
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16f));
                            }
                        });


            }
        }
        else {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mViewModel.getCameraPosition().getValue()));
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;

        googleMap.setMinZoomPreference(10);

        googleMap.setMapStyle(new MapStyleOptions(getResources().getString(R.string.map_style)
                /*"[\n" +
                        "  {\n" +
                        "    \"featureType\": \"poi\",\n" +
                        "    \"stylers\": [\n" +
                        "      { \"visibility\": \"off\" }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "]"*/
        ));

        if (points == null || points.isEmpty()) {
            if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
            else {
                enableLocation(googleMap);
            }
        }
        else {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 16f));
        }




        if (mViewModel.getCameraPosition().getValue() != null) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mViewModel.getCameraPosition().getValue()));
        }

        //Get nodes
        LiveData<List<NodeWithLocations>> data = mViewModel.getData();


        //Place markers


        data.observe(getViewLifecycleOwner(), nodes -> {
            mMarkers.clear();
            googleMap.clear();
            MarkerOptions options = new MarkerOptions();
            for (NodeWithLocations node : nodes) {

                options.title(node.locations == null || node.locations.isEmpty() ? getResources().getString(R.string.map_empty_node) : node.locations.get(0).name)
                .position(new LatLng(node.node.latitude, node.node.longitude))
                .draggable(mViewModel.getMode().getValue() != null && mViewModel.getMode().getValue())
                .alpha(node.locations == null || node.locations.isEmpty() ? 0.3f : 0.5f)
                .icon(BitmapDescriptorFactory.defaultMarker(node.locations == null || node.locations.isEmpty() ? BitmapDescriptorFactory.HUE_AZURE : BitmapDescriptorFactory.HUE_RED))
                .flat(true);


                mMarkers.put(googleMap.addMarker(options), node);
            }

            //Place polyline
            if (points != null) {
                for (int i = 0; i < points.size() - 1; i++) {
                    LatLng start = points.get(i);
                    LatLng end = points.get(i + 1);

                    List<LatLng> p = new ArrayList<>();
                    DirectionsResult directions = PathRepository.getInstance().getPath(new com.google.maps.model.LatLng(start.latitude, start.longitude), new com.google.maps.model.LatLng(end.latitude, end.longitude));
                    if (directions != null && directions.routes.length != 0) {
                        DirectionsStep[] steps = directions.routes[0].legs[0].steps;
                        for (DirectionsStep step : steps) {
                            step.polyline.decodePath().forEach(ll -> p.add(new LatLng(ll.lat, ll.lng)));
                            //googleMap.addPolyline(new PolylineOptions().add(step.polyline.decodePath().stream().map(ll -> new LatLng(ll.lat, ll.lng)).toArray(LatLng[]::new)));
                        }
                    }
                    PolylineOptions po = new PolylineOptions().zIndex(0).color(POLYLINE_BASE_COLOR).clickable(true).startCap(new RoundCap()).endCap(new RoundCap())/*.pattern(Arrays.asList(new Gap(10), new Dash(30), new Gap(10)))*/.jointType(JointType.ROUND);
                    if (start.longitude != end.longitude || start.latitude != end.latitude) {
                        googleMap.addPolyline(po.add(start).add(p.toArray(new LatLng[]{})).add(end));
                    }

                }
            }
        });

        googleMap.setOnPolylineClickListener(polyline -> {
            if (selectedPolyline != null) {
                selectedPolyline.setColor(POLYLINE_BASE_COLOR);
                selectedPolyline.setZIndex(0);
            }
            selectedPolyline = polyline;
            polyline.setColor(POLYLINE_SELECTED_COLOR);
            selectedPolyline.setZIndex(1);
        });

        googleMap.setOnCameraMoveListener(() -> mViewModel.setCameraPosition(googleMap.getCameraPosition()));

        googleMap.setOnMapClickListener(latLng -> {
            mSelectedMarker = null;
            if (selectedPolyline != null) {
                selectedPolyline.setColor(POLYLINE_BASE_COLOR);
                selectedPolyline.setZIndex(0);
            }
            selectedPolyline = null;
        });

        googleMap.setOnMarkerClickListener(marker -> {
            mSelectedMarker = marker;
            return false;
        });

        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                if (mViewModel.getMode().getValue() != null && mViewModel.getMode().getValue()) {
                    NodeWithLocations node = mMarkers.get(marker);
                    if (node != null) {
                        node.node.latitude = marker.getPosition().latitude;
                        node.node.longitude = marker.getPosition().longitude;
                        mViewModel.updateNode(node.node);
                    }
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int i = 0; i < permissions.length; i++) {
            if (requestCode == LOCATION_PERMISSION_REQUEST_CODE && permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                if (mGoogleMap != null) {
                    enableLocation(mGoogleMap);
                }
            }
        }

    }
}
