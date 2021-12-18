package com.openclassrooms.realestatemanager.utils;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;

import com.openclassrooms.realestatemanager.MainApplication;

public class ResourceArrayHelper {
    @SuppressLint("ResourceType")
    public static float getMaxRangeFromArray(int resId){
        TypedArray array = MainApplication.getApplication().getResources().obtainTypedArray(resId);
        if ((array != null) && (array.length() > 0)){
            String s = array.getString(1);
            try {
                Float f = new Float(s);
                return f;
            } catch (NumberFormatException e){
                return 0f;
            }
        }
        return 0f;
    }
}
