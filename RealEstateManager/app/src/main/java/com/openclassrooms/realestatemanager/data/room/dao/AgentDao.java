package com.openclassrooms.realestatemanager.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.room.model.Agent;

import java.util.List;

@Dao
public interface AgentDao {
    @Query("SELECT * FROM agent ORDER BY agent.id")
    LiveData<List<Agent>> getAgents();

    @Query("SELECT * FROM agent WHERE agent.id = :id")
    LiveData<Agent> getAgentById(long id);

    @Insert
    long insert(Agent agent);

    @Update
    int update(Agent agent);

    @Query("DELETE FROM agent WHERE agent.id = :id")
    int delete(long id);
}
