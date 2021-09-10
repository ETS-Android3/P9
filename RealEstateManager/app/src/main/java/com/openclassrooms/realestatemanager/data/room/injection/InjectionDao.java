package com.openclassrooms.realestatemanager.data.room.injection;

import android.app.Application;

import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyCategoryRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;

import java.util.concurrent.Executor;

public class InjectionDao {

    public static AgentRepository getAgentRepositiory(Application application, Executor executor) {
        return new AgentRepository(AppDatabase.getInstance(application, executor).getAgentDao());
    }

    public static PhotoRepository getPhotoRepositiory(Application application, Executor executor) {
        return new PhotoRepository(AppDatabase.getInstance(application, executor).getPhotoDao());
    }

    public static PropertyCategoryRepository getPropertyCategoryRepositiory(Application application, Executor executor) {
        return new PropertyCategoryRepository(AppDatabase.getInstance(application, executor).getPropertyCategoryDao());
    }

    public static PropertyRepository getPropertyRepositiory(Application application, Executor executor) {
        return new PropertyRepository(AppDatabase.getInstance(application, executor).getPropertyDao());
    }

    public static PropertyTypeRepository getPropertyTypeRepositiory(Application application, Executor executor) {
        return new PropertyTypeRepository(AppDatabase.getInstance(application, executor).getPropertyTypeDao());
    }

    
}
