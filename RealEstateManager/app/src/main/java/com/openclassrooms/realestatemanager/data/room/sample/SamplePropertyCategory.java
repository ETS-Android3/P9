package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;

public class SamplePropertyCategory {
    public PropertyCategory[] getSample(){
        return new PropertyCategory[]{
                new PropertyCategory(1, "For sale"),
                new PropertyCategory(2, "For rent")
        };
    }
}
