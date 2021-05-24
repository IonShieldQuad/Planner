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

import com.ionshield.planner.database.entities.Plan;
import com.ionshield.planner.databinding.FragmentPlanBinding;

import java.util.Objects;

public class PlanRecyclerViewAdapter extends ListAdapter<Plan, PlanRecyclerViewAdapter.ViewHolder> {

    private final PlanRecyclerViewAdapter that = this;
    private Mode mMode;
    private SelectionListener<Plan> listener;

    public static final DiffUtil.ItemCallback<Plan> DIFF_CALLBACK = new DiffUtil.ItemCallback<Plan>() {
        @Override
        public boolean areItemsTheSame(@NonNull Plan oldItem, @NonNull Plan newItem) {
            return oldItem.planId == newItem.planId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Plan oldItem, @NonNull Plan newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public PlanRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }

    public void setOnSelectionListener(SelectionListener<Plan> l) {
        listener = l;
    }

    public void notifyListeners(Plan select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    @NonNull
    @Override
    public PlanRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlanRecyclerViewAdapter.ViewHolder(FragmentPlanBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final PlanRecyclerViewAdapter.ViewHolder holder, int position) {
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

        public Plan mPlan;
        public int mPosition;

        public ViewHolder(FragmentPlanBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mNameView = binding.name;

            mEditButton = binding.editButton;
            mDeleteButton = binding.deleteButton;
            mSelectButton = binding.selectButton;

            setMode(that.mMode);
        }

        public void bindTo(Plan plan, int position, Mode mode) {
            mPlan = plan;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mNameView.setText(plan.name);

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
                    mSelectButton.setOnClickListener(l -> notifyListeners(mPlan, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case EDIT:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mPlan, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mPlan, SelectionMode.DELETE));
                    break;
                case SELECT_EDIT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mPlan, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mPlan, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mPlan, SelectionMode.DELETE));
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
