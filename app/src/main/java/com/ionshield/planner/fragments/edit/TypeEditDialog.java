package com.ionshield.planner.fragments.edit;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ionshield.planner.InputFilterMinMax;
import com.ionshield.planner.R;

public class TypeEditDialog extends DialogFragment {
    private TypeEditDialogViewModel mViewModel;

    public static TypeEditDialog newInstance(long id) {
        TypeEditDialog dialog = new TypeEditDialog();
        Bundle args = new Bundle();
        args.putLong("id", id);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.type_edit_dialog_fragment, container, false);

        long id = 0;
        if (getArguments() != null) {
            id = getArguments().getLong("id", 0);
        }

        mViewModel = new ViewModelProvider(this).get(TypeEditDialogViewModel.class);

        mViewModel.setTypeById(id);

        MaterialToolbar appBar = root.findViewById(R.id.app_bar);


        appBar.setNavigationOnClickListener(l -> {
            getParentFragmentManager().popBackStack();
        });

        appBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                mViewModel.writeType();
                getParentFragmentManager().popBackStack();
                return true;
            }
            return false;
        });

        TextInputEditText nameField = root.findViewById(R.id.name_field);
        TextInputEditText descField = root.findViewById(R.id.desc_field);

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
                    //updateText(descField, editable.toString());
                }
            }
        });

        TextInputEditText redField = root.findViewById(R.id.red_field);
        TextInputEditText greenField = root.findViewById(R.id.green_field);
        TextInputEditText blueField = root.findViewById(R.id.blue_field);

        redField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (redField.hasFocus()) {
                    int val = 0;
                    try {
                        val = Integer.parseInt(editable.toString());
                    }
                    catch (NumberFormatException ignored) {}

                    mViewModel.setRed(val);
                    //updateText(descField, editable.toString());
                }
            }
        });
        greenField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (greenField.hasFocus()) {
                    int val = 0;
                    try {
                        val = Integer.parseInt(editable.toString());
                    }
                    catch (NumberFormatException ignored) {}
                    mViewModel.setGreen(val);
                    //updateText(descField, editable.toString());
                }
            }
        });
        blueField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (blueField.hasFocus()) {
                    int val = 0;
                    try {
                        val = Integer.parseInt(editable.toString());
                    }
                    catch (NumberFormatException ignored) {}
                    mViewModel.setBlue(val);
                    //updateText(descField, editable.toString());
                }
            }
        });

        redField.setFilters(new InputFilter[]{new InputFilterMinMax(0, 255)});
        greenField.setFilters(new InputFilter[]{new InputFilterMinMax(0, 255)});
        blueField.setFilters(new InputFilter[]{new InputFilterMinMax(0, 255)});

        TextView colorView = root.findViewById(R.id.color);

        mViewModel.getType().observe(getViewLifecycleOwner(), type -> {
            if (!nameField.hasFocus()) {
                nameField.setText(type.name);
            }
            if (!descField.hasFocus()) {
                descField.setText(type.description);
            }
            if (!redField.hasFocus()) {
                redField.setText(String.valueOf(Color.red(type.color)));
            }
            if (!greenField.hasFocus()) {
                greenField.setText(String.valueOf(Color.green(type.color)));
            }
            if (!blueField.hasFocus()) {
                blueField.setText(String.valueOf(Color.blue(type.color)));
            }



            colorView.setBackgroundColor(type.color);
            appBar.setTitle(type.typeId == 0 ? R.string.title_add_type : R.string.title_edit_type);
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
