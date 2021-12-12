package com.openclassrooms.realestatemanager.ui.loancalculator;

public class LoanCalculatorViewState {
    private final String strMonthlyPayment;
    private final String strAmount;
    private final String strRate;
    private final String strDuration;
    private final float amount;
    private final float rate;
    private final float duration;

    public LoanCalculatorViewState(String strMonthlyPayment, String strAmount, String strRate, String strDuration, float amount, float rate, float duration) {
        this.strMonthlyPayment = strMonthlyPayment;
        this.strAmount = strAmount;
        this.strRate = strRate;
        this.strDuration = strDuration;
        this.amount = amount;
        this.rate = rate;
        this.duration = duration;
    }

    public String getStrMonthlyPayment() {
        return strMonthlyPayment;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public String getStrRate() {
        return strRate;
    }

    public String getStrDuration() {
        return strDuration;
    }

    public float getAmount() {
        return amount;
    }

    public float getRate() {
        return rate;
    }

    public float getDuration() {
        return duration;
    }
}
