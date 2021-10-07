package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleGeocodeRepository;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PropertyEditViewModel extends ViewModel {

    private static final int UNINITIALIZED_INTEGER_VALUE = -1;
    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final GoogleGeocodeRepository googleGeocodeRepository;

    private MutableLiveData<String> errorMutableLiveData = new MutableLiveData<>();
    public LiveData<String> getErrorLiveData() { return errorMutableLiveData; }

    private LiveData<LatLng> locationLiveData = null;
    public LiveData<LatLng> getLocationLiveData() { return locationLiveData; }

    private LiveData<List<Agent>> agentsLiveData = null;
    public LiveData<List<Agent>> getAgentsLiveData() { return agentsLiveData; }

    private LiveData<List<PropertyType>> propertyTypeLiveData = null;
    public LiveData<List<PropertyType>> getPropertyTypeLiveData() { return propertyTypeLiveData; }

    public PropertyEditViewModel(@NonNull DatabaseRepository databaseRepository, @NonNull GoogleGeocodeRepository googleGeocodeRepository) {
        this.databaseRepository = databaseRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
        locationLiveData = this.googleGeocodeRepository.getLocationByAddressLiveData();
        agentsLiveData = this.databaseRepository.getAgentRepository().getAgents();
        propertyTypeLiveData = this.databaseRepository.getPropertyTypeRepository().getPropertyTypes();
    }

    public void loadLocationByAddress(String address){
        googleGeocodeRepository.loadLocationByAddress(address);
    }

    private static int tryParse(String text){
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return UNINITIALIZED_INTEGER_VALUE;
        }
    }

    public void addProperty(String addressTitle,
                            String address,
                            String price,
                            String surface,
                            String rooms,
                            String description,
                            String pointOfInterest,
                            String entryDate,
                            String saleDate,
                            boolean available,
                            boolean forSale,
                            LatLng latLng){

        if (addressTitle == null) {
            errorMutableLiveData.setValue("Title !");
            return;
        }

        int intPrice = tryParse(price);
        if (intPrice < 0) {
            errorMutableLiveData.setValue("price !");
            return;
        }

        int intSurface = tryParse(surface);
        if (intSurface < 0) {
            errorMutableLiveData.setValue("surface !");
            return;
        }

        int intRooms = tryParse(rooms);
        if (intRooms < 0) {
            errorMutableLiveData.setValue("rooms !");
            return;
        }

        double latitude = (latLng == null) ? 0 : latLng.latitude;
        double longitude = (latLng == null) ? 0 : latLng.longitude;

        int typeId = 1;
        int categoryId = 1;
        int agentId = 1;

        Date dateEntryDate = Calendar.getInstance().getTime() ;
        Date dateSaleDate = null;

        Property property = new Property(0,
                intPrice,
                intSurface,
                description,
                addressTitle,
                address,
                pointOfInterest,
                available,
                dateEntryDate,
                dateSaleDate,
                typeId,
                categoryId,
                agentId,
                intRooms,
                latitude,
                longitude);

        databaseRepository.getPropertyRepository().insert(property);
    }


}
