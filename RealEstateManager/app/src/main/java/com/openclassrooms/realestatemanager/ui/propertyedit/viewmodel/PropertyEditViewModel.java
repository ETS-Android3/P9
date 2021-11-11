package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.net.Uri;
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
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleStaticMapRepository;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownViewstate;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.FieldState;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.PropertyEditViewState;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.StaticMapViewState;
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
    @NonNull
    private final GoogleStaticMapRepository googleStaticMapRepository;

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
    public PropertyEditViewModel(@NonNull DatabaseRepository databaseRepository,
                                 @NonNull GoogleGeocodeRepository googleGeocodeRepository,
                                 @NonNull GoogleStaticMapRepository googleStaticMapRepository) {
        this.databaseRepository = databaseRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
        this.googleStaticMapRepository = googleStaticMapRepository;

        cache = new CachePropertyEditViewModel();
        cache.getAgents().addAll(databaseRepository.getAgentRepository().getAgents());
        cache.getPropertyTypes().addAll(databaseRepository.getPropertyTypeRepository().getPropertyTypes());

        // default control values
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
        configureGoogleStaticMapUrlLiveData();
    }

    private MutableLiveData<String> addressMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<String> getAddressMutableLiveData() {
        return addressMutableLiveData;
    }

    private MediatorLiveData<StaticMapViewState> googleStaticMapViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<StaticMapViewState> getGoogleStaticMapViewState() {
        return googleStaticMapViewStateMediatorLiveData;
    }

    public void configureGoogleStaticMapUrlLiveData(){
        Log.d(Tag.TAG, "PropertyEditViewModel.configureGoogleStaticMapUrlLiveData() called");
        LiveData<LatLng> latLngLiveData = Transformations.switchMap(addressMutableLiveData,
                address -> {return googleGeocodeRepository.getLocationByAddressLiveData(address);});

        googleStaticMapViewStateMediatorLiveData.addSource(addressMutableLiveData, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.d(Tag.TAG, "PropertyEditViewModel.configureGoogleStaticMapUrlLiveData -> address onChanged() called with: s = [" + s + "]");
            }
        });

        googleStaticMapViewStateMediatorLiveData.addSource(latLngLiveData, new Observer<LatLng>() {
            @Override
            public void onChanged(LatLng latLng) {
                Log.d(Tag.TAG, "PropertyEditViewModel.configureGoogleStaticMapUrlLiveData -> url onChanged() called with: latLng = [" + latLng + "]");
                String url = googleStaticMapRepository.getUrlImage(latLng.latitude, latLng.longitude);
                googleStaticMapViewStateMediatorLiveData.setValue(new StaticMapViewState(latLng, url));
            }
        });
    }

    public LiveData<PropertyEditViewState> getViewStateLiveData(long propertyId) {
        Log.d(Tag.TAG, "PropertyEditViewModel.getViewStateLiveData.() called with: propertyId = [" + propertyId + "]");
        LiveData<List<Photo>> pendingPhotosLiveData = cache.getPendingPhotosLiveData();
        LiveData<PropertyDetailData> propertyDetailDataLiveData = databaseRepository.getPropertyRepository().getPropertyDetailByIdLiveData(propertyId);
        LiveData<List<Photo>> databasePhotosLiveData = databaseRepository.getPhotoRepository().getPhotosByPropertyId(propertyId);

        MediatorLiveData<PropertyEditViewState> mediatorLiveData = new MediatorLiveData<>();
        mediatorLiveData.addSource(propertyDetailDataLiveData,
                new Observer<PropertyDetailData>() {
                    @Override
                    public void onChanged(PropertyDetailData propertyDetailData) {
                        Log.d(Tag.TAG, "PropertyEditViewModel.getViewStateLiveData()->combine 1. propertyId = [" + propertyId + "]");
                        combine(mediatorLiveData,
                                propertyId,
                                propertyDetailData,
                                databasePhotosLiveData.getValue(),
                                pendingPhotosLiveData.getValue());
                    }
                });

        mediatorLiveData.addSource(databasePhotosLiveData,
                new Observer<List<Photo>>() {
                    @Override
                    public void onChanged(List<Photo> photos) {
                        Log.d(Tag.TAG, "PropertyEditViewModel.getViewStateLiveData()->combine 2. propertyId = [" + propertyId + "]");

                        combine(mediatorLiveData,
                                propertyId,
                                propertyDetailDataLiveData.getValue(),
                                photos,
                                pendingPhotosLiveData.getValue());
                    }
                });

        mediatorLiveData.addSource(pendingPhotosLiveData,
                new Observer<List<Photo>>() {
                    @Override
                    public void onChanged(List<Photo> photos) {
                        Log.d(Tag.TAG, "PropertyEditViewModel.getViewStateLiveData()->combine 3. propertyId = [" + propertyId + "]");

                        combine(mediatorLiveData,
                                propertyId,
                                propertyDetailDataLiveData.getValue(),
                                databasePhotosLiveData.getValue(),
                                photos);
                    }
                });
        return mediatorLiveData;
    }

    private String debugString(String value) {
        final int MAX_CAR = 20;
        if ((value == null) || (value.length() <= MAX_CAR)){
            return value;
        }
        else {
            return value.substring(0, MAX_CAR) + "...";
        }
    }

    private String getLastValue(RememberFieldKey cacheKey, String databaseValue){
        String cacheValue = cache.getRememberFieldList().getValue(cacheKey);
        Log.d(Tag.TAG, "PropertyEditViewModel.getLastValue() cacheKey = [" + cacheKey + "] cacheValue + [" + cacheValue + "]");
        Log.d(Tag.TAG, "PropertyEditViewModel.getLastValue() databaseValue = [" + debugString(databaseValue) + "]");
        String result = (cacheValue == null) ? databaseValue : cacheValue;
        Log.d(Tag.TAG, "PropertyEditViewModel.getLastValue() return = [" + debugString(result) + "]");
        return  result;
    }

    private long getLastValue(RememberFieldKey cacheKey, long databaseValue){
        String cacheValue = cache.getRememberFieldList().getValue(cacheKey);
        if (cacheValue != null) {

        }
        Log.d(Tag.TAG, "PropertyEditViewModel.getLastValue() cacheKey = [" + cacheKey + "] cacheValue + [" + cacheValue + "]");
        Log.d(Tag.TAG, "PropertyEditViewModel.getLastValue() databaseValue = [" + databaseValue + "]");
        long result = 0;
        if (cacheValue == null) {
            result = databaseValue;
        } else {
            cacheValue = cacheValue.trim();
            if (cacheValue.isEmpty()) {
                result = databaseValue;
            } else {
                result = Long.parseLong(cacheValue);
            }
        }
        Log.d(Tag.TAG, "PropertyEditViewModel.getLastValue() return = [" + result + "]");
        return result;
    }

    private void combine(MediatorLiveData<PropertyEditViewState> mediatorLiveData,
                         long propertyId,
                         PropertyDetailData propertyDetailData,
                         List<Photo> databasePhotos,
                         List<Photo> pendingPhotos) {
        Log.d(Tag.TAG, "PropertyEditViewModel.combine() called with: propertyId = [" + propertyId + "], propertyDetailData = [" + propertyDetailData + "], databasePhotos = [" + databasePhotos + "], pendingPhotos = [" + pendingPhotos + "]");

        if (propertyId == PropertyConst.PROPERTY_ID_NOT_INITIALIZED) {
            PropertyEditViewState propertyEditViewState = new PropertyEditViewState(pendingPhotos);
            // todo : get cache !
            mediatorLiveData.setValue(propertyEditViewState);
            return;
        }

        if (propertyDetailData == null) {
            return;
        }

        String databaseEntryDate = Utils.convertDateToLocalFormat(propertyDetailData.getEntryDate());
        String databaseSaleDate = Utils.convertDateToLocalFormat(propertyDetailData.getSaleDate());
        String databasePrice = Integer.toString(propertyDetailData.getPrice());
        String databaseSurface = Integer.toString(propertyDetailData.getSurface());
        String databaseRooms = Integer.toString(propertyDetailData.getRooms());

        List<Photo> photos = new ArrayList<>();
        if (pendingPhotos != null) {
            photos.addAll(pendingPhotos);
        }
        if(databasePhotos != null) {
            photos.addAll(databasePhotos);
        }

        // get values from cache or from database
        String addressTitle = getLastValue(RememberFieldKey.ADDRESS_TITLE, propertyDetailData.getAddressTitle());
        String address = getLastValue(RememberFieldKey.ADDRESS, propertyDetailData.getAddress());
        String description = getLastValue(RememberFieldKey.DESCRIPTION, propertyDetailData.getDescription());
        String pointOfInterest = getLastValue(RememberFieldKey.POINT_OF_INTEREST, propertyDetailData.getPointsOfInterest());
        String price = getLastValue(RememberFieldKey.PRICE, databasePrice);
        String surface = getLastValue(RememberFieldKey.SURFACE, databaseSurface);
        String rooms = getLastValue(RememberFieldKey.ROOMS, databaseRooms);
        String entryDate = getLastValue(RememberFieldKey.ENTRY_DATE, databaseEntryDate);
        String saleDate = getLastValue(RememberFieldKey.SALE_DATE, databaseSaleDate);

        long agentId = getLastValue(RememberFieldKey.AGENT_ID, propertyDetailData.getAgentId());
        String agentName = getLastValue(RememberFieldKey.AGENT_NAME, propertyDetailData.getAgentName());
        long propertyTypeId = getLastValue(RememberFieldKey.PROPERTY_TYPE_ID, propertyDetailData.getAgentId());
        String propertyTypeName = getLastValue(RememberFieldKey.PROPERTY_TYPE_NAME, propertyDetailData.getTypeName());

        // googleStaticMapRepository is not async so we can call it in combine
        String googleStaticMapUrl = googleStaticMapRepository.getUrlImage(propertyDetailData.getLatitude(), propertyDetailData.getLongitude());

        PropertyEditViewState propertyEditViewState = new PropertyEditViewState(
                addressTitle,
                address,
                description,
                pointOfInterest,
                price,
                surface,
                rooms,
                entryDate,
                saleDate,
                agentId,
                agentName,
                propertyTypeId,
                propertyTypeName,
                propertyDetailData.getLatitude(),
                propertyDetailData.getLongitude(),
                photos, googleStaticMapUrl);
        mediatorLiveData.setValue(propertyEditViewState);
    }

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
        return  (error) ? R.string.value_required : PropertyConst.NO_STRING_ID;
    }

    private boolean checkIsInt(String value){
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private MutableLiveData<FieldState> onCheckAddressTitleValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAddressTitleValueLiveData() { return onCheckAddressTitleValueMutableLiveData; }
    public boolean checkAddressTitleValue(String value){
        boolean valueOk = !PropertyEditViewModel.emptyString(value);
        onCheckAddressTitleValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckAddressValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAddressValueLiveData() { return onCheckAddressValueMutableLiveData; }
    public boolean checkAddressValue(String value){
        boolean valueOk = !PropertyEditViewModel.emptyString(value);
        onCheckAddressValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckDescriptionValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckDescriptionValueLiveData() { return onCheckDescriptionValueMutableLiveData; }
    public boolean checkDescriptionValue(String value){
        boolean valueOk =!PropertyEditViewModel.emptyString(value);
        onCheckDescriptionValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckPointOfInterestValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPointOfInterestValueLiveData() { return onCheckPointOfInterestValueMutableLiveData; }
    public boolean checkPointOfInterestValue(String value){
        boolean valueOk = !PropertyEditViewModel.emptyString(value);
        onCheckPointOfInterestValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckPriceValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPriceValueLiveData() { return onCheckPriceValueMutableLiveData; }
    public boolean checkPriceValue(String value){
        boolean valueOk = checkIsInt(value);
        onCheckPriceValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckSurfaceValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckSurfaceValueLiveData() { return onCheckSurfaceValueMutableLiveData; }
    public boolean checkSurfaceValue(String value){
        boolean valueOk = checkIsInt(value);
        onCheckSurfaceValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckRoomsValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckRoomsValueLiveData() { return onCheckRoomsValueMutableLiveData; }
    public boolean checkRoomsValue(String value){
        boolean valueOk = checkIsInt(value);
        onCheckRoomsValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckEntryDateValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckEntryDateValueLiveData() { return onCheckEntryDateValueMutableLiveData; }
    public boolean checkEntryDateValue(String value){
        boolean valueOk = PropertyEditViewModel.validDate(value);
        onCheckEntryDateValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckSaleDateValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckSaleDateValueLiveData() { return onCheckSaleDateValueMutableLiveData; }
    public boolean checkSaleDateValue(String value){
        boolean valueOk = PropertyEditViewModel.validOrNullDate(value);
        onCheckSaleDateValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckAgentIdValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckAgentIdValueLiveData() { return onCheckAgentIdValueMutableLiveData; }
    public boolean checkAgentIdValue(long id){
        boolean valueOk = (findAgentById(id) != null);
        onCheckAgentIdValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private MutableLiveData<FieldState> onCheckPropertyTypeIdValueMutableLiveData = new MutableLiveData<>();
    public LiveData<FieldState> getOnCheckPropertyTypeIdValueLiveData() { return onCheckPropertyTypeIdValueMutableLiveData; }
    public boolean checkPropertyTypeIdValue(long id){
        boolean valueOk = (findPropertyTypeById(id) != null);
        onCheckPropertyTypeIdValueMutableLiveData.setValue(new FieldState(getResIdError(!valueOk)));
        return valueOk;
    }

    private boolean checkPendingPhoto(){
        return cache.isAllPhotoOk();
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
     * @param entryDate
     * @param saleDate
     * @param propertyTypeId
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
                    // now we have new property id and we can send pending photos to database
                    List<Photo> photos = new ArrayList<>();
                    photos.addAll(cache.getPendingPhotos());
                    for (Photo photo : photos) {
                        // change property id with new property id
                        photo.setPropertyId(propertyId);
                        // clear cache and send to database
                        updatePhoto(photo);
                    }
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
        boolean valuesOk = checkPriceValue(price) &
                checkSurfaceValue(surface) &
                checkDescriptionValue(description) &
                checkAddressTitleValue(addressTitle) &
                checkAddressValue(address) &
                checkPointOfInterestValue(pointOfInterest) &
                checkEntryDateValue(entryDate) &
                checkSaleDateValue(saleDate) &
                checkPropertyTypeIdValue(propertyTypeId) &
                checkAgentIdValue(agentId) &
                checkRoomsValue(rooms) &
                checkPendingPhoto();

        onCheckAllValuesMutableLiveData.setValue(valuesOk);
        return valuesOk;
    }


    public void addPhoto(Uri uri, String caption, long propertyId){
        Log.d(Tag.TAG, "PropertyEditViewModel.addPhoto() called with: caption = [" + caption + "], propertyId = [" + propertyId + "], uri = [\" + uri + \"]");
        Photo photo = new Photo(0, 0, uri.toString(), caption, propertyId);
        updatePhoto(photo);
    }

    /**
     * send photo to cache or to database
     * @param photo
     */
    public void updatePhoto(Photo photo){
        if ((photo.getPropertyId() == PropertyConst.PROPERTY_ID_NOT_INITIALIZED) || (!cache.isValidePhoto(photo))) {
            cache.update(photo);
        }
        else {
            cache.removePhoto(photo);
            if (photo.getId() == PropertyConst.PHOTO_ID_NOT_INITIALIZED) {
                databaseRepository.getPhotoRepository().insert(photo);
            }
            else {
                databaseRepository.getPhotoRepository().update(photo);
            }
        }
    }

    public void clearFieldsCache(){
        cache.getRememberFieldList().clearAll();
    }

    public boolean rememberValue(RememberFieldKey key, String value){
        cache.getRememberFieldList().setValue(key, value);
        return false;
    }

    public void deletePhoto(Photo photo){
        cache.removePhoto(photo);
        if (photo.getId() != PropertyConst.PHOTO_ID_NOT_INITIALIZED) {
            databaseRepository.getPhotoRepository().delete(photo.getId());
        }
    }
}
