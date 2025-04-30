package com.afaryseller.ui.shoplist;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.FragmentShopListBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.addproduct.AddProductNewAct;
import com.afaryseller.ui.addtime.AddTimeFragment;
import com.afaryseller.ui.addtime.TimeModel;
import com.afaryseller.ui.addtime.adapter.HolidaysAdapter;
import com.afaryseller.ui.addtime.adapter.WeeklyAdapter;
import com.afaryseller.ui.addtime.model.HolidaysModel;
import com.afaryseller.ui.editproduct.EditProductAct;
import com.afaryseller.ui.shoplist.listerner.ShopListener;
import com.afaryseller.ui.subscription.SubscriptionAct;
import com.afaryseller.ui.subseller.updateprofile.UpdateSubSellerProfileAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShopListFragment extends BaseFragment<FragmentShopListBinding, ShopListViewModel> implements ShopListener {
    FragmentShopListBinding binding;
    ShopListViewModel shopListViewModel;
    String sub_categary_id="",name="";
    ShopAdapter adapter;
    ArrayList<ShopModel.Result>shopArrayList;
    ShopModel.Result shopData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shop_list, container, false);
        shopListViewModel = new ShopListViewModel();
        binding.setShopListViewModel(shopListViewModel);
        binding.getLifecycleOwner();
        shopListViewModel.init(getActivity());
        initViews();
        return binding.getRoot() ;
    }

    private void initViews() {

        if(this.getArguments()!=null) {
            sub_categary_id = getArguments().getString("id");
            name = getArguments().getString("name");

        }

        shopArrayList = new ArrayList<>();

        adapter =  new ShopAdapter(getActivity(),shopArrayList,ShopListFragment.this);
        binding.rvShops.setAdapter(adapter);

        binding.RRback.setOnClickListener(v -> getActivity().onBackPressed());

        binding.ivAddShop.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id",sub_categary_id);
            bundle.putString("name",name);
            Navigation.findNavController(v).navigate(R.id.action_shoplist_fragment_to_addshop,bundle);

        });

        observeLoader();
        observeResponse();


      //  binding.recyclerViewDays.setAdapter(new WeeklyAdapter(getActivity(), weeklyList, AddTimeFragment.this));

    }

    private void getShops() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
       // map.put("category_id", sub_categary_id);
        map.put("merchant_id", sub_categary_id);
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("sub_seller_id", DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id());
        map.put("shop_id",SessionManager.readString(requireActivity(), Constant.shopId, ""));

        shopListViewModel.getAllShops(getActivity(),headerMap,map);

    }

    private void updateShopStatus(String shopId,String status) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String> map = new HashMap<>();
        map.put("status", status);
        map.put("shop_id", shopId);
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        shopListViewModel.updateShopStatus(getActivity(),headerMap,map);

    }

    private void checkPlan() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId()+"");
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        shopListViewModel.checkStatsPlan(getActivity(),headerMap,map);

    }




    public void observeResponse(){
        shopListViewModel.isResponse.observe(getActivity(),dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.GET_ALL_SHOP){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get daily close Day===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                binding.tvNotFound.setVisibility(View.GONE);
                                ShopModel shopModel = new Gson().fromJson(stringResponse, ShopModel.class);
                                shopArrayList.clear();
                                shopArrayList.addAll(shopModel.getResult());
                                adapter.notifyDataSetChanged();


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                shopArrayList.clear();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                binding.tvNotFound.setVisibility(View.VISIBLE);

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


                if(dynamicResponseModel.getApiName()== ApiConstant.CHECK_PLAN_STATUS){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response update shop status===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                if(shopData!=null){
                                     startActivity(new Intent(getActivity(), AddProductAct.class)
                                         .putExtra("shopId", shopData.getShopId())
                                         .putExtra("shop_name", shopData .getName())
                                             .putExtra("currency",shopData.getCurrency())
                                             .putExtra("images",jsonObject.getJSONObject("plan_detail").getString("image_limit")));
                                }
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                               // Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                               if(shopData!=null) dialogAlert(shopData);
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



            }
        } );
    }

    private void observeLoader() {
        shopListViewModel.isLoading.observe(getActivity(),aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getShops();
    }

    @Override
    public void editShop(String shopId,View v, ShopModel.Result data, String tag) {
        shopData = data;
     if(tag.equals("Edit")) {
         Bundle bundle = new Bundle();
         bundle.putString("id", sub_categary_id);
         bundle.putString("name", name);
         bundle.putSerializable("shopData", data);
         Navigation.findNavController(v).navigate(R.id.action_shoplist_fragment_to_editShop, bundle);
     }
     else if(tag.equals("Add")){
       /*  String addProductCount ="";
         String ProductCount = "";
         if(data.getAddProductCount().equals("")) addProductCount = "0";
         else addProductCount = data.getAddProductCount();
         if(data.getProductQut().equals("")) ProductCount =  "1";
         else ProductCount =   data.getProductQut();
         if (!data.getPlanId().equals("")) {
             // if (data.getPlanCount().equals("0")) {
             if(Integer.parseInt(addProductCount)< Integer.parseInt(ProductCount))
             {
                // startActivity(new Intent(getActivity(), AddProductAct.class)
                    //     .putExtra("shopId", data.getId())
                    //     .putExtra("shop_name", data.getName()));
               //  dialogAlert(data);
             }
             else dialogAlert(data);
             
        }
        else{
            startActivity(new Intent(getActivity(), SubscriptionAct.class)
                    .putExtra("shopId", data.getId())
                    .putExtra("shop_name", data.getName()));

        }*/

         checkPlan();


     }


     else if(tag.equals("Active") || tag.equals("Deactive")){
         activeDeActiveDialogAlerts (shopId,tag);

     }



    }

    private void dialogAlert(ShopModel.Result data) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.get_plan))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.get_plann), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(getActivity(), SubscriptionAct.class)
                                .putExtra("shopId", data.getShopId())
                                .putExtra("shop_name", data.getName())
                                .putExtra("currency",data.getCurrency()));



                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void activeDeActiveDialogAlerts(String shopId,String status) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.are_you_sure_you_want_to)+" " + status + " "+getString(R.string.this_shop))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        updateShopStatus(shopId,status);
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}
