package com.ionshield.planner.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.maps.model.LatLng;
import com.ionshield.planner.R;
import com.ionshield.planner.database.entities.Plan;
import com.ionshield.planner.fragments.list.ListConstraintFragment;
import com.ionshield.planner.fragments.list.ListEventFragment;
import com.ionshield.planner.fragments.list.ListNoteFragment;
import com.ionshield.planner.fragments.list.ListPlanFragment;
import com.ionshield.planner.fragments.list.Mode;
import com.ionshield.planner.fragments.list.ScheduleFragment;
import com.ionshield.planner.fragments.list.SelectionMode;

import java.time.LocalDateTime;

public class PlanMenuFragment extends Fragment {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private PlanMenuViewModel mViewModel;

    public static PlanMenuFragment newInstance() {
        return new PlanMenuFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.plan_menu_fragment, container, false);
        mViewModel = new ViewModelProvider(requireActivity()).get(PlanMenuViewModel.class);

        Button selectButton = root.findViewById(R.id.select_button);
        if (selectButton != null) {
            selectButton.setOnClickListener(c -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                ListPlanFragment fragment = ListPlanFragment.newInstance(1, Mode.SELECT, true);
                fragment.setOnSelectionListener((selected, mode) -> {
                    if (mode == SelectionMode.SELECT) {
                        mViewModel.setPlan(selected);
                        fragmentManager.popBackStack();
                    }
                });
                fragmentManager.beginTransaction()
                        .add(android.R.id.content, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            });
        }

        Button editButton = root.findViewById(R.id.edit_button);
        if (editButton != null) {
            editButton.setOnClickListener(c -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                ListPlanFragment fragment = ListPlanFragment.newInstance(1, Mode.EDIT, true);
                fragmentManager.beginTransaction()
                        .add(android.R.id.content, fragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack(null)
                        .commit();
            });
        }

        Button notesButton = root.findViewById(R.id.notes_button);
        if (notesButton != null) {
            notesButton.setOnClickListener(c -> {
                Plan plan = mViewModel.getPlan().getValue();
                if (plan == null) {
                    Toast.makeText(requireActivity(), R.string.plan_not_selected, Toast.LENGTH_SHORT).show();
                }
                else {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    ListNoteFragment fragment = ListNoteFragment.newInstance(1, Mode.EDIT, true, plan.planId);
                    fragmentManager.beginTransaction()
                            .add(android.R.id.content, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }


        Button constraintsButton = root.findViewById(R.id.constraints_button);
        if (constraintsButton != null) {
            constraintsButton.setOnClickListener(c -> {
                Plan plan = mViewModel.getPlan().getValue();
                if (plan == null) {
                    Toast.makeText(requireActivity(), R.string.plan_not_selected, Toast.LENGTH_SHORT).show();
                }
                else {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    ListConstraintFragment fragment = ListConstraintFragment.newInstance(1, Mode.EDIT, true, plan.planId);
                    fragmentManager.beginTransaction()
                            .add(android.R.id.content, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }


        Button eventsButton = root.findViewById(R.id.events_button);
        if (eventsButton != null) {
            eventsButton.setOnClickListener(c -> {
                Plan plan = mViewModel.getPlan().getValue();
                if (plan == null) {
                    Toast.makeText(requireActivity(), R.string.plan_not_selected, Toast.LENGTH_SHORT).show();
                }
                else {
                    FragmentManager fragmentManager = getParentFragmentManager();
                    ListEventFragment fragment = ListEventFragment.newInstance(1, Mode.EDIT, true, plan.planId);
                    fragmentManager.beginTransaction()
                            .add(android.R.id.content, fragment)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            .addToBackStack(null)
                            .commit();
                }
            });
        }

        Button calendarButton = root.findViewById(R.id.calendar_button);

        Button optimizeButton = root.findViewById(R.id.optimize_button);

        if (optimizeButton != null) {
            optimizeButton.setOnClickListener(c -> {
                Plan plan = mViewModel.getPlan().getValue();
                if (plan == null) {
                    Toast.makeText(requireActivity(), R.string.plan_not_selected, Toast.LENGTH_SHORT).show();
                }
                else {
                    FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
                    if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                    }
                    else {
                        fusedLocationClient.getLastLocation().addOnSuccessListener(requireActivity(), location -> {
                            if (location != null) {
                                FragmentManager fragmentManager = getParentFragmentManager();
                                ScheduleFragment fragment = ScheduleFragment.newInstance(1, true, plan.planId, new LatLng(location.getLatitude(), location.getLongitude()), LocalDateTime.now());
                                fragmentManager.beginTransaction()
                                        .add(android.R.id.content, fragment)
                                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });
                    }


                }
            });
        }


        EditText planNameView = root.findViewById(R.id.plan_name_view);

        mViewModel.getPlan().observe(getViewLifecycleOwner(), plan -> {
            boolean p = plan != null && plan.planId != 0;
            planNameView.setText(plan == null ? "" : plan.name);
            if (notesButton != null) notesButton.setEnabled(p);
            if (eventsButton != null) eventsButton.setEnabled(p);
            if (constraintsButton != null) constraintsButton.setEnabled(p);
            if (calendarButton != null) calendarButton.setEnabled(p);
            if (optimizeButton != null) optimizeButton.setEnabled(p);


        });



        return root;
    }

}