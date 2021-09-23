package com.openclassrooms.realestatemanager.utils;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;

public class LandscapeHelper {
    public static boolean isLandscape(){
        return MainApplication.getApplication().getResources().getBoolean(R.bool.is_landscape);
    }
}
