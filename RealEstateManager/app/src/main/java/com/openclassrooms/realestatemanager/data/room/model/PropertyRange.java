package com.openclassrooms.realestatemanager.data.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;

@Entity
public class PropertyRange {
    @ColumnInfo(name = "min_price")
    private final int minPrice;
    @ColumnInfo(name = "max_price")
    private final int maxPrice;
    @ColumnInfo(name = "min_surface")
    private final int minSurface;
    @ColumnInfo(name = "max_surface")
    private final int maxSurface;
    @ColumnInfo(name = "min_rooms")
    private final int minRooms;
    @ColumnInfo(name = "max_rooms")
    private final int maxRooms;

    public PropertyRange(int minPrice, int maxPrice, int minSurface, int maxSurface, int minRooms, int maxRooms) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minSurface = minSurface;
        this.maxSurface = maxSurface;
        this.minRooms = minRooms;
        this.maxRooms = maxRooms;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public int getMinSurface() {
        return minSurface;
    }

    public int getMaxSurface() {
        return maxSurface;
    }

    public int getMinRooms() {
        return minRooms;
    }

    public int getMaxRooms() {
        return maxRooms;
    }
}
