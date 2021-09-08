package com.openclassrooms.realestatemanager.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Photo {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String url;
    private String legend;
    private long propertyId;
}
