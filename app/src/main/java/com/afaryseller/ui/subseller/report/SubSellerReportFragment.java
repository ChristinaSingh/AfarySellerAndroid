package com.afaryseller.ui.subseller.report;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.FragmentSubSellerPeriodicReportBinding;
import com.afaryseller.databinding.OrderFragmentBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.bookedorder.OrderAdapter;
import com.afaryseller.ui.bookedorder.OrderFragment;
import com.afaryseller.ui.bookedorder.OrderListener;
import com.afaryseller.ui.bookedorder.OrderModel;
import com.afaryseller.ui.bookedorder.OrderViewModel;
import com.afaryseller.ui.sellerreport.SellerReportAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class SubSellerReportFragment extends BaseFragment<OrderFragmentBinding, ReportViewModel> implements OrderListener {
    FragmentSubSellerPeriodicReportBinding binding;
    ReportViewModel reportViewModel;
    ReportAdapter adapter;
    ArrayList<OrderModel.Result> arrayList;
    private String startDate="",endDate="";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_seller_periodic_report, container, false);
        reportViewModel = new ReportViewModel();
        binding.setReportViewModel(reportViewModel);
        binding.getLifecycleOwner();
        reportViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }



    private void initViews() {

      /*  if (this.getArguments() != null) {
            catId = getArguments().getString("id");

        }*/
        arrayList = new ArrayList<>();

        adapter = new ReportAdapter(getActivity(), arrayList, SubSellerReportFragment.this);
        binding.rvReports.setAdapter(adapter);


        observeLoader();
        observeResponse();

       // binding.RRback.setOnClickListener(v -> getActivity().onBackPressed());

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

        // callGetReport("THIS_MONTH");

        callGetReport(startDate+"TO"+endDate);
    }

    private void callGetReport(String filter) {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("cat_id", "1");
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        map.put("sub_seller_id", DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id());
        map.put("shop_id", SessionManager.readString(requireActivity(), Constant.shopId, ""));
        map.put("filter",filter);

        reportViewModel.getPeriodicReport(getActivity(), headerMap, map);
    }



    public void observeResponse() {
        reportViewModel.isResponse.observe(getActivity(), dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_PERIODIC_REPORT) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                OrderModel model = new Gson().fromJson(stringResponse, OrderModel.class);
                                binding.tvNotFound.setVisibility(View.GONE);
                                arrayList.clear();
                                arrayList.addAll(model.getResult());

                                if(!arrayList.isEmpty()){
                                    binding.cardSale.setVisibility(View.VISIBLE);
                                    binding.textAmount.setText("FCFA"+jsonObject.getString("total_amount"));
                                }
                                else {
                                    binding.cardSale.setVisibility(View.GONE);

                                }

                                adapter.notifyDataSetChanged();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                binding.tvNotFound.setVisibility(View.VISIBLE);
                                binding.cardSale.setVisibility(View.GONE);
                                arrayList.clear();
                                adapter.notifyDataSetChanged();
                                // Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }
                        } else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        });
    }

    private void observeLoader() {
        reportViewModel.isLoading.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
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
                requireActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // String date = selectedYear  + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    startDate = date;
                    binding.tvStartDate.setText(date);
                    callGetReport(startDate+"TO"+endDate);

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
                requireActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // String date = selectedYear  + "-" + (selectedMonth + 1) + "-" + selectedDay;
                    String date = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay);
                    endDate = date;
                    binding.tvEndDate.setText(date);
                    callGetReport(startDate+"TO"+endDate);

                },
                year, month, day
        );

        datePickerDialog.show();
    }


}
