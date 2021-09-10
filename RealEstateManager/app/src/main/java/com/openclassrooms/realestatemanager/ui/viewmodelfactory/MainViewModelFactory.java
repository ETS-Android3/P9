package com.openclassrooms.realestatemanager.ui.viewmodelfactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.room.injection.InjectionDao;
import com.openclassrooms.realestatemanager.data.room.repository.AgentRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PhotoRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyCategoryRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyRepository;
import com.openclassrooms.realestatemanager.data.room.repository.PropertyTypeRepository;
import com.openclassrooms.realestatemanager.ui.viewmodel.MainViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainViewModelFactory implements ViewModelProvider.Factory {

    private volatile static MainViewModelFactory sInstance;

    @NonNull
    private final AgentRepository agentRepository;
    @NonNull
    private final PhotoRepository photoRepository;
    @NonNull
    private final PropertyRepository propertyRepository;
    @NonNull
    private final PropertyCategoryRepository propertyCategoryRepository;
    @NonNull
    private final PropertyTypeRepository propertyTypeRepository;

    private static Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    public static MainViewModelFactory getInstance() {
        if (sInstance == null) {
            // Double Checked Locking singleton pattern with Volatile works on Android since Honeycomb
            synchronized (MainViewModelFactory.class) {
                if (sInstance == null) {
                    //Application application = MainApplication.getApplication();
                    sInstance = new MainViewModelFactory(
                            InjectionDao.getAgentRepositiory(MainApplication.getApplication(), provideExecutor()),
                            InjectionDao.getPhotoRepositiory(MainApplication.getApplication(), provideExecutor()),
                            InjectionDao.getPropertyRepositiory(MainApplication.getApplication(), provideExecutor()),
                            InjectionDao.getPropertyCategoryRepositiory(MainApplication.getApplication(), provideExecutor()),
                            InjectionDao.getPropertyTypeRepositiory(MainApplication.getApplication(), provideExecutor()));
                }
            }
        }

        return sInstance;
    }

    private MainViewModelFactory(@NonNull AgentRepository agentRepository,
                                 @NonNull PhotoRepository photoRepository,
                                 @NonNull PropertyRepository propertyRepository,
                                 @NonNull PropertyCategoryRepository propertyCategoryRepository,
                                 @NonNull PropertyTypeRepository propertyTypeRepository) {
        this.agentRepository = agentRepository;
        this.photoRepository = photoRepository;
        this.propertyRepository = propertyRepository;
        this.propertyCategoryRepository = propertyCategoryRepository;
        this.propertyTypeRepository = propertyTypeRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MainViewModel.class)) {
            return (T) new MainViewModel(
                    agentRepository,
                    photoRepository,
                    propertyRepository,
                    propertyCategoryRepository,
                    propertyTypeRepository
            );
        }
        throw new IllegalArgumentException("Unknown ViewModel class : " + modelClass);
    }
}
