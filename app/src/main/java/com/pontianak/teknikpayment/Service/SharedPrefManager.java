package com.pontianak.teknikpayment.Service;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    public static final String SP_SEKOLAH_APP = "spumpay";

    public static final String SP_ID = "spId";
    public static final String SP_NAMA = "spNama";
    public static final String SP_USERNAME = "spUsername";
    public static final String SP_PASSWORD = "spPassword";
    public static final String SP_COOKIE = "Set-Cookie";
    public static final String SP_LEVEL="spLevel";
    public static final String SP_IDDOSEN="idDosen";
    public static final String SP_NAMADOSEN="namaDosen";
    public static final String SP_TOKEN="token";
    public static final String SP_EXPTOKEN="exptoken";


    public static final String SP_PHOTO="photo";

    public static final String SP_ISLOGIN="islogin";

    public static final String SP_PHONE="phone";
    public static final String SP_fitur="spfitur";

    public static final String SP_LAST_DESTINATION="splastdestination";
    public static final String SP_LAST_CODE_PRODUCT="splastcodeproduct";
    public static final String SP_LAST_ACTION="splastaction";
    public static final String SP_IS_MOCK="ismock";
    //public static final Boolean IS_FIREBASE_AUTH="firebaseauth";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_SEKOLAH_APP, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }



    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }


    public String getSpId(){
        return sp.getString(SP_ID, " - ");
    }

    public String getSpCookie(){
        return sp.getString(SP_COOKIE, " - ");
    }

    public String getSpLevel(){
        return sp.getString(SP_LEVEL, " - ");
    }

    public String getSpNama(){
        return sp.getString(SP_NAMA, " - ");
    }
    public String getSpString(String param){
        return sp.getString(param, " - ");
    }
    public int getSpInt(String param){
        return sp.getInt(param, 0);
    }

    public boolean getSpBoolean(String param){
        return sp.getBoolean(param, false);
    }

    public String getSpUsername(){
        return sp.getString(SP_USERNAME, " - ");
    }

}


