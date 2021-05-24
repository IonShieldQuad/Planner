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

import com.ionshield.planner.database.entities.Type;
import com.ionshield.planner.databinding.FragmentTypeBinding;

import java.util.Objects;

public class TypeRecyclerViewAdapter extends ListAdapter<Type, TypeRecyclerViewAdapter.ViewHolder> {
    private final TypeRecyclerViewAdapter that = this;
    private Mode mMode;
    private SelectionListener<Type> listener;

    public static final DiffUtil.ItemCallback<Type> DIFF_CALLBACK = new DiffUtil.ItemCallback<Type>() {
        @Override
        public boolean areItemsTheSame(@NonNull Type oldItem, @NonNull Type newItem) {
            return oldItem.typeId == newItem.typeId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Type oldItem, @NonNull Type newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public TypeRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }

    public void setOnSelectionListener(SelectionListener<Type> l) {
        listener = l;
    }

    public void notifyListeners(Type select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    @NonNull
    @Override
    public TypeRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeRecyclerViewAdapter.ViewHolder(FragmentTypeBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final TypeRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindTo(getItem(position), position + 1, mMode);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Mode mMode;

        public final View mRoot;
        public final TextView mNumberView;
        public final TextView mNameView;
        public final TextView mColorView;

        public final Button mEditButton;
        public final Button mDeleteButton;
        public final Button mSelectButton;

        public Type mType;
        public int mPosition;

        public ViewHolder(FragmentTypeBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mNameView = binding.name;
            mColorView = binding.color;

            mEditButton = binding.editButton;
            mDeleteButton = binding.deleteButton;
            mSelectButton = binding.selectButton;

            setMode(that.mMode);
        }

        public void bindTo(Type type, int position, Mode mode) {
            mType = type;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mNameView.setText(type.name);
            mColorView.setBackgroundColor(type.color);


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
                    mSelectButton.setOnClickListener(l -> notifyListeners(mType, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case EDIT:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mType, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mType, SelectionMode.DELETE));
                    break;
                case SELECT_EDIT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mType, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mType, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mType, SelectionMode.DELETE));
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
