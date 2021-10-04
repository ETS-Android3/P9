package com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.location.LocationRepository;
import com.openclassrooms.realestatemanager.data.permission_checker.PermissionChecker;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class PropertyDetailViewModel extends ViewModel {

    private static final int CATEGORY_FOR_SAL_ID = 1;
    private static final int CATEGORY_FOR_RENT_ID = 2;

    private long propertyId;
    /**
     * repositories
     */
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;

    /**
     * Mediator expose PropertyListViewState
     */
    private final MediatorLiveData<PropertyDetailViewState> propertyDetailViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<PropertyDetailViewState> getViewState() { return propertyDetailViewStateMediatorLiveData; }

    public PropertyDetailViewModel(PermissionChecker permissionChecker,
                                   LocationRepository locationRepository,
                                   DatabaseRepository databaseRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.databaseRepository = databaseRepository;
    }

    private void configureMediatorLiveData(long propertyId) {
        Log.d(Tag.TAG, "PropertyDetailViewModel.configureMediatorLiveData(propertyId=" + propertyId + ")");
        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
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

        propertyDetailViewStateMediatorLiveData.addSource(locationLiveData, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                if (location != null) {
                    combine(location,
                            propertyLiveData.getValue(),
                            photosLiveData.getValue(),
                            categoryLiveData.getValue(),
                            propertyTypeLiveData.getValue(),
                            agentLiveData.getValue());
                    // must remove source to avoid bug "This source was already added with the different observer"
                    propertyDetailViewStateMediatorLiveData.removeSource(locationLiveData);
                }
            }
        });

        propertyDetailViewStateMediatorLiveData.addSource(propertyLiveData, new Observer<Property>() {
            @Override
            public void onChanged(Property property) {
                combine(locationLiveData.getValue(),
                        property,
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
                combine(locationLiveData.getValue(),
                        propertyLiveData.getValue(),
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
                combine(locationLiveData.getValue(),
                        propertyLiveData.getValue(),
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
                combine(locationLiveData.getValue(),
                        propertyLiveData.getValue(),
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
                combine(locationLiveData.getValue(),
                        propertyLiveData.getValue(),
                        photosLiveData.getValue(),
                        categoryLiveData.getValue(),
                        propertyTypeLiveData.getValue(),
                        agent
                );
            }
        });
    }

    public void load(long propertyId){
        Log.d(Tag.TAG, "PropertyDetailViewModel.load(" + propertyId + ")");
        refreshLocation();

        if (propertyId == PropertyConst.PROPERTY_ID_NOT_INITIALIZED) {
            // when don't know propertyId load first property
            try {
                long id = databaseRepository.getPropertyRepository().getFirstPropertyId();
                Log.d(Tag.TAG, "PropertyDetailViewModel.load() getFirstPropertyId=" + id);
                this.propertyId = id;
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        else {
            this.propertyId = propertyId;
        }
        configureMediatorLiveData(this.propertyId);
    }

    @SuppressLint("MissingPermission")
    private void refreshLocation() {
        // No GPS permission
        if (!permissionChecker.hasLocationPermission()) {
            locationRepository.stopLocationRequest();
        } else {
            locationRepository.startLocationRequest();
        }
    }

    private void combine(@Nullable Location location,
                         @Nullable Property property,
                         @Nullable List<Photo> photos,
                         @Nullable PropertyCategory category,
                         @Nullable PropertyType propertyType,
                         @Nullable Agent agent){

        if (location == null || property == null || photos == null || category == null ||
                propertyType == null || agent == null) {
            return;
        }
        Log.d(Tag.TAG, "PropertyDetailViewModel.combine() called");

        String propertyState = "";

        if (property.getPropertyCategoryId() == CATEGORY_FOR_SAL_ID) {
            if (property.isAvailable()){
                propertyState = "For sal";
            } else
                propertyState = "Sold";
        } else {
            if (property.isAvailable()){
                propertyState = "For rent";
            } else
                propertyState = "Rented";
        }

        String entryDate = Utils.convertDateToString(property.getEntryDate());
        String saleDate = Utils.convertDateToString(property.getSaleDate());


        // ViewModel emit ViewState
        propertyDetailViewStateMediatorLiveData.setValue(new PropertyDetailViewState(location,
                property, photos, category, propertyType, agent, propertyState, entryDate, saleDate));

    }
}

