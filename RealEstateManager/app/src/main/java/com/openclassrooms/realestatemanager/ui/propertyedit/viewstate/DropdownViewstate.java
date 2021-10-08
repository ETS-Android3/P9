package com.openclassrooms.realestatemanager.ui.propertyedit.viewstate;

import java.util.List;

public class DropdownViewstate {
    private final List<DropdownItem> agentItems;
    private final List<DropdownItem> propertyTypeItems;

    public DropdownViewstate(List<DropdownItem> agentItems, List<DropdownItem> propertyTypeItems) {
        this.agentItems = agentItems;
        this.propertyTypeItems = propertyTypeItems;
    }

    public List<DropdownItem> getAgentItems() {
        return agentItems;
    }

    public List<DropdownItem> getPropertyTypeItems() {
        return propertyTypeItems;
    }
}
