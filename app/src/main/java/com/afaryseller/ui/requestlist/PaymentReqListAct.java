package com.afaryseller.ui.requestlist;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityPaymentReqBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.utility.DataManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentReqListAct extends AppCompatActivity implements onPositionClickListener {
    private ActivityPaymentReqBinding binding;
    private AfarySeller apiInterface;
    private ArrayList<PaymentReqModel.Datum> get_result = new ArrayList<>();
    PaymentReqAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_req);
        apiInterface = ApiClient.getClient(PaymentReqListAct.this).create(AfarySeller.class);
        initViews();
    }

    private void initViews() {

        adapter = new PaymentReqAdapter((Context) this,get_result,PaymentReqListAct.this);
        binding.rvReq.setAdapter(adapter);


        binding.RRback.setOnClickListener(v -> finish());

        getPaymentReq();
    }


    private void getPaymentReq() {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentReqListAct.this).getResult().getId());

        Log.e("MapMap", "" + map);

        Call<ResponseBody> loginCall = apiInterface.paymentReqApi(map);

        loginCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e("MapMap", "Exersice_List RESPONSE" + object);
                    if (object.getString("status").equals("1")) {
                        PaymentReqModel data = new Gson().fromJson(responseData, PaymentReqModel.class);
                        get_result.clear();
                        get_result.addAll(data.getData());
                        adapter.notifyDataSetChanged();
                        binding.tvNotFound.setVisibility(View.GONE);
                    } else if (object.getString("status").equals("0")) {
                        Toast.makeText(PaymentReqListAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.tvNotFound.setVisibility(View.VISIBLE);
                        get_result.clear();
                        adapter.notifyDataSetChanged();

                    }




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                binding.tvNotFound.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onPosition(int position, String name, String countryId) {
        PaymentReqAcceptCancelDialog(PaymentReqListAct.this,get_result.get(position).getMessage_noti()
                ,get_result.get(position).getId(),get_result.get(position).getSenderId(),get_result.get(position).getReceiverId(),
                get_result.get(position).getAmount());
    }


    public void PaymentReqAcceptCancelDialog(Context context,String msg,String reqId,String userId,String toUserID,String amount) {
        // Dialog dialog = new Dialog(context, R.style.FullScreenDialog);

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_payment_req_accept_cancel);
        dialog.setCanceledOnTouchOutside(true);

        TextView tv = dialog.findViewById(R.id.tvMsg);
        RelativeLayout btnAccept = dialog.findViewById(R.id.btnAccept);
        RelativeLayout btnCancel = dialog.findViewById(R.id.btnCancel);

        tv.setText(msg);
        btnAccept.setOnClickListener(view -> {
            paymentReqAcceptCancel(userId,"Accepted",reqId,toUserID,amount,msg);

            dialog.dismiss();

        });

        btnCancel.setOnClickListener(view -> {
            paymentReqAcceptCancel(userId,"Cancelled",reqId,toUserID,amount,msg);
            dialog.dismiss();

        });





        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();
    }

    private void paymentReqAcceptCancel(String userId, String status, String requestId, String toUserId, String amount,String msg) {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("status",status);
        map.put("request_id",requestId);

        Log.e("MapMap", "" + map);

        Call<ResponseBody> loginCall = apiInterface.paymentReqAcceptCancelApi(map);

        loginCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e("MapMap", "Payment Request RESPONSE" + object);
                    if (object.getString("status").equals("1")) {
                        if (status.equals("Accepted")) paymentDialog(userId, status, requestId, toUserId, amount,msg);

                    } else if (object.getString("status").equals("0")) {
                        Toast.makeText(PaymentReqListAct.this, object.getString("message")/*getString(R.string.wrong_username_password)*/, Toast.LENGTH_SHORT).show();

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

    private void paymentDialog(String userId, String status, String requestId, String toUserId, String amount,String msg) {

        Dialog dialog = new Dialog(PaymentReqListAct.this);
        dialog.setContentView(R.layout.dialog_payment_req);
        dialog.setCanceledOnTouchOutside(true);

        TextView tv = dialog.findViewById(R.id.tvMsg);
        TextView tvTotalAmt = dialog.findViewById(R.id.tvTotalAmt);

        tvTotalAmt.setText("FCFA"+amount);


        RelativeLayout btnPayNow = dialog.findViewById(R.id.btnPayNow);

        tv.setText(msg);
        btnPayNow.setOnClickListener(view -> {
            paymentReq(userId,requestId,toUserId,amount);
            dialog.dismiss();

        });








        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.getWindow().setAttributes(layoutParams);
        dialog.show();

    }

    private void paymentReq(String userId, String requestId, String toUserId, String amount) {
        DataManager.getInstance().showProgressMessage(this, getString(R.string.please_wait));

        Map<String, String> map = new HashMap<>();
        map.put("user_id", toUserId);
        map.put("payment_request_user_id", userId);
        map.put("amount", amount);
        map.put("datetime", DataManager.getCurrent());
        map.put("total_amount", amount);
        map.put("admin_fees", "0");
        map.put("payment_request_id",requestId);
        map.put("register_id", DataManager.getInstance().getUserData(PaymentReqListAct.this).getResult().getRegisterId());

        Log.e("MapMap", "TransferMoney First REQUEST" + map);


        Call<ResponseBody> loginCall = apiInterface.paymentReqPayApi(map);

        loginCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e("MapMap", "Payment Request Pay RESPONSE" + object);
                    if (object.getString("status").equals("1")) {
                        getPaymentReq();
                        Toast.makeText(PaymentReqListAct.this, getString(R.string.payment_sucessful), Toast.LENGTH_SHORT).show();
                    } else if (object.getString("status").equals("0")) {
                        Toast.makeText(PaymentReqListAct.this, object.getString("message")/*getString(R.string.wrong_username_password)*/, Toast.LENGTH_SHORT).show();

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
