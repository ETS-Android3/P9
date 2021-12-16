package com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleStaticMapRepository;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.PropertyDetailData;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewstate.PropertyDetailViewState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.List;

public class PropertyDetailViewModel extends ViewModel {

    /**
     * repositories
     */
    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final GoogleStaticMapRepository googleStaticMapRepository;

    private long currentPropertyId = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;

    public long getCurrentPropertyId() {
        return currentPropertyId;
    }

    /**
     * Mediator expose PropertyListViewState
     */
    private final MediatorLiveData<PropertyDetailViewState> propertyDetailViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<PropertyDetailViewState> getViewState() { return propertyDetailViewStateMediatorLiveData; }

    public PropertyDetailViewModel(@NonNull DatabaseRepository databaseRepository,
                                   @NonNull GoogleStaticMapRepository googleStaticMapRepository) {
        this.databaseRepository = databaseRepository;
        this.googleStaticMapRepository = googleStaticMapRepository;
    }

    private void configureMediatorLiveData(long propertyId) {
        Log.d(Tag.TAG, "PropertyDetailViewModel.configureMediatorLiveData(propertyId=" + propertyId + ")");
        // Property detail with agent information, and type
        LiveData<PropertyDetailData> propertyDetailDataLiveData = databaseRepository.getPropertyRepository().getPropertyDetailByIdLiveData(propertyId);
        // photos
        LiveData<List<Photo>> photosLiveData = databaseRepository.getPhotoRepository().getPhotosByPropertyId(propertyId);

        propertyDetailViewStateMediatorLiveData.addSource(propertyDetailDataLiveData,
                propertyDetailData -> combine(propertyDetailData, photosLiveData.getValue()));

        propertyDetailViewStateMediatorLiveData.addSource(photosLiveData,
                photos -> combine(propertyDetailDataLiveData.getValue(), photos));
    }

    public LiveData<Long> getFirstOrValidId(long initialId){
        Log.d(Tag.TAG, "getFirstOrValidId() called with: initialId = [" + initialId + "]");
        return databaseRepository.getPropertyRepository().getFirstOrValidIdLiveData(initialId);
    }

    public void load(long propertyId){
        Log.d(Tag.TAG, "PropertyDetailViewModel.load(" + propertyId + ")");
        this.currentPropertyId = propertyId;
        configureMediatorLiveData(propertyId);
    }

    private void combine(@Nullable PropertyDetailData propertyDetailData,
                         @Nullable List<Photo> photos){

        if (propertyDetailData == null || photos == null) {
            return;
        }
        Log.d(Tag.TAG, "PropertyDetailViewModel.combine() called");

        int propertyStateResId;

        if (propertyDetailData.getSaleDate() == null) {
            propertyStateResId = R.string.for_sal;
        } else {
            propertyStateResId = R.string.sold;
        }

        String entryDate = Utils.convertDateToString(propertyDetailData.getEntryDate());
        String saleDate = Utils.convertDateToString(propertyDetailData.getSaleDate());

        // googleStaticMapRepository is not async so we can call it in combine
        String googleStaticMapUrl = googleStaticMapRepository.getUrlImage(propertyDetailData.getLatitude(), propertyDetailData.getLongitude());

        // ViewModel emit ViewState
        propertyDetailViewStateMediatorLiveData.setValue(new PropertyDetailViewState(propertyDetailData,
                photos, propertyStateResId, entryDate, saleDate, googleStaticMapUrl));
    }
}

