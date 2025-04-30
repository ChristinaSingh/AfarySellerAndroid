package com.afaryseller.ui.home;

import static androidx.navigation.Navigation.findNavController;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.afaryseller.R;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.home.adapter.HomeCatAdapter;
import com.afaryseller.ui.home.adapter.SliderAdapterExample;
import com.afaryseller.ui.home.model.BannerModal1;
import com.afaryseller.ui.home.model.HomeCatModel;
import com.afaryseller.ui.login.LoginAct;
import com.afaryseller.ui.login.model.LoginModel;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeAct extends AppCompatActivity {
    String status="",msg="";
    AfarySeller apiInterface;


    BroadcastReceiver OrderStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("status") != null) {
                Log.e("msg====",intent.getStringExtra("msg"));
                try {
                    if("You have been logged out because you have logged in on another device".equals(intent.getStringExtra("msg")))
                    {
                        Log.e("Logout msg====",intent.getStringExtra("msg"));
                        SessionManager.clearSession(HomeAct.this);
                        startActivity(new Intent(HomeAct.this, SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    };




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        apiInterface = ApiClient.getClient(HomeAct.this).create(AfarySeller.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_service, R.id.navigation_wallet, R.id.navigation_profile)
                .build();

        NavController navController = findNavController(this, R.id.nav_host_fragment);
       // NavigationUI.setupActionBarWithNavController(navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        if(getIntent()!=null) {
            status = getIntent().getStringExtra("status");
            msg = getIntent().getStringExtra("msg");

          if(status!=null || !status.equalsIgnoreCase("")) {
              if (status.equals("cancelByUser")) createAndShowDialog(HomeAct.this, msg);
            else  if (status.equals("ProductValidate")) createAndShowDialog(HomeAct.this, msg);
            else if(status.equals("updateStock")){
                  updateProductStockDialog(HomeAct.this,msg,getIntent().getStringExtra("product_id"),getIntent().getStringExtra("user_id"),
                          getIntent().getStringExtra("product_name"),getIntent().getStringExtra("product_sku"),getIntent().getStringExtra("product_image"));
              }
          }
        }

       // GetProfile();

    }



    public void createAndShowDialog(Context context, String msg) {
     //   Dialog dialog = new Dialog(context, R.style.FullScreenDialog1);
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.layout_fullscreen_dialog);

        TextView tv = dialog.findViewById(R.id.tvMsg);
        RelativeLayout btnOk = dialog.findViewById(R.id.btnOk);

        tv.setText(msg);
        btnOk.setOnClickListener(view -> dialog.dismiss());

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }


    public void updateProductStockDialog(Context context, String msg,String productId,String userId,String productName,String productSku,String productImg) {
        //   Dialog dialog = new Dialog(context, R.style.FullScreenDialog1);
        Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.layout_update_stock_dialog);

        TextView tv = dialog.findViewById(R.id.tvMsg);
        TextView tvProductName = dialog.findViewById(R.id.tvProductName);
        TextView tvProductId = dialog.findViewById(R.id.tvProductId);
        TextView tvProductSku = dialog.findViewById(R.id.tvProductSku);
        ImageView ivProductImg = dialog.findViewById(R.id.ivProductImg);



        RelativeLayout btnCancel = dialog.findViewById(R.id.btnCancel);
        RelativeLayout btnUpdate = dialog.findViewById(R.id.btnUpdate);

        tv.setText(msg);
        tvProductName.setText(getString(R.string.product_name_new)+" " + productName);
        tvProductId.setText("Product ID : " + productId);
        tvProductSku.setText("SKU : " + productSku);
        Glide.with(HomeAct.this).load(productImg).override(100,100).error(R.drawable.logo_well1).into(ivProductImg);


        btnCancel.setOnClickListener(view -> {
            //dialog.dismiss()
            updateProductAvailability(productId,userId,dialog,"No");

        });

        btnUpdate.setOnClickListener(view -> {
            updateProductAvailability(productId,userId,dialog,"Yes");
        });



        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }

    private void updateProductAvailability(String productId,String userId,Dialog dialog,String stock) {

        DataManager.getInstance().showProgressMessage(HomeAct.this, getString(R.string.please_wait));
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(HomeAct.this).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("product_id", productId);
        map.put("seller_id",DataManager.getInstance().getUserData(HomeAct.this).getResult().getId());
        map.put("in_stock", stock);
        map.put("user_seller_id",DataManager.getInstance().getUserData(HomeAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(HomeAct.this).getResult().getRegisterId());

        Log.e("MapMap", "Update Product availability" + map);


        Call<ResponseBody> loginCall = apiInterface.updateProductAvailabilityApi(headerMap, map);

        loginCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    Log.e("response===", response.body().toString());
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);

                    if (jsonObject.getString("status").toString().equals("1")) {
                        dialog.dismiss();
                        //    Toast.makeText(ProductDetailAct.this, " Add Wish List ", Toast.LENGTH_SHORT).show();
                    } else if (jsonObject.getString("status").toString().equals("0")) {

                        //  Toast.makeText(ProductDetailAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    else if (jsonObject.getString("status").toString().equals("5")) {
                        SessionManager.logout(HomeAct.this);
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

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(OrderStatusReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(OrderStatusReceiver, new IntentFilter("check_statusLogout"),Context.RECEIVER_EXPORTED);
        }
        else {
            registerReceiver(OrderStatusReceiver, new IntentFilter("check_statusLogout"));        }


    }





  /*  private void GetProfile() {
        DataManager.getInstance().showProgressMessage(HomeAct.this, getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(HomeAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(HomeAct.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(HomeAct.this).getResult().getRegisterId());
        Call<ResponseBody> loginCall = apiInterface.getSellerProfileApi(headerMap,map);

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();
                try {
                    Log.e("response===", response.body().toString());
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);
                    if (jsonObject.getString("status").equals("1")) {
                        JSONObject object = jsonObject.getJSONObject("result");
                        LoginModel loginModel = new Gson().fromJson(stringResponse,LoginModel.class);
                        SessionManager.writeString(HomeAct.this, Constant.SELLER_INFO, stringResponse);

                        if(loginModel.getResult().getLanguage().equalsIgnoreCase("en")) changeLocale("en");
                        else if(loginModel.getResult().getLanguage().equalsIgnoreCase("fr")) changeLocale("fr");
                        else changeLocale("en");

                    } else if (jsonObject.getString("status").equals("0")) {
                        Toast.makeText(HomeAct.this, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
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


    private void changeLocale(String en) {
        updateResources(HomeAct.this,en);

    }


    private void updateResources(Context wellcomeScreen, String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Resources resources = wellcomeScreen.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }*/

}
