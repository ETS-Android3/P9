package com.openclassrooms.realestatemanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.openclassrooms.realestatemanager.data.room.database.AppDatabase;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertySearchParameters;
import com.openclassrooms.realestatemanager.tag.Tag;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

@RunWith(AndroidJUnit4.class)
public class PropertySearchParametersInstrumentedTest {

    DatabaseRepository databaseRepository;

    @Before
    public void setUp(){
        Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                AppDatabase.class)
                .allowMainThreadQueries()
                .build();
        databaseRepository = InjectionDao.getDatabaseRepository(MainApplication.getApplication());
    }

    private interface PropertySearchParametersTestInterface{
        void onSetValue(PropertySearchParameters psp);
        void onGetProperties(List<Property> properties);
    }

    private void testPropertySearchParameters(PropertySearchParametersTestInterface testInterface){

        PropertySearchParameters psp = new PropertySearchParameters();

        testInterface.onSetValue(psp);

        SimpleSQLiteQuery query = psp.getQuery();
        Log.d(Tag.TAG, "PropertySearchParametersTest() query = " + query.getSql());
        assertNotNull(query);

        List<Property> properties = databaseRepository.getPropertyRepository().getPropertiesWithFilter(query);

        testInterface.onGetProperties(properties);
    }

    @Test
    public void priceTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setPrice(new Pair<>(0, 1000000));
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(2, properties.size());
            }
        });
    }

    @Test
    public void surfaceTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setSurface(new Pair<>(0, 3000));
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(6, properties.size());
            }
        });
    }

    @Test
    public void roomsTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setSurface(new Pair<>(0, 30));
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(1, properties.size());
            }
        });
    }

    @Test
    public void descriptionTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setDescription("dream");
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(1, properties.size());
            }
        });
    }

    @Test
    public void pointOfInterestTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setPointOfInterest("Central Park");
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(2, properties.size());
            }
        });
    }

    @Test
    public void addressTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setAddress("Manhattan");
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(2, properties.size());
            }
        });
    }

    @Test
    public void titleAddressTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setAddressTitle("chelsea");
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(1, properties.size());
            }
        });
    }

    @Test
    public void priceAndSurfaceTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setPrice(new Pair<>(0, 50000000));
                psp.setSurface(new Pair<>(2000, 10000));
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(2, properties.size());
            }
        });
    }

    @Test
    public void priceAndSurfaceAndRoomsTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setPrice(new Pair<>(0, 50000000));
                psp.setSurface(new Pair<>(2000, 10000));
                psp.setRooms(new Pair<>(0, 30));
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(2, properties.size());
            }
        });
    }

    @Test
    public void multipleParametersTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setPrice(new Pair<>(0, 50000000));
                psp.setSurface(new Pair<>(0, 10000));
                psp.setRooms(new Pair<>(0, 18));
                psp.setDescription("the");
                psp.setPointOfInterest("Central Park");
                psp.setAddress("CENTRAL PARK TOWER");
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(1, properties.size());
            }
        });
    }

    @Test
    public void entreyDateTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {

                try {
                    Date start = new SimpleDateFormat("dd/MM/yyy").parse("09/09/2021");
                    Date end = new SimpleDateFormat("dd/MM/yyy").parse("11/11/2021");
                    psp.setEntryDate(new Pair<>(start.getTime(), end.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(8, properties.size());
            }
        });
    }

    @Test
    public void saleDateTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {

                try {
                    Date start = new SimpleDateFormat("dd/MM/yyy").parse("23/09/2021");
                    Date end = new SimpleDateFormat("dd/MM/yyy").parse("23/09/2021");
                    psp.setSaleDate(new Pair<>(start.getTime(), end.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(1, properties.size());
            }
        });
    }

    @Test
    public void propertyTypeTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setPropertyTypeId(1L);
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(1, properties.size());
            }
        });
    }

    @Test
    public void agentTest(){
        testPropertySearchParameters(new PropertySearchParametersTestInterface() {
            @Override
            public void onSetValue(PropertySearchParameters psp) {
                psp.setAgentId(1L);
            }

            @Override
            public void onGetProperties(List<Property> properties) {
                assertEquals(2, properties.size());
            }
        });
    }
}
