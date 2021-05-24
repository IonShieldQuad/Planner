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

import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.compound.EventAndItem;
import com.ionshield.planner.databinding.FragmentEventBinding;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

public class EventRecyclerViewAdapter extends ListAdapter<EventAndItem, EventRecyclerViewAdapter.ViewHolder> {
    private final EventRecyclerViewAdapter that = this;
    private Mode mMode;
    private SelectionListener<Event> listener;

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    public static final DiffUtil.ItemCallback<EventAndItem> DIFF_CALLBACK = new DiffUtil.ItemCallback<EventAndItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull EventAndItem oldItem, @NonNull EventAndItem newItem) {
            return oldItem.event.eventId == newItem.event.eventId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull EventAndItem oldItem, @NonNull EventAndItem newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public EventRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }


    public void setMode(Mode mode) {
        mMode = mode;
    }

    public void setOnSelectionListener(SelectionListener<Event> l) {
        listener = l;
    }

    public void notifyListeners(Event select, SelectionMode mode) {
        if (listener != null) {
            listener.onSelect(select, mode);
        }
    }

    @NonNull
    @Override
    public EventRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewItem) {
        return new EventRecyclerViewAdapter.ViewHolder(FragmentEventBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final EventRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindTo(getItem(position), position + 1, mMode);
    }


    private DateTimeFormatter getDateFormatter(boolean fullDate) {
        return fullDate ? dateTimeFormatter : timeFormatter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public Mode mMode;

        public final View mRoot;
        public final TextView mNumberView;
        public final TextView mNameView;
        public final TextView mItemNameView;
        public final TextView mDatetimeMin;
        public final TextView mDatetimeMax;

        public final Button mEditButton;
        public final Button mDeleteButton;
        public final Button mSelectButton;

        public EventAndItem mEventAndItem;
        public int mPosition;

        public ViewHolder(FragmentEventBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mNameView = binding.name;
            mItemNameView = binding.itemName;
            mDatetimeMin = binding.datetimeMin;
            mDatetimeMax = binding.datetimeMax;

            mEditButton = binding.editButton;
            mDeleteButton = binding.deleteButton;
            mSelectButton = binding.selectButton;

            setMode(that.mMode);
        }

        public void bindTo(EventAndItem eventAndItem, int position, Mode mode) {
            mEventAndItem = eventAndItem;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mNameView.setText(eventAndItem.event.name);
            mItemNameView.setText(eventAndItem.item == null ? "" : eventAndItem.item.name);

            mDatetimeMin.setText(eventAndItem.event.datetimeMin == null ? "" : eventAndItem.event.datetimeMin.format(getDateFormatter(eventAndItem.event.isDateUsed)));
            mDatetimeMax.setText(eventAndItem.event.datetimeMax == null ? "" : eventAndItem.event.datetimeMax.format(getDateFormatter(eventAndItem.event.isDateUsed)));

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
                    mSelectButton.setOnClickListener(l -> notifyListeners(mEventAndItem.event, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.GONE);
                    mEditButton.setOnClickListener(null);
                    mDeleteButton.setVisibility(View.GONE);
                    mDeleteButton.setOnClickListener(null);
                    break;
                case EDIT:
                    mSelectButton.setVisibility(View.GONE);
                    mSelectButton.setOnClickListener(null);
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mEventAndItem.event, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mEventAndItem.event, SelectionMode.DELETE));
                    break;
                case SELECT_EDIT:
                    mSelectButton.setVisibility(View.VISIBLE);
                    mSelectButton.setOnClickListener(l -> notifyListeners(mEventAndItem.event, SelectionMode.SELECT));
                    mEditButton.setVisibility(View.VISIBLE);
                    mEditButton.setOnClickListener(l -> notifyListeners(mEventAndItem.event, SelectionMode.EDIT));
                    mDeleteButton.setVisibility(View.VISIBLE);
                    mDeleteButton.setOnClickListener(l -> notifyListeners(mEventAndItem.event, SelectionMode.DELETE));
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
