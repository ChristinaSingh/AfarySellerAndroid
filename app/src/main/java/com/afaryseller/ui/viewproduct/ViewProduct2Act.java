package com.afaryseller.ui.viewproduct;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityViewProduct2Binding;
import com.afaryseller.databinding.ActivityViewProductBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addproduct.adapter.ProductImageSliderAdapterCopy;
import com.afaryseller.ui.addproduct.adapter.SelectCategoryAdapter;
import com.afaryseller.ui.addproduct.listener.ImageChangeListener;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.BrandModel;
import com.afaryseller.ui.addproduct.model.MainCateModel;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;
import com.afaryseller.ui.addproduct.model.SubCatModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;
import com.afaryseller.ui.editproduct.EditProductAct;
import com.afaryseller.ui.home.adapter.SliderAdapterExample;
import com.afaryseller.ui.shopdetails.ShopDetailsFragment;
import com.afaryseller.ui.shopdetails.model.ShopDetailModel;
import com.afaryseller.ui.viewproduct.adapter.MainAttributeAdapter2;
import com.afaryseller.ui.viewproduct.adapter.SliderAdapterExampleNew;
import com.afaryseller.ui.viewproduct.adapter.ViewSelectSubCatAdapter;
import com.afaryseller.ui.viewproduct.adapter.ViewVariationAdapter;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class ViewProduct2Act extends BaseActivity<ActivityViewProductBinding, ViewProductViewModel> implements AddDateLister, ImageChangeListener {
    ActivityViewProduct2Binding binding;
    ViewProductViewModel viewProductViewModel;

    private ArrayList<ShoppingProductModal.Result> get_result1;

    private ArrayList<ShoppingProductModal.Result.ValidateName> validateNameArrayList = new ArrayList<>();

    ArrayList<BrandModel.Result> brandArrayList;
    String position = "";


    private String chk;
    private static String shopId = "", str_image_path = "", productId = "", str_image_path1 = "", str_image_path2 = "", shopIds = "", deliveryCharges = "0", selectsubCate;
    public JSONArray ja;
    private ArrayList<String> productImgList;
    SliderAdapterExampleNew adapter1;
    ShopDetailModel.Result.Product product;

    String currency = "",shopName="";
    boolean checkRead = false;
    String NoImages ="";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_product2);
        viewProductViewModel = new ViewProductViewModel();
        binding.setViewProductViewModel(viewProductViewModel);
        binding.getLifecycleOwner();
        viewProductViewModel.init(ViewProduct2Act.this);
        initViews();

        observeLoader();
        observeResponse();


        getProductDetail();

    }


    private void getProductDetail() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(ViewProduct2Act.this).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
      /*  map.put("pro_id", productId);
        map.put("seller_id", DataManager.getInstance().getUserData(ViewProduct2Act.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(ViewProduct2Act.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(ViewProduct2Act.this).getResult().getId());*/

        map.put("user_id", DataManager.getInstance().getUserData(ViewProduct2Act.this).getResult().getId());
        map.put("restaurant_id", shopId);
        map.put("pro_id", productId);
        map.put("register_id", DataManager.getInstance().getUserData(ViewProduct2Act.this).getResult().getRegisterId());
        viewProductViewModel.getProductDetail(ViewProduct2Act.this, headerMap, map);

    }


    private void initViews() {

        get_result1 = new ArrayList<>();
        validateNameArrayList = new ArrayList<>();
        productImgList = new ArrayList<>();


        if (getIntent() != null) {
            product = (ShopDetailModel.Result.Product) getIntent().getSerializableExtra("data");

            NoImages = getIntent().getStringExtra("images");
            shopName = getIntent().getStringExtra("shop_name");
            shopId =  getIntent().getStringExtra("shopId");
            productId =   product.getProId();



            if(getIntent().getStringExtra("currency").equals("XAF") || getIntent().getStringExtra("currency").equals("XOF")) currency = "FCFA";
            else currency =   getIntent().getStringExtra("currency");
           // currency = getIntent().getStringExtra("currency");


            if(Integer.parseInt(product.getProductQut())<=5){
                alertProductQuantity();
            }

        }

        binding.RRback.setOnClickListener(v -> finish());

        binding.btnReadMore.setOnClickListener(view -> {
          /*  if(checkRead==false){
                binding.productDetails.setMaxLines(Integer.MAX_VALUE);//your TextView
                checkRead = true;
            }else {
                binding.productDetails.setMaxLines(5);//your TextView
                checkRead = false;

            }*/

            toggleText();

        });



    }


    private void alertProductQuantity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewProduct2Act.this);
        builder.setMessage(getString(R.string.dear_partner_your_product_is_almost_out_of_stock_do_you_want_to_increase_the_stock))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(ViewProduct2Act.this, EditProductAct.class)
                                .putExtra("data",product)
                                .putExtra("currency",currency)
                                .putExtra("shopId", shopId)
                                .putExtra("shop_name",shopName)
                                .putExtra("images",NoImages ));
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


    private void toggleText() {
        if (checkRead) {
            // Collapse to one line
            binding.productDetails.setMaxLines(1);
            binding.btnReadMore.setText("READ MORE");
        } else {
            // Expand to five lines
            binding.productDetails.setMaxLines(10);
            binding.btnReadMore.setText("READ LESS");
        }
        checkRead = !checkRead; // Toggle the state
    }


    public void observeResponse() {
        viewProductViewModel.isResponse.observe(ViewProduct2Act.this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();

                if (dynamicResponseModel.getApiName() == ApiConstant.GET_PRODUCT_DETAILS) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response add shop product===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                ShoppingProductModal productModal = new Gson().fromJson(stringResponse, ShoppingProductModal.class);
                                get_result1.clear();
                                validateNameArrayList.clear();
                                get_result1.addAll(productModal.getResult());

                                if (!get_result1.get(0).getProductImages().equalsIgnoreCase("")) {
                                    productImgList.add(get_result1.get(0).getProductImages());
                                }

                                if (!get_result1.get(0).getImage3().equalsIgnoreCase("")) {
                                    productImgList.add(get_result1.get(0).getImage3());
                                }

                                if (!get_result1.get(0).getImage1().equalsIgnoreCase("")) {
                                    productImgList.add(get_result1.get(0).getImage1());
                                }
                                if (!get_result1.get(0).getImage2().equalsIgnoreCase("")) {
                                    productImgList.add(get_result1.get(0).getImage2());
                                }

                                validateNameArrayList.addAll(get_result1.get(0).getValidateName());
                                if (get_result1.get(0).getDeliveryCharges().equalsIgnoreCase("1"))
                                    binding.ivDeliveryType.setVisibility(View.VISIBLE);
                                else binding.ivDeliveryType.setVisibility(View.GONE);


                                if (validateNameArrayList.size() > 0) {

                                    binding.rvMain.setAdapter(new MainAttributeAdapter2(ViewProduct2Act.this, validateNameArrayList));
                                }

                                if (!get_result1.get(0).getProductBrand().equalsIgnoreCase("")) {
                                    binding.tvBrand.setVisibility(View.VISIBLE);
                                    binding.tvBrand.setText(getString(R.string.brand) + " : " + get_result1.get(0).getProductBrand());

                                } else {
                                    binding.tvBrand.setVisibility(View.GONE);

                                }

                                binding.tvSellerName.setText(" " + get_result1.get(0).getSellerName().trim());


                                if (get_result1.get(0).getInStock().equalsIgnoreCase("Yes")) {
                                    binding.tvStock.setVisibility(View.VISIBLE);
                                    binding.tvStock.setText(getString(R.string.in_stock).toUpperCase());
                                    binding.tvStock.setTextColor(getResources().getColor(R.color.colorGreen));

                                } else {
                                    binding.tvStock.setVisibility(View.GONE);

                                }
                                if (get_result1.get(0).getVerify().equalsIgnoreCase("Yes")) {
                                    binding.ivVerify.setVisibility(View.VISIBLE);
                                    binding.rlCertifySeller.setVisibility(View.VISIBLE);

                                } else {
                                    binding.ivVerify.setVisibility(View.GONE);
                                    binding.rlCertifySeller.setVisibility(View.GONE);
                                }


                                adapter1 = new SliderAdapterExampleNew(ViewProduct2Act.this, productImgList);
                                binding.productImgSlider.setSliderAdapter(adapter1);
                                binding.productImgSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                binding.productImgSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                binding.productImgSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                                //     binding.imageSlider.setIndicatorSelectedColor(R.color.colorPrimary);
                                //      binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                                binding.productImgSlider.setScrollTimeInSec(3);
                                binding.productImgSlider.setAutoCycle(true);
                                binding.productImgSlider.startAutoCycle();

                                binding.tvTitle.setText(get_result1.get(0).getProductName());
                                binding.shopName.setText(get_result1.get(0).getProductName().toUpperCase());
                                // binding.productDetails.setMaxLines(5);//your TextView


                                binding.productDetails.setText(get_result1.get(0).productDetails);
                                binding.productPrice.setText(currency + get_result1.get(0).productPrice);

                                binding.tvRate.setText(get_result1.get(0).getAvgRating() + " ");
                                binding.ratingbar.setRating(Float.parseFloat(get_result1.get(0).getAvgRating()));


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(ViewProduct2Act.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ViewProduct2Act.this);
                            }

                        } else {
                            Toast.makeText(ViewProduct2Act.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }


    private void observeLoader() {
        viewProductViewModel.isLoading.observe(ViewProduct2Act.this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(ViewProduct2Act.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }


    @Override
    public void onDate(String date, int position, String tag, boolean chk) {
    }

    @Override
    public void onDate(String date, int position, String tag) {

    }


    @Override
    public void imageChange(int position, String image) {
        chk = "3";
        this.position = position + "";
        // if (image.equals("change")) showImageSelection();
    }
}

