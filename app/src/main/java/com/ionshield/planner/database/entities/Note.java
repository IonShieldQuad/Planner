package com.ionshield.planner.database.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity(indices = {@Index("name"), @Index("plan_id")},
        foreignKeys = {@ForeignKey(entity = Plan.class, parentColumns = "plan_id", childColumns = "plan_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)})
public class Note {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "note_id")
    public long noteId;

    public String name;
    public String content;

    @ColumnInfo(name = "plan_id")
    public long planId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return noteId == note.noteId &&
                planId == note.planId &&
                Objects.equals(name, note.name) &&
                Objects.equals(content, note.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, name, content, planId);
    }
}
