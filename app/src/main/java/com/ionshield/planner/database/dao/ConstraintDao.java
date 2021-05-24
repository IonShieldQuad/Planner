package com.ionshield.planner.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.entities.Constraint;

import java.util.List;

@Dao
public interface ConstraintDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Constraint... constraints);

    @Update
    ListenableFuture<Integer> updateAll(Constraint... constraints);

    @Delete
    ListenableFuture<Integer> deleteAll(Constraint... constraints);

    @Query("SELECT * FROM 'constraint'")
    ListenableFuture<List<Constraint>> queryConstraints();

    @Query("SELECT * FROM 'constraint' WHERE constraint_id IN (:id)")
    ListenableFuture<List<Constraint>> queryConstraints(long... id);

    @Query("SELECT * FROM 'constraint' WHERE name LIKE :name")
    ListenableFuture<List<Constraint>> queryConstraints(String name);


    @Query("SELECT * FROM 'constraint' WHERE plan_id IN (:id)")
    ListenableFuture<List<Constraint>> queryConstraintsByPlanId(long... id);

    @Query("SELECT * FROM 'constraint' WHERE name LIKE :name AND plan_id IN (:id)")
    ListenableFuture<List<Constraint>> queryConstraintsByPlanId(String name, long...id);


    @Query("SELECT * FROM 'constraint'")
    LiveData<List<Constraint>> liveConstraints();

    @Query("SELECT * FROM 'constraint' WHERE constraint_id IN (:id)")
    LiveData<List<Constraint>> liveConstraints(long... id);

    @Query("SELECT * FROM 'constraint' WHERE name LIKE :name")
    LiveData<List<Constraint>> liveConstraints(String name);


    @Query("SELECT * FROM 'constraint' WHERE plan_id IN (:id)")
    LiveData<List<Constraint>> liveConstraintsByPlanId(long... id);

    @Query("SELECT * FROM 'constraint' WHERE name LIKE :name AND plan_id IN (:id)")
    LiveData<List<Constraint>> liveConstraintsByPlanId(String name, long...id);
}
