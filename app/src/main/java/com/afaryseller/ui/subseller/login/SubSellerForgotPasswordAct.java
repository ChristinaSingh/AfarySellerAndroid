package com.afaryseller.ui.subseller.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityForgotPasswordBinding;
import com.afaryseller.databinding.ActivityForgotPasswordSubSellerBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.login.ForgotPassword;
import com.afaryseller.ui.login.SendAdminRequestBottomSheet;
import com.afaryseller.ui.sellerreport.SellerReportAct;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.utility.DataManager;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SubSellerForgotPasswordAct extends AppCompatActivity implements AskListener {

    ActivityForgotPasswordSubSellerBinding binding;
    private AfarySeller apiInterface;
    private String code;
    String firebaseToken="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password_sub_seller);
        apiInterface = ApiClient.getClient(this).create(AfarySeller.class);
        SetupUI();
    }


    private void SetupUI() {

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);
            firebaseToken = token;

        });

        binding.RRSend.setOnClickListener(v -> {


            if (binding.edEmail.getText().toString().trim().isEmpty()) {
                binding.edEmail.setError(getString(R.string.can_not_empty));

                Toast.makeText(SubSellerForgotPasswordAct.this, getString(R.string.please_enter_valid_email), Toast.LENGTH_SHORT).show();

            } else {
                ForgotPasswordAPI(binding.edEmail.getText().toString());
                //  startActivity(new Intent(ForgotPassword.this, VerificationScreen.class)
                //      .putExtra("user_email", binding.edtmobile.getText().toString()));


            }
        });

        binding.llEmail.setOnClickListener(v -> {
            binding.llEmail.setBackground(getDrawable(R.drawable.rounded_corner_stroke_10));
            binding.llSendAdmin.setBackground(getDrawable(R.drawable.rounded_corner_white_10));
        });


        binding.llSendAdmin.setOnClickListener(v -> {
                    binding.llEmail.setBackground(getDrawable(R.drawable.rounded_corner_white_10));
                    binding.llSendAdmin.setBackground(getDrawable(R.drawable.rounded_corner_stroke_10));
                    new SendAdminRequestBottomSheet(SubSellerForgotPasswordAct.this).callBack(this::ask).show(getSupportFragmentManager(), "");

                }
        );



        binding.edEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Validate email and update button visibility
                if (isValidEmail(s.toString())) {
                    binding.RRSend.setVisibility(View.VISIBLE);
                } else {
                    binding.RRSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        });


    }


    private boolean isValidEmail(CharSequence email) {
        // Check if the email is valid using Android's Patterns utility
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }



    private void ForgotPasswordAPI(String user_email) {

        DataManager.getInstance().showProgressMessage(SubSellerForgotPasswordAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("email", user_email);
        map.put("device_id", firebaseToken);
        Log.e("MapMap", "FORGOT PASSWORD REQUEST" + map);
        Call<ResponseBody> loginCall = apiInterface.forgotPasswordSubSellerApi(map);

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {

                    Log.e("response===",response.body().toString());
                    String stringResponse = response.body().string();
                    JSONObject jsonObject = new JSONObject(stringResponse);



                    if (jsonObject.getString("status").equals("1")) {

                        Toast.makeText(SubSellerForgotPasswordAct.this, getString(R.string.please_check_your_email_address), Toast.LENGTH_LONG).show();
                        finish();

                    } else if (jsonObject.getString("status").equals("0")) {
                        Toast.makeText(SubSellerForgotPasswordAct.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    @Override
    public void ask(String Value,String status) {
        finish();
    }
}
