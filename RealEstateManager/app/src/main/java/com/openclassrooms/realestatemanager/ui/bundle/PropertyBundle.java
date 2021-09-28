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

    private static Bundle createBundle(long propertyId, boolean withTitle) {
        Bundle bundle = new Bundle();
        bundle.putLong(PropertyConst.ARG_PROPERTY_ID_KEY, propertyId);
        if (withTitle){
            bundle.putString(PropertyConst.ARG_PROPERTY_TITLE, getBundleTitle(propertyId));
        }
        return bundle;
    }

    public static Bundle createEditBundle(long propertyId){
        return createBundle(propertyId, true);
    }

    public static Bundle createDetailBundle(long propertyId){
        return createBundle(propertyId, false);
    }
}
