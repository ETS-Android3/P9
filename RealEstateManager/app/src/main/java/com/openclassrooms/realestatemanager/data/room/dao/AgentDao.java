package com.openclassrooms.realestatemanager.data.room.dao;

import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.room.model.Agent;

import java.util.List;

@Dao
public interface AgentDao {
    @Query("SELECT * FROM agent ORDER BY id")
    List<Agent> getAgents();

    @Query("SELECT * FROM agent ORDER BY id")
    Cursor getAgentsWithCursor();

    @Query("SELECT * FROM agent WHERE id = :id")
    LiveData<Agent> getAgentById(long id);

    @Query("SELECT * FROM agent WHERE id = :id")
    Cursor getAgentByIdWithCursor(long id);

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(Agent agent);

    @Update
    int update(Agent agent);

    @Query("DELETE FROM agent WHERE id = :id")
    int delete(long id);
}
