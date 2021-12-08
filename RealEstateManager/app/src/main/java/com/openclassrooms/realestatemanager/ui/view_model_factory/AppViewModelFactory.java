package com.openclassrooms.realestatemanager.ui.view_model_factory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleGeocodeRepository;
import com.openclassrooms.realestatemanager.data.googlemaps.repository.GoogleStaticMapRepository;
import com.openclassrooms.realestatemanager.data.location.LocationRepository;
import com.openclassrooms.realestatemanager.data.permission_checker.PermissionChecker;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.loancalculator.LoanCalculatorViewModel;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodel.PropertyEditViewModel;
import com.openclassrooms.realestatemanager.ui.propertylist.viewmodel.PropertyListViewModel;
import com.openclassrooms.realestatemanager.ui.propertymap.viewmodel.PropertyMapViewModel;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel.PropertySearchViewModel;

public class AppViewModelFactory implements ViewModelProvider.Factory {

    private volatile static AppViewModelFactory sInstance;

    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final GoogleStaticMapRepository googleStaticMapRepository;
    @NonNull
    private final GoogleGeocodeRepository googleGeocodeRepository;

    public static AppViewModelFactory getInstance(){
        if (sInstance == null) {
            synchronized (AppViewModelFactory.class) {
                if (sInstance == null) {
                    sInstance = new AppViewModelFactory(new PermissionChecker(MainApplication.getApplication()),
                            new LocationRepository(LocationServices.getFusedLocationProviderClient(MainApplication.getApplication())),
                            InjectionDao.getDatabaseRepository(MainApplication.getApplication()),
                            new GoogleStaticMapRepository(MainApplication.getGoogleApiKey()),
                            new GoogleGeocodeRepository(MainApplication.getGoogleApiKey()));
                }
            }
        }

        return sInstance;
    }

    public AppViewModelFactory(@NonNull PermissionChecker permissionChecker, @NonNull LocationRepository locationRepository, @NonNull DatabaseRepository databaseRepository, @NonNull GoogleStaticMapRepository googleStaticMapRepository, @NonNull GoogleGeocodeRepository googleGeocodeRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.databaseRepository = databaseRepository;
        this.googleStaticMapRepository = googleStaticMapRepository;
        this.googleGeocodeRepository = googleGeocodeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {

        // list
        if (modelClass.isAssignableFrom(PropertyListViewModel.class)) {
            return (T) new PropertyListViewModel(databaseRepository);
        }

        // detail
        if (modelClass.isAssignableFrom(PropertyDetailViewModel.class)) {
            return (T) new PropertyDetailViewModel(databaseRepository, googleStaticMapRepository);
        }

        // edit
        if (modelClass.isAssignableFrom(PropertyEditViewModel.class)) {
            return (T) new PropertyEditViewModel(databaseRepository, googleGeocodeRepository, googleStaticMapRepository);
        }

        // map
        if(modelClass.isAssignableFrom(PropertyMapViewModel.class)) {
            return (T) new PropertyMapViewModel(permissionChecker, locationRepository, databaseRepository);
        }

        //search
        if (modelClass.isAssignableFrom(PropertySearchViewModel.class)) {
            return (T) new PropertySearchViewModel(databaseRepository);
        }

        //loan calculator
        //search
        if (modelClass.isAssignableFrom(LoanCalculatorViewModel.class)) {
            return (T) new LoanCalculatorViewModel();
        }

        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }
}
