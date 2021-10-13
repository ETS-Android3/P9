package com.openclassrooms.realestatemanager.ui.propertyedit.viewstate;

import androidx.annotation.NonNull;

/**
 * use this class to load list Agent and list PropertyType
 */
public class DropdownItem {
    private final long id;
    private final String name;

    public DropdownItem(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /**
     * With toString() function, by default will display the name in Dropdown list.
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
