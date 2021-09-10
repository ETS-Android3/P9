package com.openclassrooms.realestatemanager.data.room.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyCategoryDao;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;

import java.util.List;

public class PropertyCategoryRepository {
    private final PropertyCategoryDao propertyCategoryDao;

    public PropertyCategoryRepository(PropertyCategoryDao propertyCategoryDao){
        this.propertyCategoryDao = propertyCategoryDao;
    }

    public LiveData<List<PropertyCategory>> getPropertyCategorys() {return propertyCategoryDao.getPropertyCategorys();}
    public void insert(PropertyCategory propertyCategory) {propertyCategoryDao.insert(propertyCategory);}
    public void update(PropertyCategory propertyCategory) {propertyCategoryDao.update(propertyCategory);}
    public void delete(long id) {propertyCategoryDao.delete(id);}
}
