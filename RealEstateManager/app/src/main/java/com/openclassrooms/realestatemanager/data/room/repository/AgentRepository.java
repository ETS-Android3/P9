package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.data.room.dao.AgentDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AgentRepository {

    private final AgentDao agentDao;

    public AgentRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        agentDao = db.agentDao();
    }

    public List<Agent> getAgents(){
        Callable<List<Agent>> callable = new Callable<List<Agent>>() {
            @Override
            public List<Agent> call() throws Exception {
                return agentDao.getAgents();
            }
        };

        List<Agent> agents = new ArrayList<>();
        Future<List<Agent>> future = AppDatabase.getExecutor().submit(callable);
        try {
            List<Agent> list = future.get();
            agents.addAll(list);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return agents;
    }

    public LiveData<List<Agent>> getAgentsLiveData() {
        MutableLiveData<List<Agent>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(getAgents());
        return mutableLiveData;
    }

    public LiveData<Agent> getAgentById(long id) {return agentDao.getAgentById(id);}

    public Cursor getAgentsWithCursor(){ return agentDao.getAgentsWithCursor(); }
    public Cursor getAgentByIdWithCursor(long id){
        return agentDao.getAgentByIdWithCursor(id);
    }

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
