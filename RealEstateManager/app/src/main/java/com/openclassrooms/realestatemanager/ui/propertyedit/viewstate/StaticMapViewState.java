package com.openclassrooms.realestatemanager.ui.propertyedit.viewstate;

import com.google.android.gms.maps.model.LatLng;

public class StaticMapViewState {
    private LatLng latLang;
    private String url;

    public StaticMapViewState(LatLng latLang, String url) {
        this.latLang = latLang;
        this.url = url;
    }

    public LatLng getLatLang() {
        return latLang;
    }

    public String getUrl() {
        return url;
    }
}
