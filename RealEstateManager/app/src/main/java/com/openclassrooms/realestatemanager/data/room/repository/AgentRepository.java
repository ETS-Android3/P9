package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.AgentDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.Agent;

import java.util.List;

public class AgentRepository {

    private final AgentDao agentDao;

    public AgentRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        agentDao = db.agentDao();
    }

    public LiveData<List<Agent>> getAgents() {return agentDao.getAgents();}
    public LiveData<Agent> getAgentById(long id) {return agentDao.getAgentById(id);}

    public void insert(Agent agent) {
        AppDatabase.getExecutor().execute(() -> {
            agentDao.insert(agent);
        });
    }

    public void update(Agent agent) {
        AppDatabase.getExecutor().execute(() -> {
            agentDao.update(agent);
        });
    }

    public void delete(long id) {
        AppDatabase.getExecutor().execute(() -> {
            agentDao.delete(id);
        });
    }
}
