package com.ionshield.planner.fragments.edit;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.ionshield.planner.R;
import com.ionshield.planner.fragments.list.ListItemFragment;
import com.ionshield.planner.fragments.list.ListTypeFragment;
import com.ionshield.planner.fragments.list.Mode;
import com.ionshield.planner.fragments.list.SelectionMode;

public class TypeItemRefEditDialog extends DialogFragment {
    private TypeItemRefEditDialogViewModel mViewModel;

    public static TypeItemRefEditDialog newInstance(long id) {
        TypeItemRefEditDialog dialog = new TypeItemRefEditDialog();
        Bundle args = new Bundle();
        args.putLong("id", id);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.type_item_ref_edit_dialog_fragment, container, false);

        long id = 0;
        if (getArguments() != null) {
            id = getArguments().getLong("id", 0);
        }

        mViewModel = new ViewModelProvider(this).get(TypeItemRefEditDialogViewModel.class);

        mViewModel.setTypeItemRefById(id);

        MaterialToolbar appBar = root.findViewById(R.id.app_bar);


        appBar.setNavigationOnClickListener(l -> {
            getParentFragmentManager().popBackStack();
        });

        appBar.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_confirm) {
                mViewModel.writeRef();
                getParentFragmentManager().popBackStack();
                return true;
            }
            return false;
        });

        TextInputEditText itemNameField = root.findViewById(R.id.item_name_field);
        TextInputEditText typeNameField = root.findViewById(R.id.type_name_field);

        Button itemSelectButton = root.findViewById(R.id.item_select_button);
        Button typeSelectButton = root.findViewById(R.id.type_select_button);

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


        mViewModel.getRef().observe(getViewLifecycleOwner(), ref -> {

            itemNameField.setText(ref.item == null ? "" : ref.item.name);
            typeNameField.setText(ref.type == null ? "" : ref.type.name);


            appBar.setTitle(ref.ref.typeItemRefId == 0 ? R.string.title_add_ref : R.string.title_edit_ref);
        });

        return root;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;

    }
}
