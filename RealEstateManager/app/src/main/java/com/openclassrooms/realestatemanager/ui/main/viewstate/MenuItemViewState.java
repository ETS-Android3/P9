package com.openclassrooms.realestatemanager.ui.main.viewstate;

public class MenuItemViewState {
    private final boolean enabled;
    private final boolean visible;

    public MenuItemViewState(boolean enabled, boolean visible) {
        this.enabled = enabled;
        this.visible = visible;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isVisible() {
        return visible;
    }
}
