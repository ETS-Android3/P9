package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

import java.util.ArrayList;
import java.util.List;

public class CachePropertyEditViewModel {

    private List<Agent> agents;
    private List<PropertyType> propertyTypes;

    public CachePropertyEditViewModel() {
        agents = new ArrayList<>();
        propertyTypes = new ArrayList<>();
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public List<PropertyType> getPropertyTypes() {
        return propertyTypes;
    }

    public void setPropertyTypes(List<PropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }
}
