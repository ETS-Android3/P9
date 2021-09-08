package com.openclassrooms.realestatemanager.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Agent {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
}
