package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 1 = House
 * 2 = Apartment
 * 3 = Loft
 * 4 = Manor
 * 5 = Condominium
 * 6 = Townhouse
 */
@Entity
public class PropertyType {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;

    public PropertyType(long id, String name) {
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
