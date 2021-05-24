package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.Item;

import java.util.Objects;

public class EventAndItem {
    @Embedded public Event event;

    @Relation(
            parentColumn = "item_id",
            entityColumn = "item_id"
    )
    public Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventAndItem that = (EventAndItem) o;
        return Objects.equals(event, that.event) &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(event, item);
    }
}
