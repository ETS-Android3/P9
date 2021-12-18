package com.openclassrooms.realestatemanager.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Philippe on 21/02/2018.
 */

public class Utils {

    /**
     * rate used to convert dollars to euros
     * @return
     */
    private static double getRate(){
        return 0.812f;
    }

    /**
     * Conversion d'un prix d'un bien immobilier (Dollars vers Euros)
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param dollars
     * @return
     */
    public static int convertDollarToEuro(int dollars){
        return (int) Math.round(dollars * Utils.getRate());
    }

    /**
     *  Converting euros to dollars
     * @param euros
     * @return
     */
    public static int convertEuroToDollar(int euros){
        return (int) Math.round(euros / Utils.getRate());
    }
    /**
     * Conversion de la date d'aujourd'hui en un format plus approprié
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @return
     */
    public static String getTodayDate(){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(new Date());
    }

    /**
     * Vérification de la connexion réseau
     * NOTE : NE PAS SUPPRIMER, A MONTRER DURANT LA SOUTENANCE
     * @param context
     * @return
     */
    public static Boolean isInternetAvailable(Context context){
        //WifiManager wifi = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
        //return wifi.isWifiEnabled();
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = Objects.requireNonNull(connectivityManager).getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * price
     */
    public static String convertPriceToString(int price){
        // convert to local currency
        // € : 135000000 -> "135 000 000 €"
        // $ : 135000000 -> "$135,000,000"
        if (UnitLocale.getDefault() == UnitLocale.Metric) {
            price = convertDollarToEuro(price);
        }

        return formatPrice(price);
    }

    public static String formatPrice(int price){
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        // Suppress decimal places
        formatter.setMaximumFractionDigits(0);
        return formatter.format(price);
    }

    /**
     * surface
     */
    private static String formatSurfaceToImperial(int surface){
        // 9768 -> "9,768 ft²"
        return String.format(Locale.getDefault(), "%,d ft²", surface);
    }

    private static String formatSurfaceToMeter(int surface){
        return String.format(Locale.getDefault(), "%,d m²", surface);
    }

    private static int convertSurfaceToMeter(int surface){
        return (int) Math.round(surface / 3.2808);
    }

    public static int convertSurfaceToImperial(int surface){
        return (int) Math.round(surface * 3.2808);
    }

    public static String convertSurfaceToString(int surface){
        if (UnitLocale.getDefault() == UnitLocale.Imperial) {
            return formatSurfaceToImperial(surface);
        } else {
            return formatSurfaceToMeter(convertSurfaceToMeter(surface));
        }
    }

    /**
     * Date
     */
    private static String formatDateToUS(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    private static String formatDateToEuropean(Date date){
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(date);
    }

    public static String convertDateToString(Date date){
        if (date == null) return "";

        if (UnitLocale.getDefault() == UnitLocale.Imperial) {
            return formatDateToUS(date);
        }
        else {
            return formatDateToEuropean(date);
        }
    }

    public static String convertDateToLocalFormat(Date date){
        try {
            return DateFormat.getDateInstance(DateFormat.LONG).format(date);
        } catch (NullPointerException e) {
            return "";
        }
    }

    public static Date convertStringInLocalFormatToDate(String text){
        try {
            return DateFormat.getDateInstance(DateFormat.LONG).parse(text);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
