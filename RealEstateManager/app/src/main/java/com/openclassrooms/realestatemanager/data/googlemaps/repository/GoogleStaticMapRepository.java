package com.openclassrooms.realestatemanager.data.googlemaps.repository;

import com.openclassrooms.realestatemanager.data.googlemaps.api.GoogleGeocodeClient;
import com.openclassrooms.realestatemanager.data.googlemaps.api.GoogleGeocodeInterface;

public class GoogleStaticMapRepository {

    private final String apiKey;

    public GoogleStaticMapRepository(String apiKey) {
        this.apiKey = apiKey;
    }

    private String getApiKey() { return apiKey; }

    private String getBaseUrl(){
        return "https://maps.googleapis.com/maps/api/staticmap";
    }

    private String formatParamValue(String param, String value){
        return String.format("%s=%s", param, value);
    }

    private String formatSizeParam(){
        return formatParamValue("size", "400x400");
    }

    private String formatZoomParam(){
        return formatParamValue("zoom", "16");
    }

    private String formatImageFormatParam(){
        return formatParamValue("format", "jpg");
    }

    private String formatMapTypeParam(){
        return formatParamValue("maptype", "roadmap");
    }

    private String formatKeyParam(){
        return formatParamValue("key", getApiKey());
    }

    private String formatCenterParam(double latitude, double longitude){
        String centerValue = String.format("%f,%f", latitude, longitude);
        return formatParamValue("center", centerValue);
    }

    public String getImage(double latitude, double longitude){
        return String.format("%s?%s&%s&%s&%s&%s&%s",
                getBaseUrl(),
                formatCenterParam(latitude, longitude),
                formatKeyParam(),
                formatSizeParam(),
                formatZoomParam(),
                formatImageFormatParam(),
                formatMapTypeParam());
    }
}
