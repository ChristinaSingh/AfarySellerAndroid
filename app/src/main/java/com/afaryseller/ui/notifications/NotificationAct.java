package com.afaryseller.ui.notifications;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityNotificationBinding;
import com.afaryseller.retrofit.ApiConstant;

import com.afaryseller.ui.home.fragment.HomeViewModel;
import com.afaryseller.ui.notifications.adapter.NotificationAdapter;
import com.afaryseller.ui.notifications.model.NotificationModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationAct extends BaseActivity<ActivityNotificationBinding,NotificationViewModel> {
    ActivityNotificationBinding binding;
    NotificationViewModel notificationViewModel;
    private ArrayList<NotificationModel.Result> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_notification);
        notificationViewModel = new NotificationViewModel();
        binding.setNotificationViewModel(notificationViewModel);
        binding.getLifecycleOwner();
        notificationViewModel.init(this);
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        binding.RRback.setOnClickListener(v -> finish());

        observeLoader();
        observeResponse();

        callNotification();
    }


    private void callNotification() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(NotificationAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(NotificationAct.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(NotificationAct.this).getResult().getId());
        notificationViewModel.getAllNotifications(this,headerMap,map);

    }

    public void observeResponse(){
        notificationViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();


                if(dynamicResponseModel.getApiName()== ApiConstant.GET_NOTIFICATIONS){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                binding.tvNotFound.setVisibility(View.GONE);
                                arrayList.clear();
                                NotificationModel model = new Gson().fromJson(stringResponse,NotificationModel.class);
                                arrayList.addAll(model.getResult());
                                binding.rvNotification.setAdapter(new NotificationAdapter(this, (ArrayList<NotificationModel.Result>) model.getResult()));
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                binding.tvNotFound.setVisibility(View.VISIBLE);
                                Toast.makeText(this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(NotificationAct.this);
                            }

                        }
                        else {
                            binding.tvNotFound.setVisibility(View.VISIBLE);
                            Toast.makeText(this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        } );
    }

    private void observeLoader() {
        notificationViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

}
