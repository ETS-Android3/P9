package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public LiveData<Long> getFirstPropertyIdLiveData() {return propertyDao.getFirstPropertyIdLiveData();}
    public LiveData<Long> getIsIdExistLiveData(Long id) {return propertyDao.getIsIdExistLiveData(id);}

    // return a long. This is the newly generated ID
    public long insert(Property property) throws ExecutionException, InterruptedException {
        Log.d(Tag.TAG, "insert() called with: property = [" + property + "]");
        Callable<Long> callable = new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                return propertyDao.insert(property);
            }
        };

        Future<Long> future = AppDatabase.getExecutor().submit(callable);
        Long id = future.get();

        return  id;
    }

    // Return the number of updated rows
    public int update(Property property) throws ExecutionException, InterruptedException {
        Log.d(Tag.TAG, "update() called with: property = [" + property + "]");
        Callable<Integer> callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return propertyDao.update(property);
            }
        };

        Future<Integer> future = AppDatabase.getExecutor().submit(callable);
        int count = future.get();

        return count;
    }

    public void delete(long id) {
        AppDatabase.getExecutor().execute(() -> {
            propertyDao.delete(id);
        });
    }

    public LiveData<List<PropertyLocationData>> getOtherPropertiesLocationById(long id){
        return propertyDao.getOtherPropertiesLocationById(id);
    }

    public LiveData<PropertyDetailData> getPropertyDetailByIdLiveData(long id) {
        return propertyDao.getPropertyDetailById(id);
    }
}
