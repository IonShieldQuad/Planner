package com.ionshield.planner.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.entities.Plan;
import com.ionshield.planner.database.entities.compound.PlanWithAll;
import com.ionshield.planner.database.entities.compound.PlanWithConstraints;
import com.ionshield.planner.database.entities.compound.PlanWithEvents;
import com.ionshield.planner.database.entities.compound.PlanWithNotes;

import java.util.List;

@Dao
public interface PlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Plan... plans);

    @Update
    ListenableFuture<Integer> updateAll(Plan... plans);

    @Delete
    ListenableFuture<Integer> deleteAll(Plan... plans);


    @Query("SELECT * FROM 'plan'")
    ListenableFuture<List<Plan>> queryPlans();

    @Query("SELECT * FROM 'plan' WHERE plan_id = :id")
    ListenableFuture<List<Plan>> queryPlans(long... id);

    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    ListenableFuture<List<Plan>> queryPlans(String... name);

    @Transaction
    @Query("SELECT * FROM 'plan'")
    ListenableFuture<List<PlanWithNotes>> queryPlansWithNotes();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    ListenableFuture<List<PlanWithNotes>> queryPlansWithNotes(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    ListenableFuture<List<PlanWithNotes>> queryPlansWithNotes(String name);


    @Transaction
    @Query("SELECT * FROM 'plan'")
    ListenableFuture<List<PlanWithConstraints>> queryPlansWithConstraints();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    ListenableFuture<List<PlanWithConstraints>> queryPlansWithConstraints(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    ListenableFuture<List<PlanWithConstraints>> queryPlansWithConstraints(String name);


    @Transaction
    @Query("SELECT * FROM 'plan'")
    ListenableFuture<List<PlanWithEvents>> queryPlansWithEvents();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    ListenableFuture<List<PlanWithEvents>> queryPlansWithEvents(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    ListenableFuture<List<PlanWithEvents>> queryPlansWithEvents(String name);


    @Transaction
    @Query("SELECT * FROM 'plan'")
    ListenableFuture<List<PlanWithAll>> queryPlansWithAll();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    ListenableFuture<List<PlanWithAll>> queryPlansWithAll(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    ListenableFuture<List<PlanWithAll>> queryPlansWithAll(String name);


    @Query("SELECT * FROM 'plan'")
    LiveData<List<Plan>> livePlans();

    @Query("SELECT * FROM 'plan' WHERE plan_id = :id")
    LiveData<List<Plan>> livePlans(long... id);

    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    LiveData<List<Plan>> livePlans(String... name);

    @Transaction
    @Query("SELECT * FROM 'plan'")
    LiveData<List<PlanWithNotes>> livePlansWithNotes();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    LiveData<List<PlanWithNotes>> livePlansWithNotes(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    LiveData<List<PlanWithNotes>> livePlansWithNotes(String name);


    @Transaction
    @Query("SELECT * FROM 'plan'")
    LiveData<List<PlanWithConstraints>> livePlansWithConstraints();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    LiveData<List<PlanWithConstraints>> livePlansWithConstraints(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    LiveData<List<PlanWithConstraints>> livePlansWithConstraints(String name);


    @Transaction
    @Query("SELECT * FROM 'plan'")
    LiveData<List<PlanWithEvents>> livePlansWithEvents();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    LiveData<List<PlanWithEvents>> livePlansWithEvents(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    LiveData<List<PlanWithEvents>> livePlansWithEvents(String name);


    @Transaction
    @Query("SELECT * FROM 'plan'")
    LiveData<List<PlanWithAll>> livePlansWithAll();

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE plan_id IN (:id)")
    LiveData<List<PlanWithAll>> livePlansWithAll(long... id);

    @Transaction
    @Query("SELECT * FROM 'plan' WHERE name LIKE :name")
    LiveData<List<PlanWithAll>> livePlansWithAll(String name);
}
