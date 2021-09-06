package com.openclassrooms.realestatemanager;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class UtilsUnitTest {

    @Test
    public void convertDollarToEuro() {
        // 100$ =>81€
        final int dollars = 100;
        final int euros = Utils.convertDollarToEuro(dollars);
        assertEquals(euros, 81);
    }

    @Test
    public void convertEuroToDollar() {
        // 81€ => 100$
        final int euros = 81;
        final int dollars = Utils.convertEuroToDollar(euros);
        assertEquals(dollars, 100);
    }
}