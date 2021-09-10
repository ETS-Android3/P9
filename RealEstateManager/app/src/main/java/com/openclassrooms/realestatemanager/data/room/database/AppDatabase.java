package com.openclassrooms.realestatemanager.data.room.database;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.openclassrooms.realestatemanager.BuildConfig;
import com.openclassrooms.realestatemanager.data.room.dao.AgentDao;
import com.openclassrooms.realestatemanager.data.room.dao.PhotoDao;
import com.openclassrooms.realestatemanager.data.room.dao.PropertyCategoryDao;
import com.openclassrooms.realestatemanager.data.room.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.room.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.sample.SampleAgent;
import com.openclassrooms.realestatemanager.data.room.sample.SamplePhoto;
import com.openclassrooms.realestatemanager.data.room.sample.SampleProperty;
import com.openclassrooms.realestatemanager.data.room.sample.SamplePropertyCategory;
import com.openclassrooms.realestatemanager.data.room.sample.SamplePropertyType;
import com.openclassrooms.realestatemanager.tag.Tag;

import java.text.ParseException;
import java.util.concurrent.Executor;

@Database(entities = {Agent.class, Photo.class, PropertyCategory.class, PropertyType.class, Property.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "RealEstate.db";

    // --- SINGLETON ---
    private static volatile AppDatabase instance;

    // --- DAO ---
    public abstract AgentDao getAgentDao();
    public abstract PhotoDao getPhotoDao();
    public abstract PropertyDao getPropertyDao();
    public abstract PropertyCategoryDao getPropertyCategoryDao();
    public abstract PropertyTypeDao getPropertyTypeDao();

    // --- INSTANCE ---
    public static AppDatabase getInstance(Application application, Executor executor) {

        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    int d = Log.d(Tag.TAG, "RealEstateDatabase.getInstance()");
                    instance = create(application, executor);
                }
            }
        }
        return instance;
    }

    private static AppDatabase create(Application application, Executor executor){
        Builder<AppDatabase> builder = Room.databaseBuilder(application.getApplicationContext(),
                    AppDatabase.class, DB_NAME)
                    .addCallback(prepopulateDatabase(application, executor));

/*        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration();
        }*/
        return builder.build();
    }

    private static Callback prepopulateDatabase(Application application, Executor executor){
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);

                executor.execute(() -> {
                    populateAgents(application, executor);
                    populatePropertyCategory(application, executor);
                    populatePropertyType(application, executor);
                    try {
                        populateProperty(application, executor);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    populatePhoto(application, executor);
                });
            }
        };
    }

    private static void populateAgents(Application application, Executor executor) {
        SampleAgent sampleAgent = new SampleAgent();
        for (Agent agent : sampleAgent.getSample()){
            // use insert Dao instead db with ContentValue
            AppDatabase.getInstance(application, executor).getAgentDao().insert(agent);
        }
    }

    private static void populatePropertyCategory(Application application, Executor executor){
        SamplePropertyCategory samplePropertyCategory = new SamplePropertyCategory();
        for (PropertyCategory propertyCategory : samplePropertyCategory.getSample()){
            AppDatabase.getInstance(application, executor).getPropertyCategoryDao().insert(propertyCategory);
        }
    }

    private static void populatePropertyType(Application application, Executor executor){
        SamplePropertyType samplePropertyType = new SamplePropertyType();
        for (PropertyType propertyType : samplePropertyType.getSample()){
            AppDatabase.getInstance(application, executor).getPropertyTypeDao().insert(propertyType);
        }
    }

    private static void populateProperty(Application application, Executor executor) throws ParseException {
        SampleProperty sampleProperty = new SampleProperty();
        for (Property property : sampleProperty.getSample()){
            AppDatabase.getInstance(application, executor).getPropertyDao().insert(property);
        }
    }

    private static void populatePhoto(Application application, Executor executor) {
        SamplePhoto samplePhoto = new SamplePhoto();
        for (Photo photo : samplePhoto.getSample()){
            AppDatabase.getInstance(application, executor).getPhotoDao().insert(photo);
        }
    }

}
