package com.ionshield.planner.fragments.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.compound.LocationWithTypes;
import com.ionshield.planner.databinding.FragmentLocationBinding;

import java.util.Objects;

public class LocationRecyclerViewAdapter extends ListAdapter<LocationWithTypes, LocationRecyclerViewAdapter.ViewHolder> {
    private final LocationRecyclerViewAdapter that = this;
    private Mode mMode;
    private SelectionListener<Location> listener;

    public static final DiffUtil.ItemCallback<LocationWithTypes> DIFF_CALLBACK = new DiffUtil.ItemCallback<LocationWithTypes>() {
        @Override
        public boolean areItemsTheSame(@NonNull LocationWithTypes oldItem, @NonNull LocationWithTypes newItem) {
            return oldItem.location.locationId == newItem.location.locationId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull LocationWithTypes oldItem, @NonNull LocationWithTypes newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public LocationRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }

    public void setOnSelectionListener(SelectionListener<Location> l) {
        listener = l;
    }

    public void notifyListeners(Location select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    @NonNull
    @Override
    public LocationRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewLocation) {
        return new LocationRecyclerViewAdapter.ViewHolder(FragmentLocationBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final LocationRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindTo(getItem(position), position + 1, mMode);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Mode mMode;

        public final View mRoot;
        public final TextView mNumberView;
        public final TextView mNameView;
        public final TextView mTypeNameView;

        public final Button mEditButton;
        public final Button mDeleteButton;
        public final Button mSelectButton;

        public LocationWithTypes mLocationWithTypes;
        public int mPosition;

        public ViewHolder(FragmentLocationBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mNameView = binding.name;
            mTypeNameView = binding.typeName;

            mEditButton = binding.editButton;
            mDeleteButton = binding.deleteButton;
            mSelectButton = binding.selectButton;

            setMode(that.mMode);
        }

        public void bindTo(LocationWithTypes location, int position, Mode mode) {
            mLocationWithTypes = location;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mNameView.setText(location.location.name);
            mTypeNameView.setText(location.types == null || location.types.isEmpty() ? "" : location.types.get(0).name);


            mEditButton.setText("");
            mDeleteButton.setText("");

            if (mode != mMode) setMode(mode);

        }

        public void setMode(Mode mode) {
            mMode = mode;
            switch (mMode) {
                case NONE:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case SELECT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mLocationWithTypes.location, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case EDIT:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mLocationWithTypes.location, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mLocationWithTypes.location, SelectionMode.DELETE));
                    break;
                case SELECT_EDIT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mLocationWithTypes.location, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mLocationWithTypes.location, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mLocationWithTypes.location, SelectionMode.DELETE));
                    break;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
