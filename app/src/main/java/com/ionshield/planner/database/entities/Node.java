package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index("name"), @Index(value = {"longitude", "latitude"}), @Index(value = {"latitude", "longitude"})})
public class Node {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "node_id")
    public long nodeId;

    public String name;

    public double longitude;
    public double latitude;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return nodeId == node.nodeId &&
                Double.compare(node.longitude, longitude) == 0 &&
                Double.compare(node.latitude, latitude) == 0 &&
                Objects.equals(name, node.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodeId, name, longitude, latitude);
    }
}
