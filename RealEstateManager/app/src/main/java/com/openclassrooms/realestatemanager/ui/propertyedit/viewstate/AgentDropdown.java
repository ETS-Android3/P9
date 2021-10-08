package com.openclassrooms.realestatemanager.ui.propertyedit.viewstate;

import androidx.annotation.NonNull;

public class AgentDropdown {
    private final long id;
    private final String name;

    public AgentDropdown(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @NonNull
    @Override
    public String toString() {
        return getName();
    }
}
