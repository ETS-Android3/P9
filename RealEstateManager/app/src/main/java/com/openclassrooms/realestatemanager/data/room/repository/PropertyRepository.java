package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;
import com.openclassrooms.realestatemanager.tag.Tag;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PropertyRepository {
    private final PropertyDao propertyDao;

    public PropertyRepository(Application application){
        this.propertyDao = AppDatabase.getInstance(application).propertyDao();
    }

    public LiveData<List<Property>> getProperties() {return propertyDao.getProperties();}
    public LiveData<Property> getPropertyById(long id) {return propertyDao.getPropertyById(id);}

    // use it with asynchronous mode like ExecutorService
    public Long getFirstPropertyId() throws ExecutionException, InterruptedException {
        Log.d(Tag.TAG, "PropertyRepository.getFirstPropertyId() called");
        Callable<Long> callable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return propertyDao.getFirstPropertyId();
            }
        };

        Future<Long> future = AppDatabase.getExecutor().submit(callable);
        Long id = future.get();
        return id;
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

    public LiveData<PropertyDetailData> getPropertyDetailById(long id) {
        return propertyDao.getPropertyDetailById(id);
    }

    public LiveData<List<PropertyLocationData>> getOtherPropertiesLocationById(long id){
        return propertyDao.getOtherPropertiesLocationById(id);
    }
}
