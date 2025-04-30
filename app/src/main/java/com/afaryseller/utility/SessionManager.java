package com.afaryseller.utility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.splash.SplashAct;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SessionManager {
    public static String TAG = "SessionManager";
    public static final String PREF_NAME = "AfarySeller";
    public static final String USER_INFO = "userinfo";


    public static final int MODE = Context.MODE_PRIVATE;


    public static void writeInteger(Context context, String key, int value) {
        getEditor(context).putInt(key, value).commit();

    }

    public static int readInteger(Context context, String key, int defValue) {
        return getPreferences(context).getInt(key, defValue);
    }

    public static void writeString(Context context, String key, String value) {
        getEditor(context).putString(key, value).commit();

    }

    public static String readString(Context context, String key, String defValue) {
        return getPreferences(context).getString(key, defValue);
    }

    public static void writeBoolean(Context context, String key, Boolean value) {
        getEditor(context).putBoolean(key, value).commit();

    }

    public static Boolean readBoolean(Context context, String key,
                                      Boolean defValue) {
        return getPreferences(context).getBoolean(key, defValue);
    }

    public static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREF_NAME, MODE);
    }

    public static SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public static void clear(final Context context, String user_id) {
        Logout(user_id,context);

       // getEditor(context).clear().commit();
      //  Intent intent = new Intent(context, SplashAct.class);
      //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
      //  context.startActivity(intent);
    }


    public static void logout(final  Context context){
         getEditor(context).clear().commit();
         Intent intent = new Intent(context, SplashAct.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
          context.startActivity(intent);
    }



    public static void Logout(String id, Context context) {
        DataManager.getInstance().showProgressMessage((Activity) context,"Please wait...");

        AfarySeller apiInterface = ApiClient.getClient(context.getApplicationContext()).create(AfarySeller.class);
        Map<String, String> map = new HashMap<>();
        map.put("user_id",id);
        Log.e(TAG,"User Logout Request "+map);
        Call<ResponseBody> loginCall = apiInterface.logoutApi(map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);

                    if(jsonObject.getString("status").equals("1")){
                        getEditor(context).clear().commit();
                       // DataManager.updateResources(context,"en");
                        Intent intent = new Intent(context, SplashAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        context.startActivity(intent);
                    }
                    else if(jsonObject.getString("status").equals("0")){
                        //App.showToast(context,"data not available", Toast.LENGTH_SHORT);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    public static void clearSession(final Context context) {
        getEditor(context).clear().commit();
    }
}
