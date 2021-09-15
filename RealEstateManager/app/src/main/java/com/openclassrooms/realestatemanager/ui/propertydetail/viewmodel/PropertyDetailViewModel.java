package com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
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
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;

import java.util.List;

public class PropertyDetailViewModel extends ViewModel {

    private long propertyId;
    private final DatabaseRepository databaseRepository;

    /**
     * Mediator expose PropertyListViewState
     */
    private final MediatorLiveData<PropertyDetailViewState> propertyDetailViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<PropertyDetailViewState> getViewState() { return propertyDetailViewStateMediatorLiveData; }

    public PropertyDetailViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
        //configureMediatorLiveData();
    }

    private void configureMediatorLiveData(long propertyId) {
        // property
        LiveData<Property> propertyLiveData = databaseRepository.getPropertyRepository().getPropertyById(propertyId);
        // photos
        LiveData<List<Photo>> photosLiveData = databaseRepository.getPhotoRepository().getPhotosByPropertyId(propertyId);

        // get Agent id from property
        LiveData<Long> agentIdLiveData = Transformations.map(propertyLiveData, property -> {
            return new Long(property.getAgentId());
        });
        // get Agent from agent id
        LiveData<Agent> agentLiveData = Transformations.switchMap(agentIdLiveData, id ->{
            return databaseRepository.getAgentRepository().getAgentById(id);
        });

        // get category id from property and send liveData with category id in categoryIdLiveData
        LiveData<Long> categoryIdLiveData = Transformations.map(propertyLiveData, property -> {
            return new Long(property.getPropertyCategoryId());
        });
        // get PropertyCategory from category id emit by categoryIdLiveData.
        LiveData<PropertyCategory> categoryLiveData = Transformations.switchMap(categoryIdLiveData, id -> {
            return databaseRepository.getPropertyCategoryRepository().getCategoryById(id);
        });

        // get type id from property
        LiveData<Long> typeIdLiveData = Transformations.map(propertyLiveData, property -> {
            return new Long(property.getPropertyTypeId());
        });
        // get PropertyType from type id
        LiveData<PropertyType> propertyTypeLiveData = Transformations.switchMap(typeIdLiveData, id ->{
            return databaseRepository.getPropertyTypeRepository().getPropertyTypeById(id);
        });

        propertyDetailViewStateMediatorLiveData.addSource(propertyLiveData, new Observer<Property>() {
            @Override
            public void onChanged(Property property) {
                combine(property,
                        photosLiveData.getValue(),
                        categoryLiveData.getValue(),
                        propertyTypeLiveData.getValue(),
                        agentLiveData.getValue()
                );
            }
        });

        propertyDetailViewStateMediatorLiveData.addSource(photosLiveData, new Observer<List<Photo>>() {
            @Override
            public void onChanged(List<Photo> photos) {
                combine(propertyLiveData.getValue(),
                        photos,
                        categoryLiveData.getValue(),
                        propertyTypeLiveData.getValue(),
                        agentLiveData.getValue()
                );
            }
        });

        propertyDetailViewStateMediatorLiveData.addSource(categoryIdLiveData, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
            }
        });
        propertyDetailViewStateMediatorLiveData.addSource(categoryLiveData, new Observer<PropertyCategory>() {
            @Override
            public void onChanged(PropertyCategory propertyCategory) {
                combine(propertyLiveData.getValue(),
                        photosLiveData.getValue(),
                        propertyCategory,
                        propertyTypeLiveData.getValue(),
                        agentLiveData.getValue()
                );
            }
        });

        propertyDetailViewStateMediatorLiveData.addSource(typeIdLiveData, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
            }
        });
        propertyDetailViewStateMediatorLiveData.addSource(propertyTypeLiveData, new Observer<PropertyType>() {
            @Override
            public void onChanged(PropertyType propertyType) {
                combine(propertyLiveData.getValue(),
                        photosLiveData.getValue(),
                        categoryLiveData.getValue(),
                        propertyType,
                        agentLiveData.getValue()
                );
            }
        });

        propertyDetailViewStateMediatorLiveData.addSource(agentIdLiveData, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
            }
        });
        propertyDetailViewStateMediatorLiveData.addSource(agentLiveData, new Observer<Agent>() {
            @Override
            public void onChanged(Agent agent) {
                combine(propertyLiveData.getValue(),
                        photosLiveData.getValue(),
                        categoryLiveData.getValue(),
                        propertyTypeLiveData.getValue(),
                        agent
                );
            }
        });
    }

    public void load(long propertyId){
        this.propertyId = propertyId;
        configureMediatorLiveData(propertyId);
    }

    private void combine(@Nullable Property property,
                         @Nullable List<Photo> photos,
                         @Nullable PropertyCategory category,
                         @Nullable PropertyType propertyType,
                         @Nullable Agent agent){

        if (property == null || photos == null || category == null ||
                propertyType == null || agent == null) {
            return;
        }
        Log.d(Tag.TAG, "combine() called with: property = [" + property + "], photos = [" + photos + "], category = [" + category + "], propertyType = [" + propertyType + "], agent = [" + agent + "]");

        // ViewModel emit ViewState
        propertyDetailViewStateMediatorLiveData.setValue(new PropertyDetailViewState(
                property, photos, category, propertyType, agent));
    }
}

