package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "type_item_ref", indices = {@Index("type_id"), @Index("item_id")},
        foreignKeys = {@ForeignKey(entity = Type.class, parentColumns = "type_id", childColumns = "type_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE),
                @ForeignKey(entity = Item.class, parentColumns = "item_id", childColumns = "item_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class TypeItemRef {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "type_item_ref_id")
    public long typeItemRefId;

    @ColumnInfo(name = "type_id")
    public long typeId;

    @ColumnInfo(name = "item_id")
    public long itemId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TypeItemRef that = (TypeItemRef) o;
        return typeItemRefId == that.typeItemRefId &&
                typeId == that.typeId &&
                itemId == that.itemId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeItemRefId, typeId, itemId);
    }
}
