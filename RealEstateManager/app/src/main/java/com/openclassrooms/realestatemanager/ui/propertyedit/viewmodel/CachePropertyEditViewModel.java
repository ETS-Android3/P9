package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;
import com.openclassrooms.realestatemanager.tag.Tag;

import java.util.ArrayList;
import java.util.List;

public class CachePropertyEditViewModel {

    private long propertyId;
    private final List<Agent> agents;
    private final List<PropertyType> propertyTypes;
    private final List<Photo> pendingPhotos;
    private final FieldList fields;

    public CachePropertyEditViewModel() {
        agents = new ArrayList<>();
        propertyTypes = new ArrayList<>();
        pendingPhotos = new ArrayList<>();
        fields = new FieldList();
    }

    public void setPropertyId(long id) {
        if (this.propertyId != id) {
            // clear cache when id change
            clear();
            this.propertyId = id;
        }
    }

    public List<Agent> getAgents() {
        return agents;
    }
    public void setAgents(List<Agent> agents) {
        this.agents.clear();
        this.agents.addAll(agents);
    }

    public List<PropertyType> getPropertyTypes() {
        return propertyTypes;
    }
    public void setPropertyTypes(List<PropertyType> propertyTypes) {
        this.propertyTypes.clear();
        this.propertyTypes.addAll(propertyTypes);
    }

    public List<Photo> getPendingPhotos() {
        return pendingPhotos;
    }

    private final MutableLiveData<List<Photo>> pendingPhotosMutableLiveData = new MutableLiveData<>();
    public LiveData<List<Photo>> getPendingPhotosLiveData(){ return pendingPhotosMutableLiveData; }

    private int indexOfPhoto(Photo photo){
        for (int i=0; i<pendingPhotos.size(); i++){
            if (pendingPhotos.get(i).getUrl().equals(photo.getUrl())) {
                return i;
            }
        }
        return -1;
    }

    public void update(Photo photo){
        Log.d(Tag.TAG, "CachePropertyEditViewModel.addPhoto() called with: photo = [" + photo + "]");
        removePhoto(photo);
        pendingPhotos.add(photo);
        pendingPhotosMutableLiveData.setValue(pendingPhotos);
    }

    public void removePhoto(Photo photo){
        int idx = indexOfPhoto(photo);
        Log.d(Tag.TAG, "CachePropertyEditViewModel.removePhoto() called with: idx=[" + idx + "] photo = [" + photo + "]");
        if (idx > -1){
            pendingPhotos.remove(idx);
            pendingPhotosMutableLiveData.setValue(pendingPhotos);
        }
    }

    public boolean isNotValidPhoto(Photo photo){
        return (photo.getLegend().isEmpty());
    }

    private int getInvalidPhotoCaptionCount(){
        int count = 0;
        for (Photo photo : pendingPhotos) {
            if (isNotValidPhoto(photo)) {
                count++;
            }
        }
        return count;
    }

    public boolean isAllPhotoOk(){
        return getInvalidPhotoCaptionCount() == 0;
    }

    public void setValue(FieldKey key, String value){
        fields.setValue(key, value);
    }

    public String getValue(FieldKey key){
        return fields.getValue(key);
    }

    /**
     * if cache exist get cache value else get database value
     * @param key - Key
     * @param defaultValue - Default value
     * @return - Value from cache or value from database
     */

    public String getValue(FieldKey key, String defaultValue){
        String cacheValue = getValue(key);
        //Log.d(Tag.TAG, "cache.getValue() key = [" + key + "] cacheValue + [" + cacheValue + "] defaultValue = [" + debugString(defaultValue) + "]");
        return (cacheValue == null) ? defaultValue : cacheValue;
    }

    public long getValue(FieldKey key, long defaultValue){
        String cacheValue = getValue(key);
        long result;
        if (cacheValue == null) {
            result = defaultValue;
        } else {
            cacheValue = cacheValue.trim();
            if (cacheValue.isEmpty()) {
                result = defaultValue;
            } else {
                result = Long.parseLong(cacheValue);
            }
        }
        return result;
    }

    public double getValue(FieldKey key, double defaultValue){
        String cacheValue = getValue(key);
        double result;
        if (cacheValue == null) {
            result = defaultValue;
        } else {
            cacheValue = cacheValue.trim();
            if (cacheValue.isEmpty()) {
                result = defaultValue;
            } else {
                result = Double.parseDouble(cacheValue);
            }
        }
        return result;
    }

    public void clear(){
        Log.d(Tag.TAG, "CachePropertyEditViewModel.clear() called");
        fields.clear();
        pendingPhotos.clear();
    }
}
