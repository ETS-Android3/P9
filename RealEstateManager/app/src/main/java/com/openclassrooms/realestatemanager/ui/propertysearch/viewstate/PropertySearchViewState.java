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
    private List<Float> minMaxPrice;
    private List<Float> valuesPrice;
    private String captionPrice;
    private List<Float> minMaxSurface;
    private List<Float> valuesSurface;
    private String captionSurface;
    private List<Float> minMaxRooms;
    private List<Float> valuesRooms;
    private String captionRooms;

    public PropertySearchViewState(List<DropdownItem> agents, int agentIndex, List<DropdownItem> propertyTypes, int propertyTypeIndex, String fullText, List<Float> minMaxPrice, List<Float> valuesPrice, String captionPrice, List<Float> minMaxSurface, List<Float> valuesSurface, String captionSurface, List<Float> minMaxRooms, List<Float> valuesRooms, String captionRooms) {
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
}
