package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index("plan_id"), @Index("name")},
    foreignKeys = {@ForeignKey(entity = Plan.class, parentColumns = "plan_id", childColumns = "plan_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Constraint {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "constraint_id")
    public long constraintId;

    @ColumnInfo(name = "plan_id")
    public long planId;

    public String name;

    public String content;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Constraint that = (Constraint) o;
        return constraintId == that.constraintId &&
                planId == that.planId &&
                Objects.equals(name, that.name) &&
                Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constraintId, planId, name, content);
    }
}
