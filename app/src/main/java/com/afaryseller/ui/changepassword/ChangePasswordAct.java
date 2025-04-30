package com.afaryseller.ui.changepassword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityChangePasswordBinding;
import com.afaryseller.databinding.ActivityLoginBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.editTime.EditTimeFragment;
import com.afaryseller.ui.home.HomeAct;
import com.afaryseller.ui.login.LoginAct;
import com.afaryseller.ui.login.LoginViewModel;
import com.afaryseller.ui.login.model.LoginModel;
import com.afaryseller.ui.otp.OtpAct;
import com.afaryseller.ui.selectskills.SelectSkillsAct;
import com.afaryseller.ui.splash.PermissionPageOneAct;
import com.afaryseller.ui.uploadids.UploadIdsAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordAct extends BaseActivity<ActivityChangePasswordBinding,ChangePassViewModel> {
    public String TAG = "ChangePasswordAct";
    ActivityChangePasswordBinding binding;
    ChangePassViewModel changePassViewModel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        changePassViewModel = new ChangePassViewModel();
        binding.setChangePassViewModel(changePassViewModel);
        binding.getLifecycleOwner();
        changePassViewModel.init(ChangePasswordAct.this);
        initViews();
        observeLoader();
        observeResponse();

    }

    private void initViews() {
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
            headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ChangePasswordAct.this).getResult().getAccessToken());
            headerMap.put("Accept","application/json");
            HashMap<String,String> map = new HashMap<>();
            map.put("user_id", DataManager.getInstance().getUserData(ChangePasswordAct.this).getResult().getId());
            map.put("old_password",binding.oldPassword.getText().toString());
            map.put("new_password",binding.newPassword.getText().toString());
            map.put("seller_register_id", DataManager.getInstance().getUserData(ChangePasswordAct.this).getResult().getRegisterId());

            Log.e(TAG, " Login Request ==="+ map);
            changePassViewModel.changePass(ChangePasswordAct.this,headerMap,map);
        }


    }


    public void observeResponse(){
        changePassViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.CHANGE_PASSWORD){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                Toast.makeText(ChangePasswordAct.this, getString(R.string.password_changed_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(ChangePasswordAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ChangePasswordAct.this);
                            }


                        }
                        else {
                            Toast.makeText(ChangePasswordAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }

    private void observeLoader() {
        changePassViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(ChangePasswordAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

}
