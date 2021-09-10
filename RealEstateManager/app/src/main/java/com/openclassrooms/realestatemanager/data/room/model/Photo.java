package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    private String legend;
    private long propertyId;

    public Photo(long id, String url, String legend, long propertyId) {
        this.id = id;
        this.url = url;
        this.legend = legend;
        this.propertyId = propertyId;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getLegend() {
        return legend;
    }

    public long getPropertyId() {
        return propertyId;
    }
}
