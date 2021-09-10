package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.Photo;

public class SamplePhoto {
    public Photo[] getSample(){
        return new Photo[]{
                new Photo(1, "", "photo 1", 1),
                new Photo(1, "", "photo 2", 1),
                new Photo(1, "", "photo 3", 1),
                new Photo(1, "", "photo 4", 2),
                new Photo(1, "", "photo 5", 2),
                new Photo(1, "", "photo 6", 2),
                new Photo(1, "", "photo 7", 3),
                new Photo(1, "", "photo 8", 3),
        };
    }
}
