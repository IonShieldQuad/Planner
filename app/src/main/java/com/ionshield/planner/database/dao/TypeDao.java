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
import com.ionshield.planner.database.entities.Type;
import com.ionshield.planner.database.entities.compound.TypeWithItems;
import com.ionshield.planner.database.entities.compound.TypeWithLocations;
import com.ionshield.planner.database.entities.compound.TypeWithLocationsAndNodes;

import java.util.List;

@Dao
public interface TypeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Type... types);

    @Update
    ListenableFuture<Integer> updateAll(Type... types);

    @Delete
    ListenableFuture<Integer> deleteAll(Type... types);

    @Query("SELECT * FROM 'type'")
    ListenableFuture<List<Type>> queryTypes();

    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    ListenableFuture<List<Type>> queryTypes(long... id);

    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    ListenableFuture<List<Type>> queryTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'type'")
    ListenableFuture<List<TypeWithItems>> queryTypesWithItems();


    @Transaction
    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    ListenableFuture<List<TypeWithItems>> queryTypesWithItems(long... id);


    @Transaction
    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    ListenableFuture<List<TypeWithItems>> queryTypesWithItems(String name);


    @Transaction
    @Query("SELECT * FROM 'type'")
    ListenableFuture<List<TypeWithLocations>> queryTypesWithLocations();


    @Transaction
    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    ListenableFuture<List<TypeWithLocations>> queryTypesWithLocations(long... id);


    @Transaction
    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    ListenableFuture<List<TypeWithLocations>> queryTypesWithLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'type'")
    ListenableFuture<List<TypeWithLocationsAndNodes>> queryTypesWithLocationsAndNodes();


    @Transaction
    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    ListenableFuture<List<TypeWithLocationsAndNodes>> queryTypesWithLocationsAndNodes(long... id);


    @Transaction
    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    ListenableFuture<List<TypeWithLocationsAndNodes>> queryTypesWithLocationsAndNodes(String name);


    @Query("SELECT * FROM 'type'")
    LiveData<List<Type>> liveTypes();

    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    LiveData<List<Type>> liveTypes(long... id);

    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    LiveData<List<Type>> liveTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'type'")
    LiveData<List<TypeWithItems>> liveTypesWithItems();


    @Transaction
    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    LiveData<List<TypeWithItems>> liveTypesWithItems(long... id);


    @Transaction
    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    LiveData<List<TypeWithItems>> liveTypesWithItems(String name);


    @Transaction
    @Query("SELECT * FROM 'type'")
    LiveData<List<TypeWithLocations>> liveTypesWithLocations();


    @Transaction
    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    LiveData<List<TypeWithLocations>> liveTypesWithLocations(long... id);


    @Transaction
    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    LiveData<List<TypeWithLocations>> liveTypesWithLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'type'")
    LiveData<List<TypeWithLocationsAndNodes>> liveTypesWithLocationsAndNodes();


    @Transaction
    @Query("SELECT * FROM 'type' WHERE type_id IN (:id)")
    LiveData<List<TypeWithLocationsAndNodes>> liveTypesWithLocationsAndNodes(long... id);


    @Transaction
    @Query("SELECT * FROM 'type' WHERE name LIKE :name")
    LiveData<List<TypeWithLocationsAndNodes>> liveTypesWithLocationsAndNodes(String name);
}
