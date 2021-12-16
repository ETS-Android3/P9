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
import com.openclassrooms.realestatemanager.data.room.dao.PropertyDao;
import com.openclassrooms.realestatemanager.data.room.dao.PropertyTypeDao;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.sample.SampleAgent;
import com.openclassrooms.realestatemanager.data.room.sample.SamplePhoto;
import com.openclassrooms.realestatemanager.data.room.sample.SampleProperty;
import com.openclassrooms.realestatemanager.data.room.sample.SamplePropertyType;
import com.openclassrooms.realestatemanager.tag.Tag;

import java.text.ParseException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Agent.class,
                    Photo.class,
                    PropertyType.class,
                    Property.class},
                    version = 7 )
public abstract class AppDatabase extends RoomDatabase {
    private static final String DB_NAME = "RealEstate.db";

    // --- SINGLETON ---
    private static volatile AppDatabase instance;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static ExecutorService getExecutor() {
        return databaseWriteExecutor;
    }

    // --- DAO ---
    public abstract AgentDao agentDao();
    public abstract PhotoDao photoDao();
    public abstract PropertyDao propertyDao();
    public abstract PropertyTypeDao propertyTypeDao();

    // --- INSTANCE ---
    public static AppDatabase getInstance(Application application) {

        if (instance == null) {
            synchronized (AppDatabase.class) {
                if (instance == null) {
                    instance = create(application);
                }
            }
        }
        return instance;
    }

    private static AppDatabase create(Application application){
        Log.d(Tag.TAG, "create() called with: application");
        Builder<AppDatabase> builder = Room.databaseBuilder(application.getApplicationContext(),
                    AppDatabase.class, DB_NAME)
                    //.createFromAsset("RealEstate.db");
                    .addCallback(prepopulateDatabase(application));

        if (BuildConfig.DEBUG) {
            builder.fallbackToDestructiveMigration();
        }
        return builder.build();
    }

    private static Callback prepopulateDatabase(Application application){
        Log.d(Tag.TAG, "prepopulateDatabase()");
        return new Callback() {

            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                Log.d(Tag.TAG, "prepopulateDatabase() 2");
                databaseWriteExecutor.execute(() -> {
                    Log.d(Tag.TAG, "prepopulateDatabase() 3");
                    populateAgents(application);
                    populatePropertyType(application);
                    try {
                        populateProperty(application);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    populatePhoto(application);
                });
            }
        };
    }

    private static void populateAgents(Application application) {
        Log.d(Tag.TAG, "populateAgents()");
        SampleAgent sampleAgent = new SampleAgent();
        for (Agent agent : sampleAgent.getSample()){
            // use insert Dao instead db with ContentValue
            AppDatabase.getInstance(application).agentDao().insert(agent);
        }
    }

    private static void populatePropertyType(Application application){
        SamplePropertyType samplePropertyType = new SamplePropertyType();
        for (PropertyType propertyType : samplePropertyType.getSample()){
            AppDatabase.getInstance(application).propertyTypeDao().insert(propertyType);
        }
    }

    private static void populateProperty(Application application) throws ParseException {
        SampleProperty sampleProperty = new SampleProperty();
        for (Property property : sampleProperty.getSample()){
            AppDatabase.getInstance(application).propertyDao().insert(property);
        }
    }

    private static void populatePhoto(Application application) {
        SamplePhoto samplePhoto = new SamplePhoto();
        for (Photo photo : samplePhoto.getSample()){
            AppDatabase.getInstance(application).photoDao().insert(photo);
        }
    }

}
