package com.openclassrooms.realestatemanager.ui.main.viewmodel;

import android.content.res.Configuration;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.ui.main.MenuItemViewState;
import com.openclassrooms.realestatemanager.ui.main.NavigationState;
import com.openclassrooms.realestatemanager.ui.main.viewstate.MainViewState;

public class MainViewModel extends ViewModel {

    private int orientation = Configuration.ORIENTATION_UNDEFINED;

    private final MutableLiveData<Boolean> isLandscapeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsLandscapeMutableLiveData() {
        return isLandscapeMutableLiveData;
    }

    private final MutableLiveData<NavigationState> navigationStateMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<NavigationState> getNavigationStateMutableLiveData() {
        return navigationStateMutableLiveData;
    }

    private final MediatorLiveData<MainViewState> mainViewStateMediatorLiveData = new MediatorLiveData<>();

    public LiveData<MainViewState> getMainViewStateLiveData() {
        return mainViewStateMediatorLiveData;
    }

    public MainViewModel() {
        configureMediator();
    }

    private void configureMediator(){

        mainViewStateMediatorLiveData.addSource(isLandscapeMutableLiveData, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Boolean orientationChanged = true;

                if (aBoolean && orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    orientationChanged = false;
                } else {
                    if (!aBoolean && orientation == Configuration.ORIENTATION_PORTRAIT) {
                        orientationChanged = false;
                    }
                }

                // maj cache
                if (aBoolean)
                    orientation = Configuration.ORIENTATION_LANDSCAPE;
                else
                    orientation = Configuration.ORIENTATION_PORTRAIT;

                combine(orientationChanged, aBoolean, navigationStateMutableLiveData.getValue());
            }
        });

        mainViewStateMediatorLiveData.addSource(navigationStateMutableLiveData, new Observer<NavigationState>() {
            @Override
            public void onChanged(NavigationState navigationState) {
                combine(false, isLandscapeMutableLiveData.getValue(), navigationState);
            }
        });
    }

    private void combine(boolean orientationChanged, Boolean isLandscape, NavigationState navigationState){

        if ((isLandscape == null) || (navigationState == null))
            return;

        MenuItemViewState home = new MenuItemViewState(NavigationState.HOME.isEnable(isLandscape, navigationState),
                NavigationState.HOME.isVisible(isLandscape, navigationState));
        MenuItemViewState detail = new MenuItemViewState(NavigationState.DETAIL.isEnable(isLandscape, navigationState),
                NavigationState.DETAIL.isVisible(isLandscape, navigationState));
        MenuItemViewState edit = new MenuItemViewState(NavigationState.EDIT.isEnable(isLandscape, navigationState),
                NavigationState.EDIT.isVisible(isLandscape, navigationState));
        MenuItemViewState add = new MenuItemViewState(NavigationState.ADD.isEnable(isLandscape, navigationState),
                NavigationState.ADD.isVisible(isLandscape, navigationState));
        MenuItemViewState map = new MenuItemViewState(NavigationState.MAP.isEnable(isLandscape, navigationState),
                NavigationState.MAP.isVisible(isLandscape, navigationState));
        MenuItemViewState search = new MenuItemViewState(NavigationState.SEARCH.isEnable(isLandscape, navigationState),
                NavigationState.SEARCH.isVisible(isLandscape, navigationState));

        MainViewState mainViewState = new MainViewState(navigationState, isLandscape, orientationChanged,
                home, detail, edit, add, map, search);

        mainViewStateMediatorLiveData.setValue(mainViewState);
    }
}
