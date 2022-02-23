package com.pontianak.teknikpayment;

import android.content.SharedPreferences;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Bantuan {
    public SharedPreferences prefs;
    public SharedPreferences.Editor meditor;

    public String session;
    public Locale localeID = new Locale("in", "ID");
    public NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeID);

    public Bantuan(){



    }
    public String cariwaktu(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String cariwaktusimpel(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
    public String WaktuOnlyHourAndTime(String time) {
        String inputPattern = "HH:mm:ss";
        String outputPattern = "HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public String torupiah(int val) {
        return "Rp. " + String.format("%,d", val).replace(',', '.');

    }
    public String torupiah(double val) {
        int blt=(int)val;
        return "Rp. " + String.format("%,d", blt).replace(',', '.');

    }

    public int meisinteger(String mystring) {
        if(mystring==null){
            mystring="0";
        }
        try {
            return Integer.parseInt(mystring);

        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
    public float meisfloat(String mystring) {
        if(mystring==null){
            mystring="0";
        }
        try {
            return Float.parseFloat(mystring);

        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
    public double meisdouble(String mystring) {
        if(mystring==null){
            mystring="0";
        }
        try {
            return Double.parseDouble(mystring);

        } catch (NumberFormatException nfe) {
            return 0;
        }
    }
    public int pembulatanuang(float uang,int pembulat){
        float a=uang/pembulat;
        int multiplier=(int) a+1;
        return multiplier*pembulat;


    }
}
