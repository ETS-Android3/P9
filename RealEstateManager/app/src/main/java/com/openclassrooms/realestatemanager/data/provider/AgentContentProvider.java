package com.openclassrooms.realestatemanager.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;

public class AgentContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider.agent";
    public static final String TABLE_NAME = Agent.class.getSimpleName();
    public static final Uri URI_AGENT = Uri.parse("content://" + AUTHORITY + "/" + TABLE_NAME);

    @Override
    public boolean onCreate() {
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        if (getContext() != null){

            final AgentRepository agentRepository = new AgentRepository(MainApplication.getApplication());

            try {
                long agentId = ContentUris.parseId(uri);
                final Cursor cursor = agentRepository.getAgentByIdWithCursor(agentId);
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
            } catch (IllegalArgumentException exception) {
                final Cursor cursor = agentRepository.getAgentsWithCursor();
                cursor.setNotificationUri(getContext().getContentResolver(), uri);
                return cursor;
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
