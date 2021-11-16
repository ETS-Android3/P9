package com.openclassrooms.realestatemanager.ui.main;

public enum NavigationState {
    HOME {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return true;
        }

        @Override
        public boolean isVisible(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    },
    DETAIL {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return currentState == NavigationState.EDIT;
        }

        @Override
        public boolean isVisible(boolean isLandscape, NavigationState currentState) {
            return false;
        }
    },
    EDIT {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return (currentState == NavigationState.DETAIL) || (isLandscape && currentState == NavigationState.HOME);
        }

        @Override
        public boolean isVisible(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    },
    ADD {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return (currentState == NavigationState.HOME) ||
                    (currentState == NavigationState.DETAIL) ||
                    (currentState == NavigationState.MAP) ||
                    (currentState == NavigationState.SEARCH);
        }

        @Override
        public boolean isVisible(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    },
    MAP {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return true;
        }

        @Override
        public boolean isVisible(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    },
    SEARCH {
        @Override
        public boolean isEnable(boolean isLandscape, NavigationState currentState) {
            return true;
        }

        @Override
        public boolean isVisible(boolean isLandscape, NavigationState currentState) {
            return true;
        }
    };

    public abstract boolean isEnable(boolean isLandscape, NavigationState currentState);
    public abstract boolean isVisible(boolean isLandscape, NavigationState currentState);
}
