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
import com.ionshield.planner.database.entities.Item;
import com.ionshield.planner.database.entities.compound.ItemWithEvents;
import com.ionshield.planner.database.entities.compound.ItemWithTypes;
import com.ionshield.planner.database.entities.compound.ItemWithTypesWithLocations;
import com.ionshield.planner.database.entities.compound.ItemWithTypesWithLocationsAndNodes;

import java.util.List;

@Dao
public interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Item... items);

    @Update
    ListenableFuture<Integer> updateAll(Item... items);

    @Delete
    ListenableFuture<Integer> deleteAll(Item... items);

    @Query("SELECT * FROM 'item'")
    ListenableFuture<List<Item>> queryItems();

    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    ListenableFuture<List<Item>> queryItems(long... id);

    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    ListenableFuture<List<Item>> queryItems(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    ListenableFuture<List<ItemWithEvents>> queryItemsWithEvents();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    ListenableFuture<List<ItemWithEvents>> queryItemsWithEvents(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    ListenableFuture<List<ItemWithEvents>> queryItemsWithEvents(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    ListenableFuture<List<ItemWithTypes>> queryItemsWithTypes();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    ListenableFuture<List<ItemWithTypes>> queryItemsWithTypes(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    ListenableFuture<List<ItemWithTypes>> queryItemsWithTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    ListenableFuture<List<ItemWithTypesWithLocations>> queryItemsWithTypesWithLocations();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    ListenableFuture<List<ItemWithTypesWithLocations>> queryItemsWithTypesWithLocations(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    ListenableFuture<List<ItemWithTypesWithLocations>> queryItemsWithTypesWithLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    ListenableFuture<List<ItemWithTypesWithLocationsAndNodes>> queryItemsWithTypesWithLocationsAndNodes();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    ListenableFuture<List<ItemWithTypesWithLocationsAndNodes>> queryItemsWithTypesWithLocationsAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    ListenableFuture<List<ItemWithTypesWithLocationsAndNodes>> queryItemsWithTypesWithLocationsAndNodes(String name);


    @Query("SELECT * FROM 'item'")
    LiveData<List<Item>> liveItems();

    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    LiveData<List<Item>> liveItems(long... id);

    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    LiveData<List<Item>> liveItems(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    LiveData<List<ItemWithEvents>> liveItemsWithEvents();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    LiveData<List<ItemWithEvents>> liveItemsWithEvents(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    LiveData<List<ItemWithEvents>> liveItemsWithEvents(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    LiveData<List<ItemWithTypes>> liveItemsWithTypes();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    LiveData<List<ItemWithTypes>> liveItemsWithTypes(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    LiveData<List<ItemWithTypes>> liveItemsWithTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    LiveData<List<ItemWithTypesWithLocations>> liveItemsWithTypesWithLocations();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    LiveData<List<ItemWithTypesWithLocations>> liveItemsWithTypesWithLocations(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    LiveData<List<ItemWithTypesWithLocations>> liveItemsWithTypesWithLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'item'")
    LiveData<List<ItemWithTypesWithLocationsAndNodes>> liveItemsWithTypesWithLocationsAndNodes();

    @Transaction
    @Query("SELECT * FROM 'item' WHERE item_id IN (:id)")
    LiveData<List<ItemWithTypesWithLocationsAndNodes>> liveItemsWithTypesWithLocationsAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'item' WHERE name LIKE :name")
    LiveData<List<ItemWithTypesWithLocationsAndNodes>> liveItemsWithTypesWithLocationsAndNodes(String name);
}
