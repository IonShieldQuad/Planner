package com.ionshield.planner.fragments.list;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.ionshield.planner.R;
import com.ionshield.planner.database.PlannerRepository;
import com.ionshield.planner.database.dao.TypeItemRefDao;
import com.ionshield.planner.database.entities.TypeItemRef;
import com.ionshield.planner.fragments.edit.TypeItemRefEditDialog;

public class ListTypeItemRefFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String ARG_MODE = "mode";
    public static final String ARG_SHOW_TOOLBAR = "show_toolbar";
    private int mColumnCount = 1;

    private SelectionListener<TypeItemRef> listener;
    private ListTypeItemRefViewModel mModel;
    private boolean mShowToolbar = false;

    private View root;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ListTypeItemRefFragment() {
    }


    @SuppressWarnings("unused")
    public static ListTypeItemRefFragment newInstance(int columnCount, Mode mode, boolean showToolbar) {
        ListTypeItemRefFragment fragment = new ListTypeItemRefFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putString(ARG_MODE, mode.name());
        args.putBoolean(ARG_SHOW_TOOLBAR, showToolbar);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = new ViewModelProvider(this).get(ListTypeItemRefViewModel.class);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT, 1);
            String modeStr = getArguments().getString(ARG_MODE, null);
            Mode mode = modeStr == null ? null : Mode.valueOf(modeStr);
            if (mode != null) {
                setMode(mode);
            }
            mShowToolbar = getArguments().getBoolean(ARG_SHOW_TOOLBAR, false);
        }

        if (listener == null) {
            setOnSelectionListener(new ListTypeItemRefFragment.DefaultListener(this));
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_type_item_ref_list, container, false);

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
            TypeItemRefRecyclerViewAdapter adapter = new TypeItemRefRecyclerViewAdapter();
            adapter.setOnSelectionListener(this::notifyListeners);
            recyclerView.setAdapter(adapter);

            mModel.getData().observe(getViewLifecycleOwner(), adapter::submitList);
            //adapter.submitList(mModel.getData().getValue());

            mModel.getMode().observe(getViewLifecycleOwner(), adapter::setMode);
        }

        EditText searchField = root.findViewById(R.id.search_field);
        searchField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                mModel.setSearch(editable.toString());
            }
        });

        searchField.setText(mModel.getSearch().getValue());

        Button addButton = root.findViewById(R.id.add_button);
        if (addButton != null) {
            addButton.setOnClickListener(l -> notifyListeners(null, SelectionMode.ADD));
        }
        mModel.getMode().observe(getViewLifecycleOwner(), this::setupButton);

        return root;
    }


    public void setOnSelectionListener(SelectionListener<TypeItemRef> l) {
        listener = l;
    }

    public void notifyListeners(TypeItemRef select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    private void setupButton(Mode mode) {
        if (root != null)  {
            Button addButton = root.findViewById(R.id.add_button);
            if (addButton != null) {
                switch (mode) {
                    case NONE:
                    case SELECT:
                        addButton.setVisibility(View.GONE);
                        break;
                    case SELECT_EDIT:
                    case EDIT:
                        addButton.setVisibility(View.VISIBLE);
                        break;
                }
            }
        }
    }

    public void setMode(Mode mode) {
        if (mModel != null) {
            mModel.setMode(mode);
            setupButton(mode);
        }
    }

    public static class DefaultListener implements SelectionListener<TypeItemRef> {
        private final Fragment fragment;
        public DefaultListener(@NonNull Fragment fragment) {
            this.fragment = fragment;
        }

        @Override
        public void onSelect(TypeItemRef selected, SelectionMode mode) {
            TypeItemRefDao dao = PlannerRepository.getInstance().getTypeItemRefDao();
            switch (mode) {
                case DELETE: {
                    dao.deleteAll(selected);
                }
                break;
                case ADD: {
                    if (fragment.getActivity() != null) {
                        FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                        TypeItemRefEditDialog dialog = TypeItemRefEditDialog.newInstance(0);
                        fragmentManager.beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .add(android.R.id.content, dialog)
                                .addToBackStack(null)
                                .commit();
                    }
                }
                break;
                case EDIT: {
                    if (fragment.getActivity() != null) {
                        FragmentManager fragmentManager = fragment.getActivity().getSupportFragmentManager();
                        TypeItemRefEditDialog dialog = TypeItemRefEditDialog.newInstance(selected.typeItemRefId);
                        fragmentManager.beginTransaction()
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .add(android.R.id.content, dialog)
                                .addToBackStack(null)
                                .commit();
                    }
                }
                break;
            }
        }
    }
}
