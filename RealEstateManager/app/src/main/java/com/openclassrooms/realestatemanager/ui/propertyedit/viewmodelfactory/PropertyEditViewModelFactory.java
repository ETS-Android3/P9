package com.openclassrooms.realestatemanager.ui.propertyedit.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleGeocodeRepository;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleStaticMapRepository;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.PropertyEditViewModel;

public class PropertyEditViewModelFactory implements ViewModelProvider.Factory {

    private volatile static PropertyEditViewModelFactory sInstance;

    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final GoogleGeocodeRepository googleGeocodeRepository;
    @NonNull
    private final GoogleStaticMapRepository googleStaticMapRepository;

    public PropertyEditViewModelFactory(@NonNull DatabaseRepository databaseRepository,
                                        @NonNull GoogleGeocodeRepository googleGeocodeRepository,
                                        @NonNull GoogleStaticMapRepository googleStaticMapRepository) {
        this.databaseRepository = databaseRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
        this.googleStaticMapRepository = googleStaticMapRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyEditViewModel.class)) {
            return (T) new PropertyEditViewModel(databaseRepository, googleGeocodeRepository, googleStaticMapRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

    public static PropertyEditViewModelFactory getInstance() {
        if (sInstance == null) {
            synchronized (PropertyEditViewModelFactory.class) {
                if (sInstance == null) {
                    sInstance = new PropertyEditViewModelFactory(
                            InjectionDao.getDatabaseRepository(MainApplication.getApplication()),
                            new GoogleGeocodeRepository(MainApplication.getGoogleApiKey()),
                            new GoogleStaticMapRepository((MainApplication.getGoogleApiKey())));
                }
            }
        }

        return sInstance;
    }
}
