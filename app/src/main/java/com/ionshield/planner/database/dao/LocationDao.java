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
import com.ionshield.planner.database.entities.Location;
import com.ionshield.planner.database.entities.compound.LocationAndNode;
import com.ionshield.planner.database.entities.compound.LocationWithTypes;
import com.ionshield.planner.database.entities.compound.LocationWithTypesAndNode;

import java.util.List;

@Dao
public interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Location... locations);

    @Update
    ListenableFuture<Integer> updateAll(Location... locations);

    @Delete
    ListenableFuture<Integer> deleteAll(Location... locations);

    @Query("SELECT * FROM 'location'")
    ListenableFuture<List<Location>> queryLocations();

    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    ListenableFuture<List<Location>> queryLocations(long... id);

    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    ListenableFuture<List<Location>> queryLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'location'")
    ListenableFuture<List<LocationAndNode>> queryLocationsAndNodes();

    @Transaction
    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    ListenableFuture<List<LocationAndNode>> queryLocationsAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    ListenableFuture<List<LocationAndNode>> queryLocationsAndNodes(String name);


    @Transaction
    @Query("SELECT * FROM 'location'")
    ListenableFuture<List<LocationWithTypes>> queryLocationsWithTypes();

    @Transaction
    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    ListenableFuture<List<LocationWithTypes>> queryLocationsWithTypes(long... id);

    @Transaction
    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    ListenableFuture<List<LocationWithTypes>> queryLocationsWithTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'location'")
    ListenableFuture<List<LocationWithTypesAndNode>> queryLocationsWithTypesAndNodes();

    @Transaction
    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    ListenableFuture<List<LocationWithTypesAndNode>> queryLocationsWithTypesAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    ListenableFuture<List<LocationWithTypesAndNode>> queryLocationsWithTypesAndNodes(String name);


    @Query("SELECT * FROM 'location'")
    LiveData<List<Location>> liveLocations();

    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    LiveData<List<Location>> liveLocations(long... id);

    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    LiveData<List<Location>> liveLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'location'")
    LiveData<List<LocationAndNode>> liveLocationsAndNodes();

    @Transaction
    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    LiveData<List<LocationAndNode>> liveLocationsAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    LiveData<List<LocationAndNode>> liveLocationsAndNodes(String name);


    @Transaction
    @Query("SELECT * FROM 'location'")
    LiveData<List<LocationWithTypes>> liveLocationsWithTypes();

    @Transaction
    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    LiveData<List<LocationWithTypes>> liveLocationsWithTypes(long... id);

    @Transaction
    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    LiveData<List<LocationWithTypes>> liveLocationsWithTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'location'")
    LiveData<List<LocationWithTypesAndNode>> liveLocationsWithTypesAndNodes();

    @Transaction
    @Query("SELECT * FROM 'location' WHERE location_id IN (:id)")
    LiveData<List<LocationWithTypesAndNode>> liveLocationsWithTypesAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'location' WHERE name LIKE :name")
    LiveData<List<LocationWithTypesAndNode>> liveLocationsWithTypesAndNodes(String name);
}
