package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleGeocodeRepository;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;

import java.util.ArrayList;
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

    /**
     * init
     */

    private final MediatorLiveData<DropdownViewstate> dropDownViewstateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<DropdownViewstate> getDropDownViewstateMediatorLiveData() {
        return dropDownViewstateMediatorLiveData;
    }

    private DropdownItem agentToDropdownItem(Agent agent){
        return new DropdownItem(agent.getId(), agent.getName());
    }

    private List<DropdownItem> agentsToDropdownItems(List<Agent> agents){
        List<DropdownItem> items = new ArrayList<>();
        for (Agent agent : agents) {
            items.add(agentToDropdownItem(agent));
        }
        return items;
    }

    private DropdownItem propertyTypeToDropdownItem(PropertyType propertyType){
        return new DropdownItem(propertyType.getId(), propertyType.getName());
    }

    private List<DropdownItem> propertyTypesToDropdownItems(List<PropertyType> propertyTypes){
        List<DropdownItem> items = new ArrayList<>();
        for (PropertyType propertyType : propertyTypes) {
            items.add(propertyTypeToDropdownItem(propertyType));
        }
        return items;
    }

    private void configureDownViewstateMediatorLiveData(){
        LiveData<List<Agent>> agentLiveData = databaseRepository.getAgentRepository().getAgents();
        LiveData<List<DropdownItem>> agentItemsLiveData = Transformations.map(agentLiveData, this::agentsToDropdownItems);

        LiveData<List<PropertyType>> propertyTypeLiveData = databaseRepository.getPropertyTypeRepository().getPropertyTypes();
        LiveData<List<DropdownItem>> propertyTypeItemsLiveData = Transformations.map(propertyTypeLiveData, this::propertyTypesToDropdownItems);

        dropDownViewstateMediatorLiveData.addSource(agentLiveData, new Observer<List<Agent>>() {
            @Override
            public void onChanged(List<Agent> agents) {
            }
        });

        dropDownViewstateMediatorLiveData.addSource(agentItemsLiveData, new Observer<List<DropdownItem>>() {
            @Override
            public void onChanged(List<DropdownItem> items) {
                conbineDropDown(items, propertyTypeItemsLiveData.getValue());
            }
        });

        dropDownViewstateMediatorLiveData.addSource(propertyTypeLiveData, new Observer<List<PropertyType>>() {
            @Override
            public void onChanged(List<PropertyType> propertyTypes) {
            }
        });
        dropDownViewstateMediatorLiveData.addSource(propertyTypeItemsLiveData, new Observer<List<DropdownItem>>() {
            @Override
            public void onChanged(List<DropdownItem> items) {
                conbineDropDown(agentItemsLiveData.getValue(), items);
            }
        });
    }

    private void conbineDropDown(@Nullable List<DropdownItem> agentItems,
                                 @Nullable List<DropdownItem> propertyTypeItems){

        if ((agentItems == null) || (propertyTypeItems == null)) {
            return;
        }
        dropDownViewstateMediatorLiveData.setValue(new DropdownViewstate(agentItems, propertyTypeItems));
    }

    public void loadDropDownLists(){
        // load agent and load typeRepository
        configureDownViewstateMediatorLiveData();
    }

    /**
     * validation
     * @param text
     * @return
     */
    private static int tryParse(String text){
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return UNINITIALIZED_INTEGER_VALUE;
        }
    }

    public void addProperty(String price,
                            String surface,
                            String description,
                            String addressTitle,
                            String address,
                            String pointOfInterest,
                            boolean available,
                            String entryDate,
                            String saleDate,
                            long propertyTypeId,
                            boolean forSale,
                            long agentId,
                            String rooms,
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

        int categoryId = 1;

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
                propertyTypeId,
                categoryId,
                agentId,
                intRooms,
                latitude,
                longitude);

        databaseRepository.getPropertyRepository().insert(property);
    }


}
