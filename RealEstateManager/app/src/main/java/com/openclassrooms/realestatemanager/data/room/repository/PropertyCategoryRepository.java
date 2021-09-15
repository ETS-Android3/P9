package com.openclassrooms.realestatemanager.data.room.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyCategoryDao;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;

import java.util.List;

public class PropertyCategoryRepository {
    private final PropertyCategoryDao propertyCategoryDao;

    public PropertyCategoryRepository(Application application){
        AppDatabase db = AppDatabase.getInstance(application);
        this.propertyCategoryDao = db.propertyCategoryDao();
    }

    public LiveData<List<PropertyCategory>> getCategories() {return propertyCategoryDao.getCategories();}
    public LiveData<PropertyCategory> getCategoryById(long id) {return propertyCategoryDao.getCategoryById(id);}

    public void insert(PropertyCategory propertyCategory) {
        AppDatabase.getExecutor().execute(() -> {
            propertyCategoryDao.insert(propertyCategory);
        });
    }

    public void update(PropertyCategory propertyCategory) {
        AppDatabase.getExecutor().execute(() -> {
            propertyCategoryDao.update(propertyCategory);
        });
    }

    public void delete(long id) {
        AppDatabase.getExecutor().execute(() -> {
            propertyCategoryDao.delete(id);
        });
    }
}
