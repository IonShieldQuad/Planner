package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Item;
import com.ionshield.planner.database.entities.Type;
import com.ionshield.planner.database.entities.TypeItemRef;

import java.util.List;

public class TypeWithItems {
    @Embedded
    public Type type;

    @Relation(
            parentColumn = "type_id",
            entityColumn = "item_id",
            associateBy = @Junction(TypeItemRef.class)
    )
    public List<Item> items;
}
