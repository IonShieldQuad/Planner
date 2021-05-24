package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.Node;

import java.util.List;

public class NodeWithLocations {
    @Embedded
    public Node node;

    @Relation(
            parentColumn = "node_id",
            entityColumn = "node_id"
    )
    public List<Location> locations;
}
