package com.openclassrooms.realestatemanager.ui.propertydetail.viewstate;

import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;

import java.util.List;

public class PropertyDetailViewState {
    private final PropertyDetailData propertyDetailData;
    private final List<Photo> photos;
    private final int propertyStateResId;
    private final String entryDate;
    private final String saleDate;
    private final String staticMapUrl;

    public PropertyDetailViewState(PropertyDetailData propertyDetailData, List<Photo> photos, int propertyStateResId, String entryDate, String saleDate, String staticMapUrl) {
        this.propertyDetailData = propertyDetailData;
        this.photos = photos;
        this.propertyStateResId = propertyStateResId;
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

    public int getPropertyStateResId() {
        return propertyStateResId;
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
