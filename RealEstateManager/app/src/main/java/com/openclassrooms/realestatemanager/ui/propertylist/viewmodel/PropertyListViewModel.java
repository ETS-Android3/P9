package com.openclassrooms.realestatemanager.ui.propertylist.viewmodel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyCategoryRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.PropertyListViewState;
import com.openclassrooms.realestatemanager.ui.propertylist.viewstate.RowPropertyViewState;

import java.util.ArrayList;
import java.util.List;

public class PropertyListViewModel extends ViewModel {
    private final DatabaseRepository databaseRepository;

    /**
     * Mediator expose PropertyListViewState
     */
    private final MediatorLiveData<PropertyListViewState> propertyListViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<PropertyListViewState> getViewState() { return propertyListViewStateMediatorLiveData; }

    public PropertyListViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;

        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {
        LiveData<List<Agent>> agentsLiveData = databaseRepository.getAgentRepository().getAgents();
        LiveData<List<Photo>> photosLiveData = databaseRepository.getPhotoRepository().getPhotos();
        LiveData<List<Property>> propertiesLiveData = databaseRepository.getPropertyRepository().getProperties();
        LiveData<List<PropertyCategory>> categoriesLiveData = databaseRepository.getPropertyCategoryRepository().getCategories();
        LiveData<List<PropertyType>> typesLiveData = databaseRepository.getPropertyTypeRepository().getPropertyTypes();

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
        databaseRepository.getAgentRepository().getAgents();
    }

    private String findFirstPhotoUrlByPropertyId(List<Photo> photos, long id){
        for (Photo photo : photos){
            if (photo.getPropertyId() == id) {
                return photo.getUrl();
            }
        }
        return "";
    }

    private String findTypeNameById(List<PropertyType> types, long id){
        for (PropertyType propertyType : types) {
            if (propertyType.getId() == id) {
                return  propertyType.getName();
            }
        }
        return "";
    }

    private String convertPriceToString(int price){
        return "" + price;
    }

    private void combine(@Nullable List<Agent> agents,
                         @Nullable List<Photo> photos,
                         @Nullable List<Property> properties,
                         @Nullable List<PropertyCategory> categories,
                         @Nullable List<PropertyType> types){
        //Log.d(Tag.TAG, "combine() called with: agents = [" + agents + "], photos = [" + photos + "], properties = [" + properties + "], categories = [" + categories + "], types = [" + types + "]");

        if (agents == null || photos == null || properties == null ||
                categories == null || types == null) {
            return;
        }

        List<RowPropertyViewState> rowPropertyViewStates = new ArrayList<>();
        for (Property property : properties) {
            rowPropertyViewStates.add(new RowPropertyViewState(property.getId(),
                    findFirstPhotoUrlByPropertyId(photos, property.getId()),
                    findTypeNameById(types, property.getPropertyTypeId()),
                    property.getAddress(),
                    convertPriceToString(property.getPrice())));
        }

        // ViewModel emit ViewState
        propertyListViewStateMediatorLiveData.setValue(new PropertyListViewState(rowPropertyViewStates));
    }
}
