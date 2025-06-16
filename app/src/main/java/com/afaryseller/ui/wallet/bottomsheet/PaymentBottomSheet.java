package com.afaryseller.ui.wallet.bottomsheet;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.FragmentPaymentBootmsheetBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.home.HomeAct;
import com.afaryseller.ui.orderdetails.OrderDetailsAct;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.ui.wallet.GetProfileModal;
import com.afaryseller.ui.wallet.PaymentByAnotherAct;
import com.afaryseller.ui.wallet.PaymentWebViewAct;
import com.afaryseller.utility.CountryCodes;
import com.afaryseller.utility.DataManager;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.hbb20.CountryCodePicker;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "PaymentBottomSheet";
    BottomSheetDialog dialog;
    FragmentPaymentBootmsheetBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public AskListener listener;
    String money="",anotherPersonId="";
    private AfarySeller apiInterface;
    CountryCodePicker ccp;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    GetProfileModal data;


    BroadcastReceiver PaymentStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("PaymentStatusReceiver ====","=====");
            try {
                JSONObject object = new JSONObject(intent.getStringExtra("object"));
                if(intent.getStringExtra("object")!= null) {
                    JSONObject result = object.getJSONObject("result");
                    Log.e("PaymentStatusReceiver ====",intent.getStringExtra("object")+"");

                    if(object.getString("status").equals("1")){
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        //  startActivity(new Intent(getActivity(), HomeActivity.class)
                        //       .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        //  finish();
                        listener.ask("Your Money Is Add SuccessFully ","add money");
                        dismiss();

                    }


                    else if (object.getString("status").equals("3")) {
                        Log.e("Payment under processing ====",intent.getStringExtra("object")+"");


                    } else {
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(getActivity.this,HomeActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        // finish();
                        listener.ask("Failed ","");
                        dismiss();
                    }


                }


            }
            catch (Exception e){
                e.printStackTrace();
            }

        }
    };





    public PaymentBottomSheet(String money) {
        this.money = money;
    }


    public PaymentBottomSheet callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_payment_bootmsheet, null, false);
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

        binding.llMoov.setOnClickListener(view -> {
            dialogMoov("MC","060110217");
        });

        binding.llAirtel.setOnClickListener(view -> {
            dialogAirtel("AM","074272732");
        });

        binding.btnSendAnother.setOnClickListener(v->{
            dialogSendAnotherPerson();
        });

        binding.llTransfer.setOnClickListener(v -> {
            walletRechargeByCard();
        });


        binding.RRtoolbar.setOnClickListener(v -> {
           dialog.dismiss();
        });





        GetProfile();
    }


    private void GetProfile() {
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
       // map.put("country_id",PreferenceConnector.readString(getActivity(), PreferenceConnector.countryId, ""));

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

                        if(data.getResult().getCountry().equals("79")){
                            binding.rlMblMoney.setVisibility(View.VISIBLE);
                        }
                        else {
                            binding.rlMblMoney.setVisibility(View.GONE);

                        }

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



    private void walletRechargeByCard() {
        // binding.loader.setVisibility(View.VISIBLE);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("amount", money);
        map.put("operateur", "VM");
        map.put("num_marchand", "");
        map.put("user_number", "");
        map.put("datetime", DataManager.getCurrent());
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        Log.e("MapMap", "WALLET RECHARGE REQUEST" + map);


        Call<ResponseBody> SignupCall = apiInterface.addMoneyToWalletFromCard(headerMap,map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                //   binding.loader.setVisibility(View.GONE);

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "WALLET Card RECHARGE RESPONSE" + object);
                    if (object.getString("status").equals("1")) {
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.transId,object.getJSONObject("ressult").getString("reference"));
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.serviceType,"AddMoney");
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.ShareUserId,"");
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.PaymentType,"AddMoney");
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), PaymentWebViewAct.class)
                                .putExtra("url",object.getJSONObject("ressult").getString("webviewurl"))
                                .putExtra("ref",object.getJSONObject("ressult").getString("reference")));


                    } else if (object.getString("status").equals("0")) {
                        binding.rlPayment.setVisibility(View.VISIBLE);
                        binding.rlPaymentStatus.setVisibility(View.GONE);
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




    public void dialogAirtel(String operator,String operatorNumber){
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_airtel);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);


        btnPayNow.setText("FCFA"+money + " " +getString(R.string.pay_now));

        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(getActivity(), "Please enter number", Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                PaymentAPI(operator,edNumber.getText().toString(), operatorNumber);
            }

        });
        mDialog.show();

    }

    public void dialogMoov(String operator,String operatorNumber){
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_moov);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);


        btnPayNow.setText("FCFA"+money + " " +getString(R.string.pay_now));
        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(getActivity(), getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                PaymentAPI(operator,edNumber.getText().toString(), operatorNumber);
            }

        });

        mDialog.show();
    }


    private void PaymentAPI(String operator,String number,String  operatorNumber) {
        // binding.loader.setVisibility(View.VISIBLE);

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("amount", money);

        map.put("operateur", operator);
        map.put("num_marchand", operatorNumber);
        map.put("user_number", number);
        map.put("datetime", DataManager.getCurrent());
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());


        Log.e("MapMap", "WALLET RECHARGE REQUEST" + map);

        Call<ResponseBody> SignupCall = apiInterface.add_money(headerMap,map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                //   binding.loader.setVisibility(View.GONE);

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "WALLET RECHARGE RESPONSE" + object);
                    if (object.getString("status").equals("1")) {
                        binding.rlPayment.setVisibility(View.GONE);
                        binding.rlPaymentStatus.setVisibility(View.VISIBLE);
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.transId,object.getJSONObject("ressult").getString("reference"));
                        PreferenceConnector.writeString(getActivity(),PreferenceConnector.serviceType,"AddMoney");
                        getActivity().startService(new Intent(getActivity(), MyService.class));


                    } else if (object.getString("status").equals("0")) {
                        binding.rlPayment.setVisibility(View.VISIBLE);
                        binding.rlPaymentStatus.setVisibility(View.GONE);
                        dismiss();
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                    else if (object.optString("status").equals("5")) {
                     /*   PreferenceConnector.writeString(getActivity(), PreferenceConnector.LoginStatus, "false");
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
                // binding.loader.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().registerReceiver(PaymentStatusReceiver, new IntentFilter("check_payment_status"),Context.RECEIVER_EXPORTED);

        }
        else {
            getActivity().registerReceiver(PaymentStatusReceiver, new IntentFilter("check_payment_status"));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().stopService(new Intent(getActivity(), MyService.class));
        getActivity().unregisterReceiver(PaymentStatusReceiver);

    }


    public void dialogSendAnotherPerson(){
        Dialog mDialog = new Dialog(getActivity());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_send_another_person);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnCancel = mDialog.findViewById(R.id.btnCancel);
        AppCompatButton btnSend = mDialog.findViewById(R.id.btnSend);
        ccp = mDialog.findViewById(R.id.ccp);

        setCountryCodeFromLocation(ccp);



/*
        edNumber.addTextChangedListener(new TextWatcher() {
                                            @Override
                                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                            }

                                            @Override
                                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                // Check if the input is numeric
                                                if (s.length()==8 */
        /*|| s.length()==10*//*
) {
                                                    checkUserExit(ccp.getSelectedCountryCode()+"-"+edNumber.getText().toString());
                                                }

                                            }

                                            @Override
                                            public void afterTextChanged(Editable s) {
                                            }
                                        });
*/


        btnCancel.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnSend.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(getActivity(), getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                //  if(!anotherPersonId.equalsIgnoreCase(""))
                checkUserExit(ccp.getSelectedCountryCode()+"-"+edNumber.getText().toString());

            }

        });
        mDialog.show();

    }

    private void checkUserExit(String number) {
        //binding.loader.setVisibility(View.VISIBLE);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");


        Map<String, String> map = new HashMap<>();
        map.put("user_id",DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("identity", number);

        Log.e("MapMap", "Check user Exit Request" + map);

        Call<ResponseBody> loginCall = apiInterface.checkUserExitApi(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                // binding.loader.setVisibility(View.GONE);
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Check user Exit RESPONSE" + object);
                    if (object.optString("status").equals("1")) {
                        anotherPersonId =  object.getJSONObject("data").getString("id");
                        sendLinkAnotherPerson(anotherPersonId);
                    } else if (object.optString("status").equals("0")) {
                        //binding.loader.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();                    }


                    else if (object.optString("status").equals("5")) {
                        PreferenceConnector.writeString(getActivity(), PreferenceConnector.LoginStatus, "false");
                        startActivity(new Intent(getActivity(), SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();
                    }


                } catch (Exception e) {
                    Log.e("error>>>>", "" + e);
                   // binding.loader.setVisibility(View.GONE);
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


    public String createDeepLink(String paymentId) {
        return "https://www.afaycode.com/payment?paymentId="; /*+ paymentId;*/
    }



/*
    private void sharePaymentLink(String paymentId) {
        String link = createDeepLink(paymentId);
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share payment link"));
    }
*/


    private void sendLinkAnotherPerson(String anotherPersonId) {
        //binding.loader.setVisibility(View.VISIBLE);
        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");


        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("other_user_id", anotherPersonId);
        map.put("amount",money);
        Log.e("MapMap", "Send Invoice anotherPerson Request" + map);

        Call<ResponseBody> loginCall = apiInterface.sendWalletRechargeInvoiceAnotherApi(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                // binding.loader.setVisibility(View.GONE);
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Send Invoice anotherPerson RESPONSE" + object);
                    if (object.optString("status").equals("1")) {
                        Toast.makeText(getActivity(), getString(R.string.invoice_send_sucessfully), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getActivity(), HomeAct.class)
                                .putExtra("status","")
                                .putExtra("msg","").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();

                    } else if (object.optString("status").equals("0")) {
                        //binding.loader.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();                    }


                    else if (object.optString("status").equals("5")) {

                    }


                } catch (Exception e) {
                    Log.e("error>>>>", "" + e);
                    //binding.loader.setVisibility(View.GONE);
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




    private void setCountryCodeFromLocation(CountryCodePicker ccp) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            try {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
                fusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                String countryCode = addresses.get(0).getCountryCode();
                                if (countryCode != null && !countryCode.isEmpty()) {
                                    Log.e("country code===", CountryCodes.getPhoneCode(countryCode) + "");
                                    ccp.setCountryForPhoneCode(/*getCountryPhoneCode(countryCode)*/
                                            CountryCodes.getPhoneCode(countryCode));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Error determining location", Toast.LENGTH_SHORT).show();
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
                setCountryCodeFromLocation(ccp);
            }
        }
    }





}
