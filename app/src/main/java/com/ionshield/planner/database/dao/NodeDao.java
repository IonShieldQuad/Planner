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
import com.ionshield.planner.database.entities.Node;
import com.ionshield.planner.database.entities.compound.NodeWithLocations;

import java.util.List;

@Dao
public interface NodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    ListenableFuture<List<Long>> insertAll(Node... nodes);

    @Update
    ListenableFuture<Integer> updateAll(Node... nodes);

    @Delete
    ListenableFuture<Integer> deleteAll(Node... nodes);

    @Query("SELECT * FROM 'node'")
    ListenableFuture<List<Node>> queryNodes();

    @Query("SELECT * FROM 'node' WHERE node_id IN (:id)")
    ListenableFuture<List<Node>> queryNodes(long... id);

    @Query("SELECT * FROM 'node' WHERE name LIKE :name")
    ListenableFuture<List<Node>> queryNodes(String name);


    @Transaction
    @Query("SELECT * FROM 'node'")
    ListenableFuture<List<NodeWithLocations>> queryNodesWithLocations();

    @Transaction
    @Query("SELECT * FROM 'node' WHERE node_id IN (:id)")
    ListenableFuture<List<NodeWithLocations>> queryNodesWithLocations(long... id);

    @Transaction
    @Query("SELECT * FROM 'node' WHERE name LIKE :name")
    ListenableFuture<List<NodeWithLocations>> queryNodesWithLocations(String name);


    @Transaction
    @Query("SELECT * FROM 'node'")
    LiveData<List<Node>> liveNodes();

    @Transaction
    @Query("SELECT * FROM 'node' WHERE node_id IN (:id)")
    LiveData<List<Node>> liveNodes(long... id);

    @Transaction
    @Query("SELECT * FROM 'node' WHERE name LIKE :name")
    LiveData<List<Node>> liveNodes(String name);


    @Transaction
    @Query("SELECT * FROM 'node'")
    LiveData<List<NodeWithLocations>> liveNodesWithLocations();

    @Transaction
    @Query("SELECT * FROM 'node' WHERE node_id IN (:id)")
    LiveData<List<NodeWithLocations>> liveNodesWithLocations(long... id);

    @Transaction
    @Query("SELECT * FROM 'node' WHERE name LIKE :name")
    LiveData<List<NodeWithLocations>> liveNodesWithLocations(String name);
}
