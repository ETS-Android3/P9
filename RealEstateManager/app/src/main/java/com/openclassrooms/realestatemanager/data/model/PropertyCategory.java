package com.openclassrooms.realestatemanager.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 1 = for sale
 * 2 = for rent
 */
@Entity
public class PropertyCategory {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
}
