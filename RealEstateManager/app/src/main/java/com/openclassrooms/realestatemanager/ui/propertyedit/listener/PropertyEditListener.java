package com.openclassrooms.realestatemanager.ui.propertyedit.listener;

public interface PropertyEditListener {
    public void onCancel(long propertyId);
    public void onValidate(long propertyId);
    public void onSell(long propertyId);
}
