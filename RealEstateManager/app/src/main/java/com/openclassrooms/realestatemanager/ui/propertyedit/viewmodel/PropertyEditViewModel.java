package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleGeocodeRepository;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;

public class PropertyEditViewModel extends ViewModel {

    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final GoogleGeocodeRepository googleGeocodeRepository;

    private LiveData<LatLng> locationLiveData = null;
    public LiveData<LatLng> getLocationLiveData() { return locationLiveData; }

    public PropertyEditViewModel(@NonNull DatabaseRepository databaseRepository, @NonNull GoogleGeocodeRepository googleGeocodeRepository) {
        this.databaseRepository = databaseRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
        locationLiveData = this.googleGeocodeRepository.getLocationByAddressLiveData();
    }

    public void loadLocationByAddress(String address){
        googleGeocodeRepository.loadLocationByAddress(address);
    }
}
