package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Agent {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;

    public Agent(long id, String name) {
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
