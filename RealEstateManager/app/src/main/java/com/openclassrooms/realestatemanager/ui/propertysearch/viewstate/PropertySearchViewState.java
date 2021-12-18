package com.openclassrooms.realestatemanager.ui.propertysearch.viewstate;

import com.openclassrooms.realestatemanager.ui.propertyedit.viewstate.DropdownItem;

import java.util.List;

public class PropertySearchViewState {
    private List<DropdownItem> agents;
    private int agentIndex;
    private List<DropdownItem> propertyTypes;
    private int propertyTypeIndex;
    private String fullText;
    private List<Float> minMaxPrice;
    private List<Float> valuesPrice;
    private String captionPrice;
    private List<Float> minMaxSurface;
    private List<Float> valuesSurface;
    private String captionSurface;
    private List<Float> minMaxRooms;
    private List<Float> valuesRooms;
    private String captionRooms;
    private String captionEntryDate;
    private String captionSaleDate;

    public PropertySearchViewState(List<DropdownItem> agents, int agentIndex, List<DropdownItem> propertyTypes, int propertyTypeIndex, String fullText, List<Float> minMaxPrice, List<Float> valuesPrice, String captionPrice, List<Float> minMaxSurface, List<Float> valuesSurface, String captionSurface, List<Float> minMaxRooms, List<Float> valuesRooms, String captionRooms, String captionEntryDate, String captionSaleDate) {
        this.agents = agents;
        this.agentIndex = agentIndex;
        this.propertyTypes = propertyTypes;
        this.propertyTypeIndex = propertyTypeIndex;
        this.fullText = fullText;
        this.minMaxPrice = minMaxPrice;
        this.valuesPrice = valuesPrice;
        this.captionPrice = captionPrice;
        this.minMaxSurface = minMaxSurface;
        this.valuesSurface = valuesSurface;
        this.captionSurface = captionSurface;
        this.minMaxRooms = minMaxRooms;
        this.valuesRooms = valuesRooms;
        this.captionRooms = captionRooms;
        this.captionEntryDate = captionEntryDate;
        this.captionSaleDate = captionSaleDate;
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

    public List<Float> getMinMaxPrice() {
        return minMaxPrice;
    }

    public List<Float> getValuesPrice() {
        return valuesPrice;
    }

    public String getCaptionPrice() {
        return captionPrice;
    }

    public List<Float> getMinMaxSurface() {
        return minMaxSurface;
    }

    public List<Float> getValuesSurface() {
        return valuesSurface;
    }

    public String getCaptionSurface() {
        return captionSurface;
    }

    public List<Float> getMinMaxRooms() {
        return minMaxRooms;
    }

    public List<Float> getValuesRooms() {
        return valuesRooms;
    }

    public String getCaptionRooms() {
        return captionRooms;
    }

    public String getCaptionEntryDate() {
        return captionEntryDate;
    }

    public String getCaptionSaleDate() {
        return captionSaleDate;
    }
}
