package com.openclassrooms.realestatemanager.ui.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyCategoryRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;

import java.util.List;

public class MainViewModel extends ViewModel {

    private final AgentRepository agentRepository;
    private final PhotoRepository photoRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyCategoryRepository propertyCategoryRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    private LiveData<List<Agent>> agents;
    public LiveData<List<Agent>> getAgentLiveData() { return agents; }

    private LiveData<List<Property>> propertys;
    public LiveData<List<Property>> getPropertysLiveData() { return propertys; }


    public MainViewModel(AgentRepository agentRepository,
                         PhotoRepository photoRepository,
                         PropertyRepository propertyRepository,
                         PropertyCategoryRepository propertyCategoryRepository,
                         PropertyTypeRepository propertyTypeRepository) {
        this.agentRepository = agentRepository;
        this.photoRepository = photoRepository;
        this.propertyRepository = propertyRepository;
        this.propertyCategoryRepository = propertyCategoryRepository;
        this.propertyTypeRepository = propertyTypeRepository;
    }

    public void load(){

        agents = agentRepository.getAgents();
        propertys = propertyRepository.getPropertys();
    }
}
