package com.afaryseller.ui.wallet.bottomsheet;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConnector {

    public static final String PREF_NAME = "AfarySeller";
    public static final int MODE = Context.MODE_PRIVATE;
    public static final String LoginStatus = "statuss";
    public static final String User_id = "user_idd";

    public static final String Register_id = "register_id";

    public static final String Cat_id = "cat_id";

    public static final String User_Mobile = "User_Mobile";
    public static final String OTP = "Otp";
    public static String User_First_name ="User_First_name";
    public static final String add = "sdfg";
    public static String User_email="User_email";
    public static final String Password = "Password";
    public static final String Firebash_Token ="Firebash_Token";
    public static final String LAT ="lat";
    public static final String LON ="lon";
    public static final String LANGUAGE ="language";


    public static final String COUNTRY_ID ="country_id";

    public static final String ADDRESS_ID ="address_id";

    public static final String filterType ="filterType";




    public static final String FROM ="from";


    public static String User_name ="user_name";
    public static String User_img ="user_img";

    public static String access_token ="access_token";

    public static String countryId ="country_id";

    public static String countryName ="country_name";

    public static String transId ="transaction_id";

    public static String serviceType ="service_type";

    public static String Booking ="Booking";

    public static String Wallet ="Wallet";

    public static String ShareUserId ="ShareUserId";

    public static String PaymentType ="PaymentType";


    public static String writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

        return key;
    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }


    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }


    public static void clearSession(final Context context) {
        getEditor(context).clear().commit();
    }

}
