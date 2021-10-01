package com.openclassrooms.realestatemanager;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.content.pm.ApplicationInfo;

import com.openclassrooms.realestatemanager.tag.Tag;

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
