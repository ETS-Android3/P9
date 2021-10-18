package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.text.TextUtils;
import android.util.Log;

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
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.FieldState;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.PropertyEditViewState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;

public class PropertyEditViewModel extends ViewModel {
    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final GoogleGeocodeRepository googleGeocodeRepository;

    private MutableLiveData<String> errorMutableLiveData = new MutableLiveData<>();
    public LiveData<String> getErrorLiveData() { return errorMutableLiveData; }

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
        this.databaseRepository = databaseRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
        cache = new CachePropertyEditViewModel();
        cache.getAgents().addAll(databaseRepository.getAgentRepository().getAgents());
        cache.getPropertyTypes().addAll(databaseRepository.getPropertyTypeRepository().getPropertyTypes());

        onCheckAddressTitleValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckAddressValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckDescriptionValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckPointOfInterestValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckPriceValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckSurfaceValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckRoomsValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckEntryDateValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckSaleDateValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckAgentIdValueMutableLiveData.setValue(new FieldState(getResIdError(true)));
        onCheckPropertyTypeIdValueMutableLiveData.setValue(new FieldState(getResIdError(true)));

        initDropdownViewstateMediatorLiveData();
    }

    public LiveData<LatLng> getLocationLiveDataByAddress(String address){
        return googleGeocodeRepository.getLocationByAddressLiveData(address);
    }

    /**
     * init
     */
    private final MediatorLiveData<DropdownViewstate> dropDownViewstateMediatorLiveData = new MediatorLiveData<>();
    public MediatorLiveData<DropdownViewstate> getDropDownViewstateMediatorLiveData() {
        return dropDownViewstateMediatorLiveData;
    }

    private void initDropdownViewstateMediatorLiveData(){

        LiveData<List<Agent>> agentLiveData = databaseRepository.getAgentRepository().getAgentsLiveData();
        LiveData<List<DropdownItem>> agentItemsLiveData = Transformations.map(agentLiveData,
                agents -> {
                    List<DropdownItem> items = new ArrayList<>();
                    for (Agent agent : agents) {
                        items.add(new DropdownItem(agent.getId(), agent.getName()));
                    }
                    return items;
                });

        LiveData<List<PropertyType>> propertyTypeLiveData = databaseRepository.getPropertyTypeRepository().getPropertyTypesLiveData();
        LiveData<List<DropdownItem>> propertyTypeItemsLiveData = Transformations.map(propertyTypeLiveData,
                propertyTypes -> {
                    List<DropdownItem> items = new ArrayList<>();
                    for (PropertyType propertyType : propertyTypes) {
                        items.add(new DropdownItem(propertyType.getId(), propertyType.getName()));
                    }
                    return items;
                });

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

    private static boolean validOrNullDate(String text) {
        return (TextUtils.isEmpty(text) || (Utils.convertStringInLocalFormatToDate(text) != null));
    }

    private Agent findAgentById(long id) {
        Log.d(Tag.TAG, "findAgentById() called with: id = [" + id + "]");
        if ((cache != null) && (cache.getAgents() != null)){
            Iterator<Agent> iterator = cache.getAgents().iterator();
            while (iterator.hasNext()) {
                Agent agent = iterator.next();
                if (agent.getId() == id) {
                    Log.d(Tag.TAG, "findAgentById() return agent [" + agent + "]");
                    return agent;
                }
            }
        }
        Log.d(Tag.TAG, "findAgentById() return null");
        return null;
    }

    private int getResIdError(boolean error) {
        return  (error) ? R.string.value_required : PropertyConst.NO_STRING_ID;
    }

    private boolean checkIsInt(String value){
        try {
            Integer.parseInt(value);
            Log.d(Tag.TAG, "checkIsInt() value = [" + value + "] return = [true]");
            return true;
        } catch (NumberFormatException e) {
            Log.d(Tag.TAG, "checkIsInt() value = [" + value + "] return = [false]");
            return false;
        }
    }

    private MutableLiveData<FieldState> onCheckAddressTitleValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAddressTitleValueLiveData() { return onCheckAddressTitleValueMutableLiveData; }
    public boolean checkAddressTitleValue(String value){
        boolean valueOk = !PropertyEditViewModel.emptyString(value);
        onCheckAddressTitleValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkAddressTitleValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckAddressValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAddressValueLiveData() { return onCheckAddressValueMutableLiveData; }
    public boolean checkAddressValue(String value){
        boolean valueOk = !PropertyEditViewModel.emptyString(value);
        onCheckAddressValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkAddressValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckDescriptionValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckDescriptionValueLiveData() { return onCheckDescriptionValueMutableLiveData; }
    public boolean checkDescriptionValue(String value){
        boolean valueOk =!PropertyEditViewModel.emptyString(value);
        onCheckDescriptionValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkDescriptionValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckPointOfInterestValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPointOfInterestValueLiveData() { return onCheckPointOfInterestValueMutableLiveData; }
    public boolean checkPointOfInterestValue(String value){
        boolean valueOk = !PropertyEditViewModel.emptyString(value);
        onCheckPointOfInterestValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkPointOfInterestValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckPriceValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPriceValueLiveData() { return onCheckPriceValueMutableLiveData; }
    public boolean checkPriceValue(String value){
        boolean valueOk = checkIsInt(value);
        onCheckPriceValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkPriceValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckSurfaceValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckSurfaceValueLiveData() { return onCheckSurfaceValueMutableLiveData; }
    public boolean checkSurfaceValue(String value){
        boolean valueOk = checkIsInt(value);
        onCheckSurfaceValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkSurfaceValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckRoomsValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckRoomsValueLiveData() { return onCheckRoomsValueMutableLiveData; }
    public boolean checkRoomsValue(String value){
        boolean valueOk = checkIsInt(value);
        onCheckRoomsValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkRoomsValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckEntryDateValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckEntryDateValueLiveData() { return onCheckEntryDateValueMutableLiveData; }
    public boolean checkEntryDateValue(String value){
        boolean valueOk = PropertyEditViewModel.validDate(value);
        onCheckEntryDateValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkEntryDateValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckSaleDateValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckSaleDateValueLiveData() { return onCheckSaleDateValueMutableLiveData; }
    public boolean checkSaleDateValue(String value){
        boolean valueOk = PropertyEditViewModel.validOrNullDate(value);
        onCheckSaleDateValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkSaleDateValue() return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckAgentIdValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAgentIdValueLiveData() { return onCheckAgentIdValueMutableLiveData; }
    public boolean checkAgentIdValue(long id){
        boolean valueOk = (findAgentById(id) != null);
        Log.d(Tag.TAG, "checkAgentIdValue() valueOk= [" + valueOk + "]");
        onCheckAgentIdValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkAgentIdValue(" + id + ") return = [" + valueOk + "]");
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckPropertyTypeIdValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPropertyTypeIdValueLiveData() { return onCheckPropertyTypeIdValueMutableLiveData; }
    public boolean checkPropertyTypeIdValue(long id){
        boolean valueOk = (findPropertyTypeById(id) != null);
        onCheckPropertyTypeIdValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        Log.d(Tag.TAG, "checkPropertyTypeIdValue() return = [" + valueOk + "]");
        return valueOk;
    }

    public interface AddPropertyInterface{
        void onPropertyAdded(long propertyId);
    }
     /**
     * check values,
     * if values are ok send data to database and emit ok to view
     *
     * @param price
     * @param surface
     * @param description
     * @param addressTitle
     * @param address
     * @param pointOfInterest
     * @param available
     * @param entryDate
     * @param saleDate
     * @param propertyTypeId
     * @param forSale
     * @param agentId
     * @param rooms
     * @param latLng
     */
    public void insertOrUpdateProperty(
                            long propertyId,
                            String price,
                            String surface,
                            String description,
                            String addressTitle,
                            String address,
                            String pointOfInterest,
                            String entryDate,
                            String saleDate,
                            long propertyTypeId,
                            long agentId,
                            String rooms,
                            LatLng latLng,
                            AddPropertyInterface addPropertyInterface){

        // check all values
        boolean valuesOk = checkAllValues(price, surface, description, addressTitle, address,
                pointOfInterest, entryDate, saleDate, propertyTypeId, agentId, rooms, latLng);

        if (valuesOk) {
            double latitude = (latLng == null) ? 0 : latLng.latitude;
            double longitude = (latLng == null) ? 0 : latLng.longitude;

            int intPrice = Integer.parseInt(price);
            int intSurface = Integer.parseInt(surface);
            int intRooms = Integer.parseInt(rooms);

            Date dateEntryDate = Utils.convertStringInLocalFormatToDate(entryDate);
            Date dateSaleDate = Utils.convertStringInLocalFormatToDate(saleDate);

            Property property = new Property(propertyId,
                intPrice,
                intSurface,
                description,
                addressTitle,
                address,
                pointOfInterest,
                dateEntryDate,
                dateSaleDate,
                propertyTypeId,
                agentId,
                intRooms,
                latitude,
                longitude);

            try {
                if (propertyId == PropertyConst.PROPERTY_ID_NOT_INITIALIZED) {
                    propertyId = databaseRepository.getPropertyRepository().insert(property);
                } else
                    databaseRepository.getPropertyRepository().update(property);
                // Callback to close windows
                addPropertyInterface.onPropertyAdded(propertyId);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private MutableLiveData<Boolean> onCheckAllValuesMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> getOnCheckAllValuesLiveData() {return onCheckAllValuesMutableLiveData;}
    public boolean checkAllValues(String price,
                                  String surface,
                                  String description,
                                  String addressTitle,
                                  String address,
                                  String pointOfInterest,
                                  String entryDate,
                                  String saleDate,
                                  long propertyTypeId,
                                  long agentId,
                                  String rooms,
                                  LatLng latLng){

        // check all values
        boolean valuesOk = checkAddressTitleValue(addressTitle) &
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

        onCheckAllValuesMutableLiveData.setValue(valuesOk);
        return valuesOk;
    }

    private PropertyEditViewState getPropertyEditViewState(long propertyId){
        if (propertyId == PropertyConst.PROPERTY_ID_NOT_INITIALIZED){
            // default values
            return new PropertyEditViewState();
        } else {
            PropertyDetailData p = databaseRepository.getPropertyRepository().getPropertyDetailById(propertyId);

            String entryDate = Utils.convertDateToLocalFormat(p.getEntryDate());
            String saleDate = Utils.convertDateToLocalFormat(p.getSaleDate());
            String price = Integer.toString(p.getPrice());
            String surface = Integer.toString(p.getSurface());
            String rooms = Integer.toString(p.getRooms());

            return new PropertyEditViewState(p.getAddressTitle(),
                    p.getAddress(),
                    p.getDescription(),
                    p.getPointsOfInterest(),
                    price,
                    surface,
                    rooms,
                    entryDate,
                    saleDate,
                    p.getAgentId(),
                    p.getAgentName(),
                    p.getPropertyTypeId(),
                    p.getTypeName(),
                    p.getLatitude(),
                    p.getLongitude());
        }
    }

    public LiveData<PropertyEditViewState> getPropertyEditViewStateLiveData(long propertyId) {
        MutableLiveData<PropertyEditViewState> propertyEditViewStateMutableLiveData = new MutableLiveData<>();
        PropertyEditViewState propertyEditViewState = getPropertyEditViewState(propertyId);
        propertyEditViewStateMutableLiveData.setValue(propertyEditViewState);
        return propertyEditViewStateMutableLiveData;
    }
}
