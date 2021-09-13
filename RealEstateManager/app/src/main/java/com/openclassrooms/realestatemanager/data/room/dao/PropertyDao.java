package com.openclassrooms.realestatemanager.data.room.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.room.model.Property;

import java.util.List;

@Dao
public interface PropertyDao {
    @Query("SELECT * FROM property ORDER BY property.id")
    LiveData<List<Property>> getProperties();

    @Insert
    long insert(Property property);

    @Update
    int update(Property property);

    @Query("DELETE FROM property WHERE property.id = :id")
    int delete(long id);
}
