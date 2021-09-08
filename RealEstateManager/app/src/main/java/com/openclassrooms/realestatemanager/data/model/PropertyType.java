package com.openclassrooms.realestatemanager.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 0 = HOUSE
 * 1 = APARTMENT
 * 2 = LOFT
 * 3 = MANOR
 */
@Entity
public class PropertyType {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
}
