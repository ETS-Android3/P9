package com.openclassrooms.realestatemanager.data.room.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.room.model.Photo;

import java.util.List;

public class PhotoRepository {
    private final PhotoDao photoDao;

    public PhotoRepository(PhotoDao photoDao){
        this.photoDao = photoDao;
    }

    public LiveData<List<Photo>> getPhotos() {return photoDao.getPhotos();}
    public LiveData<List<Photo>> getPhotosByPropertyId(long id) {return photoDao.getPhotosByPropertyId(id);}
    public void insert(Photo photo) {photoDao.insert(photo);}
    public void update(Photo photo) {photoDao.update(photo);}
    public void delete(long id) {photoDao.delete(id);}
}
