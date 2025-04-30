package com.afaryseller.ui.login;

import static com.afaryseller.retrofit.Constant.emailPattern;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityLoginBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.home.HomeAct;
import com.afaryseller.ui.login.model.LoginModel;
import com.afaryseller.ui.otp.OtpAct;
import com.afaryseller.ui.selectskills.SelectSkillsAct;
import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.ui.signup.SignupAct;
import com.afaryseller.ui.signup.SignupViewModel;
import com.afaryseller.ui.splash.AskAct;
import com.afaryseller.ui.splash.AskBottomSheet;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.ui.splash.PermissionPageOneAct;
import com.afaryseller.ui.splash.WebViewAct;
import com.afaryseller.ui.splash.WebViewBottomSheet;
import com.afaryseller.ui.subscription.SubscriptionAct;
import com.afaryseller.ui.subseller.login.SubSellerLoginAct;
import com.afaryseller.ui.uploadids.UploadIdsAct;
import com.afaryseller.utility.CountryCodes;
import com.afaryseller.utility.SessionManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class LoginAct extends BaseActivity<ActivityLoginBinding,LoginViewModel> implements AskListener {
    public String TAG = "LoginAct";
    ActivityLoginBinding binding;
    LoginViewModel loginViewModel;
    String firebaseToken="";
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_login);
        loginViewModel = new LoginViewModel();
        binding.setLoginViewModel(loginViewModel);
        binding.getLifecycleOwner();
        loginViewModel.init(LoginAct.this);
        initViews();
        observeLoader();
        observeResponse();
    }

    private void initViews() {

        setCountryCodeFromLocation();

        if(getIntent()!=null){
            if(getIntent().getStringExtra("type").equalsIgnoreCase("Become a Seller")){
                binding.tvTitle.setVisibility(View.VISIBLE);
            }
            else  binding.tvTitle.setVisibility(View.GONE);

        }

        //binding.ccp.setCountryForPhoneCode(241);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);
            firebaseToken = token;

        });

        binding.forgotPassword.setOnClickListener(view -> startActivity(new Intent(LoginAct.this,ForgotPassword.class)));

        binding.txtRegister.setOnClickListener(view -> {
            startActivity(new Intent(this, AskAct.class));
            finish();
        });

        binding.btnSubSellerLogin.setOnClickListener(view -> {
            startActivity(new Intent(this, SubSellerLoginAct.class));
          //  startActivity(new Intent(this, OtpAct.class));


        });

        binding.btnLogin.setOnClickListener(view -> validation());

        binding.txtTerms.setOnClickListener(v ->
              //  startActivity(new Intent(LoginAct.this, WebViewAct.class)
//                        .putExtra("title",getString(R.string.terms_and_conditions))));

        new WebViewBottomSheet(ApiConstant.TERMS_AND_CONDITIONS,getString(R.string.terms_and_conditions)).callBack(this::ask).show(getSupportFragmentManager(),""));

    }

    public void validation() {
        if (binding.edtEmail.getText().toString().trim().equals("")) {
            binding.edtEmail.setError(getString(R.string.required));
            binding.edtEmail.setFocusable(true);
        }
        else if (binding.password.getText().toString().equals("")) {
            binding.password.setError(getString(R.string.enter_password));
            binding.password.setFocusable(true);
        }
        else {
            HashMap<String,String> map = new HashMap<>();
            map.put("email",binding.edtEmail.getText().toString().trim());
            map.put("password",binding.password.getText().toString());
            map.put("country_code",binding.ccp.getSelectedCountryCode()+"");
            map.put("lat","22.25664");
            map.put("lon","22.25664");
            // map.put("language","en");
            map.put("type", Constant.SUBADMIN);
          //   map.put("register_id",firebaseToken);
            map.put("seller_register_id",firebaseToken);

            Log.e(TAG, " Login Request ==="+ map);
            loginViewModel.login(LoginAct.this,map);
        }


    }


    public void observeResponse(){
        loginViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.SELLER_LOGIN){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("result");
                                LoginModel loginModel = new Gson().fromJson(stringResponse,LoginModel.class);
                                if(loginModel.getResult().getType().equalsIgnoreCase(Constant.SUBADMIN)){
                                    if(loginModel.getResult().getStep().equalsIgnoreCase("1")){
                                      /*  startActivity(new Intent(LoginAct.this, OtpAct.class)
                                                .putExtra("mobile",loginModel.getResult().getMobile())
                                                .putExtra("user_id",loginModel.getResult().getId())
                                                .putExtra("type","reg"));
                                        finish();*/
                                        startActivity(new Intent(LoginAct.this, SelectSkillsAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId())
                                                .putExtra("type","reg"));
                                        finish();
                                    }
                                    else if(loginModel.getResult().getStep().equalsIgnoreCase("2")){
                                        startActivity(new Intent(LoginAct.this, SelectSkillsAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId())
                                                        .putExtra("type","reg"));
                                        finish();
                                    }
                                    else if(loginModel.getResult().getStep().equalsIgnoreCase("3")){
                                        startActivity(new Intent(LoginAct.this, UploadIdsAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId()));
                                        finish();
                                    }

                                    else if(loginModel.getResult().getStep().equalsIgnoreCase("4")){
                                        startActivity(new Intent(LoginAct.this, PermissionPageOneAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId()));
                                        finish();
                                    }

                                    else {
                                        Toast.makeText(LoginAct.this, getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
                                        SessionManager.writeString(LoginAct.this, Constant.SELLER_INFO, stringResponse);
                                        SessionManager.writeString(LoginAct.this, Constant.shopId, loginModel.getResult().getShopId());

                                        startActivity(new Intent(LoginAct.this, HomeAct.class)
                                                .putExtra("status","")
                                                .putExtra("msg","")
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }


                                }

                                else if(loginModel.getResult().getType().equalsIgnoreCase(Constant.USER)){
                                    if(loginModel.getResult().getStep().equalsIgnoreCase("1")){
                                       /* startActivity(new Intent(LoginAct.this, OtpAct.class)
                                                .putExtra("mobile",loginModel.getResult().getMobile())
                                                .putExtra("countryCode",loginModel.getResult().getCountryCode())
                                                .putExtra("user_id",loginModel.getResult().getId()));

                                        finish();*/

                                        startActivity(new Intent(LoginAct.this, SelectSkillsAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId())
                                                .putExtra("type","reg"));
                                        finish();
                                    }
                                    else if(loginModel.getResult().getStep().equalsIgnoreCase("2")){
                                        startActivity(new Intent(LoginAct.this, SelectSkillsAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId())
                                                .putExtra("type","reg"));
                                        finish();
                                    }
                                    else if(loginModel.getResult().getStep().equalsIgnoreCase("3")){
                                        startActivity(new Intent(LoginAct.this, UploadIdsAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId()));
                                        finish();
                                    }
                                    else if(loginModel.getResult().getStep().equalsIgnoreCase("4")){
                                        startActivity(new Intent(LoginAct.this, PermissionPageOneAct.class)
                                                .putExtra("user_id",loginModel.getResult().getId()));
                                        finish();
                                    }

                                    else {
                                        Toast.makeText(LoginAct.this, getString(R.string.login_successfully), Toast.LENGTH_SHORT).show();
                                        SessionManager.writeString(LoginAct.this, Constant.SELLER_INFO, stringResponse);
                                        startActivity(new Intent(LoginAct.this, HomeAct.class)
                                                .putExtra("status","")
                                                .putExtra("msg","")
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                        finish();
                                    }


                                }

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(LoginAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            /*if (jsonObject.getString("status").toString().equals("10")) {
                                startActivity(new Intent(LoginAct.this, OtpAct.class)
                                        .putExtra("mobile", jsonObject.getJSONObject("result").getString("mobile"))
                                        .putExtra("countryCode", jsonObject.getJSONObject("result").getString("country_code"))
                                        .putExtra("user_id", jsonObject.getJSONObject("result").getString("id")));
                                finish();
                            }*/


                            else if (jsonObject.getString("status").toString().equals("3")) {
                               // LoginModel loginModel = new Gson().fromJson(stringResponse,LoginModel.class);
                                dialogAlert(jsonObject.getString("seller_id"),jsonObject.getString("message"));
                            }

                        }
                        else {
                            Toast.makeText(LoginAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } );
    }

    private void observeLoader() {
        loginViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(LoginAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    private void dialogAlert(String sellerId,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginAct.this);
        builder.setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.go), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(LoginAct.this, SelectSkillsAct.class)
                                .putExtra("user_id",sellerId)
                                .putExtra("type","reg"));
                        finish();

                    }
                })/*.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })*/;

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void ask(String Value,String status) {

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

    private int getCountryPhoneCode(String countryCode) {
        switch (countryCode) {
            case "IN": return 91; // India
            case "US": return 1;  // United States
            case "GA": return 241; // Gabon

            // Add other countries as needed
            default: return 1; // Default to US if unknown
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




}
