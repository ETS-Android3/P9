package com.openclassrooms.realestatemanager.ui.propertymap.viewstate;

public class PropertyMapItem {
    private final long id;
    private final String title;
    private final double latitude;
    private final double longitude;

    public PropertyMapItem(long id, String title, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
