package com.afaryseller.ui.login;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.FragmentSendAdminBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.utility.DataManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.messaging.FirebaseMessaging;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SendAdminRequestBottomSheet extends BottomSheetDialogFragment {
    Context context;
    BottomSheetDialog dialog;
    String firebaseToken="";
    private AfarySeller apiInterface;
    FragmentSendAdminBinding binding;
    AskListener listener;
    private BottomSheetBehavior<View> mBehavior;
    public SendAdminRequestBottomSheet(Context context) {
        this.context = context;
    }


    public SendAdminRequestBottomSheet callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_send_admin, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return  dialog;
    }

    private void initBinding() {
        apiInterface = ApiClient.getClient(getActivity()).create(AfarySeller.class);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);
            firebaseToken = token;

        });

        binding.RRLogin.setOnClickListener(v -> {


            if (binding.edNumber.getText().toString().trim().isEmpty()) {
                binding.edNumber.setError(getString(R.string.can_not_empty));

                Toast.makeText(getActivity(), getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();

            } else {
                sendAdminRequestAPI(binding.ccp.getSelectedCountryCode()+"-"+binding.edNumber.getText().toString());
                //  startActivity(new Intent(ForgotPassword.this, VerificationScreen.class)
                //      .putExtra("user_email", binding.edtmobile.getText().toString()));


            }
        });
    }

    private void sendAdminRequestAPI(String email) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));

        Map<String,String> headerMap = new HashMap<>();
    //    headerMap.put("Authorization","Bearer " + PreferenceConnector.readString(getActivity(), PreferenceConnector.access_token,""));
   //     headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("user_type", "Seller");
        map.put("device_id", firebaseToken);
        Log.e("MapMap", "Send Admin Request" + map);
        Call<ResponseBody> SignupCall = apiInterface.sendAdminRequest(map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e("MapMap", "Send Admin Response" + responseData);
                    if (object.optString("status").equals("1")) {
                        Toast.makeText(getContext(), getString(R.string.send_admin_request_successfull), Toast.LENGTH_LONG).show();
                        listener.ask("send","");
                        dialog.dismiss();
                    } else if (object.optString("status").equals("0")) {
                       // listener.ask("send");
                      //  dialog.dismiss();
                        Toast.makeText(getContext(), object.optString("message"), Toast.LENGTH_LONG).show();

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




}
