package com.openclassrooms.realestatemanager.ui.propertymap.viewstate;

import android.location.Location;

import java.util.List;

public class PropertyMapViewState {
    private final Location userLocation;
    private final List<PropertyMapItem> propertyMapItems;

    public PropertyMapViewState(Location userLocation, List<PropertyMapItem> propertyMapItems) {
        this.userLocation = userLocation;
        this.propertyMapItems = propertyMapItems;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public List<PropertyMapItem> getPropertyMapItems() {
        return propertyMapItems;
    }
}
