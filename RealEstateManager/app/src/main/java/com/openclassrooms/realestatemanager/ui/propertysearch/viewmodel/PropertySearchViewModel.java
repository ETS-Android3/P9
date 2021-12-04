package com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel;

import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.LogManager;

public class PropertySearchViewModel extends ViewModel {

    private final float RANGE_PRICE_MAX = 50000f;
    private final DatabaseRepository databaseRepository;

    public PropertySearchViewModel(DatabaseRepository databaseRepository) {
        Log.d(Tag.TAG, "PropertySearch ViewModel() called with: databaseRepository = [" + databaseRepository + "]");
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

    private final MutableLiveData<String> fullTextMutableLiveData = new MutableLiveData<>();
    public void afterFullTextChanged(String text){
        Log.d(Tag.TAG, "PropertySearch ViewModel setFullText() called with: text = [" + text + "]");
        if (fullTextMutableLiveData.getValue() == null)
            fullTextMutableLiveData.setValue(text);
        else
            if (! fullTextMutableLiveData.getValue().equals(text))
                fullTextMutableLiveData.setValue(text);
    }

    private MutableLiveData<List<Float>> priceRangeMutableLiveData = new MutableLiveData<>();
    public LiveData<List<Float>> getPriceRangeLiveData() {
        return priceRangeMutableLiveData;
    }
    public void setPriceRange(List<Float> floats){
        priceRangeMutableLiveData.setValue(floats);
    }

    private MutableLiveData<String> priceRangeCaptionMutableLiveData = new MutableLiveData<>();
    public LiveData<String> getPriceRangeCaptionLiveData() {
        return Transformations.map(priceRangeMutableLiveData, floats -> {
            int min = floats.get(0).intValue() * 1000;
            int max = floats.get(1).intValue() * 1000;

            return String.format("%s to %s",
                    Utils.convertPriceToString(min),
                    Utils.convertPriceToString(max));
        });
    }

    private String getFullText(){
        Log.d(Tag.TAG, "getFullText: " + fullTextMutableLiveData.getValue());
        return fullTextMutableLiveData.getValue();
    }

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
                combine(items, agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(agentIndexMutableLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(Tag.TAG, "PropertySearch ViewModel configureMediatorLiveData->agentIndexMutableLiveData->onChanged() called with: integer = [" + integer + "]");
                combine(agentItemsLiveData.getValue(), integer,
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(propertyTypeItemsLiveData, new Observer<List<DropdownItem>>() {
            @Override
            public void onChanged(List<DropdownItem> items) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        items, propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(propertyTypeIndexMutableLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), integer,
                        fullTextMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(fullTextMutableLiveData, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        s);

            }
        });

/*        propertySearchViewStateMediatorLiveData.addSource(priceRangeMutableLiveData, new Observer<Pair<Integer, Integer>>() {
            @Override
            public void onChanged(Pair<Integer, Integer> integerIntegerPair) {

            }
        });*/
    }

    private void combine(List<DropdownItem> agents, int agentIndex,
                         List<DropdownItem> propertyTypes, int propertyTypeIndex,
                         String fullText){

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

        Log.d(Tag.TAG, "PropertySearch ViewModel combine() called with: agentIndex = [" + agentIndex + "]");
        Log.d(Tag.TAG, "PropertySearch ViewModel combine() called with: propertyTypeIndex = [" + propertyTypeIndex + "]");
        Log.d(Tag.TAG, "PropertySearch ViewModel combine() called with: fullText = [" + fullText + "]");

        PropertySearchViewState propertySearchViewState = new PropertySearchViewState(agents, agentIndex, propertyTypes, propertyTypeIndex, fullText);

        propertySearchViewStateMediatorLiveData.setValue(propertySearchViewState);
    }

    public void setSearchValues(long agentId, long propertyType){
        Log.d(Tag.TAG, "PropertySearch ViewModel setSearchValues() agentIndex = [" + agentIndexMutableLiveData.getValue() + "]");
        Log.d(Tag.TAG, "PropertySearch ViewModel setSearchValues() propertyTypeIndex = [" + propertyTypeIndexMutableLiveData.getValue() + "]");
        Log.d(Tag.TAG, "PropertySearch ViewModel setSearchValues() fullText = [" + getFullText() + "]");

        PropertySearchParameters psp = new PropertySearchParameters();

        psp.setFullText(getFullText());

        if (agentId >= 0) psp.setAgentId(agentId);
        if (propertyType >= 0) psp.setPropertyTypeId(propertyType);

        // price values are displayed in Kilo $
        List<Float> floats = priceRangeMutableLiveData.getValue();
        int min = floats.get(0).intValue() * 1000;
        int max = floats.get(1).intValue() * 1000;
        psp.setPrice(new Pair<>(min, max));

        databaseRepository.getPropertyRepository().setPropertySearchParameters(psp);
    }

    public void resetSearch(){
        agentIndexMutableLiveData.setValue(0);
        propertyTypeIndexMutableLiveData.setValue(0);
        fullTextMutableLiveData.setValue("");
        setPriceRange(Arrays.asList(0f, RANGE_PRICE_MAX));

        setSearchValues(-1, -1);
    }
}
