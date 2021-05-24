package com.ionshield.planner.fragments.edit;


import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ionshield.planner.R;

public class ItemEditDialog extends DialogFragment {

    private ItemEditDialogViewModel mViewModel;

    public static ItemEditDialog newInstance(long id) {
        ItemEditDialog dialog = new ItemEditDialog();
        Bundle args = new Bundle();
        args.putLong("id", id);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.item_edit_dialog_fragment, container, false);

        long id = 0;
        if (getArguments() != null) {
            id = getArguments().getLong("id", 0);
        }

        mViewModel = new ViewModelProvider(this).get(ItemEditDialogViewModel.class);

        mViewModel.setItemById(id);

        MaterialToolbar appBar = root.findViewById(R.id.app_bar);


        appBar.setNavigationOnClickListener(l -> {
            getParentFragmentManager().popBackStack();
        });

        appBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                mViewModel.writeItem();
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

        mViewModel.getItem().observe(getViewLifecycleOwner(), item -> {
            if (!nameField.hasFocus()) {
                nameField.setText(item.name);
            }
            if (!descField.hasFocus()) {
                descField.setText(item.description);
            }
            appBar.setTitle(item.itemId == 0 ? R.string.title_add_item : R.string.title_edit_item);
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