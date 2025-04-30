package com.afaryseller.ui.notifyadmin;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityNotificationBinding;
import com.afaryseller.databinding.ActivityNotifyAdminBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.chat.ChatAct;
import com.afaryseller.ui.notifications.NotificationViewModel;
import com.afaryseller.ui.notifications.adapter.NotificationAdapter;
import com.afaryseller.ui.notifications.model.NotificationModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;




public class NotifyAdminAct extends BaseActivity<ActivityNotifyAdminBinding, NotifyAdminViewModel> {
    ActivityNotifyAdminBinding binding;
    NotifyAdminViewModel notifyAdminViewModel;
    private ArrayList<NotificationModel.Result> arrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notify_admin);
        notifyAdminViewModel = new NotifyAdminViewModel();
        binding.setNotifyAdminViewModel(notifyAdminViewModel);
        binding.getLifecycleOwner();
        notifyAdminViewModel.init(this);
        initializeViews();
        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();

        binding.RRback.setOnClickListener(v -> finish());

        observeLoader();
        observeResponse();

        getMsg();
    }


    private void initializeViews() {
        binding.RRback.setOnClickListener(v -> finish());
        binding.ChatLayout.imgSendIcon.setOnClickListener(v -> {
            if (binding.ChatLayout.tvMessage.getText().toString().length() > 0 ) {
                String messageText = binding.ChatLayout.tvMessage.getText().toString().trim();
                if (!messageText.equals("")) {
                    notifyToAdmin(messageText);
                }
                binding.ChatLayout.tvMessage.setText("");
            } else {
                binding.ChatLayout.tvMessage.setError("Field is Blank");
            }
        });

        disablesend_button();

        binding.ChatLayout.tvMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.ChatLayout.imgSendIcon.setEnabled(true);
                    binding.ChatLayout.imgSendIcon.setAlpha((float) 1.0);
                } else {
                    disablesend_button();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void notifyToAdmin(String messageText) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(NotifyAdminAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("message",messageText);
        map.put("user_id", DataManager.getInstance().getUserData(NotifyAdminAct.this).getResult().getId());

        Log.e("NotifyAdminAct", "Notify admin msg Request :" + map);
        notifyAdminViewModel.sendAdminMsg(NotifyAdminAct.this,headerMap,map);
    }


    private void disablesend_button() {
        binding.ChatLayout.imgSendIcon.setEnabled(false);
        binding.ChatLayout.imgSendIcon.setAlpha((float) 0.3);
    }


    private void getMsg() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(NotifyAdminAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(this).getResult().getId());
        notifyAdminViewModel.getAdminMsg(this,headerMap,map);

    }

    public void observeResponse(){
        notifyAdminViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();


                if(dynamicResponseModel.getApiName()== ApiConstant.GET_ADMIN_MSG){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                binding.tvMsg.setVisibility(View.VISIBLE);
                                if(!jsonObject.getJSONArray("data").getJSONObject(0).getString("message_from_admin").contains("null")){
                                    binding.tvMsg.setText(jsonObject.getJSONArray("data").getJSONObject(0).getString("message_from_admin"));
                                }
                                else {
                                    binding.tvMsg.setVisibility(View.GONE);
                                }

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                binding.tvMsg.setVisibility(View.GONE);

                            }



                        }
                        else {
                            binding.tvMsg.setVisibility(View.GONE);
                            Toast.makeText(this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.SEND_ADMIN_MSG){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                Toast.makeText(this,getString(R.string.notify_admin_successfully), Toast.LENGTH_SHORT).show();
                                finish();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }



                        }
                        else {
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
        notifyAdminViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

}

