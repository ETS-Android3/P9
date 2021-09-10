package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

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
    @TypeConverters(DateConverter.class)
    private Date entryDate;
    @TypeConverters(DateConverter.class)
    private Date saleDate;
    private long propertyTypeId;
    private long propertyCategoryId;
    private long agentId;

    public Property(long id, int price, int surface, String description, String address, String pointsOfInterest, boolean available, Date entryDate, Date saleDate, long propertyTypeId, long propertyCategoryId, long agentId) {
        this.id = id;
        this.price = price;
        this.surface = surface;
        this.description = description;
        this.address = address;
        this.pointsOfInterest = pointsOfInterest;
        this.available = available;
        this.entryDate = entryDate;
        this.saleDate = saleDate;
        this.propertyTypeId = propertyTypeId;
        this.propertyCategoryId = propertyCategoryId;
        this.agentId = agentId;
    }

    public long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getSurface() {
        return surface;
    }

    public String getDescription() {
        return description;
    }

    public String getAddress() {
        return address;
    }

    public String getPointsOfInterest() {
        return pointsOfInterest;
    }

    public boolean isAvailable() {
        return available;
    }

    public Date getEntryDate() {
        return entryDate;
    }

    public Date getSaleDate() {
        return saleDate;
    }

    public long getPropertyTypeId() {
        return propertyTypeId;
    }

    public long getPropertyCategoryId() {
        return propertyCategoryId;
    }

    public long getAgentId() {
        return agentId;
    }
}
