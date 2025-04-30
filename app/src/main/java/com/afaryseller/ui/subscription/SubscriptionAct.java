package com.afaryseller.ui.subscription;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySubscriptionBinding;
import com.afaryseller.databinding.DialogSubscriptionPayBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.ui.shoplist.listerner.ShopListener;
import com.afaryseller.ui.subscription.adapter.SubscriptionAdapter;
import com.afaryseller.ui.subscription.model.SubscriptionModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionAct extends BaseActivity<ActivitySubscriptionBinding,SubscriptionViewModel> implements ShopListener {
    public String TAG = "SubscriptionAct";
    ActivitySubscriptionBinding binding;
    SubscriptionViewModel subscriptionViewModel;
    ArrayList<SubscriptionModel.Result>subscriptionArrayList;
    String shopId="",shopName="",planType="",currency="",position="", image ="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_subscription);
        subscriptionViewModel = new SubscriptionViewModel();
        binding.setSubscriptionViewModel(subscriptionViewModel);
        binding.getLifecycleOwner();
        subscriptionViewModel.init(SubscriptionAct.this);
        initViews();
    }

    private void initViews() {
        subscriptionArrayList = new ArrayList<>();

        binding.ivBack.setOnClickListener(v -> finish());


        if(getIntent()!=null) {
            shopId = getIntent().getStringExtra("shopId");
            shopName = getIntent().getStringExtra("shop_name");
            if(getIntent().getStringExtra("currency").equals("XAF") || getIntent().getStringExtra("currency").equals("XOF")) currency = "FCFA";
            else currency =   getIntent().getStringExtra("currency");

           // currency = getIntent().getStringExtra("currency");


        }

        observeLoader();
        observeResponse();

        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");
        subscriptionViewModel.getSubscriptionPlan(SubscriptionAct.this,headerMap,DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getCountry());

    }



    public void observeResponse(){
        subscriptionViewModel.isResponse.observe(SubscriptionAct.this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();

                if(dynamicResponseModel.getApiName()== ApiConstant.GET_SUBSCRIPTION_PLAN){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get subscription plan===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                SubscriptionModel subscriptionModel = new Gson().fromJson(stringResponse, SubscriptionModel.class);
                                subscriptionArrayList.clear();
                                subscriptionArrayList.addAll(subscriptionModel.getResult());
                                binding.rvSubscription.setAdapter(new SubscriptionAdapter(SubscriptionAct.this, subscriptionArrayList,SubscriptionAct.this));

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubscriptionAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(SubscriptionAct.this);
                            }

                        }
                        else {
                            Toast.makeText(SubscriptionAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(dynamicResponseModel.getApiName()== ApiConstant.PAY_SUBSCRIPTION_PLAN){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response pay subscription plan===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                Toast.makeText(SubscriptionAct.this, "Payment SuccessFully", Toast.LENGTH_SHORT).show();
                              /*  String image ="";
                                if(position.equalsIgnoreCase("0")) image = "2";
                               else if(position.equalsIgnoreCase("1")) image = "2";
                               else if(position.equalsIgnoreCase("2")) image = "4";
                               else if(position.equalsIgnoreCase("3")) image = "2";*/

                                startActivity(new Intent(SubscriptionAct.this, AddProductAct.class)
                                       .putExtra("shopId",shopId)
                                       .putExtra("shop_name", shopName)
                                       .putExtra("currency",currency)
                                        .putExtra("images",image));
                                finish();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubscriptionAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(SubscriptionAct.this);
                            }

                        }
                        else {
                            Toast.makeText(SubscriptionAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


/*
                if(dynamicResponseModel.getApiName()== ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response pay zero subscription plan===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                Toast.makeText(SubscriptionAct.this, "Subscribe SuccessFully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SubscriptionAct.this, AddProductAct.class)
                                        .putExtra("shopId",shopId)
                                        .putExtra("shop_name", shopName));
                                finish();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubscriptionAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SubscriptionAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
*/

                if(dynamicResponseModel.getApiName()== ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response pay zero subscription plan===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                Toast.makeText(SubscriptionAct.this, "Subscribe SuccessFully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SubscriptionAct.this, AddProductAct.class)
                                        .putExtra("shopId",shopId)
                                        .putExtra("shop_name", shopName));
                                finish();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubscriptionAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(SubscriptionAct.this);
                            }

                        }
                        else {
                            Toast.makeText(SubscriptionAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } );
    }

    private void observeLoader() {
        subscriptionViewModel.isLoading.observe(SubscriptionAct.this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SubscriptionAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

    @Override
    public void editShop(String shopId, View v, ShopModel.Result data, String tag) {
        planType = subscriptionArrayList.get(Integer.parseInt(shopId)).getPlanName();
        position = shopId;
        image =  subscriptionArrayList.get(Integer.parseInt(shopId)).getImageLimit();
        if(subscriptionArrayList.get(Integer.parseInt(shopId)).getPrice().equals("0")){
            payZeroSubscription(subscriptionArrayList.get(Integer.parseInt(shopId)).getId(),subscriptionArrayList.get(Integer.parseInt(shopId)).getPrice(),subscriptionArrayList.get(Integer.parseInt(shopId)).getPlanType());
        }
       else payDialog(subscriptionArrayList.get(Integer.parseInt(shopId)).getId(),subscriptionArrayList.get(Integer.parseInt(shopId)).getPrice());
    }

    private void payDialog(String planId,String price) {
        Dialog dialog = new Dialog(SubscriptionAct.this, WindowManager.LayoutParams.MATCH_PARENT);
        DialogSubscriptionPayBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(SubscriptionAct.this), R.layout.dialog_subscription_pay, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        dialogBinding.tvPlanType.setText("Plan type : " + planType);
        dialogBinding.tvPlanAmount.setText("Amount : " + price);

        dialogBinding.RRback.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialogBinding.btnPay.setOnClickListener(v -> {
           // startActivity(new Intent(CheckOutPayment.this, SuccessScreen.class));
           // finish();
        });

        dialogBinding.llTransfer.setOnClickListener(v -> {
            // PaymentAPI("VM", strList);
        });

        dialogBinding.llMoov.setOnClickListener(v -> {
            dialogMoov("MC",planId,price);

        });

        dialogBinding.llAirtel.setOnClickListener(v -> {
            dialogAirtel("AM",planId,price);
        });

        dialogBinding.llCod.setOnClickListener(v -> {
            // PaymentAPI("AM", strList);


        });


        dialog.show();
    }



    public void dialogAirtel(String operator,String planId,String price){
        Dialog mDialog = new Dialog(SubscriptionAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_airtel);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);

        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(SubscriptionAct.this, "Please enter number", Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                paySubscription(planId,price,operator,edNumber.getText().toString());
            }

        });
        mDialog.show();

    }

    public void dialogMoov(String operator,String planId,String price){
        Dialog mDialog = new Dialog(SubscriptionAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_moov);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);

        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(SubscriptionAct.this, "Please enter number", Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                paySubscription(planId,price,operator,edNumber.getText().toString());
            }

        });

        mDialog.show();
    }



    public void paySubscription(String planId,String price,String operator,String number){
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getId());
        map.put("plan_id",planId);
      //  map.put("shop_id",shopId);
     //  if(operator.equals("free")) map.put("amount",/*price*/ "120");
        map.put("operateur",operator);
        if(operator.equals("MC"))  map.put("num_marchand", "060110217");
        else if(operator.equals("AM")) map.put("num_marchand", "074272732");
        else if(operator.equals("free")) map.put("num_marchand", "");
       // map.put("type", "Seller");
        map.put("user_number",number);
        map.put("seller_register_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getId());

        subscriptionViewModel.purchaseSubscriptionPlan(SubscriptionAct.this,headerMap,map);
        Log.e(TAG,"pay subscription plan===="+map);
    }


    public void payZeroSubscription(String planId,String price,String planType){
       /* Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

    *//*    HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getId());
        map.put("plan_id",planId);
        map.put("shop_id",shopId);
        map.put("amount","0");*//*
     //   map.put("register_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getRegisterId());
      //  subscriptionViewModel.purchaseZeroSubscriptionPlan(SubscriptionAct.this,headerMap,map);
        subscriptionViewModel.purchaseZeroSubscriptionPlan(SubscriptionAct.this,headerMap,map);

*/
        String planTy="";
        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getId());
        map.put("plan_id",planId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(SubscriptionAct.this).getResult().getId());
        if(planType.equalsIgnoreCase("Free")) planTy = "free";
        else  planTy = "commission";
        paySubscription(planId,price,planTy,"");



        Log.e(TAG,"pay zero subscription plan===="+map);
    }



}
