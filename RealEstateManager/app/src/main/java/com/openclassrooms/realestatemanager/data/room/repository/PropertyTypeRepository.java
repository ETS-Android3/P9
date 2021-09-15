package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

import java.util.List;

public class PropertyTypeRepository {
    private final PropertyTypeDao propertyTypeDao;

    public PropertyTypeRepository(Application application){
        this.propertyTypeDao = AppDatabase.getInstance(application).propertyTypeDao();
    }

    public LiveData<List<PropertyType>> getPropertyTypes() {return propertyTypeDao.getPropertyTypes();}
    public LiveData<PropertyType> getPropertyTypeById(long id) {return propertyTypeDao.getPropertyTypeById(id);}

    public void insert(PropertyType propertyType) {
        AppDatabase.getExecutor().execute(() -> {
            propertyTypeDao.insert(propertyType);
        });
    }

    public void update(PropertyType propertyType) {
        AppDatabase.getExecutor().execute(() -> {
            propertyTypeDao.update(propertyType);
        });
    }

    public void delete(long id) {
        AppDatabase.getExecutor().execute(() -> {
            propertyTypeDao.delete(id);
        });
    }
}
