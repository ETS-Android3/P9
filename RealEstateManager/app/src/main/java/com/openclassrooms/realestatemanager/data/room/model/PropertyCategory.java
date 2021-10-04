package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 1 = for sale
 * 2 = for rent
 */
@Entity(tableName = "property_category")
public class PropertyCategory {
    @PrimaryKey(autoGenerate = true)
    private final long id;
    private final String name;

    public PropertyCategory(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
