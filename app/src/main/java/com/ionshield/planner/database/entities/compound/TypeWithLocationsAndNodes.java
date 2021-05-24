package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.LocationTypeRef;
import com.ionshield.planner.database.entities.Type;

import java.util.List;

public class TypeWithLocationsAndNodes {
    @Embedded
    public Type type;

    @Relation(
            entity = Location.class,
            parentColumn = "type_id",
            entityColumn = "location_id",
            associateBy = @Junction(LocationTypeRef.class)
    )
    public List<LocationAndNode> locationAndNode;
}
