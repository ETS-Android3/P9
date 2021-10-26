package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "photo")
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final int order;
    private final String url;
    private final String legend;
    @ColumnInfo(name = "property_id")
    private long propertyId;

    public Photo(long id, int order, String url, String legend, long propertyId) {
        this.id = id;
        this.order = order;
        this.url = url;
        this.legend = legend;
        this.propertyId = propertyId;
    }

    public long getId() {
        return id;
    }

    public int getOrder() {
        return order;
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

    public void setPropertyId(long propertyId) {
        this.propertyId = propertyId;
    }
}
