package com.ionshield.planner.database.entities;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "location_type_ref", indices = {@Index("location_id"), @Index("type_id")},
        foreignKeys = {@ForeignKey(entity = Location.class, parentColumns = "location_id", childColumns = "location_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Type.class, parentColumns = "type_id", childColumns = "type_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class LocationTypeRef {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "location_type_ref_id")
    public long LocationTypeRefId;

    @ColumnInfo(name = "location_id")
    public long locationId;

    @ColumnInfo(name = "type_id")
    public long typeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationTypeRef that = (LocationTypeRef) o;
        return LocationTypeRefId == that.LocationTypeRefId &&
                locationId == that.locationId &&
                typeId == that.typeId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(LocationTypeRefId, locationId, typeId);
    }
}
