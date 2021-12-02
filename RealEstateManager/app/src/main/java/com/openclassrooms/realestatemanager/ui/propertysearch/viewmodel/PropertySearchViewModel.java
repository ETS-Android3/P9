package com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertySearchParameters;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PropertySearchViewModel extends ViewModel {
    private final DatabaseRepository databaseRepository;

    public PropertySearchViewModel(DatabaseRepository databaseRepository) {
        Log.d(Tag.TAG, "PropertySearchViewModel() called with: databaseRepository = [" + databaseRepository + "]");
        this.databaseRepository = databaseRepository;

        agentIndexMutableLiveData.setValue(0);
        propertyTypeIndexMutableLiveData.setValue(0);
        configureMediatorLiveData();
    }

    /**
     * Mediator expose PropertySearchViewState
     */
    private final MediatorLiveData<PropertySearchViewState> propertySearchViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<PropertySearchViewState> getViewState() { return propertySearchViewStateMediatorLiveData; }

    private final MutableLiveData<Integer> agentIndexMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> getAgentIndexMutableLiveData() {
        return agentIndexMutableLiveData;
    }

    private final MutableLiveData<Integer> propertyTypeIndexMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> getPropertyTypeMutableLiveData() {
        return propertyTypeIndexMutableLiveData;
    }

    private void configureMediatorLiveData() {
        LiveData<List<DropdownItem>> agentItemsLiveData = Transformations.map(databaseRepository.getAgentRepository().getAgentsLiveData(),
                agents -> {
                    List<DropdownItem> items = new ArrayList<>();
                    items.add(new DropdownItem(-1, MainApplication.getApplication().getString(R.string.all_agents)));
                    for (Agent agent : agents) {
                        items.add(new DropdownItem(agent.getId(), agent.getName()));
                    }
                    return items;
                });

        LiveData<List<DropdownItem>> propertyTypeItemsLiveData = Transformations.map(databaseRepository.getPropertyTypeRepository().getPropertyTypesLiveData(),
                propertyTypes -> {
                    List<DropdownItem> items = new ArrayList<>();
                    items.add(new DropdownItem(-1, MainApplication.getApplication().getString(R.string.all_property_types)));
                    for (PropertyType propertyType : propertyTypes) {
                        items.add(new DropdownItem(propertyType.getId(), propertyType.getName()));
                    }
                    return items;
                });

        propertySearchViewStateMediatorLiveData.addSource(agentItemsLiveData, new Observer<List<DropdownItem>>() {
            @Override
            public void onChanged(List<DropdownItem> items) {
                combine(items, agentIndexMutableLiveData.getValue(), propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(agentIndexMutableLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(Tag.TAG, "PropertySearchViewModel.onChanged() called with: integer = [" + integer + "]");
                combine(agentItemsLiveData.getValue(), integer, propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(propertyTypeItemsLiveData, new Observer<List<DropdownItem>>() {
            @Override
            public void onChanged(List<DropdownItem> items) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(), items, propertyTypeIndexMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(propertyTypeIndexMutableLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(), propertyTypeItemsLiveData.getValue(), integer);
            }
        });

    }

    private void combine(List<DropdownItem> agents, int agentIndex,
                         List<DropdownItem> propertyTypes, int propertyTypeIndex){

        if ((agents == null) || (propertyTypes == null))
            return;

/*        String addressTitle;
        String address;
        String description;
        String pointOfInterest;
        Long agentId;
        Long propertyTypeId;
        int minPrice;
        int maxPrice;
        int minSurface;
        int maxSurface;
        int minRooms;
        int maxRooms;
        Date minEntryDate;
        Date maxEntryDate;
        Date minSaleDate;
        Date maxSaleDate;*/


        Log.d(Tag.TAG, "PropertySearchViewModel.combine() called with: agentIndex = [" + agentIndex + "]");
        Log.d(Tag.TAG, "PropertySearchViewModel.combine() called with: propertyTypeIndex = [" + propertyTypeIndex + "]");
        PropertySearchViewState propertySearchViewState = new PropertySearchViewState(agents, agentIndex, propertyTypes, propertyTypeIndex);

        propertySearchViewStateMediatorLiveData.setValue(propertySearchViewState);
    }

    public void setSearchValues(String fullText, long agentId, long propertyType){
        PropertySearchParameters psp = new PropertySearchParameters();
        psp.setFullText(fullText);
        if (agentId >= 0) psp.setAgentId(agentId);
        if (propertyType >= 0) psp.setPropertyTypeId(propertyType);

        Log.d(Tag.TAG, "PropertySearchViewModel.setSearchValues() agentIndex = [" + agentIndexMutableLiveData.getValue() + "]");
        Log.d(Tag.TAG, "PropertySearchViewModel.setSearchValues() agentIndex = [" + agentIndexMutableLiveData.getValue() + "]");
        databaseRepository.getPropertyRepository().setPropertySearchParameters(psp);
    }

    public void resetSearch(){
        databaseRepository.getPropertyRepository().resetSearch();
    }
}
