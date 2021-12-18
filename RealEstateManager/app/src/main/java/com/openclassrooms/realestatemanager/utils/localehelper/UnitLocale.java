package com.openclassrooms.realestatemanager.utils.localehelper;

import java.util.Locale;

public class UnitLocale {
    private static Culture getFrom(Locale locale) {
        String countryCode = locale.getCountry();
        if ("US".equals(countryCode)) return Culture.IMPERIAL; // USA
        if ("LR".equals(countryCode)) return Culture.IMPERIAL; // Liberia
        if ("MM".equals(countryCode)) return Culture.IMPERIAL; // Myanmar
        return Culture.METRIC;
    }

    public static boolean isImperial(){
        return (getFrom(Locale.getDefault()) == Culture.IMPERIAL);
    }
}
