package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.Node;

public class LocationAndNode {
    @Embedded public Location location;

    @Relation(
            parentColumn = "node_id",
            entityColumn = "node_id"
    )
    public Node node;
}
