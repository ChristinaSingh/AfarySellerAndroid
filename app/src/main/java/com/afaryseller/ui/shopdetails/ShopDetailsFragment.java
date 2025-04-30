package com.afaryseller.ui.shopdetails;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.FragmentShopDetailsBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.editTime.EditTimeFragment;
import com.afaryseller.ui.editproduct.EditProductAct;
import com.afaryseller.ui.shopdetails.model.ShopDetailModel;
import com.afaryseller.ui.shopdetails.adapter.ProductAdapter;
import com.afaryseller.ui.shopdetails.adapter.ShopImageSliderAdapter;
import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.ui.shoplist.listerner.ShopListener;
import com.afaryseller.ui.subscription.SubscriptionAct;
import com.afaryseller.ui.viewproduct.ViewProduct2Act;
import com.afaryseller.ui.viewproduct.ViewProductAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopDetailsFragment extends BaseActivity<FragmentShopDetailsBinding, ShopDetailViewModel> implements ShopListener {
    FragmentShopDetailsBinding binding;
    ShopDetailViewModel shopDetailViewModel;
    String shop_id ="",name ="";
    ShopImageSliderAdapter adapter;
    ArrayList<String>banner_array_list;
    ArrayList<ShopDetailModel.Result.Product>productArrayList;

    ShopDetailModel shopDetailModel;

    String currency="",tag="",position="0";
    BroadcastReceiver validateProductReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getStringExtra("type") != null) {
                try {
                    if ("validated".equals(intent.getStringExtra("type"))) {
                        AlertProductApproved(intent.getStringExtra("productId"),intent.getStringExtra("productName"));
                    }
                    else AlertProductNotApproved(intent.getStringExtra("productId"),intent.getStringExtra("productName"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


    };

    private void AlertProductNotApproved(String product_id, String product_name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsFragment.this);
        builder.setMessage(getString(R.string.dear_service_provider)+"\n"+ getString(R.string.sorry_your_product) +" "  + product_name +  " ID " + "XXXXX"+product_id +  " "+getString(R.string.has_not_been_validate)+"\n" +
                        "CAUSE: .............\n" +
                         getString(R.string.please_go_to_your_product_list) +
                        "\n" +
                        "The AfaryCode team")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if(!shop_id.equalsIgnoreCase(""))  getShopDetails();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void AlertProductApproved(String product_id, String product_name) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsFragment.this);
        builder.setMessage(getString(R.string.dear_service_provider)+"\n"+ getString(R.string.your_product)+" "  + product_name +  " ID " + "XXXXX"+product_id +  " "+  getString(R.string.product_has_been_validate) + "\n" +
                        getString(R.string.good_luck)+
                        "\n" +
                        "The AfaryCode team")
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                      if(!shop_id.equalsIgnoreCase(""))  getShopDetails();

                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.fragment_shop_details);
        shopDetailViewModel = new ShopDetailViewModel();
        binding.setShopDetailViewModel(shopDetailViewModel);
        binding.getLifecycleOwner();
        shopDetailViewModel.init(ShopDetailsFragment.this);
        initViews();
    }

    private void initViews() {
        banner_array_list = new ArrayList<>();
        productArrayList = new ArrayList<>();
        observeLoader();
        observeResponse();

        if(getIntent()!=null) {
            shop_id = getIntent().getStringExtra("shop_id");
            name = getIntent().getStringExtra("name");
            binding.tvTitle.setText(name);

        }

        binding.ivBack.setOnClickListener(v -> {
           finish();
        });

        binding.tvEdit.setOnClickListener(v -> {
            startActivity(new Intent(ShopDetailsFragment.this, EditTimeFragment.class)
                    .putExtra("shop_id",shop_id).putExtra("name",name));
        });

        binding.tvAddProduct.setOnClickListener(view -> {
            /*if(shopDetailModel!=null){
                startActivity(new Intent(ShopDetailsFragment.this, AddProductAct.class)
                        .putExtra("shopId",shop_id)
                        .putExtra("shop_name", name)
                        .putExtra("currency",currency)
                        .putExtra("images","2"));

            }*/
             tag = "add";
            checkPlan();
        });


        getShopDetails();


        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                binding.swipeRefreshLayout.setRefreshing(false);
                getShopDetails();
            }
        });

    }

    private void checkPlan() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getId()+"");
        map.put("user_seller_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getRegisterId());

        shopDetailViewModel.checkStatsPlan(ShopDetailsFragment.this,headerMap,map);

    }



    private void getShopDetails() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String>map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getRegisterId());
        map.put("sub_seller_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getSub_seller_id());

        map.put("shop_id",shop_id);
        shopDetailViewModel.getShopDetailsProduct(ShopDetailsFragment.this,headerMap,map);
    }


    public void observeResponse(){
        shopDetailViewModel.isResponse.observe(ShopDetailsFragment.this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();

                if(dynamicResponseModel.getApiName()== ApiConstant.ADD_SHOP_DETAIL_PRODUCT){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                 shopDetailModel = new Gson().fromJson(stringResponse,ShopDetailModel.class);
                              //  binding.tvNotFount.setVisibility(View.GONE);
                              //  Glide.with(ShopDetailsFragment.this).load(shopDetailModel.getResult().getImage1()).into(binding.ivCover);
                                binding.tvAddress.setText(shopDetailModel.getResult().getCity_name());
                                binding.tvFullAddress.setText(shopDetailModel.getResult().getAddress());
                                binding.tvShopName.setText(shopDetailModel.getResult().getName());
                                banner_array_list.clear();
                                binding.tvClose.setText("Close Time : "+shopDetailModel.getResult().getCloseTime());

                                banner_array_list.add(shopDetailModel.getResult().getImage1());
                                banner_array_list.add(shopDetailModel.getResult().getImage2());
                                banner_array_list.add(shopDetailModel.getResult().getImage3());

                                adapter = new ShopImageSliderAdapter(ShopDetailsFragment.this, banner_array_list);
                                binding.imageSlider.setSliderAdapter(adapter);
                                binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                                //    binding.imageSlider2.setIndicatorSelectedColor(R.color.colorPrimary);
                                //    binding.imageSlider2.setIndicatorUnselectedColor(Color.GRAY);
                                binding.imageSlider.setScrollTimeInSec(3);
                                binding.imageSlider.setAutoCycle(true);
                                binding.imageSlider.startAutoCycle();

                               /* if(shopDetailModel.getResult().getCurrency().equalsIgnoreCase("Dollars")) currency = "USD";
                               else if(shopDetailModel.getResult().getCurrency().equalsIgnoreCase("Euro")) currency = "EUR";
                               else if(shopDetailModel.getResult().getCurrency().equalsIgnoreCase("Franc CFA")) currency = "XAF";
                               else if(shopDetailModel.getResult().getCurrency().equalsIgnoreCase("INDIA RUPEE")) currency = "INR";*/
                               /*else*/
                                if(shopDetailModel.getResult().getCurrency().equals("XAF") || shopDetailModel.getResult().getCurrency().equals("XOF")) currency = "FCFA";
                                else currency =   shopDetailModel.getResult().getCurrency();
                                if(shopDetailModel.getResult().getProduct().size()!=0){
                                    productArrayList.clear();
                                    binding.tvAddProduct.setVisibility(View.VISIBLE);
                                    productArrayList.addAll(shopDetailModel.getResult().getProduct());
                                    binding.rvProduct.setAdapter(new ProductAdapter(ShopDetailsFragment.this, productArrayList,ShopDetailsFragment.this,currency));
                                }
                                else {
                                    productArrayList.clear();
                                   binding.tvAddProduct.setVisibility(View.VISIBLE);
                                   binding.rvProduct.setAdapter(new ProductAdapter(ShopDetailsFragment.this, productArrayList,ShopDetailsFragment.this,""));

                                }

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                              //  binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(ShopDetailsFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ShopDetailsFragment.this);
                            }
                        }
                        else {
                           // binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(ShopDetailsFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.PRODUCT_ACTIVE_DEACTIVE){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getShopDetails();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                //  binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(ShopDetailsFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ShopDetailsFragment.this);
                            }
                        }
                        else {
                            // binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(ShopDetailsFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.DELETE_PRODUCT){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getShopDetails();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                //  binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(ShopDetailsFragment.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ShopDetailsFragment.this);
                            }
                        }
                        else {
                            // binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(ShopDetailsFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(dynamicResponseModel.getApiName()== ApiConstant.CHECK_PLAN_STATUS){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response check plan status===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                if(shopDetailModel!=null){
                                    if(tag.equals("add")) {
                                        startActivity(new Intent(ShopDetailsFragment.this, AddProductAct.class)
                                                .putExtra("shopId", shop_id)
                                                .putExtra("shop_name", name)
                                                .putExtra("currency", currency)
                                                .putExtra("images", jsonObject.getJSONObject("plan_detail").getString("image_limit")));
                                    }

                                    else if(tag.equals("edit")){
                                          startActivity(new Intent(ShopDetailsFragment.this, EditProductAct.class)
                                                  .putExtra("data",productArrayList.get(Integer.parseInt(position)))
                                                  .putExtra("currency",currency)
                                                  .putExtra("shopId", shop_id)
                                                  .putExtra("shop_name",name)
                                                  .putExtra("images", jsonObject.getJSONObject("plan_detail").getString("image_limit")));
                                    }


                                    else if(tag.equals("view")){
                                        startActivity(new Intent(ShopDetailsFragment.this, ViewProduct2Act.class)
                                                .putExtra("data",productArrayList.get(Integer.parseInt(position)))
                                                .putExtra("currency",currency)
                                                .putExtra("shopId", shop_id)
                                                .putExtra("shop_name",name)
                                                .putExtra("images", jsonObject.getJSONObject("plan_detail").getString("image_limit")));
                                    }




                                }





                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                if(shopDetailModel!=null) dialogPlanAlert();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ShopDetailsFragment.this);
                            }

                        }
                        else {
                            Toast.makeText(ShopDetailsFragment.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        } );
    }

    private void observeLoader() {
        shopDetailViewModel.isLoading.observe(ShopDetailsFragment.this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(ShopDetailsFragment.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    private void dialogPlanAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsFragment.this);
        builder.setMessage(getString(R.string.get_plan))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.get_plann), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(ShopDetailsFragment.this, SubscriptionAct.class)
                                .putExtra("shopId", shop_id)
                                .putExtra("shop_name", name)
                                .putExtra("currency",currency));



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



    @Override
    public void editShop(String shopId, View v, ShopModel.Result data, String tag) {
        this.tag = tag;
        if(tag.equals("edit")){
        //    startActivity(new Intent(ShopDetailsFragment.this, EditProductAct.class)
         //           .putExtra("data",productArrayList.get(Integer.parseInt(shopId)))
          //          .putExtra("currency",currency));

            position = shopId;
            checkPlan();
        }

       else if(tag.equals("view")){
         /*   startActivity(new Intent(ShopDetailsFragment.this, ViewProductAct.class)
                    .putExtra("data",productArrayList.get(Integer.parseInt(shopId)))
                    .putExtra("currency",currency));*/
            position = shopId;
            checkPlan();

         /*   startActivity(new Intent(ShopDetailsFragment.this, ViewProduct2Act.class)
                    .putExtra("data",productArrayList.get(Integer.parseInt(shopId)))
                    .putExtra("currency",currency)
                    .putExtra("shopId",shopDetailModel.getResult().getId()));*/



        }
        else if(tag.equals("delete")){dialogAlert(shopId);
        }


        else {
            Map<String,String> headerMap = new HashMap<>();
            headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getAccessToken());
            headerMap.put("Accept","application/json");

            HashMap<String, String> map = new HashMap<>();
            map.put("status", tag);
            map.put("product_id", shopId);
            map.put("user_seller_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getId());
            map.put("seller_register_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getRegisterId());
            shopDetailViewModel.updateProductStatus(ShopDetailsFragment.this,headerMap, map);
        }
    }


    private void dialogAlert(String proId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShopDetailsFragment.this);
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_product))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Map<String,String> headerMap = new HashMap<>();
                        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getAccessToken());
                        headerMap.put("Accept","application/json");

                        HashMap<String, String> map = new HashMap<>();
                        map.put("id", productArrayList.get(Integer.parseInt(proId)).getProId());
                        map.put("user_seller_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getId());
                        map.put("seller_register_id", DataManager.getInstance().getUserData(ShopDetailsFragment.this).getResult().getRegisterId());
                        shopDetailViewModel.deleteProduct(ShopDetailsFragment.this,headerMap, map);

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


    @Override
    protected void onResume() {
        super.onResume();
      //  registerReceiver(validateProductReceiver, new IntentFilter("check_product"));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            registerReceiver(validateProductReceiver, new IntentFilter("check_product"),Context.RECEIVER_EXPORTED);
        }
        else {
            registerReceiver(validateProductReceiver, new IntentFilter("check_product"));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(validateProductReceiver);
      //  pauseProgressDialog();
    }

    @Override
    protected void onStop() {
        super.onStop();
      //  pauseProgressDialog();
    }
}