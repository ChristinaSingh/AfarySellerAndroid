package com.afaryseller.utility;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyService extends Service {
    public static String TAG ="MyService";
    FusedLocationProviderClient mFusedLocationClient;
    public static final int notify = 5000;  // interval between two services(Here Service run every 1 Minute)
    private Handler mHandler = new Handler();   // run on another Thread to avoid crash
    private Timer mTimer = null; // timer handling
    AfarySeller apiInterface;

    public MyService() {
    }

    @Override
    public void onCreate() {
        apiInterface = ApiClient.getClient(this).create(AfarySeller.class);


        String channelId = "channel-01";

//        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channelId);
//        startForeground((int) System.currentTimeMillis(), mBuilder.build());

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startMyOwnForeground();
        } else {
            startForeground(1, new Notification());
        }*/

      // startForeground(1, new Notification());


        if (mTimer != null) // Cancel if already existed
            mTimer.cancel();
        else
            mTimer = new Timer();   // recreate new

        mTimer.scheduleAtFixedRate(new TimeDisplay(), 0, notify);   //Schedule task

    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    class TimeDisplay extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e("service is ", "running");
                    checkPaymentStatus();
                }
            });
        }
    }

  /*  private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            if (locationResult.getLastLocation() == null)
                return;
            currentLocation = locationResult.getLastLocation();
            if (firstTimeFlag && googleMap != null) {
                animateCamera(currentLocation);
                firstTimeFlag = false;
            }
            showMarker(currentLocation);
        }
    }; */



    public void checkPaymentStatus() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(getApplicationContext()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");



        Map<String,String> map = new HashMap<>();
        map.put("order_id",SessionManager.readString(getApplicationContext(),"orderId",""));
        map.put("afary_code",SessionManager.readString(getApplicationContext(),"afaryCode",""));
        map.put("user_seller_id",DataManager.getInstance().getUserData(getApplicationContext()).getResult().getId());
        map.put("register_id",DataManager.getInstance().getUserData(getApplicationContext()).getResult().getId());


        Log.e(TAG,"Driver Location Request "+map);
        Call<ResponseBody> loginCall = apiInterface.getDriverLocationApi(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Driver Location Response" + object);
                    Intent intent = new Intent("check_driver_location");
                    intent.putExtra("object", object+"");
                    sendBroadcast(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
            }
        });


    }





    @Override
    public void onDestroy() {
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            stopForeground(true); //true will remove notification
            this.stopSelf();
        }
*/
        mHandler.removeCallbacksAndMessages(null);
        mTimer.cancel();

    }



}