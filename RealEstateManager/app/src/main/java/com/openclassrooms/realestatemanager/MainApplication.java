package com.openclassrooms.realestatemanager;

import android.app.Application;
import android.util.Log;

import com.openclassrooms.realestatemanager.tag.Tag;

public class MainApplication extends Application {

    private static Application application;
    private static String googleApiKey;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        googleApiKey = com.openclassrooms.realestatemanager.BuildConfig.GOOGLE_PLACES_KEY;
        Log.d(Tag.TAG, "MainApplication.onCreate() called googleApiKey=\"" + googleApiKey +"\"");
    }

    public static Application getApplication() {
        return application;
    }
    public static String getGoogleApiKey() {
        return googleApiKey;
    }
}
