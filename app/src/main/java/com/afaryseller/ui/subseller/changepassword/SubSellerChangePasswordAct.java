package com.afaryseller.ui.subseller.changepassword;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySubSellerChangePasswordBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.utility.DataManager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;



public class SubSellerChangePasswordAct extends BaseActivity<ActivitySubSellerChangePasswordBinding, SubSellerChangePasswordViewModel> {
    public String TAG = "SubSellerChangePasswordAct";
    ActivitySubSellerChangePasswordBinding binding;
    SubSellerChangePasswordViewModel subSellerChangePasswordViewModel;
    String subSellerId="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_seller_change_password);
        subSellerChangePasswordViewModel = new SubSellerChangePasswordViewModel();
        binding.setSubSellerChangePasswordViewModel(subSellerChangePasswordViewModel);
        binding.getLifecycleOwner();
        subSellerChangePasswordViewModel.init(SubSellerChangePasswordAct.this);
        initViews();
        observeLoader();
        observeResponse();

    }

    private void initViews() {

        if(getIntent()!=null){
            subSellerId = getIntent().getStringExtra("subSellerId");
        }

        binding.RRChangePass.setOnClickListener(view -> validation());

        binding.backNavigation.setOnClickListener(v -> finish());

    }

    public void validation() {
        if (binding.oldPassword.getText().toString().equals("")) {
            binding.oldPassword.setError(getString(R.string.required));
            binding.oldPassword.setFocusable(true);
        }
        else if (binding.newPassword.getText().toString().equals("")) {
            binding.newPassword.setError(getString(R.string.required));
            binding.newPassword.setFocusable(true);
        }
        else if (binding.confirmPasswordd.getText().toString().equals("")) {
            binding.confirmPasswordd.setError(getString(R.string.required));
            binding.confirmPasswordd.setFocusable(true);
        }
        else if (!binding.confirmPasswordd.getText().toString().equals(binding.newPassword.getText().toString())) {
            binding.confirmPasswordd.setError(getString(R.string.password_does_not_matched));
            binding.confirmPasswordd.setFocusable(true);
        }

        else {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(SubSellerChangePasswordAct.this).getResult().getAccessToken());
            headerMap.put("Accept","application/json");
            HashMap<String,String> map = new HashMap<>();

            map.put("sub_seller_id",subSellerId);
            map.put("old_password",binding.oldPassword.getText().toString());
            map.put("password",binding.newPassword.getText().toString());
            map.put("confirm_password",binding.confirmPasswordd.getText().toString());

            Log.e(TAG, "sub seller change password  Request ==="+ map);
            subSellerChangePasswordViewModel.subSellerChangePassword(SubSellerChangePasswordAct.this,headerMap,map);
        }


    }


    public void observeResponse(){
        subSellerChangePasswordViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.SUB_SELLER_CHANGE_PASSWORD){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("sub seller change password response===",dynamicResponseModel.getJsonObject().toString());
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("1")) {
                                Toast.makeText(SubSellerChangePasswordAct.this, getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (jsonObject.getString("status").equals("0")) {
                                Toast.makeText(SubSellerChangePasswordAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }



                        }
                        else {
                            Toast.makeText(SubSellerChangePasswordAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }

    private void observeLoader() {
        subSellerChangePasswordViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SubSellerChangePasswordAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

}


