package com.openclassrooms.realestatemanager.data.room.sample;

import com.openclassrooms.realestatemanager.data.room.model.Photo;

public class SamplePhoto {
    public Photo[] getSample(){
        return new Photo[]{
                new Photo(1, "", "photo 1", 1),
                new Photo(2, "", "photo 2", 1),
                new Photo(3, "", "photo 3", 1),
                new Photo(4, "", "photo 4", 1),
                new Photo(5, "", "photo 5", 2),
                new Photo(6, "", "photo 6", 2),
                new Photo(7, "", "photo 7", 2),
                new Photo(8, "", "photo 8", 3),
        };
    }
}
