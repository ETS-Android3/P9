package com.openclassrooms.realestatemanager.ui.bundle;

import android.os.Bundle;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;

public class PropertyBundle {
    private static String getBundleTitle(long propertyId){
        if (propertyId == PropertyConst.PROPERTY_ID_NOT_INITIALIZED) {
            return MainApplication.getApplication().getString(R.string.property_edit_title_create);
        } else {
            return MainApplication.getApplication().getString(R.string.property_edit_title_modify);
        }
    }

    public static Bundle createEditBundle(long propertyId){
        Bundle bundle = new Bundle();
        bundle.putLong(PropertyConst.ARG_PROPERTY_ID_KEY, propertyId);
        bundle.putString(PropertyConst.ARG_PROPERTY_TITLE, getBundleTitle(propertyId));
        return bundle;
    }
}
