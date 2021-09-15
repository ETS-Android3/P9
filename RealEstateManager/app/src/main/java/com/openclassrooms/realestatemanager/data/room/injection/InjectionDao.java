package com.openclassrooms.realestatemanager.data.room.injection;

import android.app.Application;

import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyCategoryRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;

import java.util.concurrent.Executor;

public class InjectionDao {

    // create DatabaseRepository with all table
    public static DatabaseRepository getDatabaseRepository(Application application) {
        return new DatabaseRepository(new AgentRepository(application),
                new PhotoRepository(application),
                new PropertyCategoryRepository(application),
                new PropertyRepository(application),
                new PropertyTypeRepository(application));
    }
}
