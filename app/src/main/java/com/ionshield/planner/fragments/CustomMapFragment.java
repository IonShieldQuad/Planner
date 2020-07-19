package com.ionshield.planner.fragments;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ionshield.planner.R;
import com.ionshield.planner.database.DatabaseHelper;
import com.ionshield.planner.map.CustomMapView;
import com.ionshield.planner.pathfinding.PathResult;
import com.ionshield.planner.pathfinding.Pathfinder;

import java.util.Map;

public class CustomMapFragment extends Fragment {

    private CustomMapViewModel mViewModel;
    private CustomMapView mMap;
    private Pathfinder mPathfinder;

    public static CustomMapFragment newInstance() {
        return new CustomMapFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.custom_map_fragment, container, false);

        final CustomMapView map = root.findViewById(R.id.map_view);
        SQLiteDatabase db = new DatabaseHelper(getActivity()).getReadableDatabase();
        map.setDatabase(db);
        map.addClickListener(new CustomMapView.ClickListener() {
            @Override
            public void onClick(double absX, double absY, int nodeId) {
                map.setSelectedNodeId(nodeId);
            }
        });

        mMap = map;

        ImageButton upButton = root.findViewById(R.id.up_button);
        upButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveBy(0, 0.25);
            }
        });

        ImageButton downButton = root.findViewById(R.id.down_button);
        downButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveBy(0, -0.25);
            }
        });

        ImageButton rightButton = root.findViewById(R.id.right_button);
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveBy(0.25, 0);
            }
        });

        ImageButton leftButton = root.findViewById(R.id.left_button);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.moveBy(-0.25, 0);
            }
        });

        Button zoomInButton = root.findViewById(R.id.zoom_in_button);
        zoomInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.zoomIn(0.5, 0.5);
            }
        });

        Button zoomOutButton = root.findViewById(R.id.zoom_out_button);
        zoomOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                map.zoomOut(0.5, 0.5);
            }
        });

        CheckBox markersCheckBox = root.findViewById(R.id.markersCheckBox);
        markersCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                map.setDisplayMarkers(b);
            }
        });

        mPathfinder = new Pathfinder(db);

        /*Map<Integer, Double> tgt = new HashMap<>();
        tgt.put(5, 0.0);
        PathResult pr = pf.findPath(2, tgt);
        map.setPathLinkIds(pr.pathLinks);
        map.setPathNodeIds(pr.pathNodes);
        map.setSearchLinkIds(pr.searchLinks);
        map.setSearchNodeIds(pr.searchNodes);*/

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(CustomMapViewModel.class);
        // TODO: Use the ViewModel
    }

    public void plotPath(int startNode, Map<Integer, Double> targetNodes) {
        PathResult pr = mPathfinder.findPath(startNode, targetNodes);
        mMap.setPathLinkIds(pr.pathLinks);
        mMap.setPathNodeIds(pr.pathNodes);
        mMap.setSearchLinkIds(pr.searchLinks);
        mMap.setSearchNodeIds(pr.searchNodes);
        mMap.invalidate();
    }

    public int getSelectedNode() {
        if (mMap.getSelectedNodeIds() == null || mMap.getSelectedNodeIds().isEmpty()) return -1;
        return mMap.getSelectedNodeIds().get(0);
    }

    public void setSelectedNode(int id) {
        mMap.setSelectedNodeId(id);
    }

    public int getCurrentNode() {
        return mMap.getCurrentNodeId();
    }

    public void setCurrentNode(int id) {
        mMap.setCurrentNodeId(id);
    }

}