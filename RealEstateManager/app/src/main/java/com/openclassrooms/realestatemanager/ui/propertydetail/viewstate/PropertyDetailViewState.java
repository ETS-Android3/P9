package com.openclassrooms.realestatemanager.ui.propertydetail.viewstate;

import android.location.Location;

import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;

import java.util.List;

public class PropertyDetailViewState {
    private final PropertyDetailData propertyDetailData;
    private final List<Photo> photos;
    private final String propertyState;
    private final String entryDate;
    private final String saleDate;
    private final String staticMapUrl;

    public PropertyDetailViewState(PropertyDetailData propertyDetailData, List<Photo> photos, String propertyState, String entryDate, String saleDate, String staticMapUrl) {
        this.propertyDetailData = propertyDetailData;
        this.photos = photos;
        this.propertyState = propertyState;
        this.entryDate = entryDate;
        this.saleDate = saleDate;
        this.staticMapUrl = staticMapUrl;
    }

    public PropertyDetailData getPropertyDetailData() {
        return propertyDetailData;
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

    public String getStaticMapUrl() {
        return staticMapUrl;
    }
}
