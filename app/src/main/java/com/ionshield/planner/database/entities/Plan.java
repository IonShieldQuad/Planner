package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index("name")})
public class Plan {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "plan_id")
    public long planId;
    public String name;
    public String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plan plan = (Plan) o;
        return planId == plan.planId &&
                Objects.equals(name, plan.name) &&
                Objects.equals(description, plan.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planId, name, description);
    }
}
