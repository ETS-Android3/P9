package com.openclassrooms.realestatemanager.utils;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoanCalculatorTest {

    @Test
    public void loan_0_0_0() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(0, 0, 0);
        assertEquals(0 ,monthlyPayment, 0 );
    }

    @Test
    public void loan_12_0_0() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(12, 0, 0);
        assertEquals(12.0 ,monthlyPayment, 0 );
    }

    @Test
    public void loan_0_1_0() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(0, 1, 0);
        assertEquals(0 ,monthlyPayment, 0 );
    }

    @Test
    public void loan_0_0_120() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(0, 0, 120);
        assertEquals(0 ,monthlyPayment, 0 );
    }

    @Test
    public void loan_12_0_12() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(12, 0, 12);
        assertEquals(1.0 ,monthlyPayment, 0 );
    }

    @Test
    public void loan_100000_1_120() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(100000, 1, 120);
        assertEquals(876.0412137016194 ,monthlyPayment, 0 );
    }

    @Test
    public void loan_250000_093_240() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(250000, 0.93, 240);
        assertEquals(1141.94549410426 ,monthlyPayment, 0 );
    }

    @Test
    public void loan_1000000_125_360() {
        LoanCalculator calculator = new LoanCalculator();
        double monthlyPayment = calculator.calculateMonthlyPayment(1000000, 1.25, 360);
        assertEquals(3332.516839227643 ,monthlyPayment, 0 );
    }
}