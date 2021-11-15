package com.openclassrooms.realestatemanager.ui.main;

public enum NavigationState {
    HOME {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    },
    DETAIL {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return currentState == NavigationState.HOME;
        }
    },
    EDIT {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return (currentState == NavigationState.DETAIL);
        }
    },
    ADD {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return currentState != NavigationState.ADD;
        }
    },
    MAP {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    },
    SEARCH {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    };

    public abstract boolean isEnable(boolean isLandscape, NavigationState currentState);
}
