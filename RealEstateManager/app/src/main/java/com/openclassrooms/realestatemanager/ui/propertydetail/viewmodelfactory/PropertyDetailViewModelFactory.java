package com.openclassrooms.realestatemanager.ui.propertydetail.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.location.LocationRepository;
import com.openclassrooms.realestatemanager.data.permission_checker.PermissionChecker;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;

public class PropertyDetailViewModelFactory implements ViewModelProvider.Factory {

    private volatile static PropertyDetailViewModelFactory sInstance;

    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    private final DatabaseRepository databaseRepository;


    public static PropertyDetailViewModelFactory getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (PropertyDetailViewModelFactory.class) {
                if (sInstance == null) {
                    //Application application = MainApplication.getApplication();
                    sInstance = new PropertyDetailViewModelFactory(new PermissionChecker(MainApplication.getApplication()),
                                                                   new LocationRepository(LocationServices.getFusedLocationProviderClient(MainApplication.getApplication())),
                                                                   InjectionDao.getDatabaseRepository(MainApplication.getApplication()));
                }
            }
        }

        return sInstance;
    }

    private PropertyDetailViewModelFactory(
            @NonNull PermissionChecker permissionChecker,
            @NonNull LocationRepository locationRepository,
            @NonNull DatabaseRepository databaseRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.databaseRepository = databaseRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyDetailViewModel.class)) {
            return (T) new PropertyDetailViewModel(permissionChecker,
                                                   locationRepository,
                                                   databaseRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }
}