package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class PropertyLocationData {
    private long id;
    private final int price;
    @ColumnInfo(name = "address_title")
    private final String addressTitle;
    private double latitude;
    private double longitude;

    public PropertyLocationData(long id, int price, String addressTitle, double latitude, double longitude) {
        this.id = id;
        this.price = price;
        this.addressTitle = addressTitle;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
