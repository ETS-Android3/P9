package com.openclassrooms.realestatemanager.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;

import java.util.List;

public class DataContentProvider extends ContentProvider {

/*    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider.data";
    public static final String TABLE_NAME = Agent.class.getSimpleName();
    public static final Uri URI_AGENT = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);*/

    @Override
    public boolean onCreate() {
        return false;
    }

    private Cursor getCursorAgentById(Uri uri, long id) {
        final AgentRepository agentRepository = new AgentRepository(MainApplication.getApplication());
        final Cursor cursor = agentRepository.getAgentByIdWithCursor(id);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorAgents(Uri uri) {
        final AgentRepository agentRepository = new AgentRepository(MainApplication.getApplication());
        final Cursor cursor = agentRepository.getAgentsWithCursor();
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorPhotoById(Uri uri, long id) {
        final PhotoRepository photoRepository = new PhotoRepository(MainApplication.getApplication());
        final Cursor cursor = photoRepository.getPhotoByIdWithCursor(id);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorPhotos(Uri uri) {
        final PhotoRepository photoRepository = new PhotoRepository(MainApplication.getApplication());
        final Cursor cursor = photoRepository.getPhotosWithCursor();
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorPropertyById(Uri uri, long id) {
        final PropertyRepository propertyRepository = new PropertyRepository(MainApplication.getApplication());
        final Cursor cursor = propertyRepository.getPropertyByIdWithCursor(id);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorProperties(Uri uri) {
        final PropertyRepository propertyRepository = new PropertyRepository(MainApplication.getApplication());
        final Cursor cursor = propertyRepository.getPropertiesWithCursor();
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorPropertyTypeById(Uri uri, long id) {
        final PropertyTypeRepository propertyTypeRepository = new PropertyTypeRepository(MainApplication.getApplication());
        final Cursor cursor = propertyTypeRepository.getPropertyTypeByIdWithCursor(id);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    private Cursor getCursorPropertyTypes(Uri uri) {
        final PropertyTypeRepository propertyTypeRepository = new PropertyTypeRepository(MainApplication.getApplication());
        final Cursor cursor = propertyTypeRepository.getPropertyTypesWithCursor();
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (getContext() != null){

            long id = -1;
            boolean withId = false;

            try {
                // get by id
                id = ContentUris.parseId(uri);
                withId = true;
            } catch (IllegalArgumentException exception) {
            }

            List<String> pathSegments = uri.getPathSegments();
            if (pathSegments.size() > 0) {
                String table = pathSegments.get(0).toLowerCase().trim();

                // Agent
                if (table.equals(Agent.class.getSimpleName().toLowerCase())) {
                    if (withId) {
                        return getCursorAgentById(uri, id);
                    } else {
                        return getCursorAgents(uri);
                    }
                }

                // Photo
                if (table.equals(Photo.class.getSimpleName().toLowerCase())) {
                    if (withId) {
                        return getCursorPhotoById(uri, id);
                    } else {
                        return getCursorPhotos(uri);
                    }
                }

                // Property
                if (table.equals(Property.class.getSimpleName().toLowerCase())) {
                    if (withId) {
                        return getCursorPropertyById(uri, id);
                    } else {
                        return getCursorProperties(uri);
                    }
                }

                // Property type
                if (table.equals(PropertyType.class.getSimpleName().toLowerCase())) {
                    if (withId) {
                        return getCursorPropertyTypeById(uri, id);
                    } else {
                        return getCursorPropertyTypes(uri);
                    }
                }
            }
        }

        throw new IllegalArgumentException("Failed to query row for uri " + uri);
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
