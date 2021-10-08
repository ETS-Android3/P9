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
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.AgentDropdown;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.PropertyTypeDropdown;

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

    private AgentDropdown agentToAgentDropdown(Agent agent){
        return new AgentDropdown(agent.getId(), agent.getName());
    }

    private List<AgentDropdown> agentListToAgentDropdownList(List<Agent> agents){
        List<AgentDropdown> agentDropdownList = new ArrayList<>();
        for (Agent agent : agents) {
            agentDropdownList.add(agentToAgentDropdown(agent));
        }
        return agentDropdownList;
    }

    private PropertyTypeDropdown propertyTypeToPropertyTypeDropdown(PropertyType propertyType){
        return new PropertyTypeDropdown(propertyType.getId(), propertyType.getName());
    }

    private List<PropertyTypeDropdown> propertyTypeListToPropertyTypeDropdownList(List<PropertyType> propertyTypes){
        List<PropertyTypeDropdown> propertyTypeDropdownList = new ArrayList<>();
        for (PropertyType propertyType : propertyTypes) {
            propertyTypeDropdownList.add(propertyTypeToPropertyTypeDropdown(propertyType));
        }
        return propertyTypeDropdownList;
    }

    private void configureDownViewstateMediatorLiveData(){
        LiveData<List<Agent>> agentLiveData = databaseRepository.getAgentRepository().getAgents();
        LiveData<List<AgentDropdown>> agentDropdownListLiveData = Transformations.map(agentLiveData, this::agentListToAgentDropdownList);

        LiveData<List<PropertyType>> propertyTypeLiveData = databaseRepository.getPropertyTypeRepository().getPropertyTypes();
        LiveData<List<PropertyTypeDropdown>> propertyTypeDropdownListLiveData = Transformations.map(propertyTypeLiveData, this::propertyTypeListToPropertyTypeDropdownList);

        dropDownViewstateMediatorLiveData.addSource(agentLiveData, new Observer<List<Agent>>() {
            @Override
            public void onChanged(List<Agent> agents) {
            }
        });

        dropDownViewstateMediatorLiveData.addSource(agentDropdownListLiveData, new Observer<List<AgentDropdown>>() {
            @Override
            public void onChanged(List<AgentDropdown> agentDropdowns) {
                conbineDropDown(agentDropdowns, propertyTypeDropdownListLiveData.getValue());
            }
        });

        dropDownViewstateMediatorLiveData.addSource(propertyTypeLiveData, new Observer<List<PropertyType>>() {
            @Override
            public void onChanged(List<PropertyType> propertyTypes) {
            }
        });
        dropDownViewstateMediatorLiveData.addSource(propertyTypeDropdownListLiveData, new Observer<List<PropertyTypeDropdown>>() {
            @Override
            public void onChanged(List<PropertyTypeDropdown> propertyTypeDropdowns) {
                conbineDropDown(agentDropdownListLiveData.getValue(), propertyTypeDropdowns);
            }
        });
    }

    private void conbineDropDown(@Nullable List<AgentDropdown> agentDropdownList,
                                 @Nullable List<PropertyTypeDropdown> propertyTypeDropdownList){

        if ((agentDropdownList == null) || (propertyTypeDropdownList == null)) {
            return;
        }
        dropDownViewstateMediatorLiveData.setValue(new DropdownViewstate(agentDropdownList, propertyTypeDropdownList));
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
