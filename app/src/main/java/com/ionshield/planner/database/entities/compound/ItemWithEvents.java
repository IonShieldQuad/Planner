package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.Item;

import java.util.List;

public class ItemWithEvents {
    @Embedded
    public Item item;

    @Relation(
            parentColumn = "item_id",
            entityColumn = "item_id"
    )
    public List<Event> events;
}
