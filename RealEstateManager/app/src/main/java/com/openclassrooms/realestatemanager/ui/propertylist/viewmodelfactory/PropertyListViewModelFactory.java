package com.openclassrooms.realestatemanager.ui.propertylist.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.propertylist.viewmodel.PropertyListViewModel;

public class PropertyListViewModelFactory implements ViewModelProvider.Factory {

    private volatile static PropertyListViewModelFactory sInstance;

    @NonNull
    private final DatabaseRepository databaseRepository;

    public static PropertyListViewModelFactory getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (PropertyListViewModelFactory.class) {
                if (sInstance == null) {
                    //Application application = MainApplication.getApplication();
                    sInstance = new PropertyListViewModelFactory(
                            InjectionDao.getDatabaseRepository(MainApplication.getApplication()));
                }
            }
        }

        return sInstance;
    }

    private PropertyListViewModelFactory(@NonNull DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PropertyListViewModel.class)) {
            return (T) new PropertyListViewModel(databaseRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }

}
