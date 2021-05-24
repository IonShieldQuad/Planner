package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.LocationTypeRef;
import com.ionshield.planner.database.entities.Node;
import com.ionshield.planner.database.entities.Type;

import java.util.List;

public class LocationWithTypesAndNode {
    @Embedded
    public Location location;

    @Relation(
            parentColumn = "node_id",
            entityColumn = "node_id"
    )
    public Node node;

    @Relation(
            parentColumn = "location_id",
            entityColumn = "type_id",
            associateBy = @Junction(LocationTypeRef.class)
    )
    public List<Type> types;
}
