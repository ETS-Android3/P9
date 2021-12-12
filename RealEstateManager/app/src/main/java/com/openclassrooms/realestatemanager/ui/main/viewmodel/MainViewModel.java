package com.openclassrooms.realestatemanager.ui.main.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.data.room.repository.DatabaseRepository;
import com.openclassrooms.realestatemanager.ui.constantes.PropertyConst;
import com.openclassrooms.realestatemanager.ui.main.NavigationState;
import com.openclassrooms.realestatemanager.ui.main.viewstate.MainViewState;
import com.openclassrooms.realestatemanager.ui.main.viewstate.MenuItemViewState;
import com.openclassrooms.realestatemanager.utils.Utils;

public class MainViewModel extends ViewModel {

    private final DatabaseRepository databaseRepository;

    private final MutableLiveData<Boolean> isLandscapeMutableLiveData = new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsLandscapeMutableLiveData() {
        return isLandscapeMutableLiveData;
    }

    private final MutableLiveData<NavigationState> navigationStateMutableLiveData = new MutableLiveData<>();
    private void setNavigationState(NavigationState navigationState) {
        navigationStateMutableLiveData.setValue(navigationState);
    }

    private final MutableLiveData<Long> propertyIdMutableLiveData = new MutableLiveData<>();
    private void setPropertyId(long propertyId){
        propertyIdMutableLiveData.setValue(propertyId);
    }

    private LiveData<Long> getValidPropertyId(){
        return Transformations.switchMap(propertyIdMutableLiveData,
                propertyId -> databaseRepository.getPropertyRepository().getFirstOrValidIdLiveData(propertyId));
    }

    public void navigateToHome(){
        setNavigationState(NavigationState.HOME);
    }

    public void navigateToDetail(){
        setPropertyId(PropertyConst.PROPERTY_ID_NOT_INITIALIZED);
        setNavigationState(NavigationState.DETAIL);
    }

    public void navigateToDetail(long propertyId){
        setPropertyId(propertyId);
        setNavigationState(NavigationState.DETAIL);
    }

    public void navigateToEdit(long propertyId){
        setPropertyId(propertyId);
        setNavigationState(NavigationState.EDIT);
    }

    public void navigateToAdd(){
        setNavigationState(NavigationState.ADD);
    }

    public void navigateToMap(){
        setNavigationState(NavigationState.MAP);
    }

    public void navigateToSearch(){
        setNavigationState(NavigationState.SEARCH);
    }

    public void navigateToLoanCalculator(){
        setNavigationState(NavigationState.LOAN_CALCULATOR);
    }

    private final MediatorLiveData<MainViewState> mainViewStateMediatorLiveData = new MediatorLiveData<>();
    public LiveData<MainViewState> getMainViewStateLiveData() {
        return mainViewStateMediatorLiveData;
    }

    public MainViewModel(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
        navigationStateMutableLiveData.setValue(NavigationState.HOME);
        configureMediator();
    }

    private void configureMediator(){
        mainViewStateMediatorLiveData.addSource(isLandscapeMutableLiveData, aBoolean -> combine(aBoolean, navigationStateMutableLiveData.getValue(), getValidPropertyId().getValue()));

        mainViewStateMediatorLiveData.addSource(navigationStateMutableLiveData, navigationState -> combine(isLandscapeMutableLiveData.getValue(), navigationState, getValidPropertyId().getValue()));

        mainViewStateMediatorLiveData.addSource(getValidPropertyId(), aLong -> combine(isLandscapeMutableLiveData.getValue(), navigationStateMutableLiveData.getValue(), aLong));
    }

    private NavigationState checkAndRedirectDestination(boolean isLandScape, NavigationState askedDestination) {
        switch (askedDestination){
            case HOME:
                return NavigationState.HOME.redirectNavigation(isLandScape, askedDestination);
            case LIST:
                return NavigationState.LIST.redirectNavigation(isLandScape, askedDestination);
            default:
                return askedDestination;
        }
    }

    private void combine(Boolean isLandscape, NavigationState askedDestination, Long propertyId){

        if ((isLandscape == null) || (askedDestination == null))
            return;

        NavigationState redirectNavigation = checkAndRedirectDestination(isLandscape, askedDestination);

        if ((redirectNavigation == NavigationState.DETAIL) && (propertyId == null)){
            if (askedDestination != NavigationState.HOME)
                return;
        }

        if ((redirectNavigation == NavigationState.EDIT) && (propertyId == null))
            return;


        boolean isWifiEnabled = Utils.isInternetAvailable(MainApplication.getApplication());

        MenuItemViewState home = new MenuItemViewState(NavigationState.HOME.isEnable(isLandscape, redirectNavigation, isWifiEnabled),
                NavigationState.HOME.isVisible(isLandscape, redirectNavigation));
        MenuItemViewState detail = new MenuItemViewState(NavigationState.DETAIL.isEnable(isLandscape, redirectNavigation, isWifiEnabled),
                NavigationState.DETAIL.isVisible(isLandscape, redirectNavigation));
        MenuItemViewState edit = new MenuItemViewState(NavigationState.EDIT.isEnable(isLandscape, redirectNavigation, isWifiEnabled),
                NavigationState.EDIT.isVisible(isLandscape, redirectNavigation));
        MenuItemViewState add = new MenuItemViewState(NavigationState.ADD.isEnable(isLandscape, redirectNavigation, isWifiEnabled),
                NavigationState.ADD.isVisible(isLandscape, redirectNavigation));
        MenuItemViewState map = new MenuItemViewState(NavigationState.MAP.isEnable(isLandscape, redirectNavigation, isWifiEnabled),
                NavigationState.MAP.isVisible(isLandscape, redirectNavigation));
        MenuItemViewState search = new MenuItemViewState(NavigationState.SEARCH.isEnable(isLandscape, redirectNavigation, isWifiEnabled),
                NavigationState.SEARCH.isVisible(isLandscape, redirectNavigation));

        long id = PropertyConst.PROPERTY_ID_NOT_INITIALIZED;
        if (propertyId != null) id = propertyId;

        // boolean mustNavigate = navigationChanged(navigationState, newNavigation);
        MainViewState mainViewState = new MainViewState(redirectNavigation, id, home, detail, edit, add, map, search);

        mainViewStateMediatorLiveData.setValue(mainViewState);
    }

}
