package com.openclassrooms.realestatemanager.ui.propertylist.viewstate;

import java.util.List;

public class PropertyListViewState {

    private List<RowPropertyViewState> rowPropertyViewStates;

    public PropertyListViewState(List<RowPropertyViewState> rowPropertyViewStates) {
        this.rowPropertyViewStates = rowPropertyViewStates;
    }

    public List<RowPropertyViewState> getRowPropertyViewStates() {
        return rowPropertyViewStates;
    }
}
