package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index("name"), @Index("node_id")},
        foreignKeys = {@ForeignKey(entity = Node.class, parentColumns = "node_id", childColumns = "node_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Location {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_id")
    public long locationId;

    public String name;
    public String description;

    @ColumnInfo(name = "node_id")
    public Long nodeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return locationId == location.locationId &&
                Objects.equals(name, location.name) &&
                Objects.equals(description, location.description) &&
                Objects.equals(nodeId, location.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationId, name, description, nodeId);
    }
}
