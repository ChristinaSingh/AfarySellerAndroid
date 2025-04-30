package com.afaryseller.ui.wallet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityPaymentAnotherBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.home.HomeAct;
import com.afaryseller.ui.login.LoginAct;
import com.afaryseller.ui.wallet.bottomsheet.CheckPaymentStatusAct;
import com.afaryseller.ui.wallet.bottomsheet.PreferenceConnector;
import com.afaryseller.utility.DataManager;
import com.google.gson.Gson;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentByAnotherAct extends AppCompatActivity {
    public String TAG = "PaymentByAnotherAct";
    ActivityPaymentAnotherBinding binding;
    GetProfileModal data;

    private AfarySeller apiInterface;
    private String type="", transferNumber="",countryCode="", totalPriceToToPay = "",deliveryCharge="",platFormsFees="",taxN1="",taxN2="",sendToServer="",mainTotalPay="",insertDeliveryId="";
    private String strList = "",cart_id_string="",paymentInsertId="",userId="";
    //private ArrayList<CartListModel> arrayList;
    //CartListAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_payment_another);
        apiInterface = ApiClient.getClient(this).create(AfarySeller.class);
        if(DataManager.getInstance().getUserData(getApplicationContext()) != null&&
                DataManager.getInstance().getUserData(getApplicationContext()).getResult() != null &&
                DataManager.getInstance().getUserData(getApplicationContext()).getResult().getId()!=null){
            initViews();
        }
        else {
            alertDialog();
        }

        // initViews();
    }


    private void alertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PaymentByAnotherAct.this);
        builder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.please_login))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivity(new Intent(PaymentByAnotherAct.this, LoginAct.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                })
                /*.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action
                        dialog.dismiss();
                    }
                })*/;

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    private void initViews() {

     //   arrayList = new ArrayList<>();

     //   adapter = new CartListAdapter(PaymentByAnotherAct.this,arrayList);
     //   binding.rvCart.setAdapter(adapter);


        if(getIntent()!=null){
            paymentInsertId = getIntent().getStringExtra("paymentInsertId");
            userId = getIntent().getStringExtra("user_id");
            type = getIntent().getStringExtra("type");

             getWalletInvoiceData("2");



        }

        // Handle the incoming deep link
        // handleDeepLink(getIntent());


        binding.llTransfer11.setOnClickListener(v -> {
            callCardPayment(type);
        });



        binding.llMoov.setOnClickListener(v -> {
            dialogMoov("MC",strList);

        });

        binding.RRback.setOnClickListener(v -> {
            startActivity(new Intent(PaymentByAnotherAct.this, HomeAct.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });


        binding.llAirtel.setOnClickListener(v -> {
            dialogAirtel("AM",strList);
        });


        binding.llWallet11.setOnClickListener(v -> {
            if(Double.parseDouble(data.getResult().getWallet()) >= Double.parseDouble(totalPriceToToPay))
                PaymentAPI("","",data.getResult().getMobile(),"Wallet");
            else Toast.makeText(this, getString(R.string.low_wallet_balance), Toast.LENGTH_SHORT).show();
        });
    }

    private void handleDeepLink(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                Log.e("payment link=====",uri.toString());

                // Extract the data from the URI
                String paymentId = uri.getQueryParameter("paymentId");
                Log.e("paymentId=====",paymentId);
                // Load payment details using paymentId
                // Show payment UI or process payment
                GetProfile();
            }
        }
    }


    private void GetProfile() {
        DataManager.getInstance().showProgressMessage(PaymentByAnotherAct.this, getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getId());
        map.put("register_id",DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getRegisterId() );
        map.put("country_id", DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getCountry());

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


/*
    public void  getInvoiceData(String apiType){
        DataManager.getInstance().showProgressMessage(PaymentByAnotherAct.this, getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +PreferenceConnector.readString(PaymentByAnotherAct.this, PreferenceConnector.access_token,""));
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("invoice_id", paymentInsertId);

        Log.e(TAG, "Get Invoice Request :" + map);
        Call<ResponseBody> loginCall     = apiInterface.getInvoiceDataApi(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Get Invoice RESPONSE" + object);
                    GetProfile();
                    if (object.optString("status").equals("1")) {
                        // JSONObject jsonObject = object.getJSONObject("result");
                        sendToServer = object.getJSONObject("data").getJSONObject("delivery_data").toString();
                        mainTotalPay = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("sub_total");
                        taxN1 = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("taxes_first");
                        taxN2 = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("taxes_second");
                        if(!object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("platform_fees").equalsIgnoreCase(""))
                        {
                            platFormsFees = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("platform_fees");
                        }
                        else {
                            platFormsFees ="0.00";
                        }

                        deliveryCharge = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("total_delivery_fees");
                        totalPriceToToPay =  object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("total_payable_amount");
                        binding.totalPriceToToPay.setText("XAF" +  totalPriceToToPay);
                        binding.tvShareBy.setText(getString(R.string.invoice_share_by)+ " "+object.getJSONObject("data").getJSONObject("cart_data").getJSONObject("cart_user_info").getString("user_name"));
                        arrayList.clear();

                        JSONArray array = object.getJSONObject("data").getJSONObject("cart_data").getJSONArray("cdi_json_cart_data");
                        for (int i =0; i<array.length();i++){
                            JSONObject objectInner = array.getJSONObject(i);
                            JSONObject productInfoObj = array.getJSONObject(i).getJSONObject("product_info");

                            CartListModel cartListModel = new CartListModel(productInfoObj.getString("product_name"),
                                    productInfoObj.getString("image_1"),productInfoObj.getString("product_price"),
                                    objectInner.getString("quantity")
                                    , objectInner.getString("currency"));
                            arrayList.add(cartListModel);
                        }

                        adapter.notifyDataSetChanged();


                    } else if (object.optString("status").equals("0")) {
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                    }


                    else if (object.optString("status").equals("2")) {
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentByAnotherAct.this, HomeActivity.class)
                                .putExtra("status","")
                                .putExtra("msg","").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    }


                    else if (object.optString("status").equals("3")) {
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentByAnotherAct.this, HomeActivity.class)
                                .putExtra("status","")
                                .putExtra("msg","").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    }


                    else if (object.getString("status").equals("5")) {
                        PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.LoginStatus, "false");
                        startActivity(new Intent(PaymentByAnotherAct.this, Splash.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
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
*/




    private void PaymentAPI(String operateur, String strList,String number,String paymentType) {
        //binding.loader.setVisibility(View.VISIBLE);
        DataManager.getInstance().showProgressMessage(PaymentByAnotherAct.this, getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");


        Map<String, String> map = new HashMap<>();
        map.put("user_id", userId/*PreferenceConnector.readString(PaymentByAnotherAct.this, PreferenceConnector.User_id, "")*/);
        map.put("amount", /*"105"*/totalPriceToToPay);

        map.put("delivery_charge", deliveryCharge);
        map.put("platFormsFees", platFormsFees);
        map.put("taxN1", taxN1);
        map.put("taxN2", taxN2);
        map.put("operateur", operateur);
        map.put("cart_id", strList);
        if(operateur.equals("MC"))  map.put("num_marchand", "060110217");
        else if(operateur.equals("AM")) map.put("num_marchand", "074272732");
        else if(operateur.equals("VM")) map.put("num_marchand", "074272732");
        else if(operateur.equals(""))map.put("num_marchand", "");
        map.put("type", "USER");
        map.put("user_number",number);
        map.put("register_id", DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getRegisterId());
        map.put("address_id", "");

        map.put("payment_type",paymentType);
        map.put("sub_orderdata",sendToServer);
        map.put("datetime",DataManager.getCurrent());

        Log.e("MapMap", "payment_params" + map);

        Call<ResponseBody> loginCall = apiInterface.payment(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                // binding.loader.setVisibility(View.GONE);
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Payment RESPONSE" + object);
                    if (object.optString("status").equals("1")) {
                        //  PaymentModal data = new Gson().fromJson(responseData, PaymentModal.class);
                        // binding.loader.setVisibility(View.GONE);
                        if(paymentType.equals("Wallet") /*|| paymentType.equals("Cash")*/) {
                            Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PaymentByAnotherAct.this, HomeAct.class).putExtra("status","")
                                    .putExtra("msg","").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                            finish();
                        }
                        else {
                            PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.transId,object.getJSONObject("ressult").getString("reference"));
                            PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.serviceType,"InvoiceWallet");
                            PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.ShareUserId,userId);
                            PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.PaymentType,"InvoiceWallet");
                            startActivity(new Intent(PaymentByAnotherAct.this, CheckPaymentStatusAct.class)
                                    .putExtra("paymentBy","anotherUser"));

                        }
                    } else if (object.optString("status").equals("0")) {
                        //binding.loader.setVisibility(View.GONE);
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();                    }


                    else if (object.optString("status").equals("5")) {

                    }


                } catch (Exception e) {
                    Log.e("error>>>>", "" + e);
                    binding.loader.setVisibility(View.GONE);
                    e.printStackTrace();
                }






            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Toast.makeText(PaymentByAnotherAct.this, "Network Error !!!!", Toast.LENGTH_SHORT).show();
                //  binding.loader.setVisibility(View.GONE);
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }


    private void PaymentWalletAPI(String operateur, String strList,String number,String paymentType) {
        DataManager.getInstance().showProgressMessage(PaymentByAnotherAct.this, getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");


        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("amount",totalPriceToToPay );
        map.put("creditCountryCode", countryCode);
        map.put("operator", operateur);
        if(operateur.equals("MC"))  map.put("num_marchand", "060110217");
        else if(operateur.equals("AM")) map.put("num_marchand", "074272732");
        map.put("fee", "0.00");
        map.put("numberCredit",transferNumber);
        map.put("numberDebit",number);
        map.put("paymentType",paymentType);
        map.put("transaction_by","Other");
        map.put("transaction_type","TransferMoneyInvoice");


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

                        PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.transId,object.getJSONObject("ressult").getString("reference"));
                        PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.serviceType,"InvoiceWallet");
                        PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.ShareUserId,userId);
                        PreferenceConnector.writeString(PaymentByAnotherAct.this,PreferenceConnector.PaymentType,"InvoiceWallet");


                        startActivity(new Intent(PaymentByAnotherAct.this, CheckPaymentStatusAct.class)
                                .putExtra("paymentBy","Transfer"));


                    } else if (object.optString("status").equals("0")) {
                        //binding.loader.setVisibility(View.GONE);
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();                    }


                    else if (object.optString("status").equals("5")) {

                    }


                } catch (Exception e) {
                    Log.e("error>>>>", "" + e);
                    e.printStackTrace();
                }






            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Toast.makeText(PaymentByAnotherAct.this, "Network Error !!!!", Toast.LENGTH_SHORT).show();
                //  binding.loader.setVisibility(View.GONE);
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



    public void dialogAirtel(String operator,String strList){
        Dialog mDialog = new Dialog(PaymentByAnotherAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_airtel);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);

        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(PaymentByAnotherAct.this, getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                if(type.equals("InvoiceToOtherUser"))  PaymentAPI(operator, cart_id_string,edNumber.getText().toString(),"Online");
                else PaymentWalletAPI(operator, cart_id_string,edNumber.getText().toString(),"Online");
            }

        });
        mDialog.show();

    }

    public void dialogMoov(String operator,String strList){
        Dialog mDialog = new Dialog(PaymentByAnotherAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_moov);
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);

        EditText edNumber = mDialog.findViewById(R.id.edNumber);
        AppCompatButton btnBack = mDialog.findViewById(R.id.btnBack);
        AppCompatButton btnPayNow = mDialog.findViewById(R.id.btnPayNow);

        btnBack.setOnClickListener(v -> {
            mDialog.dismiss();

        });

        btnPayNow.setOnClickListener(v -> {
            if(edNumber.getText().toString().equals(""))
                Toast.makeText(PaymentByAnotherAct.this, getString(R.string.please_enter_number), Toast.LENGTH_SHORT).show();

            else {
                mDialog.dismiss();
                //  if(type.equals(""))PaymentAPI(operator, cart_id_string,edNumber.getText().toString(),"Online");

                if(type.equals("InvoiceToOtherUser"))  PaymentAPI(operator, cart_id_string,edNumber.getText().toString(),"Online");
                else PaymentWalletAPI(operator, cart_id_string,edNumber.getText().toString(),"Online");
            }

        });

        mDialog.show();
    }

    public static String generateReferenceNumber() {
        // Get current date in DDMMYYYY format
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String datePart = dateFormat.format(new Date());

        // Generate two random letters
        String lettersPart = generateRandomLetters(2);

        // Combine parts to create the reference number
        return "Ref" + datePart + lettersPart;
    }


    private static String generateRandomLetters(int length) {
        StringBuilder letters = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            // Generate a random letter between 'a' and 'z'
            char randomChar = (char) ('a' + random.nextInt(26));
            letters.append(randomChar);
        }
        return letters.toString();
    }






    public void  getWalletInvoiceData(String apiType){
        DataManager.getInstance().showProgressMessage(PaymentByAnotherAct.this, getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        Map<String, String> map = new HashMap<>();
        map.put("invoice_id", paymentInsertId);

        Log.e(TAG, "Get Invoice Request :" + map);
        Call<ResponseBody>loginCall     = apiInterface.getWalletInvoiceDataApi(headerMap,map);

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Get Invoice RESPONSE" + object);
                    GetProfile();
                    if (object.optString("status").equals("1")) {
                        // JSONObject jsonObject = object.getJSONObject("result");
                        //  sendToServer = object.getJSONObject("data").getJSONObject("delivery_data").toString();
                        //  mainTotalPay = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("sub_total");
                        // taxN1 = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("taxes_first");
                        // taxN2 = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("taxes_second");
                        /*if(!object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("platform_fees").equalsIgnoreCase(""))
                        {
                            platFormsFees = object.getJSONObject("data").getJSONObject("delivery_data").getJSONObject("cddc_josn_decode").getString("platform_fees");
                        }
                        else {
                            platFormsFees ="0.00";
                        }*/

                        // deliveryCharge = object.getJSONObject("data").getString("swi_amount");
                        totalPriceToToPay =  object.getJSONObject("data").getString("swi_amount");
                        binding.totalPriceToToPay.setText("XAF" +  totalPriceToToPay);
                        binding.tvShareBy.setText(getString(R.string.invoice_share_by)+ " "+object.getJSONObject("data").getJSONObject("user_data").getString("user_name"));
                        //  arrayList.clear();
                        countryCode = object.getJSONObject("data").getJSONObject("user_data").getString("country_code");
                        transferNumber = object.getJSONObject("data").getJSONObject("user_data").getString("mobile");



                    } else if (object.optString("status").equals("0")) {
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();


                    }


                    else if (object.optString("status").equals("2")) {
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentByAnotherAct.this, HomeAct.class)
                                .putExtra("status","")
                                .putExtra("msg","").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    }


                    else if (object.optString("status").equals("3")) {
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PaymentByAnotherAct.this, HomeAct.class)
                                .putExtra("status","")
                                .putExtra("msg","").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();

                    }


                    else if (object.getString("status").equals("5")) {

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

    private void callCardPayment( String type) {
        //binding.loader.setVisibility(View.VISIBLE);
        DataManager.getInstance().showProgressMessage(PaymentByAnotherAct.this, getString(R.string.please_wait));
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");


        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getId());
        map.put("amount", /*"105"*/totalPriceToToPay);

        map.put("delivery_charge", "");
        map.put("platFormsFees", "");
        map.put("taxN1", "");
        map.put("taxN2", "");
        map.put("operateur", "VM");
        map.put("cart_id", strList);
        map.put("num_marchand", "");
        map.put("type", "USER");
        map.put("user_number","");
        map.put("register_id", DataManager.getInstance().getUserData(PaymentByAnotherAct.this).getResult().getRegisterId());
        map.put("address_id", "");
        map.put("payment_type","Card");
        map.put("sub_orderdata",sendToServer);
        map.put("datetime",DataManager.getCurrent());
        map.put("self_collect","");
    //    map.put("transaction_type","");


        Log.e("MapMap", "payment_params" + map);

        Call<ResponseBody> loginCall = apiInterface.cardPaymentApi(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                // binding.loader.setVisibility(View.GONE);
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Card Payment RESPONSE" + object);
                    if (object.optString("status").equals("1")) {
                        if(type.equals("InvoiceToOtherUser")) {
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.transId, object.getJSONObject("ressult").getString("reference"));
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.serviceType, "InvoiceWallet");
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.ShareUserId, userId);
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.PaymentType, "InvoiceWallet");
                        }
                        else {
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.transId, object.getJSONObject("ressult").getString("reference"));
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.serviceType, "Invoice");
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.ShareUserId, userId);
                            PreferenceConnector.writeString(PaymentByAnotherAct.this, PreferenceConnector.PaymentType, "Invoice");
                        }

                        startActivity(new Intent(PaymentByAnotherAct.this, PaymentWebViewAct.class)
                                .putExtra("url",object.getJSONObject("ressult").getString("webviewurl"))
                                .putExtra("ref",object.getJSONObject("ressult").getString("reference")));
                    } else if (object.optString("status").equals("0")) {
                        //binding.loader.setVisibility(View.GONE);
                        Toast.makeText(PaymentByAnotherAct.this, object.getString("message"), Toast.LENGTH_SHORT).show();

                    }



                } catch (Exception e) {
                    Log.e("error>>>>", "" + e);
                    binding.loader.setVisibility(View.GONE);
                    e.printStackTrace();
                }






            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                Toast.makeText(PaymentByAnotherAct.this, "Network Error !!!!", Toast.LENGTH_SHORT).show();
                //  binding.loader.setVisibility(View.GONE);
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }



}
