package com.openclassrooms.realestatemanager.ui.propertydetail.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertydetail.viewmodel.PropertyDetailViewModel;

public class PropertyDetailViewModelFactory implements ViewModelProvider.Factory {

    private volatile static PropertyDetailViewModelFactory sInstance;

    @NonNull
    private final DatabaseRepository databaseRepository;

    public static PropertyDetailViewModelFactory getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (PropertyDetailViewModelFactory.class) {
                if (sInstance == null) {
                    //Application application = MainApplication.getApplication();
                    sInstance = new PropertyDetailViewModelFactory(
                            InjectionDao.getDatabaseRepository(MainApplication.getApplication()));
                }
            }
        }

        return sInstance;
    }

    private PropertyDetailViewModelFactory(@NonNull DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyDetailViewModel.class)) {
            return (T) new PropertyDetailViewModel(databaseRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }
}