package com.openclassrooms.realestatemanager;

import android.app.Application;

public class MainApplication extends Application {

    private static Application application;
    private static String googleApiKey;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        googleApiKey = BuildConfig.GOOGLE_PLACES_KEY_P9;
    }

    public static Application getApplication() {
        return application;
    }
    public static String getGoogleApiKey() {
        return googleApiKey;
    }
}
