package com.ionshield.planner.database.entities.compound;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.ionshield.planner.database.entities.Note;
import com.ionshield.planner.database.entities.Plan;

import java.util.List;

public class PlanWithNotes {
    @Embedded public Plan plan;
    @Relation(
            parentColumn = "plan_id",
            entityColumn = "plan_id"
    )
    public List<Note> notes;
}
