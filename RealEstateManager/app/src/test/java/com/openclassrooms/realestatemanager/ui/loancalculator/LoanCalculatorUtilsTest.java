package com.openclassrooms.realestatemanager.ui.loancalculator;

import static org.junit.Assert.*;

import android.util.Log;

import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.utils.LoanCalculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@RunWith(Parameterized.class)
public class LoanCalculatorUtilsTest {
    private final float amount;
    private final String expectedAmount;
    private final float rate;
    private final String expectedRate;
    private final double payment;
    private final String expectedPayment;

    @Before
    public void initialize(){
    }

    public LoanCalculatorUtilsTest(float amount, String expectedAmount,
                                   float rate, String expectedRate,
                                   double payment, String expectedPayment) {
        this.amount = amount;
        this.expectedAmount = expectedAmount;
        this.rate = rate;
        this.expectedRate = expectedRate;
        this.payment = payment;
        this.expectedPayment = expectedPayment;
    }

    // ALT + 0160 to insert NBSP (non-breaking space)
    @Parameterized.Parameters
    public static Collection testEntries(){
        return Arrays.asList(new Object[][] {
                        {0f , "0 €", 0f, "0,00%", 0, "0 €"},
                        {1000f, "1 000 €", 3f, "3,00%", 1000, "1 000 €"},
                        {100f, "100 €", 1.25f, "1,25%", 123456789.12, "123 456 789 €"}
        });
    }

    @Test
    public void formatAmount() {
        System.out.println("formatAmount() called with amount = " + amount);
        String s = LoanCalculatorUtils.formatAmount(amount);
        assertEquals(expectedAmount, s);
    }

    @Test
    public void formatRate() {
        System.out.println("formatRate() called with rate = " + rate);
        String s = LoanCalculatorUtils.formatRate(rate);
        assertEquals(expectedRate, s);
    }

    @Test
    public void formatPayment() {
        System.out.println("formatPayment() called with rate = " + payment);
        String s = LoanCalculatorUtils.formatPayment(payment);
        assertEquals(expectedPayment, s);
    }
}