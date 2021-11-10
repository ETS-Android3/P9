package com.openclassrooms.realestatemanager.ui.propertymap.viewstate;

import android.location.Location;

import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;

import java.util.List;

public class PropertyMapViewState {
    private final Location userLocation;
    private final PropertyLocationData currentPropertyLocation;
    private final List<PropertyLocationData> otherPropertyLocation;

    public PropertyMapViewState(Location userLocation, PropertyLocationData currentPropertyLocation, List<PropertyLocationData> otherPropertyLocation) {
        this.userLocation = userLocation;
        this.currentPropertyLocation = currentPropertyLocation;
        this.otherPropertyLocation = otherPropertyLocation;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public PropertyLocationData getCurrentPropertyLocation() {
        return currentPropertyLocation;
    }

    public List<PropertyLocationData> getOtherPropertyLocation() {
        return otherPropertyLocation;
    }
}
