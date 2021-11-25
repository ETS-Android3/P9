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

    private void logAgent(Cursor cursor) {
        if (cursor != null) {
            long id = cursor.getLong(0);
            String name = cursor.getString(1);
            Log.d(Tag.TAG, "getAgent.id: " + id + " name = " + name);
        }
    }

    private void logCursor(Cursor cursor){
        // for debug
        if (cursor == null) {
            Log.d(Tag.TAG, "logCursor() called with: cursor = [" + cursor + "]");
        } else {
            switch (cursor.getCount()) {
                case 0:
                    Log.d(Tag.TAG, "logCursor() called with: cursor count = 0");
                    return;
                case 1:
                    Log.d(Tag.TAG, "logCursor() called with: cursor count = 1");
                    cursor.moveToFirst();
                    logAgent(cursor);
                    return;
                default:
                    Log.d(Tag.TAG, "logCursor() called with: cursor count = " + cursor.getCount());
                    cursor.moveToFirst();
                    logAgent(cursor);
                    while (cursor.moveToNext()) {
                        logAgent(cursor);
                    }
                    return;
            }
        }
    }


    @Test
    public void getAgents(){
        final Cursor cursor = mContentResolver.query(AgentContentProvider.URI_AGENT,
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
    }

    @Test
    public void getAgentById(){
        final Cursor cursor = mContentResolver.query(ContentUris.withAppendedId(AgentContentProvider.URI_AGENT, AGENT_ID),
                null, null, null, null);
        logCursor(cursor);
        assertNotNull(cursor);
        long id = cursor.getLong(0);
        assertEquals(AGENT_ID, id);
    }
}
