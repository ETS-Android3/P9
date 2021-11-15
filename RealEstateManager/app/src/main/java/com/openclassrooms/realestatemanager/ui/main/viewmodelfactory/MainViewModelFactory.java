package com.openclassrooms.realestatemanager.ui.main.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.ui.main.viewmodel.MainViewModel;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private volatile static MainViewModelFactory sInstance;

    public static MainViewModelFactory getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (MainViewModelFactory.class) {
                if (sInstance == null) {
                    //Application application = MainApplication.getApplication();
                    sInstance = new MainViewModelFactory();
                }
            }
        }

        return sInstance;
    }

    private MainViewModelFactory() {
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }
}
