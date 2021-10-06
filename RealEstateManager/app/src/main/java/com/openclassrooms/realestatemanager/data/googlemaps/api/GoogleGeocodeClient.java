package com.openclassrooms.realestatemanager.data.googlemaps.api;

import android.util.Log;

import com.openclassrooms.realestatemanager.tag.Tag;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleGeocodeClient {
    public static GoogleGeocodeInterface getClient() {
        Log.d(Tag.TAG, "GoogleGeocodeInterface.getClient() called");

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // use this to set the log detail level
        logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        okHttpClient.addInterceptor(logging);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/geocode/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();

        GoogleGeocodeInterface api = retrofit.create(GoogleGeocodeInterface.class);
        return api;
    }
}
