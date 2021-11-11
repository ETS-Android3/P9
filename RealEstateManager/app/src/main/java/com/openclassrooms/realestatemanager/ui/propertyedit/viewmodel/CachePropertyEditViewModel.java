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

    private List<Agent> agents;
    private List<PropertyType> propertyTypes;
    private List<Photo> pendingPhotos;
    private FieldList fields;

    public CachePropertyEditViewModel() {
        agents = new ArrayList<>();
        propertyTypes = new ArrayList<>();
        pendingPhotos = new ArrayList<>();
        fields = new FieldList();
    }

    public List<Agent> getAgents() {
        return agents;
    }
    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public List<PropertyType> getPropertyTypes() {
        return propertyTypes;
    }
    public void setPropertyTypes(List<PropertyType> propertyTypes) {
        this.propertyTypes = propertyTypes;
    }

    public List<Photo> getPendingPhotos() {
        return pendingPhotos;
    }

    public void setPendingPhotos(List<Photo> pendingPhotos) {
        this.pendingPhotos = pendingPhotos;
        pendingPhotosMutableLiveData.setValue(pendingPhotos);
    }

    private MutableLiveData<List<Photo>> pendingPhotosMutableLiveData = new MutableLiveData<>();
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

    public int getPendingPhotosCount(){
        return pendingPhotos.size();
    }

    public boolean isValidePhoto(Photo photo){
        return (!photo.getLegend().isEmpty());
    }

    private int getInvalidePhotoCaptionCount(){
        int count = 0;
        for (Photo photo : pendingPhotos) {
            if (!isValidePhoto(photo)) {
                count++;
            }
        }
        return count;
    }

    public boolean isAllPhotoOk(){
        return getInvalidePhotoCaptionCount() == 0;
    }

    public void setValue(FieldKey key, String value){
        fields.setValue(key, value);
    }

    public String getValue(FieldKey key){
        return fields.getValue(key);
    }

    public void clearFields(){
        fields.clear();
    }
}
