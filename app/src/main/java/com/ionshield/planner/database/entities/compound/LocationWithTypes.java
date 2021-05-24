package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.LocationTypeRef;
import com.ionshield.planner.database.entities.Type;

import java.util.List;
import java.util.Objects;

public class LocationWithTypes {
    @Embedded public Location location;

    @Relation(
            parentColumn = "location_id",
            entityColumn = "type_id",
            associateBy = @Junction(LocationTypeRef.class)
    )
    public List<Type> types;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LocationWithTypes that = (LocationWithTypes) o;
        return Objects.equals(location, that.location) &&
                Objects.equals(types, that.types);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, types);
    }
}
