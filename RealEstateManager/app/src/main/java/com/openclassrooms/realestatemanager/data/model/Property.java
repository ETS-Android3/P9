package com.openclassrooms.realestatemanager.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(foreignKeys = {
        @ForeignKey(entity = Agent.class, parentColumns = "id", childColumns = "agentId"),
        @ForeignKey(entity = PropertyCategory.class, parentColumns = "id", childColumns = "propertyCategoryId"),
        @ForeignKey(entity = PropertyType.class, parentColumns = "id", childColumns = "propertyTypeId")
    })
public class Property {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private int price;
    private int surface;
    private String description;
    private String address;
    private String pointsOfInterest;
    private boolean available;
    private Date entryDate;
    private Date saleDate;
    private long propertyTypeId;
    private long propertyCategoryId;
    private long agentId;
}
