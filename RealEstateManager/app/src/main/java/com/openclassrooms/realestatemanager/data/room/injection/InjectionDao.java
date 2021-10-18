package com.openclassrooms.realestatemanager.data.room.injection;

import android.app.Application;

import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;

public class InjectionDao {

    // create DatabaseRepository with all table
    public static DatabaseRepository getDatabaseRepository(Application application) {
        return new DatabaseRepository(new AgentRepository(application),
                new PhotoRepository(application),
                new PropertyRepository(application),
                new PropertyTypeRepository(application));
    }
}
