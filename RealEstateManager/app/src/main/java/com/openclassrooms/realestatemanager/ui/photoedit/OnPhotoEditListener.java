package com.openclassrooms.realestatemanager.ui.photoedit;

public interface OnPhotoEditListener {
    void onPhotoEditOk(long id, int order, String url, String caption, long propertyId);
    void onPhotoEditCancel();
}
