package com.openclassrooms.realestatemanager.ui.propertylist.viewstate;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

import java.util.List;

public class PropertyListViewState {

    private List<Agent> agents;
    private List<Photo> photos;
    private List<Property> properties;
    private List<PropertyCategory> categories;
    private List<PropertyType> types;

    public PropertyListViewState(List<Agent> agents, List<Photo> photos, List<Property> properties, List<PropertyCategory> categories, List<PropertyType> types) {
        this.agents = agents;
        this.photos = photos;
        this.properties = properties;
        this.categories = categories;
        this.types = types;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public List<Property> getProperties() {
        return properties;
    }

    public List<PropertyCategory> getCategories() {
        return categories;
    }

    public List<PropertyType> getTypes() {
        return types;
    }
}
