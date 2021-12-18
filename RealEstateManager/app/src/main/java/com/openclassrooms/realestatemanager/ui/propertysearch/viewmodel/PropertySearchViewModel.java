package com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel;

import android.util.Log;
import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.PropertyRange;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertySearchParameters;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewstate.PropertySearchViewState;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PropertySearchViewModel extends ViewModel {

    private final float RANGE_PRICE_MAX = 50000f;
    private final float RANGE_SURFACE_MAX = 20000f;
    private final float RANGE_ROOMS_MAX = 50f;

    private List<Float> minMaxPrice;
    private List<Float> minMaxSurface;
    private List<Float> minMaxRooms;

    private Pair<Long, Long> valuesEntryDate;
    private Pair<Long, Long> valuesSaleDate;

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

    private MutableLiveData<List<Float>> valuesPriceMutableLiveData = new MutableLiveData<>();
    public void setPriceRange(List<Float> floats){
        valuesPriceMutableLiveData.setValue(floats);
    }

    private MutableLiveData<List<Float>> valuesSurfaceMutableLiveData = new MutableLiveData<>();
    public void setSurfaceRange(List<Float> floats){
        valuesSurfaceMutableLiveData.setValue(floats);
    }

    private MutableLiveData<List<Float>> valuesRoomsMutableLiveData = new MutableLiveData<>();
    public void setRoomsRange(List<Float> floats){
        valuesRoomsMutableLiveData.setValue(floats);
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

    private final MutableLiveData<Object> valueEntryDateMutableLiveData = new MutableLiveData<>();
    public void setValueEntryDate(Object selection){
        valueEntryDateMutableLiveData.setValue(selection);
    }

    private final MutableLiveData<Object> valueSaleDateMutableLiveData = new MutableLiveData<>();
    public void setValueSaleDate(Object selection){
        valueSaleDateMutableLiveData.setValue(selection);
    }

    private void configureMediatorLiveData() {
        LiveData<PropertyRange> minMaxLiveData = databaseRepository.getPropertyRepository().getPropertiesMinMaxRanges();

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
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(agentIndexMutableLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d(Tag.TAG, "PropertySearch ViewModel configureMediatorLiveData->agentIndexMutableLiveData->onChanged() called with: integer = [" + integer + "]");
                combine(agentItemsLiveData.getValue(), integer,
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(propertyTypeItemsLiveData, new Observer<List<DropdownItem>>() {
            @Override
            public void onChanged(List<DropdownItem> items) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        items, propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(propertyTypeIndexMutableLiveData, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), integer,
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(fullTextMutableLiveData, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        s,
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(minMaxLiveData, new Observer<PropertyRange>() {
            @Override
            public void onChanged(PropertyRange propertyRange) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        propertyRange,
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(valuesPriceMutableLiveData, new Observer<List<Float>>() {
            @Override
            public void onChanged(List<Float> floats) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        floats,
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(valuesSurfaceMutableLiveData, new Observer<List<Float>>() {
            @Override
            public void onChanged(List<Float> floats) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        floats,
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(valuesRoomsMutableLiveData, new Observer<List<Float>>() {
            @Override
            public void onChanged(List<Float> floats) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        floats,
                        valueEntryDateMutableLiveData.getValue(),
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(valueEntryDateMutableLiveData, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        o,
                        valueSaleDateMutableLiveData.getValue());
            }
        });

        propertySearchViewStateMediatorLiveData.addSource(valueSaleDateMutableLiveData, new Observer<Object>() {
            @Override
            public void onChanged(Object o) {
                combine(agentItemsLiveData.getValue(), agentIndexMutableLiveData.getValue(),
                        propertyTypeItemsLiveData.getValue(), propertyTypeIndexMutableLiveData.getValue(),
                        fullTextMutableLiveData.getValue(),
                        minMaxLiveData.getValue(),
                        valuesPriceMutableLiveData.getValue(),
                        valuesSurfaceMutableLiveData.getValue(),
                        valuesRoomsMutableLiveData.getValue(),
                        valueEntryDateMutableLiveData.getValue(),
                        o);
            }
        });
    }

    private List<Float> minMaxToListFloats(int min, int max){
        List<Float> floats = new ArrayList<>();
        floats.add((float) min);
        floats.add((float) max);

        return  floats;
    }

    private List<Float> propertyRangeToMinMaxPrice(PropertyRange propertyRange){

        int min = propertyRange.getMinPrice();
        // truncate (lower round)
        min = min / 1000;
        int max = propertyRange.getMaxPrice();
        max = Math.round(max / 1000);

        return  minMaxToListFloats(min, max);
    }

    public List<Float> propertyRangeToMinMaxSurface(PropertyRange propertyRange){

        int min = propertyRange.getMinSurface();
        int max = propertyRange.getMaxSurface();

        return  minMaxToListFloats(min, max);
    }

    public List<Float> propertyRangeToMinMaxRooms(PropertyRange propertyRange){

        int min = propertyRange.getMinRooms();
        int max = propertyRange.getMaxRooms();

        return  minMaxToListFloats(min, max);
    }

    private List<Float> createValuesFromMinMax(List<Float> minMax){
        return new ArrayList<>(minMax);
    }

    private List<Float> checkValues(List<Float> minMax, List<Float> values){
        if (values == null) {
            return createValuesFromMinMax(minMax);
        }
        else {
            if ((values.get(0) < minMax.get(0)) || (values.get(0) > minMax.get(1)))
                // put min value in min range
                values.set(0, minMax.get(0));
            if ((values.get(1) < minMax.get(0)) || (values.get(1) > minMax.get(1)))
                // put max value in max range
                values.set(1, minMax.get(1));
            return values;
        }
    };

    private String getCaptionPrice(List<Float> floats) {
        int min = floats.get(0).intValue() * 1000;
        int max = floats.get(1).intValue() * 1000;

        return String.format("%s %s %s",
                Utils.convertPriceToString(min),
                MainApplication.getApplication().getString(R.string.to),
                Utils.convertPriceToString(max));
    }

    private String getCaptionSurface(List<Float> floats) {
        int min = floats.get(0).intValue();
        int max = floats.get(1).intValue();

        return String.format("%s %s %s",
                Utils.convertSurfaceToString(min),
                MainApplication.getApplication().getString(R.string.to),
                Utils.convertSurfaceToString(max));
    }

    private String getCaptionRooms(List<Float> floats) {
        int min = floats.get(0).intValue();
        int max = floats.get(1).intValue();

        return String.format(Locale.getDefault(), "%d %s %d",
                min,
                MainApplication.getApplication().getString(R.string.to),
                max);
    }

    private String getCaptionDate(Pair<Long, Long> range){
        return (range == null) ?
                MainApplication.getApplication().getString(R.string.no_date_range_selected) :
                String.format("%s %s %s",
                        Utils.convertDateToString(new Date(range.first)),
                        MainApplication.getApplication().getString(R.string.to),
                        Utils.convertDateToString(new Date(range.second)));
    }


    private Pair<Long, Long> convertPair(Object range){
        // rangePicker provide range in Object who is androidx.core.util.Pair
        // convert to android.util.Pair to be compatible with PropertySearchParameters
        if (range == null) return null;

        androidx.core.util.Pair<Long, Long> coreUtilPair = (androidx.core.util.Pair<Long, Long>) range;
        android.util.Pair<Long, Long> convertedPair = new android.util.Pair<Long, Long>(coreUtilPair.first, coreUtilPair.second);
        return convertedPair;
    }

    private void combine(List<DropdownItem> agents, int agentIndex,
                         List<DropdownItem> propertyTypes, int propertyTypeIndex,
                         String fullText,
                         PropertyRange propertyRange,
                         List<Float> valuesPrice, List<Float> valuesSurface, List<Float> valuesRooms,
                         Object selectionEntryDate, Object selectionSaleDate){

        if ((agents == null) || (propertyTypes == null) || (propertyRange == null))
            return;

        Log.d(Tag.TAG, "PropertySearch ViewModel combine() called with: agentIndex = [" + agentIndex + "]");
        Log.d(Tag.TAG, "PropertySearch ViewModel combine() called with: propertyTypeIndex = [" + propertyTypeIndex + "]");
        Log.d(Tag.TAG, "PropertySearch ViewModel combine() called with: fullText = [" + fullText + "]");

        minMaxPrice = propertyRangeToMinMaxPrice(propertyRange);
        minMaxSurface = propertyRangeToMinMaxSurface(propertyRange);
        minMaxRooms = propertyRangeToMinMaxRooms(propertyRange);

        valuesPrice = checkValues(minMaxPrice, valuesPrice);
        valuesSurface = checkValues(minMaxSurface, valuesSurface);
        valuesRooms = checkValues(minMaxRooms, valuesRooms);

        String captionPrice = getCaptionPrice(valuesPrice);
        String captionSurface = getCaptionSurface(valuesSurface);
        String captionRooms = getCaptionRooms(valuesRooms);

        valuesEntryDate = convertPair(selectionEntryDate);
        String captionEntryDate = getCaptionDate(valuesEntryDate);

        valuesSaleDate = convertPair(selectionSaleDate);
        String captionSaleDate = getCaptionDate(valuesSaleDate);

        PropertySearchViewState propertySearchViewState = new PropertySearchViewState(agents, agentIndex,
                propertyTypes, propertyTypeIndex,
                fullText,
                minMaxPrice, valuesPrice, captionPrice,
                minMaxSurface, valuesSurface, captionSurface,
                minMaxRooms, valuesRooms, captionRooms,
                captionEntryDate, captionSaleDate);

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
        List<Float> priceRange = valuesPriceMutableLiveData.getValue();
        if (priceRange != null) {
            int min = priceRange.get(0).intValue() * 1000;
            int max = priceRange.get(1).intValue() * 1000;

            psp.setPrice(new Pair<>(min, max));
        }

        List<Float> surfaceRange = valuesSurfaceMutableLiveData.getValue();
        if (surfaceRange != null) {
            int min = surfaceRange.get(0).intValue();
            int max = surfaceRange.get(1).intValue();

            psp.setSurface(new Pair<>(min, max));
        }

        List<Float> roomsRange = valuesRoomsMutableLiveData.getValue();
        if (roomsRange != null) {
            int min = roomsRange.get(0).intValue();
            int max = roomsRange.get(1).intValue();
            psp.setRooms(new Pair<>(min, max));
        }

        if (valuesEntryDate != null) {
            psp.setEntryDate(valuesEntryDate);
        }

        if (valuesSaleDate != null) {
            psp.setSaleDate(valuesSaleDate);
        }

        databaseRepository.getPropertyRepository().setPropertySearchParameters(psp);
    }

    public void resetSearch(){
        agentIndexMutableLiveData.setValue(0);
        propertyTypeIndexMutableLiveData.setValue(0);
        fullTextMutableLiveData.setValue("");
        setPriceRange(minMaxPrice);
        setSurfaceRange(minMaxSurface);
        setRoomsRange(minMaxRooms);
        setValueEntryDate(null);
        setValueSaleDate(null);

        setSearchValues(-1, -1);
    }
}
