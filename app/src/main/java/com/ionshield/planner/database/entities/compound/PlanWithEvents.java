package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.Plan;

import java.util.List;

public class PlanWithEvents {
    @Embedded
    public Plan plan;
    @Relation(
            parentColumn = "plan_id",
            entityColumn = "plan_id"
    )
    public List<Event> events;
}
