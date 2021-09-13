package com.openclassrooms.realestatemanager.data.room.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.AgentDao;
import com.openclassrooms.realestatemanager.data.room.model.Agent;

import java.util.List;

public class AgentRepository {

    private final AgentDao agentDao;

    public AgentRepository(AgentDao agentDao){
        this.agentDao = agentDao;
    }

    public LiveData<List<Agent>> getAgents() {return agentDao.getAgents();}
    public LiveData<Agent> getAgentById(long id) {return agentDao.getAgentById(id);}

    public void insert(Agent agent) {agentDao.insert(agent);}
    public void update(Agent agent) {agentDao.update(agent);}
    public void delete(long id) {agentDao.delete(id);}
}
