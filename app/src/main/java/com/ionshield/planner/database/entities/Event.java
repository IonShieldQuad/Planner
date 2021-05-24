package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity(indices = {@Index("name"), @Index("datetime_min"), @Index("datetime_max"), @Index("item_id"), @Index("plan_id")},
        foreignKeys = {@ForeignKey(entity = Plan.class, parentColumns = "plan_id", childColumns = "plan_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Item.class, parentColumns = "item_id", childColumns = "item_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    public long eventId;

    @ColumnInfo(name = "plan_id")
    public long planId;

    @ColumnInfo(name = "item_id")
    public Long itemId;

    public String name;
    public String description;

    @ColumnInfo(name = "is_done")
    public boolean isDone;

    @ColumnInfo(name = "datetime_min")
    public OffsetDateTime datetimeMin;

    @ColumnInfo(name = "datetime_max")
    public OffsetDateTime datetimeMax;

    @ColumnInfo(name = "is_date_used")
    public boolean isDateUsed;

    public Duration duration;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return eventId == event.eventId &&
                planId == event.planId &&
                isDone == event.isDone &&
                isDateUsed == event.isDateUsed &&
                Objects.equals(itemId, event.itemId) &&
                Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(datetimeMin, event.datetimeMin) &&
                Objects.equals(datetimeMax, event.datetimeMax) &&
                Objects.equals(duration, event.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventId, planId, itemId, name, description, isDone, datetimeMin, datetimeMax, isDateUsed, duration);
    }
}
