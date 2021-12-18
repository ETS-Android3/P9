package com.openclassrooms.realestatemanager.utils;

public class LoanCalculator {

    /**
     * calculate monthly payment
     * @param amount : $
     * @param rate : annual rate
     * @param duration : duration in month
     * @return monthly payment
     */
    public double calculateMonthlyPayment(float amount, double rate, int duration){


        /*
            mp = monthly payment
            pr = periodic Rate
            a =  amount
            d = duration in month

        pr = rate / 12

                                 d
                a * pr * (1 + pr)
        mp =  -----------------------------------
                        d
                (1 + pr)  -1
        */

        if (amount == 0)
            return 0;
        if (duration == 0)
            return amount;
        if (rate == 0)
            return amount / duration;

        double pr = rate / 12 / 100;
        return (amount * pr * Math.pow(1 + pr, duration)) / (Math.pow(1 + pr, duration) - 1);
    }
}
