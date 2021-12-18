package com.openclassrooms.realestatemanager.utils.localehelper;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.Locale;

public class UnitLocaleTest {

    @Test
    public void isImperial_with_US_must_be_true() {
        Locale.setDefault(new Locale("es", "US"));
        assertTrue(UnitLocale.isImperial());
    }

    @Test
    public void isImperial_with_LR_must_be_true() {
        Locale.setDefault(new Locale("es", "LR"));
        assertTrue(UnitLocale.isImperial());
    }

    @Test
    public void isImperial_with_MM_must_be_true() {
        Locale.setDefault(new Locale("my", "MM"));
        assertTrue(UnitLocale.isImperial());
    }

    @Test
    public void isImperial_with_FR_must_be_false() {
        Locale.setDefault(new Locale("fr", "FR"));
        assertFalse(UnitLocale.isImperial());
    }
}