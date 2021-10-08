package com.openclassrooms.realestatemanager.ui.propertyedit.viewstate;

import java.util.List;

public class DropdownViewstate {
    private final List<AgentDropdown> agentDropdownList;
    private final List<PropertyTypeDropdown> propertyTypeDropdownList;

    public DropdownViewstate(List<AgentDropdown> agentDropdownList, List<PropertyTypeDropdown> propertyTypeDropdownList) {
        this.agentDropdownList = agentDropdownList;
        this.propertyTypeDropdownList = propertyTypeDropdownList;
    }

    public List<AgentDropdown> getAgentDropdownList() {
        return agentDropdownList;
    }

    public List<PropertyTypeDropdown> getPropertyTypeDropdownList() {
        return propertyTypeDropdownList;
    }
}
