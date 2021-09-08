package com.openclassrooms.realestatemanager.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.model.PropertyType;

import java.util.List;

@Dao
public interface PropertyTypeDao {
    @Query("SELECT * FROM propertytype ORDER BY propertytype.id")
    LiveData<List<PropertyType>> getPropertyTypes();

    @Insert
    long insert(PropertyType propertyType);

    @Update
    int update(PropertyType propertyType);

    @Query("DELETE FROM propertytype WHERE propertytype.id = :id")
    int delete(long id);
}

