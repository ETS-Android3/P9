package com.openclassrooms.realestatemanager.ui.propertydetail.viewstate;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

import java.util.List;

public class PropertyDetailViewState {
    private Property property;
    private List<Photo> photos;
    private PropertyCategory category;
    private PropertyType propertyType;
    private Agent agent;
    private String propertyState;
    private String entryDate;
    private String saleDate;

    public PropertyDetailViewState(Property property, List<Photo> photos, PropertyCategory category, PropertyType propertyType, Agent agent, String propertyState, String entryDate, String saleDate) {
        this.property = property;
        this.photos = photos;
        this.category = category;
        this.propertyType = propertyType;
        this.agent = agent;
        this.propertyState = propertyState;
        this.entryDate = entryDate;
        this.saleDate = saleDate;
    }

    public Property getProperty() {
        return property;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public PropertyCategory getCategory() {
        return category;
    }

    public PropertyType getPropertyType() {
        return propertyType;
    }

    public Agent getAgent() {
        return agent;
    }

    public String getPropertyState() {
        return propertyState;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getSaleDate() {
        return saleDate;
    }
}
