package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.Item;

public class EventAndItemWithTypesWithLocations {
    @Embedded
    public Event event;

    @Relation(
            entity = Item.class,
            parentColumn = "item_id",
            entityColumn = "item_id"
    )
    public ItemWithTypesWithLocations itemWithTypesWithLocations;
}
