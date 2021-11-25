package com.openclassrooms.realestatemanager;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import androidx.room.Room;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.openclassrooms.realestatemanager.data.provider.AgentContentProvider;
import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.tag.Tag;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    private ContentResolver mContentResolver;
    private static long AGENT_ID = 1;

    @Before
    public void setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext().getContentResolver();
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        assertEquals("com.openclassrooms.realestatemanager", appContext.getPackageName());
    }

    //
    @Test
    public void getAgent(){
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(AgentContentProvider.URI_AGENT, AGENT_ID),
                null, null, null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                long id = cursor.getLong(0);
                String name = cursor.getString(1);
                Log.d(Tag.TAG, "getAgent.id: " + id + " name = " + name);
                while (cursor.moveToNext()) {
                    id = cursor.getLong(0);
                    name = cursor.getString(1);
                    Log.d(Tag.TAG, "getAgent.id: " + id + " name = " + name);
                }
            }
        }

        assertNotNull(cursor);
    }
}
