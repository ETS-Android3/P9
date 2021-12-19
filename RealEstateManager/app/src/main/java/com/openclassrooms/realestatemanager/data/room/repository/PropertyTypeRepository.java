package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;
import android.database.Cursor;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class PropertyTypeRepository {
    private final PropertyTypeDao propertyTypeDao;

    public PropertyTypeRepository(Application application){
        this.propertyTypeDao = AppDatabase.getInstance(application).propertyTypeDao();
    }

    public List<PropertyType> getPropertyTypes(){
        Callable<List<PropertyType>> callable = propertyTypeDao::getPropertyTypes;

        List<PropertyType> propertyTypes = new ArrayList<>();
        Future<List<PropertyType>> future = AppDatabase.getExecutor().submit(callable);

        try {
            List<PropertyType> list = future.get();
            propertyTypes.addAll(list);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return propertyTypes;
    }

    public LiveData<List<PropertyType>> getPropertyTypesLiveData() {
        MutableLiveData<List<PropertyType>> mutableLiveData = new MutableLiveData<>();
        mutableLiveData.setValue(getPropertyTypes());
        return mutableLiveData;
    }

    public void insert(PropertyType propertyType) {
        AppDatabase.getExecutor().execute(() -> propertyTypeDao.insert(propertyType));
    }

    public void update(PropertyType propertyType) {
        AppDatabase.getExecutor().execute(() -> propertyTypeDao.update(propertyType));
    }

    public void delete(long id) {
        AppDatabase.getExecutor().execute(() -> propertyTypeDao.delete(id));
    }

    public Cursor getPropertyTypeByIdWithCursor(long id){
        return propertyTypeDao.getPropertyTypeByIdWithCursor(id);
    }

    public Cursor getPropertyTypesWithCursor(){
        return propertyTypeDao.getPropertyTypesWithCursor();
    }
}
