package com.openclassrooms.realestatemanager.data.room.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

import java.util.List;

public class PropertyTypeRepository {
    private final PropertyTypeDao propertyTypeDao;

    public PropertyTypeRepository(PropertyTypeDao propertyTypeDao){
        this.propertyTypeDao = propertyTypeDao;
    }

    public LiveData<List<PropertyType>> getPropertyTypes() {return propertyTypeDao.getPropertyTypes();}
    public LiveData<PropertyType> getPropertyTypeById(long id) {return propertyTypeDao.getPropertyTypeById(id);}
    public void insert(PropertyType propertyType) {propertyTypeDao.insert(propertyType);}
    public void update(PropertyType propertyType) {propertyTypeDao.update(propertyType);}
    public void delete(long id) {propertyTypeDao.delete(id);}
}
