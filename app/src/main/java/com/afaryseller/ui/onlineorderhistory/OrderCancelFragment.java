package com.afaryseller.ui.onlineorderhistory;

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
import com.afaryseller.databinding.FragmentCancelOrderBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.bookedorder.OrderAdapter;
import com.afaryseller.ui.bookedorder.OrderListener;
import com.afaryseller.ui.bookedorder.OrderModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;


import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class OrderCancelFragment extends BaseFragment<FragmentCancelOrderBinding,OnlineHistoryViewModel> implements OrderListener {
    public String TAG = "OrderCancelFragment";

    FragmentCancelOrderBinding binding;
    OnlineHistoryViewModel onlineHistoryViewModel;
    OrderAdapter adapter;

    private ArrayList<OrderModel.Result> arrayList;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cancel_order, container, false);
        onlineHistoryViewModel = new OnlineHistoryViewModel();
        binding.setOnlineHistoryViewModel(onlineHistoryViewModel);
        binding.getLifecycleOwner();
        onlineHistoryViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        observeLoader();
        observeResponse();

        callGetCompletedOrder();

    }



    private void callGetCompletedOrder() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("status", "Cancelled");
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        onlineHistoryViewModel.getOnlineOrderHistory(getActivity(),headerMap,map);
    }


    public void observeResponse(){
        onlineHistoryViewModel.isResponse.observe(getActivity(),dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.ONLINE_ORDER_HISTORY){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);
                                OrderModel model = new Gson().fromJson(stringResponse,OrderModel.class);
                                binding.rvCancel.setAdapter(new OrderAdapter(getActivity(), (ArrayList<OrderModel.Result>) model.getResult(), OrderCancelFragment.this));
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }
                        }
                        else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        } );
    }

    private void observeLoader() {
        onlineHistoryViewModel.isLoading.observe(getActivity(),aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

    @Override
    public void onOrder(OrderModel.Result result) {

    }
}
