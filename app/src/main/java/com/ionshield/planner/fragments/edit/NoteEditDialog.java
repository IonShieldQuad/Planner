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

public class NoteEditDialog extends DialogFragment {

    private NoteEditDialogViewModel mViewModel;

    public static NoteEditDialog newInstance(long id, Long planId) {
        NoteEditDialog dialog = new NoteEditDialog();
        Bundle args = new Bundle();
        args.putLong("id", id);
        args.putLong("plan_id", planId);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.note_edit_dialog_fragment, container, false);

        long id = 0;
        Long planId = null;
        if (getArguments() != null) {
            id = getArguments().getLong("id", 0);
            planId = getArguments().getLong("plan_id", 0);
        }

        mViewModel = new ViewModelProvider(this).get(NoteEditDialogViewModel.class);

        mViewModel.setNoteById(id, planId);

        MaterialToolbar appBar = root.findViewById(R.id.app_bar);


        appBar.setNavigationOnClickListener(l -> {
            getParentFragmentManager().popBackStack();
        });

        appBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                mViewModel.writeNote();
                getParentFragmentManager().popBackStack();
                return true;
            }
            return false;
        });

        TextInputEditText nameField = root.findViewById(R.id.name_field);
        TextInputEditText contentField = root.findViewById(R.id.content_field);

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
                    mViewModel.setContent(editable.toString());
                    //updateText(contentField, editable.toString());
                }
            }
        });

        mViewModel.getNote().observe(getViewLifecycleOwner(), note -> {
            if (!nameField.hasFocus()) {
                nameField.setText(note.name);
            }
            if (!contentField.hasFocus()) {
                contentField.setText(note.content);
            }
            appBar.setTitle(note.noteId == 0 ? R.string.title_add_note : R.string.title_edit_note);
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
