package com.afaryseller.ui.sellerreport;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySellerReportBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.bookedorder.OrderListener;
import com.afaryseller.ui.bookedorder.OrderModel;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.ui.subseller.report.ReportAdapter;
import com.afaryseller.ui.subseller.report.ReportViewModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


public class SellerReportAct extends BaseActivity<ActivitySellerReportBinding, ReportViewModel> implements OrderListener , AskListener {
    ActivitySellerReportBinding binding;
    ReportViewModel reportViewModel;
    PeriodicReportAdapter adapter;
    ArrayList<PeriodicReportModel.Result> arrayList;
    private String startDate="",endDate="",groupBy="sub_seller";
    JSONArray jsonArray = new JSONArray();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_seller_report);
        reportViewModel = new ReportViewModel();
        binding.setReportViewModel(reportViewModel);
        binding.getLifecycleOwner();
        reportViewModel.init(SellerReportAct.this);
        initViews();
    }

    private void initViews() {

      /*  if (this.getArguments() != null) {
            catId = getArguments().getString("id");

        }*/
        arrayList = new ArrayList<>();

        adapter = new PeriodicReportAdapter(SellerReportAct.this, arrayList, SellerReportAct.this);
        binding.rvReports.setAdapter(adapter);


        observeLoader();
        observeResponse();

         binding.backNavigation.setOnClickListener(v -> finish());

        startDate = DataManager.currentDate();
        endDate = DataManager.currentDate();
        binding.tvStartDate.setText(startDate);
        binding.tvEndDate.setText(endDate);


        binding.cardStartDate.setOnClickListener(v->{
             showDatePickerDialog();
         });

        binding.cardEndDate.setOnClickListener(v->{
            showDatePickerDialog22();
        });


        binding.tvAdvance.setOnClickListener(v->{
            advanceCriteriaDialog();
        });


       // callGetReport("THIS_MONTH");

        callGetReport(startDate+"TO"+endDate,"");
    }

    private void callGetReport(String filter,String shopId) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(SellerReportAct.this).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(SellerReportAct.this).getResult().getId());
        map.put("cat_id", "1");
        map.put("seller_register_id", DataManager.getInstance().getUserData(SellerReportAct.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(SellerReportAct.this).getResult().getId());

     //   map.put("sub_seller_id", DataManager.getInstance().getUserData(SellerReportAct.this).getResult().getSub_seller_id());
        map.put("shop_id", shopId);
        map.put("filter",filter);
        map.put("group_by",groupBy);



        reportViewModel.getSellerPeriodicReport(SellerReportAct.this, headerMap, map);
    }



    public void observeResponse() {
        reportViewModel.isResponse.observe(SellerReportAct.this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SELLER_PERIODIC_REPORT_NEW) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                PeriodicReportModel model = new Gson().fromJson(stringResponse, PeriodicReportModel.class);
                                binding.tvNotFound.setVisibility(View.GONE);
                                arrayList.clear();
                                 try {
                                     for (int i=0;i<model.getResult().size();i++){
                                         int n4=0;
                                         for (int j=0;j<model.getResult().get(i).getOrderDetails().size();j++){
                                             int n1=0;
                                             String deliveryCharge = "0";
                                              deliveryCharge = model.getResult().get(i).getOrderDetails().get(j).getDeliveryCharges();
                                              if (deliveryCharge.equals("")) deliveryCharge = "0" ;
                                             if (model.getResult().get(i).getOrderDetails().get(j).getTotalAmount().contains(",")) n1 = parseFrenchNumber(model.getResult().get(i).getOrderDetails().get(j).getTotalAmount().replace(",",""));
                                             else n1 = parseFrenchNumber(model.getResult().get(i).getOrderDetails().get(j).getTotalAmount());

                                             int n2 = parseFrenchNumber(model.getResult().get(i).getOrderDetails().get(j).getTaxN1())
                                                     + parseFrenchNumber(model.getResult().get(i).getOrderDetails().get(j).getTaxN2())
                                                     + parseFrenchNumber(model.getResult().get(i).getOrderDetails().get(j).getPlatFormsFees())
                                                     + parseFrenchNumber(deliveryCharge);
                                             int n3 = n1 - n2;

                                              n3 = n3+ n4;
                                              n4 = n3;
                                             model.getResult().get(i).setReportTotal(String.valueOf(n3));
                                         }
                                     }

                                 }catch (Exception e){
                                     e.printStackTrace();
                                 }



                                arrayList.addAll(model.getResult());

                                 jsonArray = new JSONArray();

                                if(!arrayList.isEmpty()){

                                    binding.cardSale.setVisibility(View.VISIBLE);
                                 //   binding.cardStartDate.setVisibility(View.VISIBLE);
                                 //   binding.cardEndDate.setVisibility(View.VISIBLE);

                                    binding.textAmount.setText("FCFA"+jsonObject.getString("total_amount"));
                                }
                                else {
                                    jsonArray = new JSONArray();
                                    binding.cardSale.setVisibility(View.GONE);
                                //    binding.cardStartDate.setVisibility(View.GONE);
                                //    binding.cardEndDate.setVisibility(View.GONE);
                                }

                                adapter.notifyDataSetChanged();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                jsonArray = new JSONArray();
                                binding.tvNotFound.setVisibility(View.VISIBLE);
                                binding.cardSale.setVisibility(View.GONE);
                              //  binding.cardStartDate.setVisibility(View.GONE);
                              //  binding.cardEndDate.setVisibility(View.GONE);
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                                // Toast.makeText(SellerReportAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(SellerReportAct.this);
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(SellerReportAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        });
    }

    private void observeLoader() {
        reportViewModel.isLoading.observe(SellerReportAct.this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SellerReportAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }

    @Override
    public void onOrder(OrderModel.Result result) {

    }



    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SellerReportAct.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                   // String date = selectedYear  + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    startDate = date;
                    binding.tvStartDate.setText(date);
                    callGetReport(startDate+"TO"+endDate,"");

                },
                year, month, day
        );

        datePickerDialog.show();
    }

    private void showDatePickerDialog22() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SellerReportAct.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                   // String date = selectedYear  + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    endDate = date;
                    binding.tvEndDate.setText(date);
                    callGetReport(startDate+"TO"+endDate,"");

                },
                year, month, day
        );

        datePickerDialog.show();
    }


    public void advanceCriteriaDialog(){
        Dialog mDialog = new Dialog(SellerReportAct.this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_advance_criteria);
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        CheckBox rdStore = mDialog.findViewById(R.id.rdStore);
        CheckBox rdSubSeller = mDialog.findViewById(R.id.rdSubSeller);
        CheckBox rdStoreCountry = mDialog.findViewById(R.id.rdStoreCountry);
        TextView btnFilter = mDialog.findViewById(R.id.btnFilter);


        btnFilter.setOnClickListener(v -> {
            if(jsonArray.length()>0) {
                Log.e("filter json array===", jsonArray.toString());
                Log.e("unique ids string===", addUniqueIds(jsonArray));

                mDialog.dismiss();
                callGetReport(startDate + "TO" + endDate, addUniqueIds(jsonArray));
            }
            else Toast.makeText(this, getString(R.string.please_select_atleast_one), Toast.LENGTH_SHORT).show();

        });


        rdStore.setOnClickListener(v -> {
            //mDialog.dismiss();
            rdStore.setChecked(true);
            new StoreBottomSheet(SellerReportAct.this).callBack(this::ask).show(getSupportFragmentManager(), "StoreBottomSheet");

        });

        rdSubSeller.setOnClickListener(v -> {
          //  mDialog.dismiss();
            rdSubSeller.setChecked(true);
            new SubSellerBottomSheet(SellerReportAct.this).callBack(this::ask).show(getSupportFragmentManager(), "SubSellerBottomSheet");

        });


        rdStoreCountry.setOnClickListener(v -> {
           // mDialog.dismiss();
            rdStoreCountry.setChecked(true);
            new ShopCountryWiseBottomSheet(SellerReportAct.this).callBack(this::ask).show(getSupportFragmentManager(), "ShopCountryWiseBottomSheet");
        });







        mDialog.show();

    }


    @Override
    public void ask(String value, String status) {
      try {
          if(status.equals("store")){
              groupBy = "shop";
              JSONObject jsonObject = new JSONObject();
              jsonObject.put("id", value);
              jsonArray.put(jsonObject);

             // callGetReport(startDate+"TO"+endDate,value);

          }
          else if(status.equals("subSellerStore")) {
              groupBy = "sub_seller";
              JSONObject jsonObject = new JSONObject();
              jsonObject.put("id", value);
              jsonArray.put(jsonObject);
             // callGetReport(startDate+"TO"+endDate,value);

          }
          else {
              groupBy = "shop";
              JSONObject jsonObject = new JSONObject();
              jsonObject.put("id", value);
              jsonArray.put(jsonObject);
             // callGetReport(startDate+"TO"+endDate,value);

          }
      }catch (Exception exception){
          exception.printStackTrace();
      }

    }


    public String addUniqueIds( JSONArray array){
        String result="";
        try {
            Set<String> uniqueIds = new LinkedHashSet<>();

            // Iterate through array and collect unique IDs
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String idStr = obj.optString("id", "");
                if (!idStr.isEmpty()) {
                    String[] ids = idStr.split(",");
                    for (String id : ids) {
                        uniqueIds.add(id.trim());
                    }
                }
            }
             result = String.join(",", uniqueIds);
        }catch (Exception e){
            e.printStackTrace();
        }

        return result;

    }

    private int parseFrenchNumber(String number) {
        // Remove the commas and parse to an integer
        String cleanedNumber = number.replace(",", "");
        return Integer.parseInt(cleanedNumber);
    }


}

