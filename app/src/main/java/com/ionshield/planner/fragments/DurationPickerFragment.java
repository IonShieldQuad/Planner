package com.ionshield.planner.fragments;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;


public class DurationPickerFragment extends DialogFragment {
    TimePickerDialog.OnTimeSetListener listener;

    public DurationPickerFragment(TimePickerDialog.OnTimeSetListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), listener, 0, 0,
                true);
    }
}
