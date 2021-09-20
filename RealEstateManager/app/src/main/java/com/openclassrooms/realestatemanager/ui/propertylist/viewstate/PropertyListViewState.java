package com.openclassrooms.realestatemanager.ui.propertylist.viewstate;

import com.openclassrooms.realestatemanager.data.room.model.Agent;
import com.openclassrooms.realestatemanager.data.room.model.Photo;
import com.openclassrooms.realestatemanager.data.room.model.Property;
import com.openclassrooms.realestatemanager.data.room.model.PropertyCategory;
import com.openclassrooms.realestatemanager.data.room.model.PropertyType;

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
