package com.openclassrooms.realestatemanager.ui.photoedit;

import com.openclassrooms.realestatemanager.data.room.model.Photo;

public interface OnPhotoEditListener {
    void onPhotoEditOk(long id, int order, String url, String caption, long propertyId);
    void onPhotoEditCancel();
}
