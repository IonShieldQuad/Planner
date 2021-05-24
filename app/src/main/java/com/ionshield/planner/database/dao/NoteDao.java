package com.ionshield.planner.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.entities.Note;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Note... notes);

    @Update
    ListenableFuture<Integer> updateAll(Note... notes);

    @Delete
    ListenableFuture<Integer> deleteAll(Note... notes);

    @Query("SELECT * FROM 'note'")
    ListenableFuture<List<Note>> queryNotes();

    @Query("SELECT * FROM 'note' WHERE note_id IN (:id)")
    ListenableFuture<List<Note>> queryNotes(long... id);

    @Query("SELECT * FROM 'note' WHERE name LIKE :name")
    ListenableFuture<List<Note>> queryNotes(String name);


    @Query("SELECT * FROM 'note' WHERE plan_id IN (:id)")
    ListenableFuture<List<Note>> queryNotesByPlanId(long... id);

    @Query("SELECT * FROM 'note' WHERE name LIKE :name AND plan_id IN (:id)")
    ListenableFuture<List<Note>> queryNotesByPlanId(String name, long... id);


    @Query("SELECT * FROM 'note'")
    LiveData<List<Note>> liveNotes();

    @Query("SELECT * FROM 'note' WHERE note_id IN (:id)")
    LiveData<List<Note>> liveNotes(long... id);

    @Query("SELECT * FROM 'note' WHERE name LIKE :name")
    LiveData<List<Note>> liveNotes(String name);


    @Query("SELECT * FROM 'note' WHERE plan_id IN (:id)")
    LiveData<List<Note>> liveNotesByPlanId(long... id);

    @Query("SELECT * FROM 'note' WHERE name LIKE :name AND plan_id IN (:id)")
    LiveData<List<Note>> liveNotesByPlanId(String name, long... id);
}
