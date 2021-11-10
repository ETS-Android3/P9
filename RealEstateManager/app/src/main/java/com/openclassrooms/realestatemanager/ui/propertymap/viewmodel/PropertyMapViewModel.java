package com.openclassrooms.realestatemanager.ui.propertymap.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.location.LocationRepository;
import com.openclassrooms.realestatemanager.data.permission_checker.PermissionChecker;

import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertymap.viewstate.PropertyMapItem;
import com.openclassrooms.realestatemanager.ui.propertymap.viewstate.PropertyMapViewState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class PropertyMapViewModel extends ViewModel {
    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;

    public PropertyMapViewModel(@NonNull PermissionChecker permissionChecker,
                                @NonNull LocationRepository locationRepository,
                                @NonNull DatabaseRepository databaseRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.databaseRepository = databaseRepository;

        configureMediatorLiveData();
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

    /**
     * Mediator expose PropertyMapViewState
     */
    private final MediatorLiveData<PropertyMapViewState> propertyMapViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<PropertyMapViewState> getViewState() {
        return propertyMapViewStateMediatorLiveData;
    }

    private void configureMediatorLiveData() {
        Log.d(Tag.TAG, "PropertyMapViewModel.configureMediatorLiveData()");
        refreshLocation();

        // all properties location
        LiveData<List<PropertyLocationData>> propertiesLocationLiveData = databaseRepository.getPropertyRepository().getPropertiesLocation();
        // user location
        LiveData<Location> userLocationLiveData = locationRepository.getLocationLiveData();

        propertyMapViewStateMediatorLiveData.addSource(propertiesLocationLiveData,
                propertyLocationDataList -> combine(userLocationLiveData.getValue(), propertyLocationDataList));

        // must remove source to avoid bug "This source was already added with the different observer"
        propertyMapViewStateMediatorLiveData.removeSource(userLocationLiveData);
        propertyMapViewStateMediatorLiveData.addSource(userLocationLiveData,
                location -> { if (location != null) {
                    combine(location,
                            propertiesLocationLiveData.getValue());
            }
        });
    }

    private String formatTitleMarker(String addressTitle, int price){
        return String.format("%s %s", addressTitle, Utils.convertPriceToString(price));
    }

    private void combine(@Nullable Location userLocation,
                         @Nullable List<PropertyLocationData> propertiesLocation) {
        if (userLocation == null || propertiesLocation == null) {
            return;
        }
        Log.d(Tag.TAG, "PropertyMapViewModel.combine() called");

        List<PropertyMapItem> items = new ArrayList<>();

        for (PropertyLocationData locationData : propertiesLocation){
            items.add(new PropertyMapItem(locationData.getId(),
                    formatTitleMarker(locationData.getAddressTitle(), locationData.getPrice()),
                    locationData.getLatitude(),
                    locationData.getLongitude()));
        }

        PropertyMapViewState propertyMapViewState = new PropertyMapViewState(userLocation,
                items);

        propertyMapViewStateMediatorLiveData.setValue(propertyMapViewState);
    }
}

