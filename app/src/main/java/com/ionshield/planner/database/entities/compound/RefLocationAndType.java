package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.LocationTypeRef;
import com.ionshield.planner.database.entities.Type;

import java.util.Objects;

public class RefLocationAndType {
    @Embedded
    public LocationTypeRef ref;

    @Relation(
            parentColumn = "location_id",
            entityColumn = "location_id"
    )
    public Location location;

    @Relation(
            parentColumn = "type_id",
            entityColumn = "type_id"
    )
    public Type type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefLocationAndType that = (RefLocationAndType) o;
        return Objects.equals(ref, that.ref) &&
                Objects.equals(location, that.location) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref, location, type);
    }
}
