package com.openclassrooms.realestatemanager.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.room.model.Photo;

import java.util.List;

@Dao
public interface PhotoDao {
    @Query("SELECT * FROM photo ORDER BY photo.id")
    LiveData<List<Photo>> getPhotos();

    @Query("SELECT * FROM photo WHERE photo.propertyId = :propertyId ORDER BY photo.id")
    LiveData<List<Photo>> getPhotosByPropertyId(long propertyId);

    @Insert
    long insert(Photo photo);

    @Update
    int update(Photo photo);

    @Query("DELETE FROM photo WHERE photo.id = :id")
    int delete(long id);
}
