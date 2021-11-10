package com.openclassrooms.realestatemanager.ui.propertymap.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;
import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.location.LocationRepository;
import com.openclassrooms.realestatemanager.data.permission_checker.PermissionChecker;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertyedit.viewmodelfactory.PropertyEditViewModelFactory;
import com.openclassrooms.realestatemanager.ui.propertymap.viewmodel.PropertyMapViewModel;

public class PropertyMapViewModelFactory implements ViewModelProvider.Factory {

    private volatile static PropertyMapViewModelFactory sInstance;

    @NonNull
    private final DatabaseRepository databaseRepository;
    @NonNull
    private final PermissionChecker permissionChecker;
    @NonNull
    private final LocationRepository locationRepository;

    public PropertyMapViewModelFactory(@NonNull PermissionChecker permissionChecker,
                                @NonNull LocationRepository locationRepository,
                                @NonNull DatabaseRepository databaseRepository) {
        this.permissionChecker = permissionChecker;
        this.locationRepository = locationRepository;
        this.databaseRepository = databaseRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(PropertyMapViewModel.class)) {
            return (T) new PropertyMapViewModel(permissionChecker, locationRepository, databaseRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

    public static PropertyMapViewModelFactory getInstance(){
        if (sInstance == null) {
            synchronized (PropertyMapViewModelFactory.class) {
                if (sInstance == null) {
                    sInstance = new PropertyMapViewModelFactory(new PermissionChecker(MainApplication.getApplication()),
                            new LocationRepository(LocationServices.getFusedLocationProviderClient(MainApplication.getApplication())),
                            InjectionDao.getDatabaseRepository(MainApplication.getApplication()));
                }
            }
        }

        return sInstance;
    }
}
