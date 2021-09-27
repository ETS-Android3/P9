package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.tag.Tag;

import java.util.List;

public class PropertyRepository {
    private final PropertyDao propertyDao;

    public PropertyRepository(Application application){
        this.propertyDao = AppDatabase.getInstance(application).propertyDao();
    }

    public LiveData<List<Property>> getProperties() {return propertyDao.getProperties();}
    public LiveData<Property> getPropertyById(long id) {return propertyDao.getPropertyById(id);}
    public Long getFirstPropertyId() {
        Log.d(Tag.TAG, "*** getFirstPropertyId() called");
        return propertyDao.getFirstPropertyId();
    }

    public void insert(Property property) {
        AppDatabase.getExecutor().execute(() -> {
            propertyDao.insert(property);
        });
    }

    public void update(Property property) {
        AppDatabase.getExecutor().execute(() -> {
            propertyDao.update(property);
        });
    }

    public void delete(long id) {
        AppDatabase.getExecutor().execute(() -> {
            propertyDao.delete(id);
        });
    }
}
