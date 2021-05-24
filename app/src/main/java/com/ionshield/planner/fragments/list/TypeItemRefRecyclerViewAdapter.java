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

import com.ionshield.planner.database.entities.TypeItemRef;
import com.ionshield.planner.database.entities.compound.RefTypeAndItem;
import com.ionshield.planner.databinding.FragmentTypeItemRefBinding;

import java.util.Objects;

public class TypeItemRefRecyclerViewAdapter extends ListAdapter<RefTypeAndItem, TypeItemRefRecyclerViewAdapter.ViewHolder> {
    private final TypeItemRefRecyclerViewAdapter that = this;
    private Mode mMode;
    private SelectionListener<TypeItemRef> listener;

    public static final DiffUtil.ItemCallback<RefTypeAndItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<RefTypeAndItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull RefTypeAndItem oldItem, @NonNull RefTypeAndItem newItem) {
            return oldItem.ref.typeItemRefId == newItem.ref.typeItemRefId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull RefTypeAndItem oldItem, @NonNull RefTypeAndItem newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public TypeItemRefRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }

    public void setOnSelectionListener(SelectionListener<TypeItemRef> l) {
        listener = l;
    }

    public void notifyListeners(TypeItemRef select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    @NonNull
    @Override
    public TypeItemRefRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TypeItemRefRecyclerViewAdapter.ViewHolder(FragmentTypeItemRefBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final TypeItemRefRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindTo(getItem(position), position + 1, mMode);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Mode mMode;

        public final View mRoot;
        public final TextView mNumberView;
        public final TextView mItemNameView;
        public final TextView mTypeNameView;

        public final Button mEditButton;
        public final Button mDeleteButton;
        public final Button mSelectButton;

        public RefTypeAndItem mRefTypeAndItem;
        public int mPosition;

        public ViewHolder(FragmentTypeItemRefBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mItemNameView = binding.itemName;
            mTypeNameView = binding.typeName;

            mEditButton = binding.editButton;
            mDeleteButton = binding.deleteButton;
            mSelectButton = binding.selectButton;

            setMode(that.mMode);
        }

        public void bindTo(RefTypeAndItem refTypeAndItem, int position, Mode mode) {
            mRefTypeAndItem = refTypeAndItem;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mItemNameView.setText(refTypeAndItem.item == null ? "" : refTypeAndItem.item.name);
            mTypeNameView.setText(refTypeAndItem.type == null ? "" : refTypeAndItem.type.name);


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
                    mSelectButton.setOnClickListener(l -> notifyListeners(mRefTypeAndItem.ref, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case EDIT:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mRefTypeAndItem.ref, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mRefTypeAndItem.ref, SelectionMode.DELETE));
                    break;
                case SELECT_EDIT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mRefTypeAndItem.ref, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mRefTypeAndItem.ref, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mRefTypeAndItem.ref, SelectionMode.DELETE));
                    break;
            }
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mRefTypeAndItem.ref.typeItemRefId + "'";
        }
    }
}
