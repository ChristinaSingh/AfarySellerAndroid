package com.afaryseller.ui.wallet.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;

import com.afaryseller.R;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.ui.wallet.GetProfileModal;
import com.afaryseller.ui.wallet.PaymentByAnotherAct;
import com.afaryseller.ui.wallet.PaymentWebViewAct;
import com.afaryseller.utility.DataManager;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransferMOneyFragment extends BottomSheetDialogFragment {
    public String TAG ="TransferMoneyFragment";
    Context context;
    private EditText mobile_no_et;
    private AfarySeller apiInterface;
    private RelativeLayout payment_done;
    private EditText et_money;
    private CountryCodePicker ccp;
    private String code;
    AskListener listener;
    GetProfileModal data;


    public TransferMOneyFragment(Context context) {
        this.context = context;
    }


    public TransferMOneyFragment callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragmen_transfer_money, (ViewGroup) null);
        apiInterface = ApiClient.getClient(context).create(AfarySeller.class);

        GetProfile();

        mobile_no_et = contentView.findViewById(R.id.mobile_no_et);
        payment_done = contentView.findViewById(R.id.payment_done);
        et_money = contentView.findViewById(R.id.et_money);
        ccp = contentView.findViewById(R.id.ccp);

        payment_done.setOnClickListener(v -> {
            code = ccp.getSelectedCountryCode();
            Log.e("code>>>", code);
            if(mobile_no_et.getText().toString().equals(""))
                Toast.makeText(getActivity(),getString(R.string.enter_email_number),Toast.LENGTH_LONG).show();
            else if (et_money.getText().toString().equals("")) {
                Toast.makeText(getActivity(),getString(R.string.enter_amount),Toast.LENGTH_LONG).show();

            }
            else {
                TransferMoneyAPI(code, mobile_no_et.getText().toString()
                        , et_money.getText().toString());
            }
        });

        dialog.setContentView(contentView);

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }


    private void GetProfile() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
      //  map.put("country_id",PreferenceConnector.readString(getActivity(), PreferenceConnector.countryId, ""));

        Call<GetProfileModal> loginCall = apiInterface.get_profile(map);

        loginCall.enqueue(new Callback<GetProfileModal>() {
            @Override
            public void onResponse(Call<GetProfileModal> call,
                                   Response<GetProfileModal> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    data = response.body();
                    String dataResponse = new Gson().toJson(response.body());

                    Log.e("MapMap", "GET RESPONSE" + dataResponse);

                    if (data.status.equals("1")) {

                    } else if (data.status.equals("0")) {
                    }

                    else if (data.status.equals("5")) {
                        // PreferenceConnector.writeString(getActivity(), PreferenceConnector.LoginStatus, "false");
                        //  startActivity(new Intent(CheckOutPayment.this, Splash.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //  finish();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModal> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }




    private void TransferMoneyAPI(String countryCode,String mobile_no_et, String add_money) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("amount", add_money);
        map.put("to_country_code", countryCode);
        map.put("to_mobile", mobile_no_et);
        map.put("datetime", DataManager.getCurrent());
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        Log.e("MapMap", "TransferMoney REQUEST" + map);

        Call<ResponseBody> SignupCall = apiInterface.transfer_money(headerMap,map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e("MapMap", "TransferMoney RESPONSE" + object);
                    if (object.optString("status").equals("1")) {
                        // dialogNumberExit(object.getString("amount"),object.getString("total_amount"),object.getString("wallet_fees"),mobile_no_et , countryCode);
                        dialogChoosePaymentType(object.getString("amount"),object.getString("total_amount"),object.getString("wallet_fees"),mobile_no_et , countryCode);
                    } else if (object.optString("status").equals("0")) {
                        AlertNumberNotExit();

                    }

                    else if (object.optString("status").equals("5")) {
                      /*  PreferenceConnector.writeString(getActivity(), PreferenceConnector.LoginStatus, "false");
                        startActivity(new Intent(getActivity(), SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();*/
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

    private void dialogChoosePaymentType(String amount, String totalAmount, String walletFees, String mobileNoEt, String countryCode) {
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_choose_payment_type);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        LinearLayout llMoov = mDialog.findViewById(R.id.llMoov);
        LinearLayout llAirtel = mDialog.findViewById(R.id.llAirtel);
        LinearLayout llCard = mDialog.findViewById(R.id.llCard);
        LinearLayout llWallet = mDialog.findViewById(R.id.llWallet);
        RelativeLayout rlMblMoney = mDialog.findViewById(R.id.rlMblMoney1);


        if(data.getResult().getCountry().equals("79")){
            rlMblMoney.setVisibility(View.VISIBLE);
        }
        else {
            rlMblMoney.setVisibility(View.GONE);

        }



        llMoov.setOnClickListener(v -> {
            mDialog.dismiss();
            dialogNumberExit(amount,totalAmount,walletFees,mobileNoEt,countryCode,"MC");


        });

        llAirtel.setOnClickListener(v -> {
            mDialog.dismiss();
            dialogNumberExit(amount,totalAmount,walletFees,mobileNoEt,countryCode,"AM");

        });

        llCard.setOnClickListener(v -> {
            mDialog.dismiss();
            dialogNumberExit(amount,totalAmount,walletFees,mobileNoEt,countryCode,"Card");
        });

        llWallet.setOnClickListener(v -> {
            mDialog.dismiss();
            dialogNumberExit(amount,totalAmount,walletFees,mobileNoEt,countryCode,"wallet");
        });



        mDialog.show();
    }


    public void dialogNumberExit(String Amount,String totalAmount,String fee,String number,String countryCode,String payType){
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_transfer_money);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        TextView tvAmount = mDialog.findViewById(R.id.tvAmount);

        TextView tvFee = mDialog.findViewById(R.id.tvFee);
        TextView tvTotalAmount = mDialog.findViewById(R.id.tvTotalAmount);

        LinearLayout btnOk = mDialog.findViewById(R.id.btnOk);
        TextView tvNote = mDialog.findViewById(R.id.tvNote);

        if(payType.equals("wallet")){
            tvNote.setText(getString(R.string.your_wallet_will_be_debited_for));
        }
        else {
            tvNote.setText(getString(R.string.your_amount_will_be_debited_for));

        }

        tvAmount.setText(Html.fromHtml("<font color='#000'>" + "<b>" + getString(R.string.amount)+" : " + "</b>" + "FCFA" +Amount + "</font>"  ));
        tvFee.setText(Html.fromHtml("<font color='#000'>" + "<b>" + getString(R.string.fess)+" : " + "</b>" + "FCFA" +fee + "</font>"  ));
        tvTotalAmount.setText(Html.fromHtml("<font color='#000'>" + "<b>" + getString(R.string.total_amount)+" : " + "</b>" + "FCFA" + totalAmount + "</font>"  ));

        btnOk.setOnClickListener(v -> {
            mDialog.dismiss();

            if(payType.equals("MC")){
                dialogMoov("MC",totalAmount,number,fee);
            }
            else if(payType.equals("AM")){
                dialogAirtel("AM",totalAmount,number,fee);

            }

            else if(payType.equals("Card")){
                moneyTransferByCard(countryCode,totalAmount,number,fee,mDialog);
            }

            else {
                TransferMoneyFirstAPI(countryCode, number, Amount, totalAmount, fee);
            }

        });
        mDialog.show();

    }



    private void moneyTransferByCard(String countryCode,String money,String creditMoneyNumber,String adminFee,Dialog dialog) {
        // binding.loader.setVisibility(View.VISIBLE);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("userId", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("amount", money);
        map.put("creditCountryCode", countryCode);
        map.put("operator", "VM");
        map.put("num_marchand", "");
        map.put("fee", adminFee);
        map.put("numberCredit",creditMoneyNumber);
        map.put("numberDebit","");
        map.put("paymentType","Card");
        map.put("transaction_by","Other");
        map.put("transaction_type","TransferMoney");

        Log.e("MapMap", "WALLET RECHARGE REQUEST" + map);


        Call<ResponseBody> SignupCall = apiInterface.transferMoneyToWalletFromCard(headerMap,map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                //   binding.loader.setVisibility(View.GONE);

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "TransferMoney By Card RESPONSE" + object);
                    if (object.getString("status").equals("1")) {
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.transId,object.getJSONObject("ressult").getString("reference"));
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.serviceType,"Transfer");
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.ShareUserId,"");
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.PaymentType,"Transfer");


                      //  dialog.dismiss();
                        startActivity(new Intent(getActivity(), PaymentWebViewAct.class)
                                .putExtra("url",object.getJSONObject("ressult").getString("webviewurl"))
                                .putExtra("ref",object.getJSONObject("ressult").getString("reference")));


                    } else if (object.getString("status").equals("0")) {
                        dismiss();
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                // binding.loader.setVisibility(View.GONE);

            }
        });
    }





    private void TransferMoneyFirstAPI(String countryCode,String number, String amount,String totalAmount, String fee) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("amount", amount);
        map.put("to_country_code", countryCode);
        map.put("to_mobile", number);
        map.put("datetime", DataManager.getCurrent());
        map.put("total_amount", totalAmount);
        map.put("admin_fees", fee);
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        Log.e("MapMap", "TransferMoney First REQUEST" + map);

        Call<ResponseBody> SignupCall = apiInterface.transfer_moneyFirstApi(headerMap,map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e("MapMap", "TransferMoney First RESPONSE" + object);
                    if (object.optString("status").equals("1")) {
                        listener.ask("","transfer");
                        dismiss();
                        Toast.makeText(getContext(), "Your Money Transfer  SuccessFully ", Toast.LENGTH_SHORT).show();

                    } else if (object.optString("status").equals("0")) {

                        Toast.makeText(getActivity(), object.optString("message"), Toast.LENGTH_SHORT).show();
                        listener.ask("","transfer");
                        dismiss();

                    }

                    else if (object.getString("status").equals("5")) {
                        PreferenceConnector.writeString(getActivity(), PreferenceConnector.LoginStatus, "false");
                        startActivity(new Intent(getActivity(), SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();
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




    private void AlertNumberNotExit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Your beneficiary does not have a wallet to receive the funds. send him this link to download our app. show link")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }


    public void dialogAirtel(String operator,String totalAmount,String toTransferNumber,String walletFee){
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_airtel_transfer);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);
        TextView tvNote = mDialog.findViewById(R.id.tvNote);
        CountryCodePicker countryCodePicker = mDialog.findViewById(R.id.ccp);
        String codeCountry = countryCodePicker.getSelectedCountryCode();


        tvNote.setText(getString(R.string.please_indicate_the_airtel_money_number_by_which_you_wish_to_debit_the_payment));

        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(getActivity(), getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                TransferOnlineAPI(toTransferNumber,operator,edNumber.getText().toString(),totalAmount,"Online",walletFee, codeCountry);
            }

        });
        mDialog.show();

    }

    public void dialogMoov(String operator,String totalAmount,String toTransferNumber,String walletFee){
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_moov_transfer);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);


        TextView tvNote = mDialog.findViewById(R.id.tvNote);
        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);
        CountryCodePicker countryCodePicker = mDialog.findViewById(R.id.ccp);
        String codeCountry = countryCodePicker.getSelectedCountryCode();

        tvNote.setText(getString(R.string.please_indicate_the_moov_money_number_by_which_you_wish_to_make_the_payment));


        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(getActivity(), getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                TransferOnlineAPI(toTransferNumber,operator,edNumber.getText().toString(),totalAmount,"Online",walletFee,codeCountry);
            }

        });

        mDialog.show();
    }


    private void TransferOnlineAPI(String transferNumber,String operateur,String DebitNumber,String totalAmount,String paymentType,String walletFee,String countryCode) {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");


        Map<String, String> map = new HashMap<>();
        map.put("userId", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("amount", totalAmount);
        map.put("creditCountryCode", countryCode);
        map.put("operator", operateur);
        if(operateur.equals("MC"))  map.put("num_marchand", "060110217");
        else if(operateur.equals("AM")) map.put("num_marchand", "074272732");
        map.put("fee", walletFee);
        map.put("numberCredit",transferNumber);
        map.put("numberDebit",DebitNumber);
        map.put("paymentType",paymentType);
        //  map.put("datetime",DataManager.getCurrent());
        Log.e("MapMap", "payment transfer params" + map);

        Call<ResponseBody> loginCall = apiInterface.transferNumberMoney(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                // binding.loader.setVisibility(View.GONE);
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Payment transfer RESPONSE" + object);
                    if (object.optString("status").equals("1")) {
                        //  PaymentModal data = new Gson().fromJson(responseData, PaymentModal.class);
                        // binding.loader.setVisibility(View.GONE);

                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.transId,object.getJSONObject("ressult").getString("reference"));
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.serviceType,PreferenceConnector.Booking);

                        startActivity(new Intent(getActivity(),CheckPaymentStatusAct.class)
                                .putExtra("paymentBy","Transfer"));


                    } else if (object.optString("status").equals("0")) {
                        //binding.loader.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();                    }


                    else if (object.optString("status").equals("5")) {
                      /*  PreferenceConnector.writeString(getActivity(), PreferenceConnector.LoginStatus, "false");
                        startActivity(new Intent(getActivity(), SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();*/
                    }


                } catch (Exception e) {
                    Log.e("error>>>>", "" + e);
                    e.printStackTrace();
                }






            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Toast.makeText(getActivity(), "Network Error !!!!", Toast.LENGTH_SHORT).show();
                //  binding.loader.setVisibility(View.GONE);
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}

