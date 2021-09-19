package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Agent {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private String email;
    private String phone;

    public Agent(long id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
}
