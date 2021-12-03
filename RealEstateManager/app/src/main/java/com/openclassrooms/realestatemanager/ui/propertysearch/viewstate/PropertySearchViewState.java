package com.openclassrooms.realestatemanager.ui.propertysearch.viewstate;

import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;

import java.util.Date;
import java.util.List;

public class PropertySearchViewState {
/*    private String addressTitle;
    private String address;
    private String description;
    private String pointOfInterest;
    private Long agentId;
    private Long propertyTypeId;
    private int minPrice;
    private int maxPrice;
    private int minSurface;
    private int maxSurface;
    private int minRooms;
    private int maxRooms;
    private Date minEntryDate;
    private Date maxEntryDate;
    private Date minSaleDate;
    private Date maxSaleDate;*/
    private List<DropdownItem> agents;
    private int agentIndex;
    private List<DropdownItem> propertyTypes;
    private int propertyTypeIndex;
    private String fullText;

    public PropertySearchViewState(List<DropdownItem> agents, int agentIndex, List<DropdownItem> propertyTypes, int propertyTypeIndex, String fullText) {
        this.agents = agents;
        this.agentIndex = agentIndex;
        this.propertyTypes = propertyTypes;
        this.propertyTypeIndex = propertyTypeIndex;
        this.fullText = fullText;
    }

    public List<DropdownItem> getAgents() {
        return agents;
    }

    public int getAgentIndex() {
        return agentIndex;
    }

    public List<DropdownItem> getPropertyTypes() {
        return propertyTypes;
    }

    public int getPropertyTypeIndex() {
        return propertyTypeIndex;
    }

    public String getFullText() {
        return fullText;
    }
}
