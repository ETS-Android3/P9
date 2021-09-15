package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.Photo;

import java.util.List;

public class PhotoRepository {
    private final PhotoDao photoDao;

    public PhotoRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        this.photoDao = db.photoDao();
    }

    public LiveData<List<Photo>> getPhotos() {return photoDao.getPhotos();}
    public LiveData<List<Photo>> getPhotosByPropertyId(long id) {return photoDao.getPhotosByPropertyId(id);}

    public void insert(Photo photo) {
        AppDatabase.getExecutor().execute(() -> {
            photoDao.insert(photo);
        });
    }
    public void update(Photo photo) {
        AppDatabase.getExecutor().execute(() -> {
            photoDao.update(photo);
        });
    }

    public void delete(long id) {
        AppDatabase.getExecutor().execute(() -> {
            photoDao.delete(id);
        });
    }
}
