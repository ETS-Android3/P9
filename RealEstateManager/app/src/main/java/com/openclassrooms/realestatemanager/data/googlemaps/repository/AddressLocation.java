package com.openclassrooms.realestatemanager.data.googlemaps.repository;

import com.google.android.gms.maps.model.LatLng;

public class AddressLocation {
    private final LatLng latLng;
    private final AddressLocationStatus status;
    private final String error;

    public AddressLocation(LatLng latLng, AddressLocationStatus status, String error) {
        this.latLng = latLng;
        this.status = status;
        this.error = error;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public AddressLocationStatus getStatus() {
        return status;
    }

    public String getError() {
        return error;
    }
}
