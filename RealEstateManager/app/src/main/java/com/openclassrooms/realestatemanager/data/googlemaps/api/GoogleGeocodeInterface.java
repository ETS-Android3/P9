package com.openclassrooms.realestatemanager.data.googlemaps.api;

import com.openclassrooms.realestatemanager.data.googlemaps.model.geocode.Geocode;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GoogleGeocodeInterface {
    /**
     * documentation :
     * https://developers.google.com/maps/documentation/geocoding/overview
     *
     * example :
     * https://maps.googleapis.com/maps/api/geocode/json?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY
     */

    @GET("json?")
    Call<Geocode> getGeocode(
            @Query("address") String address,
            @Query("key") String key);
}
