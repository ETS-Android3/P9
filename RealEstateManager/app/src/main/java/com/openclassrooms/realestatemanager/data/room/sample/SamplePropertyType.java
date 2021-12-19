package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

public class SamplePropertyType {
    public PropertyType[] getSample(){
        return new PropertyType[]{
                new PropertyType(1, "House"),
                new PropertyType(2, "Apartment"),
                new PropertyType(3, "Loft"),
                new PropertyType(4, "Manor"),
                new PropertyType(5, "Condominium"),
                new PropertyType(6, "Townhouse"),
        };
    }
}
