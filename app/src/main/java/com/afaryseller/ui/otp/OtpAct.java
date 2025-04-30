package com.afaryseller.ui.otp;


import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityOtpBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.selectskills.SelectSkillsAct;
import com.afaryseller.ui.signup.SignupAct;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OtpAct  extends BaseActivity<ActivityOtpBinding,OtpViewModel> {
    public String TAG = "OtpAct";

    ActivityOtpBinding binding;
    OtpViewModel otpViewModel;
    private SmsRetrieverClient smsRetrieverClient;
    String mobile="",countryCode="",userName="",name="",email="",password="",address="",lat="",lon="",countryId="",countryName="",stateId="",cityId="",lang="",type="",currency="",zipCode="",firebaseToken="";
    private static final int REQ_USER_CONSENT = 200;
    SmsBroadcastReceiver smsBroadcastReceiver;
    private static final long TIMER_DURATION = 60000; // 1 minute in milliseconds
    private static final long TIMER_INTERVAL = 1000;  // 1 second interval
    private int resendButtonCount=0;
    private int checkOtp=0;

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_otp);
        otpViewModel = new OtpViewModel();
        binding.setOtpViewModel(otpViewModel);
        binding.getLifecycleOwner();
        otpViewModel.init(OtpAct.this);
        initViews();
        observeLoader();
        observeResponse();
        startSmartUserConsent();
    }



    private void initViews() {


        if(getIntent()!=null){
            userName = getIntent().getStringExtra("user_name");
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            mobile = getIntent().getStringExtra("mobile");
            countryCode = getIntent().getStringExtra("country_code");
            password = getIntent().getStringExtra("password");
            address = getIntent().getStringExtra("address");
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");
            countryId = getIntent().getStringExtra("country");
            countryName = getIntent().getStringExtra("country_name");
            stateId = getIntent().getStringExtra("state");
            cityId = getIntent().getStringExtra("city");
            lang = getIntent().getStringExtra("language");
            type = getIntent().getStringExtra("type");
            currency = getIntent().getStringExtra("currency");
            zipCode = getIntent().getStringExtra("zip_code");
            firebaseToken = getIntent().getStringExtra("seller_register_id");

            binding.description.setText(getString(R.string.otp_text) + " "+ "+"+ countryCode + mobile +" " + getString(R.string.with_6_digit));
        }


      /*  mobile = "9755463923";
        countryCode = "+91";*/

      //  binding.description.setText(getString(R.string.otp_text) + " "+ countryCode + mobile +" " + getString(R.string.with_6_digit));


        binding.verifyBtn.setOnClickListener(view -> {
                if(binding.Otp.getOTP().equals("")){
                    Toast.makeText(OtpAct.this,getString(R.string.enter_otp),Toast.LENGTH_LONG).show();
                }
                else {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("mobile_number",mobile);
                    map.put("country_code","+"+countryCode);
                    map.put("otp",binding.Otp.getOTP());
                    Log.e("OtpScreen", "Verify Otp Request ==="+ map);
                    otpViewModel.verifyOtpData(OtpAct.this,map);
                }

                //  finish();
        });


        binding.resendBtn.setOnClickListener(v->{
            resendButtonCount++;
            binding.Otp.setOTP("");
            startSmartUserConsent();
            senOtpCall(countryCode,mobile);
        });

        binding.tvWhatsapp.setOnClickListener(v->{
            binding.Otp.setOTP("");
            sendWhatsappOtpCall(countryCode,mobile);

        });



        senOtpCall(countryCode,mobile);

       // signupSeller();
    }

     private void senOtpCall(String countryCode,String mobileNumber){
         HashMap<String,String> map = new HashMap<>();
         map.put("mobile_number",mobileNumber);
         map.put("country_code","+"+countryCode);
         Log.e("OtpScreen", " Otp Request ==="+ map);
         otpViewModel.senOtpData(OtpAct.this,map);
     }


    private void sendWhatsappOtpCall(String countryCode,String mobileNumber){
        HashMap<String,String> map = new HashMap<>();
        map.put("mobile_number",mobileNumber);
        map.put("country_code","+"+countryCode);
        Log.e("OtpScreen", "Whatsapp Otp Request ==="+ map);
        otpViewModel.sendWhatsappOtpData(OtpAct.this,map);
    }



    private void startTimer() {
        new CountDownTimer(TIMER_DURATION, TIMER_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                // Update the timer TextView with the remaining time
                long seconds = millisUntilFinished / 1000;
                binding.tvTimer.setText(String.format("%02d:%02d", seconds / 60, seconds % 60));
            }

            @Override
            public void onFinish() {
                // Timer finished, show the resend button
                binding.resendBtn.setVisibility(View.VISIBLE);
                binding.tvTimer.setVisibility(View.GONE);

            }
        }.start();
    }



    public void observeResponse(){
        otpViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.SEND_OTP){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {


                                if(resendButtonCount>=2){
                                    binding.rlResendOtp.setVisibility(View.GONE);
                                }
                                else {
                                    binding.rlResendOtp.setVisibility(View.VISIBLE);
                                    binding.tvTimer.setVisibility(View.VISIBLE);
                                    binding.resendBtn.setVisibility(View.GONE);
                                    startTimer();
                                }
                                Toast.makeText(OtpAct.this,getString(R.string.otp_successfully_send),Toast.LENGTH_LONG).show();


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(OtpAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(OtpAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(dynamicResponseModel.getApiName()== ApiConstant.SEND_WHATSAPP_OTP){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("Whatsapp Otp response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                Toast.makeText(OtpAct.this,getString(R.string.otp_successfully_send),Toast.LENGTH_LONG).show();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(OtpAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(OtpAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.SIGNUP) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("result");
                                //  Toast.makeText(SignupAct.this, getString(R.string.p), Toast.LENGTH_SHORT).show()
                                startActivity(new Intent(this, SelectSkillsAct.class).putExtra("user_id",object.getString("id"))
                                        .putExtra("type","reg"));
                                finish();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(OtpAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(OtpAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }








        } );


        otpViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.VERIFY_OTP){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                               // JSONObject object = jsonObject.getJSONObject("result");
                                signupSeller();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                checkOtp++;
                                if (checkOtp>=2){
                                    binding.rlResendOtp.setVisibility(View.GONE);
                                }
                                Toast.makeText(OtpAct.this, getString(R.string.invalid_otp), Toast.LENGTH_SHORT).show();
                               // senOtpCall(countryCode,mobile);
                            }
                        }
                        else {
                            Toast.makeText(OtpAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        } );


    }

    private void observeLoader() {
        otpViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(OtpAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    private void startSmartUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        client.startSmsUserConsent(null);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_USER_CONSENT){

            if ((resultCode == RESULT_OK) && (data != null)){
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }

    }

    private void getOtpFromMessage(String message) {

        Pattern otpPattern = Pattern.compile("(|^)\\d{6}");
        Matcher matcher = otpPattern.matcher(message);
        if (matcher.find()){
            /// etOTP.setText();
            binding.Otp.setOTP(matcher.group(0));

        }

    }

    private void registerBroadcastReceiver(){

        smsBroadcastReceiver = new SmsBroadcastReceiver();

        smsBroadcastReceiver.smsBroadcastReceiverListener = new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
            @Override
            public void onSuccess(Intent intent) {

                startActivityForResult(intent,REQ_USER_CONSENT);

            }

            @Override
            public void onFailure() {

            }
        };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(smsBroadcastReceiver, intentFilter, Context.RECEIVER_NOT_EXPORTED);
            Log.e("11111:","====");

        }
        else {
            registerReceiver(smsBroadcastReceiver,intentFilter);
            Log.e("22222:","====");

        }


    }

    public void signupSeller(){
        HashMap<String, String> map = new HashMap<>();
        map.put("user_name", userName);
        map.put("name", name);
        map.put("email", email);
        map.put("mobile", mobile);
        map.put("country_code", countryCode);
        map.put("password", password);
        map.put("address", address);
        map.put("lat", lat);
        map.put("lon", lon);
        map.put("country", countryId/*binding.tvSelectCountry.getText().toString()*/);
        map.put("country_name", countryName/*binding.tvSelectCountry.getText().toString()*/);
        map.put("state", stateId/*binding.tvSelectCountry.getText().toString()*/);
        map.put("city", cityId/*binding.etCity.getText().toString()*/);
        map.put("language", lang);
        map.put("type", Constant.SUBADMIN);
        map.put("currency", currency);
        map.put("zip_code", zipCode);
        //  map.put("register_id",firebaseToken);
        map.put("seller_register_id", firebaseToken);
        Log.e(TAG, " Signup Request ===" + map);
        // DataManager.getInstance().showProgressMessage(SignupAct.this, getString(R.string.please_wait));
        otpViewModel.signup(OtpAct.this, map);
    }



    @Override
    protected void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);
    }

}
