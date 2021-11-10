package com.openclassrooms.realestatemanager.ui.propertymap.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.location.LocationRepository;
import com.openclassrooms.realestatemanager.data.permission_checker.PermissionChecker;

import com.openclassrooms.realestatemanager.data.room.model.PropertyLocationData;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertymap.viewstate.PropertyMapViewState;

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
    public LiveData<PropertyMapViewState> getPropertyMapViewStateLiveData() {
        return propertyMapViewStateMediatorLiveData;
    }

    private MutableLiveData<Long> propertyIdMutableLiveData = new MutableLiveData<Long>();
    public MutableLiveData<Long> getPropertyIdMutableLiveData() {
        return propertyIdMutableLiveData;
    }

    private void configureMediatorLiveData() {
        Log.d(Tag.TAG, "PropertyMapViewModel.configureMediatorLiveData()");
        refreshLocation();

        // current property location
        LiveData<PropertyLocationData> currentPropertyLocationLiveData = Transformations.switchMap(propertyIdMutableLiveData,
                id -> {
                    return databaseRepository.getPropertyRepository().getPropertyLocationById(id);
                });
        // others properties location
        LiveData<List<PropertyLocationData>> otherPropertyLocationLiveData = Transformations.switchMap(propertyIdMutableLiveData,
                id -> {
                    return databaseRepository.getPropertyRepository().getOtherPropertiesLocationById(id);
                });
        // user location
        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();

        // must remove source to avoid bug "This source was already added with the different observer"
        propertyMapViewStateMediatorLiveData.removeSource(locationLiveData);
        propertyMapViewStateMediatorLiveData.addSource(locationLiveData,
                location -> { if (location != null) {
                    combine(location,
                            currentPropertyLocationLiveData.getValue(),
                            otherPropertyLocationLiveData.getValue());
            }
        });

        propertyMapViewStateMediatorLiveData.addSource(currentPropertyLocationLiveData,
                currentPropertyLocation -> {
                    combine(locationLiveData.getValue(),
                            currentPropertyLocation,
                            otherPropertyLocationLiveData.getValue());
                });

        propertyMapViewStateMediatorLiveData.addSource(otherPropertyLocationLiveData,
                otherPropertyLocation -> {
                    combine(locationLiveData.getValue(),
                            currentPropertyLocationLiveData.getValue(),
                            otherPropertyLocation);
                });
    }

    private void combine(@Nullable Location userLocation,
                         @Nullable PropertyLocationData currentPropertyLocation,
                         @Nullable List<PropertyLocationData> otherPropertyLocation) {
        if (userLocation == null || currentPropertyLocation == null || otherPropertyLocation == null) {
            return;
        }
        Log.d(Tag.TAG, "PropertyMapViewModel.combine() called");

        PropertyMapViewState propertyMapViewState = new PropertyMapViewState(userLocation,
                currentPropertyLocation,
                otherPropertyLocation);

        propertyMapViewStateMediatorLiveData.setValue(propertyMapViewState);
    }
}

