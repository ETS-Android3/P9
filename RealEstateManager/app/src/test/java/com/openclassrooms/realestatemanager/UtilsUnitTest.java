package com.openclassrooms.realestatemanager;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

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

    @Test
    public void getTodayDate() {
        Date toDay = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(toDay);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1 ;
        int year = calendar.get(Calendar.YEAR);
        final String expectedString = String.format("%02d/%02d/%04d", day, month, year);
        final String toDayString = Utils.getTodayDate();
        assertEquals(toDayString, expectedString);
    }
}