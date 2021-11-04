package com.openclassrooms.realestatemanager.ui.photoList;

import android.view.View;

import com.openclassrooms.realestatemanager.data.room.model.Photo;

public interface OnRowPhotoListener {
    void onClickRowPhoto(Photo photo);
    void onLongClickRowPhoto(View view, Photo photo);
}
