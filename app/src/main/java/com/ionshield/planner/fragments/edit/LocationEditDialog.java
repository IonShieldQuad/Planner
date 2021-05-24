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
import com.ionshield.planner.fragments.list.ListTypeFragment;
import com.ionshield.planner.fragments.list.Mode;
import com.ionshield.planner.fragments.list.SelectionMode;

public class LocationEditDialog extends DialogFragment {

    private LocationEditDialogViewModel mViewModel;

    public static LocationEditDialog newInstance(long id, Long nodeId) {
        LocationEditDialog dialog = new LocationEditDialog();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putLong("node_id", nodeId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.location_edit_dialog_fragment, container, false);

        long id = 0;
        long nodeId = 0;
        if (getArguments() != null) {
            id = getArguments().getLong("id", 0);
            nodeId = getArguments().getLong("node_id", 0);
        }

        mViewModel = new ViewModelProvider(this).get(LocationEditDialogViewModel.class);

        mViewModel.setLocationById(id, nodeId);

        MaterialToolbar appBar = root.findViewById(R.id.app_bar);


        appBar.setNavigationOnClickListener(l -> {
            getParentFragmentManager().popBackStack();
        });

        appBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                mViewModel.writeLocation();
                getParentFragmentManager().popBackStack();
                return true;
            }
            return false;
        });

        TextInputEditText nameField = root.findViewById(R.id.name_field);
        TextInputEditText contentField = root.findViewById(R.id.content_field);
        TextInputEditText typeNameField = root.findViewById(R.id.type_name_field);

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

        contentField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                if (contentField.hasFocus()) {
                    mViewModel.setDescription(editable.toString());
                    //updateText(contentField, editable.toString());
                }
            }
        });


        Button typeSelectButton = root.findViewById(R.id.type_select_button);
        typeSelectButton.setOnClickListener(l -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            ListTypeFragment fragment = ListTypeFragment.newInstance(1, Mode.SELECT, true);
            fragment.setOnSelectionListener((selected, mode) -> {
                if (mode == SelectionMode.SELECT) {
                    mViewModel.setType(selected);
                    fragmentManager.popBackStack();
                }
            });
            fragmentManager.beginTransaction()
                    .add(android.R.id.content, fragment)
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .addToBackStack(null)
                    .commit();
        });


        mViewModel.getLocation().observe(getViewLifecycleOwner(), location -> {
            if (!nameField.hasFocus()) {
                nameField.setText(location.location.name);
            }
            if (!contentField.hasFocus()) {
                contentField.setText(location.location.description);
            }
            appBar.setTitle(location.location.locationId == 0 ? R.string.title_add_location : R.string.title_edit_location);
        });

        mViewModel.getType().observe(getViewLifecycleOwner(), type -> {
            typeNameField.setText(type == null ? "" : type.name);
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
