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
import com.ionshield.planner.database.entities.Event;
import com.ionshield.planner.database.entities.compound.EventAndItem;
import com.ionshield.planner.database.entities.compound.EventAndItemWithTypes;
import com.ionshield.planner.database.entities.compound.EventAndItemWithTypesWithLocations;
import com.ionshield.planner.database.entities.compound.EventAndItemWithTypesWithLocationsAndNodes;

import java.util.List;

@Dao
public interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Event... events);

    @Update
    ListenableFuture<Integer> updateAll(Event... events);

    @Delete
    ListenableFuture<Integer> deleteAll(Event... events);


    @Query("SELECT * FROM 'event'")
    ListenableFuture<List<Event>> queryEvents();

    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    ListenableFuture<List<Event>> queryEvents(long... id);

    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    ListenableFuture<List<Event>> queryEvents(String name);


    @Query("SELECT * FROM 'event' WHERE plan_id IN (:id)")
    ListenableFuture<List<Event>> queryEventsByPlanId(long... id);

    @Query("SELECT * FROM 'event' WHERE name LIKE :name AND plan_id IN (:id)")
    ListenableFuture<List<Event>> queryEventsByPlanId(String name, long... id);


    @Transaction
    @Query("SELECT * FROM 'event'")
    ListenableFuture<List<EventAndItem>> queryEventsAndItems();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    ListenableFuture<List<EventAndItem>> queryEventsAndItems(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    ListenableFuture<List<EventAndItem>> queryEventsAndItems(String name);


    @Transaction
    @Query("SELECT * FROM 'event' WHERE plan_id IN (:id)")
    ListenableFuture<List<EventAndItem>> queryEventsAndItemsByPlanId(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name AND plan_id IN (:id)")
    ListenableFuture<List<EventAndItem>> queryEventsAndItemsByPlanId(String name, long... id);


    @Transaction
    @Query("SELECT * FROM 'event'")
    ListenableFuture<List<EventAndItemWithTypes>> queryEventsAndItemsWithTypes();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    ListenableFuture<List<EventAndItemWithTypes>> queryEventsAndItemsWithTypes(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    ListenableFuture<List<EventAndItemWithTypes>> queryEventsAndItemsWithTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'event'")
    ListenableFuture<List<EventAndItemWithTypesWithLocations>> queryEventsAndItemsWithTypesWithLocations();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    ListenableFuture<List<EventAndItemWithTypesWithLocations>> queryEventsAndItemsWithTypesWithLocations(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    ListenableFuture<List<EventAndItemWithTypesWithLocations>> queryEventsAndItemsWithTypesWithLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'event'")
    ListenableFuture<List<EventAndItemWithTypesWithLocationsAndNodes>> queryEventsAndItemsWithTypesWithLocationsAndNodes();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    ListenableFuture<List<EventAndItemWithTypesWithLocationsAndNodes>> queryEventsAndItemsWithTypesWithLocationsAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    ListenableFuture<List<EventAndItemWithTypesWithLocationsAndNodes>> queryEventsAndItemsWithTypesWithLocationsAndNodes(String name);


    @Query("SELECT * FROM 'event'")
    LiveData<List<Event>> liveEvents();

    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    LiveData<List<Event>> liveEvents(long... id);

    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    LiveData<List<Event>> liveEvents(String name);


    @Query("SELECT * FROM 'event' WHERE plan_id IN (:id)")
    LiveData<List<Event>> liveEventsByPlanId(long... id);

    @Query("SELECT * FROM 'event' WHERE name LIKE :name AND plan_id IN (:id)")
    LiveData<List<Event>> liveEventsByPlanId(String name, long... id);


    @Transaction
    @Query("SELECT * FROM 'event'")
    LiveData<List<EventAndItem>> liveEventsAndItems();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    LiveData<List<EventAndItem>> liveEventsAndItems(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    LiveData<List<EventAndItem>> liveEventsAndItems(String name);


    @Transaction
    @Query("SELECT * FROM 'event' WHERE plan_id IN (:id)")
    LiveData<List<EventAndItem>> liveEventsAndItemsByPlanId(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name AND plan_id IN (:id)")
    LiveData<List<EventAndItem>> liveEventsAndItemsByPlanId(String name, long... id);


    @Transaction
    @Query("SELECT * FROM 'event'")
    LiveData<List<EventAndItemWithTypes>> liveEventsAndItemsWithTypes();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    LiveData<List<EventAndItemWithTypes>> liveEventsAndItemsWithTypes(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    LiveData<List<EventAndItemWithTypes>> liveEventsAndItemsWithTypes(String name);


    @Transaction
    @Query("SELECT * FROM 'event'")
    LiveData<List<EventAndItemWithTypesWithLocations>> liveEventsAndItemsWithTypesWithLocations();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    LiveData<List<EventAndItemWithTypesWithLocations>> liveEventsAndItemsWithTypesWithLocations(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    LiveData<List<EventAndItemWithTypesWithLocations>> liveEventsAndItemsWithTypesWithLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'event'")
    LiveData<List<EventAndItemWithTypesWithLocationsAndNodes>> liveEventsAndItemsWithTypesWithLocationsAndNodes();

    @Transaction
    @Query("SELECT * FROM 'event' WHERE event_id IN (:id)")
    LiveData<List<EventAndItemWithTypesWithLocationsAndNodes>> liveEventsAndItemsWithTypesWithLocationsAndNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'event' WHERE name LIKE :name")
    LiveData<List<EventAndItemWithTypesWithLocationsAndNodes>> liveEventsAndItemsWithTypesWithLocationsAndNodes(String name);
}
