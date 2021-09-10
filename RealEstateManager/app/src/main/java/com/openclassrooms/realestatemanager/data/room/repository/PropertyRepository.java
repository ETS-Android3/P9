package com.openclassrooms.realestatemanager.data.room.repository;

import androidx.lifecycle.LiveData;

import com.openclassrooms.realestatemanager.data.room.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.room.model.Property;

import java.util.List;

public class PropertyRepository {
    private final PropertyDao propertyDao;

    public PropertyRepository(PropertyDao propertyDao){
        this.propertyDao = propertyDao;
    }

    public LiveData<List<Property>> getPropertys() {return propertyDao.getPropertys();}
    public void insert(Property property) {propertyDao.insert(property);}
    public void update(Property property) {propertyDao.update(property);}
    public void delete(long id) {propertyDao.delete(id);}
}
