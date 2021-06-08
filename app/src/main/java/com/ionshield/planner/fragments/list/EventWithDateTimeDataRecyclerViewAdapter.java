package com.ionshield.planner.fragments.list;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.ionshield.planner.databinding.FragmentScheduleItemBinding;
import com.ionshield.planner.math.EventWithDateTimeData;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Objects;

public class EventWithDateTimeDataRecyclerViewAdapter extends ListAdapter<EventWithDateTimeData, EventWithDateTimeDataRecyclerViewAdapter.ViewHolder> {

    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT);
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT);

    public static final DiffUtil.ItemCallback<EventWithDateTimeData> DIFF_CALLBACK = new DiffUtil.ItemCallback<EventWithDateTimeData>() {
        @Override
        public boolean areItemsTheSame(@NonNull EventWithDateTimeData oldItem, @NonNull EventWithDateTimeData newItem) {
            return oldItem.event.eventId == newItem.event.eventId;
        }

        @Override
        public boolean areContentsTheSame(@NonNull EventWithDateTimeData oldItem, @NonNull EventWithDateTimeData newItem) {
            return Objects.equals(oldItem, newItem);
        }
    };

    public EventWithDateTimeDataRecyclerViewAdapter() {
        super(DIFF_CALLBACK);
    }



    @NonNull
    @Override
    public EventWithDateTimeDataRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewItem) {
        return new EventWithDateTimeDataRecyclerViewAdapter.ViewHolder(FragmentScheduleItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }


    @Override
    public void onBindViewHolder(final EventWithDateTimeDataRecyclerViewAdapter.ViewHolder holder, int position) {
        holder.bindTo(getItem(position), position + 1);
    }


    private DateTimeFormatter getDateFormatter(boolean fullDate) {
        return fullDate ? dateTimeFormatter : timeFormatter;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mRoot;
        public final TextView mNumberView;
        public final TextView mNameView;
        public final TextView mLocationNameView;
        public final TextView mDatetimeMin;
        public final TextView mDatetimeMax;


        public EventWithDateTimeData mEventWithDateTimeData;
        public int mPosition;

        public ViewHolder(FragmentScheduleItemBinding binding) {
            super(binding.getRoot());
            mRoot = binding.getRoot();
            mNumberView = binding.itemNumber;
            mNameView = binding.name;
            mLocationNameView = binding.locationName;
            mDatetimeMin = binding.datetimeMin;
            mDatetimeMax = binding.datetimeMax;

        }

        public void bindTo(EventWithDateTimeData eventWithDateTimeData, int position) {
            mEventWithDateTimeData = eventWithDateTimeData;
            mPosition = position;

            mNumberView.setText(String.valueOf(position));
            mNameView.setText(eventWithDateTimeData.event.name);
            mLocationNameView.setText(eventWithDateTimeData.location == null || eventWithDateTimeData.location.location.name == null ? "" : eventWithDateTimeData.location.location.name);

            mDatetimeMin.setText(eventWithDateTimeData.start == null ? "" : eventWithDateTimeData.start.format(getDateFormatter(eventWithDateTimeData.event.isDateUsed)));
            mDatetimeMax.setText(eventWithDateTimeData.finish == null ? "" : eventWithDateTimeData.finish.format(getDateFormatter(eventWithDateTimeData.event.isDateUsed)));



        }



        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mNameView.getText() + "'";
        }
    }
}
