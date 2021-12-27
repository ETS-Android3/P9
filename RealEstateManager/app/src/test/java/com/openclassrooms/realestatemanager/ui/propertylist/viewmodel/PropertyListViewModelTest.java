package com.openclassrooms.realestatemanager.ui.propertylist.viewmodel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertySearchParameters;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.data.room.sample.SampleAgent;
import com.openclassrooms.realestatemanager.data.room.sample.SamplePhoto;
import com.openclassrooms.realestatemanager.data.room.sample.SampleProperty;
import com.openclassrooms.realestatemanager.data.room.sample.SamplePropertyType;
import com.openclassrooms.realestatemanager.testutils.LiveDataTestUtils;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.PropertyListViewState;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.RowPropertyViewState;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class PropertyListViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final AgentRepository agentRepository = Mockito.mock(AgentRepository.class);
    private final PhotoRepository photoRepository = Mockito.mock(PhotoRepository.class);
    private final PropertyRepository propertyRepository = Mockito.mock(PropertyRepository.class);
    private final PropertyTypeRepository propertyTypeRepository = Mockito.mock(PropertyTypeRepository.class);

    MutableLiveData<List<Agent>> localAgentsLiveData;
    MutableLiveData<List<Photo>> localPhotosLiveData;
    MutableLiveData<List<Property>> localPropertiesLiveData;
    MutableLiveData<List<PropertyType>> localPropertyTypesLiveData;

    MutableLiveData<PropertySearchParameters> localPropertySearchParametersLiveData;

    @Before
    public void setUp() {

        localAgentsLiveData = new MutableLiveData<>();
        Mockito.doReturn(localAgentsLiveData).when(agentRepository).getAgentsLiveData();

        localPhotosLiveData = new MutableLiveData<>();
        Mockito.doReturn(localPhotosLiveData).when(photoRepository).getPhotos();

        localPropertiesLiveData = new MutableLiveData<>();
        Mockito.doReturn(localPropertiesLiveData).when(propertyRepository).getProperties();

        localPropertyTypesLiveData = new MutableLiveData<>();
        Mockito.doReturn(localPropertyTypesLiveData).when(propertyTypeRepository).getPropertyTypesLiveData();

        localPropertySearchParametersLiveData = new MutableLiveData<>();
    }

    @Test
    public void testGetViewState() throws InterruptedException {

        DatabaseRepository databaseRepository = new DatabaseRepository(agentRepository, photoRepository, propertyRepository, propertyTypeRepository);
        PropertyListViewModel viewModel = new PropertyListViewModel(databaseRepository);

        SampleAgent sampleAgent = new SampleAgent();
        localAgentsLiveData.setValue(Arrays.asList(sampleAgent.getSample().clone()));

        SamplePhoto samplePhoto = new SamplePhoto();
        localPhotosLiveData.setValue(Arrays.asList(samplePhoto.getSample()));

        SampleProperty sampleProperty = new SampleProperty();
        try {
            localPropertiesLiveData.setValue(Arrays.asList(sampleProperty.getSample()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SamplePropertyType samplePropertyType = new SamplePropertyType();
        localPropertyTypesLiveData.setValue(Arrays.asList(samplePropertyType.getSample()));

        PropertyListViewState viewState = LiveDataTestUtils.getOrAwaitValue(viewModel.getViewState(), 1);

        assertNotNull(viewState);
        assertFalse(viewState.isShowWarning());
        List<RowPropertyViewState> rowPropertyViewStates = viewState.getRowPropertyViewStates();
        assertEquals(5, rowPropertyViewStates.size());

        RowPropertyViewState property1 = rowPropertyViewStates.get(0);
        assertEquals(1, property1.getId());
        assertEquals("https://www.alsacecuisine.fr/image/partial/a/l/s/e2c71f5fc453f3c847106d4caff80c0b_alsace-cuisine06-.jpg", property1.getUrl());
        assertEquals("Manor", property1.getType());
        assertEquals("CHELSEA", property1.getAddress());
        assertEquals("24 355 939 €", property1.getPrice());

        RowPropertyViewState property2 = rowPropertyViewStates.get(1);
        assertEquals(2, property2.getId());
        assertEquals("https://www.alsacecuisine.fr/image/partial/1/5/3/e2c71f5fc453f3c847106d4caff80c0b_1539598263-alsace-cuisine14-.jpg", property2.getUrl());
        assertEquals("Manor", property2.getType());
        assertEquals("MIDTOWN MANHATTAN", property2.getAddress());
        assertEquals("24 278 799 €", property2.getPrice());

        RowPropertyViewState property3 = rowPropertyViewStates.get(2);
        assertEquals(3, property3.getId());
        assertEquals("https://www.alsacecuisine.fr/image/partial/w/e/b/e2c71f5fc453f3c847106d4caff80c0b_web-hgphoto-41-.jpg", property3.getUrl());
        assertEquals("Condominium", property3.getType());
        assertEquals("LENOX HILL", property3.getAddress());
        assertEquals("18 675 999 €", property3.getPrice());

        RowPropertyViewState property4 = rowPropertyViewStates.get(3);
        assertEquals(4, property4.getId());
        assertEquals("https://www.alsacecuisine.fr/image/partial/1/5/3/e2c71f5fc453f3c847106d4caff80c0b_1539598263-alsace-cuisine16-.jpg", property4.getUrl());
        assertEquals("House", property4.getType());
        assertEquals("PARK AVENUE", property4.getAddress());
        assertEquals("109 619 997 €", property4.getPrice());

        RowPropertyViewState property5 = rowPropertyViewStates.get(4);
        assertEquals(5, property5.getId());
        assertEquals("", property5.getUrl());
        assertEquals("Apartment", property5.getType());
        assertEquals("CENTRAL HARLEM", property5.getAddress());
        assertEquals("629 300 €", property5.getPrice());
    }
}