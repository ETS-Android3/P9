package com.openclassrooms.realestatemanager.ui.propertydetail.viewstate;

import android.location.Location;

import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;

import java.util.List;

public class PropertyDetailViewState {
    private final Location userLocation;
    private final PropertyDetailData propertyDetailData;
    private final PropertyLocationData currentPropertyLocation;
    private final List<PropertyLocationData> propertyLocationData;
    private final List<Photo> photos;
    private final String propertyState;
    private final String entryDate;
    private final String saleDate;

    public PropertyDetailViewState(Location userLocation, PropertyDetailData propertyDetailData, PropertyLocationData currentPropertyLocation, List<PropertyLocationData> propertyLocationData, List<Photo> photos, String propertyState, String entryDate, String saleDate) {
        this.userLocation = userLocation;
        this.propertyDetailData = propertyDetailData;
        this.currentPropertyLocation = currentPropertyLocation;
        this.propertyLocationData = propertyLocationData;
        this.photos = photos;
        this.propertyState = propertyState;
        this.entryDate = entryDate;
        this.saleDate = saleDate;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public PropertyDetailData getPropertyDetailData() {
        return propertyDetailData;
    }

    public PropertyLocationData getCurrentPropertyLocation() {
        return currentPropertyLocation;
    }

    public List<PropertyLocationData> getPropertyLocationData() {
        return propertyLocationData;
    }

    public List<Photo> getPhotos() {
        return photos;
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
