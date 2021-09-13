package com.openclassrooms.realestatemanager.ui.propertylist.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyCategoryRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.PropertyListViewState;

import java.util.List;

public class PropertyListViewModel {
    private final AgentRepository agentRepository;
    private final PhotoRepository photoRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyCategoryRepository propertyCategoryRepository;
    private final PropertyTypeRepository propertyTypeRepository;

    /**
     * Mediator expose PropertyListViewState
     */
    private final MediatorLiveData<PropertyListViewState> propertyListViewStateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<PropertyListViewState> getPropertyListViewStateMediatorLiveData() { return propertyListViewStateMediatorLiveData; }

    public PropertyListViewModel(AgentRepository agentRepository,
                         PhotoRepository photoRepository,
                         PropertyRepository propertyRepository,
                         PropertyCategoryRepository propertyCategoryRepository,
                         PropertyTypeRepository propertyTypeRepository) {
        this.agentRepository = agentRepository;
        this.photoRepository = photoRepository;
        this.propertyRepository = propertyRepository;
        this.propertyCategoryRepository = propertyCategoryRepository;
        this.propertyTypeRepository = propertyTypeRepository;

        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {
        LiveData<List<Agent>> agentsLiveData = agentRepository.getAgents();
        LiveData<List<Photo>> photosLiveData = photoRepository.getPhotos();
        LiveData<List<Property>> propertiesLiveData = propertyRepository.getProperties();
        LiveData<List<PropertyCategory>> categoriesLiveData = propertyCategoryRepository.getCategories();
        LiveData<List<PropertyType>> typesLiveData = propertyTypeRepository.getPropertyTypes();

        propertyListViewStateMediatorLiveData.addSource(agentsLiveData, new Observer<List<Agent>>() {
            @Override
            public void onChanged(List<Agent> agents) {
                combine(agents,
                        photosLiveData.getValue(),
                        propertiesLiveData.getValue(),
                        categoriesLiveData.getValue(),
                        typesLiveData.getValue()
                        );
            }
        });

        propertyListViewStateMediatorLiveData.addSource(photosLiveData, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                combine(agentsLiveData.getValue(),
                        photos,
                        propertiesLiveData.getValue(),
                        categoriesLiveData.getValue(),
                        typesLiveData.getValue()
                );
            }
        });

        propertyListViewStateMediatorLiveData.addSource(propertiesLiveData, new Observer<List<Property>>() {
            @Override
            public void onChanged(List<Property> properties) {
                combine(agentsLiveData.getValue(),
                        photosLiveData.getValue(),
                        properties,
                        categoriesLiveData.getValue(),
                        typesLiveData.getValue()
                );
            }
        });

        propertyListViewStateMediatorLiveData.addSource(categoriesLiveData, new Observer<List<PropertyCategory>>() {
            @Override
            public void onChanged(List<PropertyCategory> propertyCategories) {
                combine(agentsLiveData.getValue(),
                        photosLiveData.getValue(),
                        propertiesLiveData.getValue(),
                        propertyCategories,
                        typesLiveData.getValue()
                );
            }
        });

        propertyListViewStateMediatorLiveData.addSource(typesLiveData, new Observer<List<PropertyType>>() {
            @Override
            public void onChanged(List<PropertyType> propertyTypes) {
                combine(agentsLiveData.getValue(),
                        photosLiveData.getValue(),
                        propertiesLiveData.getValue(),
                        categoriesLiveData.getValue(),
                        propertyTypes
                );
            }
        });
    }

    public void load(){
    }

    private void combine(List<Agent> agents,
                         List<Photo> photos,
                         List<Property> properties,
                         List<PropertyCategory> categories,
                         List<PropertyType> types){

        // ViewModel emit ViewState
        propertyListViewStateMediatorLiveData.setValue(new PropertyListViewState());
    }
}
