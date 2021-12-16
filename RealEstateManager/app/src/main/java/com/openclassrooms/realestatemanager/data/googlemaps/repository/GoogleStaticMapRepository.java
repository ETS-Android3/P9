package com.openclassrooms.realestatemanager.data.googlemaps.repository;

import android.util.Log;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.Locale;

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
        return formatParamValue("zoom", "14");
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
        String centerValue = String.format(Locale.getDefault(),"%f,%f", latitude, longitude);
        return formatParamValue("center", centerValue);
    }

    private String formatMarkerParam(double latitude, double longitude){
        String centerValue = String.format(Locale.getDefault(), "%f,%f", latitude, longitude);
        String markerValue = String.format("color:red|%s", centerValue);
        return formatParamValue("markers", markerValue);
    }

    public String getUrlImage(double latitude, double longitude){

        boolean isWifiEnabled = Utils.isInternetAvailable(MainApplication.getApplication());
        if (! isWifiEnabled) return null;

        Log.d(Tag.TAG, "GoogleStaticMapRepository.getUrlImage() called with: latitude = [" + latitude + "], longitude = [" + longitude + "]");
        if ((latitude == 0) && (longitude == 0)) return "";

        String url = String.format("%s?%s&%s&%s&%s&%s&%s&%s",
                getBaseUrl(),
                formatCenterParam(latitude, longitude),
                formatKeyParam(),
                formatSizeParam(),
                formatZoomParam(),
                formatImageFormatParam(),
                formatMapTypeParam(),
                formatMarkerParam(latitude, longitude));
        Log.d(Tag.TAG, "GoogleStaticMapRepository.getUrlImage() return url = [" + url + "]");
        return url;
    }
}
