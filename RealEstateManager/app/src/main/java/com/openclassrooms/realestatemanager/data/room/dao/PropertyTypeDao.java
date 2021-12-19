package com.openclassrooms.realestatemanager.data.room.dao;

import android.database.Cursor;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

import java.util.List;

@Dao
public interface PropertyTypeDao {
    @Query("SELECT * FROM property_type ORDER BY id")
    List<PropertyType> getPropertyTypes();

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    long insert(PropertyType propertyType);

    @Update
    int update(PropertyType propertyType);

    @Query("DELETE FROM property_type WHERE id = :id")
    int delete(long id);

    @Query("SELECT * FROM property_type WHERE id = :id")
    Cursor getPropertyTypeByIdWithCursor(long id);

    @Query("SELECT * FROM property_type ORDER BY id")
    Cursor getPropertyTypesWithCursor();
}

