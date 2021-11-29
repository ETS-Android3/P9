package com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;

public class PropertySearchViewModel extends ViewModel {
    private final DatabaseRepository databaseRepository;

    /**
     * Mediator expose PropertySearchViewState
     */
    private final MediatorLiveData<PropertySearchViewState> propertySearchViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<PropertySearchViewState> getViewState() { return propertySearchViewStateMediatorLiveData; }

    public PropertySearchViewModel(DatabaseRepository databaseRepository) {
        Log.d(Tag.TAG, "PropertySearchViewModel() called with: databaseRepository = [" + databaseRepository + "]");
        this.databaseRepository = databaseRepository;

        configureMediatorLiveData();
    }

    private void configureMediatorLiveData() {

    }

    private void combine(){}
}
