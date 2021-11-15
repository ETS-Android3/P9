package com.openclassrooms.realestatemanager.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.ui.main.NavigationState;
import com.openclassrooms.realestatemanager.ui.main.viewstate.MainViewState;

public class MainViewModel extends ViewModel {

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
                combine(aBoolean, navigationStateMutableLiveData.getValue());
            }
        });

        mainViewStateMediatorLiveData.addSource(navigationStateMutableLiveData, new Observer<NavigationState>() {
            @Override
            public void onChanged(NavigationState navigationState) {
                combine(isLandscapeMutableLiveData.getValue(), navigationState);
            }
        });
    }

    private void combine(Boolean isLandscape, NavigationState navigationState){

        if ((isLandscape == null) || (navigationState == null))
            return;

        boolean homeIsEnabled = NavigationState.HOME.isEnable(isLandscape, navigationState);
        boolean detailIsEnabled = NavigationState.DETAIL.isEnable(isLandscape, navigationState);
        boolean editIsEnabled = NavigationState.EDIT.isEnable(isLandscape, navigationState);
        boolean addIsEnabled = NavigationState.ADD.isEnable(isLandscape, navigationState);
        boolean mapIsEnabled = NavigationState.MAP.isEnable(isLandscape, navigationState);
        boolean searchIsEnabled = NavigationState.SEARCH.isEnable(isLandscape, navigationState);

        MainViewState mainViewState = new MainViewState(navigationState, isLandscape,
                homeIsEnabled, detailIsEnabled, editIsEnabled, addIsEnabled, mapIsEnabled, searchIsEnabled);

        mainViewStateMediatorLiveData.setValue(mainViewState);
    }
}
