package com.ionshield.planner.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RewriteQueriesToDropUnusedColumns;
import androidx.room.Transaction;
import androidx.room.Update;

import com.google.common.util.concurrent.ListenableFuture;
import com.ionshield.planner.database.entities.LocationTypeRef;
import com.ionshield.planner.database.entities.compound.RefLocationAndType;

import java.util.List;

@Dao
public interface LocationTypeRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(LocationTypeRef... locationTypeRefs);

    @Update
    ListenableFuture<Integer> updateAll(LocationTypeRef... locationTypeRefs);

    @Delete
    ListenableFuture<Integer> deleteAll(LocationTypeRef... locationTypeRefs);

    @Query("SELECT * FROM 'location_type_ref'")
    ListenableFuture<List<LocationTypeRef>> queryLocationTypeRefs();

    @Query("SELECT * FROM 'location_type_ref' WHERE location_type_ref_id IN (:id)")
    ListenableFuture<List<LocationTypeRef>> queryLocationTypeRefs(long... id);

    @Query("SELECT * FROM 'location_type_ref' WHERE location_id = :locationId AND type_id = :typeId")
    ListenableFuture<List<LocationTypeRef>> queryLocationTypeRef(long locationId, long typeId);

    @Query("SELECT * FROM 'location_type_ref' WHERE location_id IN (:id)")
    ListenableFuture<List<LocationTypeRef>> queryLocationTypeRefsByLocationId(long... id);

    @Query("SELECT * FROM 'location_type_ref' WHERE type_id IN (:id)")
    ListenableFuture<List<LocationTypeRef>> queryLocationTypeRefsByTypeId(long... id);


    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref'")
    ListenableFuture<List<RefLocationAndType>> queryLocationTypeRefsAll();

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' WHERE location_type_ref_id IN (:id)")
    ListenableFuture<List<RefLocationAndType>> queryLocationTypeRefsById(long...id);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' JOIN 'location' ON location.location_id = location_type_ref.location_id JOIN 'type' ON type.type_id = location_type_ref.type_id WHERE location.name LIKE :name OR type.name LIKE :name")
    ListenableFuture<List<RefLocationAndType>> queryLocationTypeRefsByName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' JOIN 'location' ON location.location_id = location_type_ref.location_id JOIN 'type' ON type.type_id = location_type_ref.type_id WHERE type.name LIKE :name")
    ListenableFuture<List<RefLocationAndType>> queryLocationTypeRefsByTypeName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' JOIN 'location' ON location.location_id = location_type_ref.location_id JOIN 'type' ON type.type_id = location_type_ref.type_id WHERE location.name LIKE :name")
    ListenableFuture<List<RefLocationAndType>> queryLocationTypeRefsByLocationName(String name);


    @Query("SELECT * FROM 'location_type_ref'")
    LiveData<List<LocationTypeRef>> liveLocationTypeRefs();

    @Query("SELECT * FROM 'location_type_ref' WHERE location_type_ref_id IN (:id)")
    LiveData<List<LocationTypeRef>> liveLocationTypeRefs(long... id);

    @Query("SELECT * FROM 'location_type_ref' WHERE location_id = :locationId AND type_id = :typeId")
    LiveData<List<LocationTypeRef>> liveLocationTypeRef(long locationId, long typeId);

    @Query("SELECT * FROM 'location_type_ref' WHERE location_id IN (:id)")
    LiveData<List<LocationTypeRef>> liveLocationTypeRefsByLocationId(long... id);

    @Query("SELECT * FROM 'location_type_ref' WHERE type_id IN (:id)")
    LiveData<List<LocationTypeRef>> liveLocationTypeRefsByTypeId(long... id);


    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref'")
    LiveData<List<RefLocationAndType>> liveLocationTypeRefsAll();

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' WHERE location_type_ref_id IN (:id)")
    LiveData<List<RefLocationAndType>> liveLocationTypeRefsById(long... id);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' JOIN 'location' ON location.location_id = location_type_ref.location_id JOIN 'type' ON type.type_id = location_type_ref.type_id WHERE location.name LIKE :name OR type.name LIKE :name")
    LiveData<List<RefLocationAndType>> liveLocationTypeRefsByName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' JOIN 'location' ON location.location_id = location_type_ref.location_id JOIN 'type' ON type.type_id = location_type_ref.type_id WHERE type.name LIKE :name")
    LiveData<List<RefLocationAndType>> liveLocationTypeRefsByTypeName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT location_type_ref.* FROM 'location_type_ref' JOIN 'location' ON location.location_id = location_type_ref.location_id JOIN 'type' ON type.type_id = location_type_ref.type_id WHERE location.name LIKE :name")
    LiveData<List<RefLocationAndType>> liveLocationTypeRefsByLocationName(String name);
}
