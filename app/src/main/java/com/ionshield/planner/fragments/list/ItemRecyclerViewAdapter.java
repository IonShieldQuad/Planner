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

import com.ionshield.planner.database.entities.Item;
import com.ionshield.planner.databinding.FragmentItemBinding;

import java.util.Objects;


public class ItemRecyclerViewAdapter extends ListAdapter<Item, ItemRecyclerViewAdapter.ViewHolder> {

    private final ItemRecyclerViewAdapter that = this;
    private Mode mMode;
    private SelectionListener<Item> listener;

    public static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.itemId == newItem.itemId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public ItemRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }

    public void setOnSelectionListener(SelectionListener<Item> l) {
        listener = l;
    }

    public void notifyListeners(Item select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(FragmentItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.bindTo(getItem(position), position + 1, mMode);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Mode mMode;

        public final View mRoot;
        public final TextView mNumberView;
        public final TextView mNameView;

        public final Button mEditButton;
        public final Button mDeleteButton;
        public final Button mSelectButton;

        public Item mItem;
        public int mPosition;

        public ViewHolder(FragmentItemBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mNameView = binding.name;

            mEditButton = binding.editButton;
            mDeleteButton = binding.deleteButton;
            mSelectButton = binding.selectButton;

            setMode(that.mMode);
        }

        public void bindTo(Item item, int position, Mode mode) {
            mItem = item;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mNameView.setText(item.name);

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
                    mSelectButton.setOnClickListener(l -> notifyListeners(mItem, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case EDIT:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mItem, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mItem, SelectionMode.DELETE));
                    break;
                case SELECT_EDIT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mItem, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mItem, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mItem, SelectionMode.DELETE));
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