package com.afaryseller.ui.subseller.login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySubSellerLoginBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.home.HomeAct;
import com.afaryseller.ui.login.model.LoginModel;

import com.afaryseller.ui.signup.CountryModel;
import com.afaryseller.ui.subseller.home.SubSellerHomeAct;
import com.afaryseller.ui.subseller.profile.SubSellerProfileAct;
import com.afaryseller.ui.subseller.subsellerlsit.SubSellerListModel;
import com.afaryseller.ui.subseller.updateprofile.UpdateSubSellerProfileAct;
import com.afaryseller.utility.CountryCodes;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class SubSellerLoginAct extends BaseActivity<ActivitySubSellerLoginBinding, SubSellerLoginViewModel> {
    public String TAG = "SubSellerLoginAct";
    ActivitySubSellerLoginBinding binding;
    SubSellerLoginViewModel subSellerLoginViewModel;
    String firebaseToken="";
    ArrayList<SellerModel.Result> sellerArrayList;
    private  String sellerId ="";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private String countryCo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_seller_login);
        subSellerLoginViewModel = new SubSellerLoginViewModel();
        binding.setSubSellerLoginViewModel(subSellerLoginViewModel);
        binding.getLifecycleOwner();
        subSellerLoginViewModel.init(this);
        initViews();
        observeLoader();
        observeResponse();
    }

    private void initViews() {
        sellerArrayList = new ArrayList<>();

        setCountryCodeFromLocation();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);
            firebaseToken = token;

        });

        //  binding.forgotPassword.setOnClickListener(view -> startActivity(new Intent(LoginAct.this, ForgotPassword.class)));


        binding.tvSubSeller.setOnClickListener(view -> {
            if (sellerArrayList != null){
                showDropDownSeller(view, binding.tvSubSeller, sellerArrayList);
            }
        });





        binding.btnLogin.setOnClickListener(view -> {
            validation();
        });



    }

    public void validation() {
        /*  if(sellerId.equals("")){
              Toast.makeText(this, getString(R.string.please_select_seller), Toast.LENGTH_SHORT).show();
          }

      else*/ if (binding.edtEmail.getText().toString().trim().isEmpty()) {
            binding.edtEmail.setError(getString(R.string.required));
            binding.edtEmail.setFocusable(true);
        }
        else if (binding.password.getText().toString().isEmpty()) {
            binding.password.setError(getString(R.string.enter_password));
            binding.password.setFocusable(true);
        }
        else {
            HashMap<String,String> map = new HashMap<>();
            map.put("username_or_email_or_contact",binding.edtEmail.getText().toString().trim());
            map.put("password",binding.password.getText().toString());
            map.put("country_code",binding.ccp.getSelectedCountryCode());
          //  map.put("parent_seller_id",sellerId);
            map.put("seller_register_id",firebaseToken);
            Log.e(TAG, "Sub Seller Login Request ==="+ map);
            subSellerLoginViewModel.loginSubSeller(SubSellerLoginAct.this,map);
        }


    }


    public void observeResponse(){
        subSellerLoginViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName().equals(ApiConstant.SUB_SELLER_LOGIN)){
                    try {
                        if(dynamicResponseModel.getCode()==200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("sub seller login response===", stringResponse);
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("result");
                                LoginModel loginModel = new Gson().fromJson(stringResponse, LoginModel.class);
                               // if (loginModel.getResult().getType().equalsIgnoreCase(Constant.SUBADMIN)) {
                                    Toast.makeText(SubSellerLoginAct.this, getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
                                    SessionManager.writeString(SubSellerLoginAct.this, Constant.SELLER_INFO, stringResponse);
                                SessionManager.writeString(SubSellerLoginAct.this, Constant.shopId, loginModel.getResult().getShopId());
                                SessionManager.writeString(SubSellerLoginAct.this, Constant.SUB_SELLER_ID, jsonObject.getJSONObject("result").getString("sub_seller_id"));
                                SessionManager.writeString(SubSellerLoginAct.this, Constant.shopName, jsonObject.getJSONObject("result").getString("shop_name"));



                                startActivity(new Intent(SubSellerLoginAct.this, SubSellerHomeAct.class)
                                            .putExtra("status", "")
                                            .putExtra("msg", "")
                                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    finish();
                             //   }
                            } else if (jsonObject.getString("status").equals("0")) {
                                Toast.makeText(SubSellerLoginAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        }

                        }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SELLER_COUNTRY_WISE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("get sub-seller country wise response===", dynamicResponseModel.getJsonObject().toString());
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                SellerModel sellerModel = new Gson().fromJson(stringResponse, SellerModel.class);
                                sellerArrayList.clear();
                                sellerArrayList.addAll(sellerModel.getResult());
                            } else if (jsonObject.getString("status").equals("0")) {
                                Toast.makeText(SubSellerLoginAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubSellerLoginAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } );
    }

    private void observeLoader() {
        subSellerLoginViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SubSellerLoginAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    private void showDropDownSeller(View v, TextView textView, List<SellerModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(SubSellerLoginAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    sellerId = stringList.get(i).getId();

                }
            }
            return true;
        });
        popupMenu.show();
    }






    private void setCountryCodeFromLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            try {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                String countryCode = addresses.get(0).getCountryCode();
                                if (countryCode != null && !countryCode.isEmpty()) {
                                    Log.e("country code===", CountryCodes.getPhoneCode(countryCode) + "");
                                    binding.ccp.setCountryForPhoneCode(/*getCountryPhoneCode(countryCode)*/
                                            CountryCodes.getPhoneCode(countryCode));
                                    countryCo = CountryCodes.getPhoneCode(countryCode)+"";
                                    //getSeller();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error determining location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCountryCodeFromLocation();
            }
        }
    }


    private void getSeller() {
        HashMap<String, String> map = new HashMap<>();
        map.put("country_id", countryCo);

        subSellerLoginViewModel.getSeller(SubSellerLoginAct.this, map);
    }


}
