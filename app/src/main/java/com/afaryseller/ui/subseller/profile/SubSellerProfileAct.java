package com.afaryseller.ui.subseller.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySubSellerProfileBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.editprofile.EditProfileAct;
import com.afaryseller.ui.membershipplan.CurrentMemberShipPlanAct;
import com.afaryseller.ui.notifyadmin.NotifyAdminAct;
import com.afaryseller.ui.signup.CityModel;
import com.afaryseller.ui.signup.CountryModel;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.ui.subseller.changepassword.SubSellerChangePasswordAct;
import com.afaryseller.ui.subseller.subsellerlsit.SubSellerListAct;
import com.afaryseller.ui.subseller.updateprofile.UpdateSubSellerProfileAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubSellerProfileAct extends BaseActivity<ActivitySubSellerProfileBinding, SubSellerProfileViewModel> {
    public String TAG = "SubSellerProfileAct";
    ActivitySubSellerProfileBinding binding;
    SubSellerProfileViewModel subSellerProfileViewModel;
    String subSellerId="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_seller_profile);
        subSellerProfileViewModel = new SubSellerProfileViewModel();
        binding.setSubSellerProfileViewModel(subSellerProfileViewModel);
        binding.getLifecycleOwner();
        subSellerProfileViewModel.init(SubSellerProfileAct.this);
        initViews();
        observeLoader();
        observeResponse();



    }

    private void initViews() {

        if(getIntent()!=null){
            subSellerId = getIntent().getStringExtra("subSellerId");
        }


        binding.backNavigation.setOnClickListener(v -> finish());


        binding.txtupdate.setOnClickListener(v -> startActivity(new Intent(SubSellerProfileAct.this, UpdateSubSellerProfileAct.class)
                .putExtra("subSellerId",subSellerId)));


        binding.txtChangePassword.setOnClickListener(v -> startActivity(new Intent(SubSellerProfileAct.this, SubSellerChangePasswordAct.class)
                .putExtra("subSellerId",subSellerId)));

        binding.txtDeleteAccount.setOnClickListener(v -> deleteAccountDialog());
    }

    private void deleteAccountDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SubSellerProfileAct.this);
        builder.setTitle(getString(R.string.delete_account))
                .setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_account))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        deleteSubSellerAccount();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteSubSellerAccount() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(SubSellerProfileAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("sub_seller_id", subSellerId);

        subSellerProfileViewModel.deleteSubSellerProfile(SubSellerProfileAct.this,headerMap, map);
    }


    public void observeResponse() {
        subSellerProfileViewModel.isResponse.observe(this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();

                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SUB_SELLER_PROFILE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("get sub seller profile response===", dynamicResponseModel.getJsonObject().toString());

                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("data");
                                //  binding.tvName.setText(object.getString("name") + " " + object.getString("last_name"));
                                subSellerId = object.getString("id");
                                binding.tvName.setText(object.getString("name") );

                                binding.tvEmail.setText(object.getString("email"));
                                binding.tvAddress.setText(object.getString("address"));
                                //binding.tvEmail.setText(object.getString("email"));


                                Glide.with(SubSellerProfileAct.this).load(object.getString("image"))
                                        .error(R.drawable.user_default)
                                        .placeholder(R.drawable.user_default)
                                        .into(binding.ivSeller);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubSellerProfileAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }




                        } else {
                            Toast.makeText(SubSellerProfileAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.DELETE_SUB_SELLER_PROFILE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("delete sub seller account response===", dynamicResponseModel.getJsonObject().toString());
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                //SessionManager.writeString(EditProfileAct.this, Constant.SELLER_INFO, stringResponse);
                                Toast.makeText(SubSellerProfileAct.this, getString(R.string.delete_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubSellerProfileAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(SubSellerProfileAct.this);
                            }


                        } else {
                            Toast.makeText(SubSellerProfileAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




            }
        });
    }

    private void observeLoader() {
        subSellerProfileViewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SubSellerProfileAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }



    private void getSubSellerProfile() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(SubSellerProfileAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("sub_seller_id", subSellerId);
        subSellerProfileViewModel.getSubSellerProfile(SubSellerProfileAct.this,headerMap, map);
    }


    @Override
    protected void onResume() {
        super.onResume();
        getSubSellerProfile();
    }
}
