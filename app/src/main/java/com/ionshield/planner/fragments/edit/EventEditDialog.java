package com.ionshield.planner.fragments.edit;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ionshield.planner.R;
import com.ionshield.planner.fragments.DatePickerFragment;
import com.ionshield.planner.fragments.DurationPickerFragment;
import com.ionshield.planner.fragments.TimePickerFragment;
import com.ionshield.planner.fragments.list.ListItemFragment;
import com.ionshield.planner.fragments.list.Mode;
import com.ionshield.planner.fragments.list.SelectionMode;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;

public class EventEditDialog extends DialogFragment {
    private EventEditDialogViewModel mViewModel;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);

    public static EventEditDialog newInstance(long id, Long planId) {
        EventEditDialog dialog = new EventEditDialog();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putLong("plan_id", planId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.event_edit_dialog_fragment, container, false);

        long id = 0;
        Long planId = null;
        if (getArguments() != null) {
            id = getArguments().getLong("id", 0);
            planId = getArguments().getLong("plan_id", 0);
        }

        mViewModel = new ViewModelProvider(this).get(EventEditDialogViewModel.class);

        mViewModel.setEventById(id, planId);

        MaterialToolbar appBar = root.findViewById(R.id.app_bar);


        appBar.setNavigationOnClickListener(l -> getParentFragmentManager().popBackStack());

        appBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                mViewModel.writeEvent();
                getParentFragmentManager().popBackStack();
                return true;
            }
            return false;
        });

        TextInputEditText nameField = root.findViewById(R.id.name_field);
        TextInputEditText descField = root.findViewById(R.id.desc_field);

        TextInputEditText itemNameField = root.findViewById(R.id.item_name_field);

        TextInputEditText timeMinField = root.findViewById(R.id.time_min_field);
        TextInputEditText dateMinField = root.findViewById(R.id.date_min_field);

        TextInputEditText timeMaxField = root.findViewById(R.id.time_max_field);
        TextInputEditText dateMaxField = root.findViewById(R.id.date_max_field);

        TextInputEditText durationField = root.findViewById(R.id.duration_field);

        CheckBox isEnabledCheckBox = root.findViewById(R.id.is_enabled_checkbox);
        CheckBox isDateUsedCheckBox = root.findViewById(R.id.use_date_checkbox);

        nameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (nameField.hasFocus()) {
                    mViewModel.setName(editable.toString());
                    //updateText(nameField, editable.toString());
                }
            }
        });

        descField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (descField.hasFocus()) {
                    mViewModel.setDescription(editable.toString());
                }
            }
        });

        timeMinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (timeMinField.hasFocus()) {
                    try {
                        LocalTime time = timeFormatter.parse(editable.toString(), LocalTime::from);
                        mViewModel.setTimeMin(time.getHour(), time.getMinute());
                        timeMinField.setError(null);
                    }
                    catch (DateTimeParseException e) {
                        timeMinField.setError(getResources().getString(R.string.time_parse_error));
                    }
                }
            }
        });


        dateMinField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (dateMinField.hasFocus()) {
                    try {
                        LocalDate date = dateFormatter.parse(editable.toString(), LocalDate::from);
                        mViewModel.setDateMin(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
                        dateMinField.setError(null);
                    }
                    catch (DateTimeParseException e) {
                        dateMinField.setError(getResources().getString(R.string.date_parse_error));
                    }
                }
            }
        });


        timeMaxField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (timeMaxField.hasFocus()) {
                    try {
                        LocalTime time = timeFormatter.parse(editable.toString(), LocalTime::from);
                        mViewModel.setTimeMax(time.getHour(), time.getMinute());
                        timeMaxField.setError(null);
                    }
                    catch (DateTimeParseException e) {
                        timeMaxField.setError(getResources().getString(R.string.time_parse_error));
                    }
                }
            }
        });


        dateMaxField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (dateMaxField.hasFocus()) {
                    try {
                        LocalDate date = dateFormatter.parse(editable.toString(), LocalDate::from);
                        mViewModel.setDateMax(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
                        dateMaxField.setError(null);
                    }
                    catch (DateTimeParseException e) {
                        dateMaxField.setError(getResources().getString(R.string.date_parse_error));
                    }
                }
            }
        });


        durationField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (durationField.hasFocus()) {
                    try {
                        Duration duration = Duration.ofMinutes(Integer.parseInt(editable.toString()));
                        mViewModel.setDuration(duration);
                        dateMaxField.setError(null);
                    }
                    catch (NumberFormatException e) {
                        dateMaxField.setError(getResources().getString(R.string.number_format_error_message));
                    }
                }
            }
        });

        isEnabledCheckBox.setOnClickListener(l -> {
            mViewModel.setEnabled(isEnabledCheckBox.isChecked());
        });

        isDateUsedCheckBox.setOnClickListener(l -> {
            mViewModel.setFullDateUsed(isDateUsedCheckBox.isChecked());
        });


        Button timeMinBrowseButton = root.findViewById(R.id.time_min_browse_button);
        Button dateMinBrowseButton = root.findViewById(R.id.date_min_browse_button);
        Button timeMaxBrowseButton = root.findViewById(R.id.time_max_browse_button);
        Button dateMaxBrowseButton = root.findViewById(R.id.date_max_browse_button);
        Button durationBrowseButton = root.findViewById(R.id.duration_browse_button);




        timeMinBrowseButton.setOnClickListener(l -> {
            DialogFragment fragment = new TimePickerFragment((timePicker, hour, minute) -> mViewModel.setTimeMin(hour, minute));
            fragment.show(getParentFragmentManager(), "timeMinPicker");
        });

        dateMinBrowseButton.setOnClickListener(l -> {
            DialogFragment fragment = new DatePickerFragment((datePicker, year, month, day) -> mViewModel.setDateMin(year, month, day));
            fragment.show(getParentFragmentManager(), "dateMinPicker");
        });

        timeMaxBrowseButton.setOnClickListener(l -> {
            DialogFragment fragment = new TimePickerFragment((timePicker, hour, minute) -> mViewModel.setTimeMax(hour, minute));
            fragment.show(getParentFragmentManager(), "timeMaxPicker");
        });

        dateMaxBrowseButton.setOnClickListener(l -> {
            DialogFragment fragment = new DatePickerFragment((datePicker, year, month, day) -> mViewModel.setDateMax(year, month, day));
            fragment.show(getParentFragmentManager(), "dateMaxPicker");
        });

        durationBrowseButton.setOnClickListener(l -> {
            DialogFragment fragment = new DurationPickerFragment((timePicker, hour, minute) -> mViewModel.setDuration(Duration.ofMinutes(hour * 60 + minute)));
            fragment.show(getParentFragmentManager(), "durationPicker");
        });

        Button itemSelectButton = root.findViewById(R.id.item_select_button);
        itemSelectButton.setOnClickListener(l -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            ListItemFragment fragment = ListItemFragment.newInstance(1, Mode.SELECT, true);
            fragment.setOnSelectionListener((selected, mode) -> {
                if (mode == SelectionMode.SELECT) {
                    mViewModel.setItem(selected);
                    fragmentManager.popBackStack();
                }
            });
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        });


        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            if (!nameField.hasFocus()) {
                nameField.setText(event.event.name);
            }

            if (!descField.hasFocus()) {
                descField.setText(event.event.description);
            }

            itemNameField.setText(event.item == null ? "" : event.item.name);

            if (!timeMinField.hasFocus()) {
                timeMinField.setText(event.event.datetimeMin == null ? "" : event.event.datetimeMin.format(timeFormatter));
                timeMinField.setError(null);
            }
            if (!dateMinField.hasFocus()) {
                dateMinField.setText(event.event.datetimeMin == null ? "" : event.event.datetimeMin.format(dateFormatter));
                dateMinField.setError(null);
            }

            if (!timeMaxField.hasFocus()) {
                timeMaxField.setText(event.event.datetimeMax == null ? "" : event.event.datetimeMax.format(timeFormatter));
                timeMaxField.setError(null);
            }
            if (!dateMaxField.hasFocus()) {
                dateMaxField.setText(event.event.datetimeMax == null ? "" : event.event.datetimeMax.format(dateFormatter));
                dateMaxField.setError(null);
            }

            if (!durationField.hasFocus()) {
                durationField.setText(event.event.duration == null ? "" : String.valueOf(event.event.duration.toMinutes()));
                durationField.setError(null);
            }

            if (!isEnabledCheckBox.isFocused()) {
                isEnabledCheckBox.setChecked(!event.event.isDone);
            }

            if (!isDateUsedCheckBox.isFocused()) {
                isDateUsedCheckBox.setChecked(event.event.isDateUsed);
            }

            dateMinField.setEnabled(event.event.isDateUsed);
            dateMaxField.setEnabled(event.event.isDateUsed);
            dateMinBrowseButton.setEnabled(event.event.isDateUsed);
            dateMaxBrowseButton.setEnabled(event.event.isDateUsed);

            appBar.setTitle(event.event.eventId == 0 ? R.string.title_add_event : R.string.title_edit_event);
        });

        return root;
    }

    private void updateText(EditText editText, String text) {
        boolean focussed = editText.hasFocus();
        if (focussed) {
            editText.clearFocus();
        }
        editText.setText(text);
        if (focussed) {
            editText.requestFocus();
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }
}
