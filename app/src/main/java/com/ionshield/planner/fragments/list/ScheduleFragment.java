package com.ionshield.planner.fragments.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.maps.model.LatLng;
import com.ionshield.planner.R;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class ScheduleFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_SHOW_TOOLBAR = "show_toolbar";
    private static final String ARG_PLAN_ID = "plan_id";
    private static final String ARG_LAT = "lat";
    private static final String ARG_LNG = "lng";
    private static final String ARG_DATETIME = "datetime";

    private int mColumnCount = 1;

    private ScheduleViewModel mModel;
    private boolean mShowToolbar = false;

    private View root;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ScheduleFragment() {
    }


    @SuppressWarnings("unused")
    public static ScheduleFragment newInstance(int columnCount, boolean showToolbar, Long planId, LatLng origin, LocalDateTime startTime) {
        ScheduleFragment fragment = new ScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putBoolean(ARG_SHOW_TOOLBAR, showToolbar);
        args.putLong(ARG_PLAN_ID, planId);
        args.putDouble(ARG_LAT, origin.lat);
        args.putDouble(ARG_LNG, origin.lng);
        args.putLong(ARG_DATETIME, startTime.toEpochSecond(ZoneOffset.UTC));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(this).get(ScheduleViewModel.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT, 1);
            mShowToolbar = getArguments().getBoolean(ARG_SHOW_TOOLBAR, false);
            long planId = getArguments().getLong(ARG_PLAN_ID, 0);
            double lat = getArguments().getDouble(ARG_LAT, 0);
            double lng = getArguments().getDouble(ARG_LNG, 0);
            long epoch = getArguments().getLong(ARG_DATETIME, 0);
            mModel.optimizePlan(planId, new LatLng(lat, lng), LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC));
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_schedule_item_list, container, false);

        RecyclerView recyclerView = root.findViewById(R.id.list);
        recyclerView.addItemDecoration(new DividerItemDecoration(root.getContext(), DividerItemDecoration.VERTICAL));

        MaterialToolbar appBar = root.findViewById(R.id.app_bar);
        appBar.setNavigationOnClickListener(l -> getParentFragmentManager().popBackStack());
        appBar.setVisibility(mShowToolbar ? View.VISIBLE : View.GONE);

        // Set the adapter
        if (recyclerView != null) {
            Context context = root.getContext();
            //RecyclerView recyclerView = (RecyclerView) root;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            EventWithDateTimeDataRecyclerViewAdapter adapter = new EventWithDateTimeDataRecyclerViewAdapter();
            recyclerView.setAdapter(adapter);

            mModel.getResult().observe(getViewLifecycleOwner(), result -> {
                if (result == null || result.data == null) {
                    adapter.submitList(new ArrayList<>());
                    return;
                }
                adapter.submitList(result.data);
            });
        }


        ProgressBar bar = root.findViewById(R.id.progress_horizontal);
        mModel.getProgress().observe(getViewLifecycleOwner(), progress -> {
            if (progress == null) {
                if (bar.getVisibility() == View.GONE) {
                    bar.setIndeterminate(true);
                    bar.setVisibility(View.VISIBLE);
                }
                bar.setVisibility(View.GONE);
            }
            else {
                bar.setIndeterminate(false);
                bar.setVisibility(View.VISIBLE);
                bar.setProgress((int)Math.round(progress * 100), true);
            }
        });




        return root;
    }


}
