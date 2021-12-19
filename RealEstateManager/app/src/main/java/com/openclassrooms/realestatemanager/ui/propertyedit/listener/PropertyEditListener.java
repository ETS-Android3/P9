package com.openclassrooms.realestatemanager.ui.propertyedit.listener;

public interface PropertyEditListener {
    void onCancelEditProperty(long propertyId);
    void onValidateEditProperty(long propertyId);
}
