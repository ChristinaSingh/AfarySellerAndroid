package com.afaryseller.ui.onlineorderhistory;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityOnlineOrderHistoryBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.bookedorder.OrderAdapter;
import com.afaryseller.ui.bookedorder.OrderFragment;
import com.afaryseller.ui.bookedorder.OrderListener;
import com.afaryseller.ui.bookedorder.OrderModel;
import com.afaryseller.ui.bookedorder.OrderViewModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class onlineOrderHistoryAct extends BaseActivity<ActivityOnlineOrderHistoryBinding,OnlineHistoryViewModel> implements OrderListener {
    ActivityOnlineOrderHistoryBinding binding;
    OnlineHistoryViewModel onlineHistoryViewModel;
    OrderFragmentAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_online_order_history);
        onlineHistoryViewModel = new OnlineHistoryViewModel();
        binding.setOnlineHistoryViewModel(onlineHistoryViewModel);
        binding.getLifecycleOwner();
        onlineHistoryViewModel.init(this);
        initViews();


    }

    private void initViews() {

      //  observeLoader();
      //  observeResponse();


      //  binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Complete"));
      //  binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Cancel"));
      //  binding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        adapter = new OrderFragmentAdapter(onlineOrderHistoryAct.this,getSupportFragmentManager());

        binding.viewPager.setAdapter(adapter);

        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.tabLayout.getTabAt(0).setText(getString(R.string.complete));
        binding.tabLayout.getTabAt(1).setText(getString(R.string.cancel));
       // binding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabLayout));



        binding.RRback.setOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void onResume() {
        super.onResume();
      //  callGetCompletedOrder();
    }

    private void callGetCompletedOrder() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(onlineOrderHistoryAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(onlineOrderHistoryAct.this).getResult().getId());
        map.put("status", "Completed");
        map.put("user_seller_id", DataManager.getInstance().getUserData(onlineOrderHistoryAct.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(onlineOrderHistoryAct.this).getResult().getRegisterId());
        onlineHistoryViewModel.getOnlineOrderHistory(onlineOrderHistoryAct.this,headerMap,map);
    }





    public void observeResponse(){
        onlineHistoryViewModel.isResponse.observe(onlineOrderHistoryAct.this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.ONLINE_ORDER_HISTORY){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                OrderModel model = new Gson().fromJson(stringResponse,OrderModel.class);
                                binding.rvOrderHistory.setAdapter(new OrderAdapter(onlineOrderHistoryAct.this, (ArrayList<OrderModel.Result>) model.getResult(), onlineOrderHistoryAct.this));
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(onlineOrderHistoryAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }


                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(onlineOrderHistoryAct.this);
                            }
                        }
                        else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(onlineOrderHistoryAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        } );
    }

    private void observeLoader() {
        onlineHistoryViewModel.isLoading.observe(onlineOrderHistoryAct.this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(onlineOrderHistoryAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    @Override
    public void onOrder(OrderModel.Result result) {

    }
}
