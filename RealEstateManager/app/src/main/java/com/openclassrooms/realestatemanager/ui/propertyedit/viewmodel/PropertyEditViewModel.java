package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleGeocodeRepository;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.FieldState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class PropertyEditViewModel extends ViewModel {

    private static final int UNINITIALIZED_INTEGER_VALUE = -1;
    public static final int NO_STRING_ID = -1;

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

    /**
     * cache
     */
    private CachePropertyEditViewModel cache;

    /**
     * constructor
     * @param databaseRepository
     * @param googleGeocodeRepository
     */
    public PropertyEditViewModel(@NonNull DatabaseRepository databaseRepository, @NonNull GoogleGeocodeRepository googleGeocodeRepository) {
        cache = new CachePropertyEditViewModel();
        this.databaseRepository = databaseRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
        locationLiveData = this.googleGeocodeRepository.getLocationByAddressLiveData();

/*        onCheckAddressTitleValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckAddressValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckDescriptionValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckPointOfInterestValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckPriceValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckSurfaceValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckRoomsValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckEntryDateValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckSaleDateValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckAgentIdValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckPropertyTypeIdValueMutableLiveData.setValue(new FieldState(getResIdError(true)));*/
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
                // keep list in cache
                cache.getAgents().clear();
                cache.getAgents().addAll(agents);
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
                cache.getPropertyTypes().clear();
                cache.getPropertyTypes().addAll(propertyTypes);
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

    private PropertyType findPropertyTypeById(long id) {
        if ((cache != null) && (cache.getPropertyTypes() != null)){
            Iterator<PropertyType> iterator = cache.getPropertyTypes().iterator();
            while (iterator.hasNext()) {
                PropertyType propertyType = iterator.next();
                if (propertyType.getId() == id) {
                    return propertyType;
                }
            }
        }
        return null;
    }

    private static boolean emptyString(String value){
        return TextUtils.isEmpty(value.trim());
    }

    public static boolean validDate(String text){
        return (Utils.convertStringInLocalFormatToDate(text) != null);
    }

    public static boolean validOrNullDate(String text) {
        return (TextUtils.isEmpty(text) || (Utils.convertStringInLocalFormatToDate(text) != null));
    }

    private Agent findAgentById(long id) {
        if ((cache != null) && (cache.getAgents() != null)){
            Iterator<Agent> iterator = cache.getAgents().iterator();
            while (iterator.hasNext()) {
                Agent agent = iterator.next();
                if (agent.getId() == id) {
                    return agent;
                }
            }
        }
        return null;
    }

    private int getResIdError(boolean error) {
        return  (error) ? R.string.value_required : NO_STRING_ID;
    }

    private MutableLiveData<FieldState> onCheckAddressTitleValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAddressTitleValueLiveData() { return onCheckAddressTitleValueMutableLiveData; }
    public boolean checkAddressTitleValue(String value){
        boolean error = PropertyEditViewModel.emptyString(value);
        onCheckAddressTitleValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckAddressValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAddressValueLiveData() { return onCheckAddressValueMutableLiveData; }
    public boolean checkAddressValue(String value){
        boolean error = PropertyEditViewModel.emptyString(value);
        onCheckAddressValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckDescriptionValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckDescriptionValueLiveData() { return onCheckDescriptionValueMutableLiveData; }
    public boolean checkDescriptionValue(String value){
        boolean error = PropertyEditViewModel.emptyString(value);
        onCheckDescriptionValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckPointOfInterestValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPointOfInterestValueLiveData() { return onCheckPointOfInterestValueMutableLiveData; }
    public boolean checkPointOfInterestValue(String value){
        boolean error = PropertyEditViewModel.emptyString(value);
        onCheckPointOfInterestValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckPriceValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPriceValueLiveData() { return onCheckPriceValueMutableLiveData; }
    public boolean checkPriceValue(String value){
        boolean error = PropertyEditViewModel.emptyString(value);
        onCheckPriceValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckSurfaceValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckSurfaceValueLiveData() { return onCheckSurfaceValueMutableLiveData; }
    public boolean checkSurfaceValue(String value){
        boolean error = PropertyEditViewModel.emptyString(value);
        onCheckSurfaceValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckRoomsValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckRoomsValueLiveData() { return onCheckRoomsValueMutableLiveData; }
    public boolean checkRoomsValue(String value){
        boolean error = PropertyEditViewModel.emptyString(value);
        onCheckRoomsValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckEntryDateValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckEntryDateValueLiveData() { return onCheckEntryDateValueMutableLiveData; }
    public boolean checkEntryDateValue(String value){
        boolean error = ! PropertyEditViewModel.validDate(value);
        onCheckEntryDateValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckSaleDateValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckSaleDateValueLiveData() { return onCheckSaleDateValueMutableLiveData; }
    public boolean checkSaleDateValue(String value){
        boolean error = ! PropertyEditViewModel.validOrNullDate(value);
        onCheckSaleDateValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckAgentIdValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAgentIdValueLiveData() { return onCheckAgentIdValueMutableLiveData; }
    public boolean checkAgentIdValue(long id){
        boolean error = (findAgentById(id) == null);
        onCheckAgentIdValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
    }

    private MutableLiveData<FieldState> onCheckPropertyTypeIdValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPropertyTypeIdValueLiveData() { return onCheckPropertyTypeIdValueMutableLiveData; }
    public boolean checkPropertyTypeIdValue(long id){
        boolean error = (findPropertyTypeById(id) == null);
        onCheckPropertyTypeIdValueMutableLiveData.setValue(new FieldState(getResIdError(error)));
        return error;
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

        // check all values
        boolean ok = checkAddressTitleValue(addressTitle) &
                checkAddressValue(address) &
                checkPriceValue(price) &
                checkSurfaceValue(surface) &
                checkRoomsValue(rooms) &
                checkDescriptionValue(description) &
                checkPointOfInterestValue(pointOfInterest) &
                checkEntryDateValue(entryDate) &
                checkSaleDateValue(saleDate) &
                checkAgentIdValue(agentId) &
                checkPropertyTypeIdValue(propertyTypeId);


        double latitude = (latLng == null) ? 0 : latLng.latitude;
        double longitude = (latLng == null) ? 0 : latLng.longitude;

        int categoryId = 1;

        //Date dateEntryDate = Calendar.getInstance().getTime() ;
        //Date dateSaleDate = null;

/*        Property property = new Property(0,
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

        databaseRepository.getPropertyRepository().insert(property);*/
    }

}
