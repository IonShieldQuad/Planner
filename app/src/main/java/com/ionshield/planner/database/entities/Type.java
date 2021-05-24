package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index("name")})
public class Type {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "type_id")
    public long typeId;
    public String name;
    public String description;
    public int color;

    @ColumnInfo(name = "marker")
    public String marker;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Type type = (Type) o;
        return typeId == type.typeId &&
                color == type.color &&
                Objects.equals(name, type.name) &&
                Objects.equals(description, type.description) &&
                Objects.equals(marker, type.marker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeId, name, description, color, marker);
    }
}
