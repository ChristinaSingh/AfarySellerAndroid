package com.afaryseller.ui.subseller.subsellerlsit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySubSellerListBinding;
import com.afaryseller.retrofit.ApiConstant;

import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.ui.subseller.signup.SubSellerSignupAct;

import com.afaryseller.utility.DataManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SubSellerListAct  extends BaseActivity<ActivitySubSellerListBinding, SubSellerListViewModel> implements SubSellerListListener {
    ActivitySubSellerListBinding binding;
    SubSellerListViewModel subSellerListViewModel;
    SubSellerAdapter adapter;
    ArrayList<SubSellerListModel.Datum> subSellerArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_seller_list);
        subSellerListViewModel = new SubSellerListViewModel();
        binding.setSubSellerListViewModel(subSellerListViewModel);
        binding.getLifecycleOwner();
        subSellerListViewModel.init(SubSellerListAct.this);
        initViews();
        observeLoader();
        observeResponse();
    }

    private void initViews() {
        subSellerArrayList = new ArrayList<>();

        adapter = new SubSellerAdapter(SubSellerListAct.this,subSellerArrayList,SubSellerListAct.this);
        binding.rvSubSeller.setAdapter(adapter);

        binding.RRback.setOnClickListener(v ->finish());

        binding.ivAddShop.setOnClickListener(v -> {
            startActivity(new Intent(SubSellerListAct.this,SubSellerSignupAct.class));
        });


    }


    public void observeResponse(){
        subSellerListViewModel.isResponse.observe(SubSellerListAct.this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.GET_ALL_SUB_SELLER){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("response get all sub seller ===",stringResponse);
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("1")) {
                                binding.tvNotFound.setVisibility(View.GONE);
                                SubSellerListModel subSellerListModel = new Gson().fromJson(stringResponse, SubSellerListModel.class);
                                subSellerArrayList.clear();
                                subSellerArrayList.addAll(subSellerListModel.getData());
                                adapter.notifyDataSetChanged();


                            } else if (jsonObject.getString("status").equals("0")) {
                                subSellerArrayList.clear();
                                adapter.notifyDataSetChanged();
                                binding.tvNotFound.setVisibility(View.VISIBLE);

                            }



                        }
                        else {
                            Toast.makeText(SubSellerListAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

/*
                if(dynamicResponseModel.getApiName()== ApiConstant.SHOP_ACTIVE_DEACTIVE){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response update shop status===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getShops();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
*/





            }
        } );
    }

    private void observeLoader() {
        subSellerListViewModel.isLoading.observe(SubSellerListAct.this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SubSellerListAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

    private void getAllSeller() {
        HashMap<String,String> map = new HashMap<>();
        map.put("parent_seller_id", DataManager.getInstance().getUserData(SubSellerListAct.this).getResult().getId());
        subSellerListViewModel.getAllSubSeller(SubSellerListAct.this,map);

    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllSeller();
    }

    @Override
    public void onListClick(int position, ShopModel.Result data, String tag) {

    }
}
