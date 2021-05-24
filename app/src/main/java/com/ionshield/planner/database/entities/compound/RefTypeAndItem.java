package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Item;
import com.ionshield.planner.database.entities.Type;
import com.ionshield.planner.database.entities.TypeItemRef;

import java.util.Objects;

public class RefTypeAndItem {
    @Embedded
    public TypeItemRef ref;

    @Relation(
            parentColumn = "type_id",
            entityColumn = "type_id"
    )
    public Type type;

    @Relation(
            parentColumn = "item_id",
            entityColumn = "item_id"
    )
    public Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RefTypeAndItem that = (RefTypeAndItem) o;
        return Objects.equals(ref, that.ref) &&
                Objects.equals(type, that.type) &&
                Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ref, type, item);
    }
}
