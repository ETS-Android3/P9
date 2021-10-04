package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity(tableName = "property",
        foreignKeys = {
        @ForeignKey(entity = Agent.class, parentColumns = "id", childColumns = "agent_id"),
        @ForeignKey(entity = PropertyCategory.class, parentColumns = "id", childColumns = "property_category_id"),
        @ForeignKey(entity = PropertyType.class, parentColumns = "id", childColumns = "property_type_id")
    })
public class Property {
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = true)
    private final long id;
    @ColumnInfo(name = "price")
    private final int price;
    @ColumnInfo(name = "surface")
    private final int surface;
    @ColumnInfo(name = "description")
    private final String description;
    @ColumnInfo(name = "address_title")
    private final String addressTitle;
    @ColumnInfo(name = "address")
    private final String address;
    @ColumnInfo(name = "points_of_interest")
    private final String pointsOfInterest;
    @ColumnInfo(name = "available")
    private final boolean available;
    @ColumnInfo(name = "entry_date")
    @TypeConverters(DateConverter.class)
    private final Date entryDate;
    @ColumnInfo(name = "sale_date")
    @TypeConverters(DateConverter.class)
    private final Date saleDate;
    @ColumnInfo(name = "property_type_id", index = true)
    private final long propertyTypeId;
    @ColumnInfo(name = "property_category_id", index = true)
    private final long propertyCategoryId;
    @ColumnInfo(name = "agent_id", index = true)
    private final long agentId;
    @ColumnInfo(name = "rooms")
    private final int rooms;
    @ColumnInfo(name = "latitude")
    private final double latitude;
    @ColumnInfo(name = "longitude")
    private final double longitude;

    public Property(long id, int price, int surface, String description, String addressTitle, String address, String pointsOfInterest, boolean available, Date entryDate, Date saleDate, long propertyTypeId, long propertyCategoryId, long agentId, int rooms, double latitude, double longitude) {
        this.id = id;
        this.price = price;
        this.surface = surface;
        this.description = description;
        this.addressTitle = addressTitle;
        this.address = address;
        this.pointsOfInterest = pointsOfInterest;
        this.available = available;
        this.entryDate = entryDate;
        this.saleDate = saleDate;
        this.propertyTypeId = propertyTypeId;
        this.propertyCategoryId = propertyCategoryId;
        this.agentId = agentId;
        this.rooms = rooms;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public String getAddressTitle() {
        return addressTitle;
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

    public int getRooms() {
        return rooms;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
