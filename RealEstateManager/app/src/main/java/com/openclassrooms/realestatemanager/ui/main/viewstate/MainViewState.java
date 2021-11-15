package com.openclassrooms.realestatemanager.ui.main.viewstate;

import com.openclassrooms.realestatemanager.ui.main.NavigationState;

public class MainViewState {
    private NavigationState navigationState;
    private boolean landscape;
    private boolean homeEnable;
    private boolean detailEnable;
    private boolean editEnable;
    private boolean addEnable;
    private boolean mapEnable;
    private boolean searchEnable;

    public MainViewState(NavigationState navigationState, boolean landscape, boolean homeEnable, boolean detailEnable, boolean editEnable, boolean addEnable, boolean mapEnable, boolean searchEnable) {
        this.navigationState = navigationState;
        this.landscape = landscape;
        this.homeEnable = homeEnable;
        this.detailEnable = detailEnable;
        this.editEnable = editEnable;
        this.addEnable = addEnable;
        this.mapEnable = mapEnable;
        this.searchEnable = searchEnable;
    }

    public NavigationState getNavigationState() {
        return navigationState;
    }

    public boolean isLandscape() {
        return landscape;
    }

    public boolean isHomeEnable() {
        return homeEnable;
    }

    public boolean isDetailEnable() {
        return detailEnable;
    }

    public boolean isEditEnable() {
        return editEnable;
    }

    public boolean isAddEnable() {
        return addEnable;
    }

    public boolean isMapEnable() {
        return mapEnable;
    }

    public boolean isSearchEnable() {
        return searchEnable;
    }
}
