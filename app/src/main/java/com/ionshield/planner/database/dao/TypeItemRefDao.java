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
import com.ionshield.planner.database.entities.TypeItemRef;
import com.ionshield.planner.database.entities.compound.RefTypeAndItem;

import java.util.List;

@Dao
public interface TypeItemRefDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(TypeItemRef... typeItemRefs);

    @Update
    ListenableFuture<Integer> updateAll(TypeItemRef... typeItemRefs);

    @Delete
    ListenableFuture<Integer> deleteAll(TypeItemRef... typeItemRefs);

    @Query("SELECT * FROM 'type_item_ref'")
    ListenableFuture<List<TypeItemRef>> queryTypeItemRefs();

    @Query("SELECT * FROM 'type_item_ref' WHERE type_item_ref_id IN (:id)")
    ListenableFuture<List<TypeItemRef>> queryTypeItemRefs(long... id);

    @Query("SELECT * FROM 'type_item_ref' WHERE type_id = :typeId AND item_id = :itemId")
    ListenableFuture<List<TypeItemRef>> queryTypeItemRef(long typeId, long itemId);

    @Query("SELECT * FROM 'type_item_ref' WHERE type_id IN (:id)")
    ListenableFuture<List<TypeItemRef>> queryTypeItemRefsByTypeId(long... id);

    @Query("SELECT * FROM 'type_item_ref' WHERE item_id IN (:id)")
    ListenableFuture<List<TypeItemRef>> queryTypeItemRefsByItemId(long... id);


    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref'")
    ListenableFuture<List<RefTypeAndItem>> queryTypeItemRefsAll();

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' WHERE type_item_ref_id IN (:id)")
    ListenableFuture<List<RefTypeAndItem>> queryTypeItemRefsById(long... id);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' JOIN 'item' ON item.item_id = type_item_ref.item_id JOIN 'type' ON type.type_id = type_item_ref.type_id WHERE item.name LIKE :name OR type.name LIKE :name")
    ListenableFuture<List<RefTypeAndItem>> queryTypeItemRefsByName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' JOIN 'item' ON item.item_id = type_item_ref.item_id JOIN 'type' ON type.type_id = type_item_ref.type_id WHERE type.name LIKE :name")
    ListenableFuture<List<RefTypeAndItem>> queryTypeItemRefsByTypeName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' JOIN 'item' ON item.item_id = type_item_ref.item_id JOIN 'type' ON type.type_id = type_item_ref.type_id WHERE item.name LIKE :name")
    ListenableFuture<List<RefTypeAndItem>> queryTypeItemRefsByItemName(String name);


    @Query("SELECT * FROM 'type_item_ref'")
    LiveData<List<TypeItemRef>> liveTypeItemRefs();

    @Query("SELECT * FROM 'type_item_ref' WHERE type_item_ref_id IN (:id)")
    LiveData<List<TypeItemRef>> liveTypeItemRefs(long... id);

    @Query("SELECT * FROM 'type_item_ref' WHERE type_id = :typeId AND item_id = :itemId")
    LiveData<List<TypeItemRef>> liveTypeItemRef(long typeId, long itemId);

    @Query("SELECT * FROM 'type_item_ref' WHERE type_id IN (:id)")
    LiveData<List<TypeItemRef>> liveTypeItemRefsByTypeId(long... id);

    @Query("SELECT * FROM 'type_item_ref' WHERE item_id IN (:id)")
    LiveData<List<TypeItemRef>> liveTypeItemRefsByItemId(long... id);


    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref'")
    LiveData<List<RefTypeAndItem>> liveTypeItemRefsAll();

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' WHERE type_item_ref_id IN (:id)")
    LiveData<List<RefTypeAndItem>> liveTypeItemRefsById(long... id);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' JOIN 'item' ON item.item_id = type_item_ref.item_id JOIN 'type' ON type.type_id = type_item_ref.type_id WHERE item.name LIKE :name OR type.name LIKE :name")
    LiveData<List<RefTypeAndItem>> liveTypeItemRefsByName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' JOIN 'item' ON item.item_id = type_item_ref.item_id JOIN 'type' ON type.type_id = type_item_ref.type_id WHERE type.name LIKE :name")
    LiveData<List<RefTypeAndItem>> liveTypeItemRefsByTypeName(String name);

    @Transaction
    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT type_item_ref.* FROM 'type_item_ref' JOIN 'item' ON item.item_id = type_item_ref.item_id JOIN 'type' ON type.type_id = type_item_ref.type_id WHERE item.name LIKE :name")
    LiveData<List<RefTypeAndItem>> liveTypeItemRefsByItemName(String name);
}
