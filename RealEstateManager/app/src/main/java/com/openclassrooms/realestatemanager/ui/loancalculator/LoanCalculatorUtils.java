package com.openclassrooms.realestatemanager.ui.loancalculator;

import android.util.Log;

import com.openclassrooms.realestatemanager.MainApplication;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.tag.Tag;
import com.openclassrooms.realestatemanager.utils.Utils;

public class LoanCalculatorUtils {

    public static String formatAmount(float amount){
        return Utils.formatPrice((int)amount);
    }

    public static String formatRate(float rate){
        return String.format("%.02f%s", rate, "%");
    }

    public static String formatDuration(float month){
        return String.format("%d %s", (int)month, MainApplication.getApplication().getResources().getQuantityString(R.plurals.month, (int)month));
    }

    public static String formatPayment(double payment){
        return Utils.formatPrice((int)payment);
    }
}
