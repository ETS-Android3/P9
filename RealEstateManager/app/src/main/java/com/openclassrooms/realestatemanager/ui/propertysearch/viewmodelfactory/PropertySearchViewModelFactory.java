package com.openclassrooms.realestatemanager.ui.propertysearch.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertysearch.viewmodel.PropertySearchViewModel;

public class PropertySearchViewModelFactory implements ViewModelProvider.Factory {

    private volatile static PropertySearchViewModelFactory sInstance;

    @NonNull
    private final DatabaseRepository databaseRepository;

    public static PropertySearchViewModelFactory getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (PropertySearchViewModelFactory.class) {
                if (sInstance == null) {
                    //Application application = MainApplication.getApplication();
                    sInstance = new PropertySearchViewModelFactory(
                            InjectionDao.getDatabaseRepository(MainApplication.getApplication()));
                }
            }
        }

        return sInstance;
    }

    private PropertySearchViewModelFactory(@NonNull DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertySearchViewModel.class)) {
            return (T) new PropertySearchViewModel(databaseRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

}
