package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.tag.Tag;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ContentProviderInstrumentedTest {

    private ContentResolver mContentResolver;
    private final static long AGENT_ID = 1;
    private final static long PHOTO_ID = 1;
    private final static long PROPERTY_ID = 1;
    private final static long PROPERTY_TYPE_ID = 1;

    public static final String AUTHORITY = "com.openclassrooms.realestatemanager.provider.data";

    private Uri getUriFromTable(String tableName){
        return Uri.parse("content://" + AUTHORITY + "/" + tableName);
    }

    private Uri getUriFromTableWithId(String tableName, long id){
        return ContentUris.withAppendedId(getUriFromTable(tableName), id);
    }

    public Uri getUriAgent(){
        return getUriFromTable(Agent.class.getSimpleName());
    }

    public Uri getUriAgentWithId(long id){
        return getUriFromTableWithId(Agent.class.getSimpleName(), id);
    }

    public Uri getUriPhoto(){
        return getUriFromTable(Photo.class.getSimpleName());
    }

    public Uri getUriPhotoWithId(long id){
        return getUriFromTableWithId(Photo.class.getSimpleName(), id);
    }

    public Uri getUriProperty(){
        return getUriFromTable(Property.class.getSimpleName());
    }

    public Uri getUriPropertyWithId(long id){
        return getUriFromTableWithId(Property.class.getSimpleName(), id);
    }

    public Uri getUriPropertyType(){
        return getUriFromTable(PropertyType.class.getSimpleName());
    }

    public Uri getUriPropertyTypeWithId(long id){
        return getUriFromTableWithId(PropertyType.class.getSimpleName(), id);
    }

    @Before
    public void setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    private void logRow(Cursor cursor) {
        if (cursor != null) {
            int count = cursor.getColumnCount();
            for (int i=0; i<count; i++){
                String name = cursor.getColumnName(i);
                String value = cursor.getString(i);
                Log.d(Tag.TAG, "logRow() name = [" + name + "] value = [" + value + "]");
            }
        }
    }

    private void logCursor(Cursor cursor){
        // for debug
        if (cursor == null) {
            Log.d(Tag.TAG, "logCursor() called with: cursor = null");
        } else {
            switch (cursor.getCount()) {
                case 0:
                    Log.d(Tag.TAG, "logCursor() called with: cursor count = 0");
                    return;
                case 1:
                    Log.d(Tag.TAG, "logCursor() called with: cursor count = 1");
                    cursor.moveToFirst();
                    logRow(cursor);
                    return;
                default:
                    Log.d(Tag.TAG, "logCursor() called with: cursor count = " + cursor.getCount());
                    cursor.moveToFirst();
                    logRow(cursor);
                    while (cursor.moveToNext()) {
                        logRow(cursor);
                    }
            }
        }
    }

    @Test
    public void getAgents(){
        final Cursor cursor = mContentResolver.query(getUriAgent(),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getAgentById(){
        final Cursor cursor = mContentResolver.query(getUriAgentWithId(AGENT_ID),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getPhotos(){
        final Cursor cursor = mContentResolver.query(getUriPhoto(),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getPhotoById(){
        final Cursor cursor = mContentResolver.query(getUriPhotoWithId(PHOTO_ID),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getProperties(){
        final Cursor cursor = mContentResolver.query(getUriProperty(),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getPropertyById(){
        final Cursor cursor = mContentResolver.query(getUriPropertyWithId(PROPERTY_ID),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getPropertyTypes(){
        final Cursor cursor = mContentResolver.query(getUriPropertyType(),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getPropertyTypeById(){
        final Cursor cursor = mContentResolver.query(getUriPropertyTypeWithId(PROPERTY_TYPE_ID),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

}
