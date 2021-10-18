package com.openclassrooms.realestatemanager.ui.propertyedit.viewstate;

import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;

public class PropertyEditViewState {
    private final String addressTitle;
    private final String address;
    private final String description;
    private final String pointOfInterest;
    private final String price;
    private final String surface;
    private final String rooms;
    private final String entryDate;
    private final String saleDate;
    private final long agentId;
    private final String agentName;
    private final long propertyTypeId;
    private final String propertyTypeName;
    private final double latitude;
    private final double longitude;

    public PropertyEditViewState() {
        this.addressTitle = "";
        this.address = "";
        this.description = "";
        this.pointOfInterest = "";
        this.price = "";
        this.surface = "";
        this.rooms = "";
        this.entryDate = "";
        this.saleDate = "";
        this.agentId = PropertyConst.AGENT_ID_NOT_INITIALIZED;
        this.agentName = "";
        this.propertyTypeId = PropertyConst.PROPERTY_TYPE_ID_NOT_INITIALIZED;
        this.propertyTypeName = "";
        this.latitude = 0;
        this.longitude = 0;
    }

    public PropertyEditViewState(String addressTitle, String address, String description, String pointOfInterest, String price, String surface, String rooms, String entryDate, String saleDate, long agentId, String agentName, long propertyTypeId, String propertyTypeName, double latitude, double longitude) {
        this.addressTitle = addressTitle;
        this.address = address;
        this.description = description;
        this.pointOfInterest = pointOfInterest;
        this.price = price;
        this.surface = surface;
        this.rooms = rooms;
        this.entryDate = entryDate;
        this.saleDate = saleDate;
        this.agentId = agentId;
        this.agentName = agentName;
        this.propertyTypeId = propertyTypeId;
        this.propertyTypeName = propertyTypeName;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getAddressTitle() {
        return addressTitle;
    }

    public String getAddress() {
        return address;
    }

    public String getDescription() {
        return description;
    }

    public String getPointOfInterest() {
        return pointOfInterest;
    }

    public String getPrice() {
        return price;
    }

    public String getSurface() {
        return surface;
    }

    public String getRooms() {
        return rooms;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public String getSaleDate() {
        return saleDate;
    }

    public long getAgentId() {
        return agentId;
    }

    public String getAgentName() {
        return agentName;
    }

    public long getPropertyTypeId() {
        return propertyTypeId;
    }

    public String getPropertyTypeName() {
        return propertyTypeName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
