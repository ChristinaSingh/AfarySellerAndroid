package com.afaryseller.ui.viewproduct;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
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
import com.afaryseller.ui.editTime.EditTimeFragment;
import com.afaryseller.ui.shopdetails.model.ShopDetailModel;
import com.afaryseller.ui.subscription.SubscriptionAct;
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

public class ViewProductAct extends BaseActivity<ActivityViewProductBinding, ViewProductViewModel> implements AddDateLister, ImageChangeListener {
    ActivityViewProductBinding binding;
    ViewProductViewModel viewProductViewModel;
    ArrayList<MainCateModel.Result> mainCateList;
    ArrayList<SubCatModel.Result> subCateArrayList;
    ArrayList<String> subCatIdArrayList;
    ArrayList<SelectSubCateModel> selectSubCateModelArrayList;
    ArrayList<SelectSubCateModel> selectCategoryModelArrayList;
    ArrayList<AttributeModel.Result>attributeArrayList;
    ArrayList<AttributeModel.Result>selectedAttributeArrayList;
    ArrayList<AttributeModel.Result>selectAttributeFinalArrayList;
    ArrayList<AttributeModel.Result>subAttributeArrayList;
    ArrayList<SelectSubCateModel>selectSubAttributeArrayList;


    MutableLiveData<List<AttributeModel.Result>> mutableAttributeArrayList;


    ArrayList<BrandModel.Result> brandArrayList;
    String position="";

    ViewSelectSubCatAdapter selectSubCateAdapter;
    SelectCategoryAdapter selectCategoryAdapter;


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    CircleImageView ivLogo,iv1Pic,iv2Pic;
    private String chk;
    private static String shopId="",str_image_path="",productId="",str_image_path1="",str_image_path2="",shopIds="",deliveryCharges="0",selectsubCate;
    public JSONArray ja;
    ArrayList<String>banner_array_list;
    ProductImageSliderAdapterCopy adapter;
    ShopDetailModel.Result.Product product;

    String currency="";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_product);
        viewProductViewModel = new ViewProductViewModel();
        binding.setViewProductViewModel(viewProductViewModel);
        binding.getLifecycleOwner();
        viewProductViewModel.init(ViewProductAct.this);
        initViews();

        observeLoader();
        observeResponse();

        getAddedSubCat();
        getAddedAttribute();

    }

    private void getAddedSubCat() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ViewProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("pro_id", productId);
        map.put("seller_register_id", DataManager.getInstance().getUserData(ViewProductAct.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(ViewProductAct.this).getResult().getId());
        viewProductViewModel.getAddedSubCategory(ViewProductAct.this, headerMap,map);
    }

    private void getAddedAttribute() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ViewProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("pro_id", productId);
        map.put("seller_id", DataManager.getInstance().getUserData(ViewProductAct.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(ViewProductAct.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(ViewProductAct.this).getResult().getId());
        viewProductViewModel.getAddedAttribute(ViewProductAct.this,headerMap, map);
    }




    private void initViews() {

        mainCateList = new ArrayList<>();
        brandArrayList = new ArrayList<>();
        subCateArrayList = new ArrayList<>();
        subCatIdArrayList = new ArrayList<>();
        selectSubCateModelArrayList = new ArrayList<>();
        selectCategoryModelArrayList = new ArrayList<>();
        attributeArrayList = new ArrayList<>();
        subAttributeArrayList = new ArrayList<>();
        selectSubAttributeArrayList = new ArrayList<>();
        selectAttributeFinalArrayList = new ArrayList<>();
        selectedAttributeArrayList = new ArrayList<>();
        mutableAttributeArrayList = new MutableLiveData<List<AttributeModel.Result>>();
        banner_array_list = new ArrayList<>();

        if (getIntent() != null) {
            product = (ShopDetailModel.Result.Product) getIntent().getSerializableExtra("data");
            if(getIntent().getStringExtra("currency").equals("XAF") || getIntent().getStringExtra("currency").equals("XOF")) currency = "FCFA";
            else currency =   getIntent().getStringExtra("currency");
          //  currency = getIntent().getStringExtra("currency");
            shopId = getIntent().getStringExtra("shopId");

            productId =   product.getProId(); // getIntent().getStringExtra("shopId");
            //   shop_name = getIntent().getStringExtra("shop_name");
            //  shopIds = shopId;
            binding.nameProduct.setText(product.getProductName());
            binding.edSellingPrice.setText(product.getProductPrice());
            binding.edDescription.setText(product.getProductDetails());
            binding.edSKU.setText(product.getSku());
            binding.tvBrand.setVisibility(View.VISIBLE);
            binding.tvBrand.setText(product.getProductBrand());
            binding.tvCurrency.setText(currency);


            selectCategoryModelArrayList.clear();
            SelectSubCateModel hh = new SelectSubCateModel(product.getCategoryId(),product.getCategoryName());
            selectCategoryModelArrayList.add(hh);
            if(selectCategoryModelArrayList.size()!=0)   binding.layoutCategory.setVisibility(View.VISIBLE);
            selectCategoryAdapter = new SelectCategoryAdapter(ViewProductAct.this, selectCategoryModelArrayList, ViewProductAct.this);
            binding.rvSelectCate.setAdapter(selectCategoryAdapter);

         /*   if(product.getDeliveryCharges().equals("0")) binding.switchDelivery.setChecked(false);
            else binding.switchDelivery.setChecked(true);*/

            if(product.getDeliveryCharges().equals("1")) binding.switchDelivery.setChecked(false);
            else binding.switchDelivery.setChecked(true);


            if(product.getInStock().equals("No")) {
                binding.switchProductStock.setChecked(false);
                binding.switchProductStock.setText(getString(R.string.product_out_of_stock));

            }
            else {
                binding.switchProductStock.setChecked(true);
                binding.switchProductStock.setText(getString(R.string.in_stock));

            }



          if(!product.getProductImages().equals(""))  banner_array_list.add(product.getProductImages());
            if(!product.getImage1().equals("")) banner_array_list.add(product.getImage1());
            if(!product.getImage2().equals(""))  banner_array_list.add(product.getImage2());
            if(!product.getImage3().equals(""))   banner_array_list.add(product.getImage3());

            binding.layoutProduct.setVisibility(View.GONE);
            binding.imageSlider.setVisibility(View.VISIBLE);
            binding.tvAdd.setVisibility(View.GONE);

            adapter = new ProductImageSliderAdapterCopy(ViewProductAct.this, banner_array_list,ViewProductAct.this);
            binding.imageSlider.setSliderAdapter(adapter);
            binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
            binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
            //  binding.imageSlider.setIndicatorSelectedColor(R.color.colorPrimary);
            //  binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
            binding.imageSlider.setScrollTimeInSec(3);
            binding.imageSlider.setAutoCycle(true);
            binding.imageSlider.startAutoCycle();
        }




        binding.switchDelivery.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    binding.switchDelivery.setChecked(true);
                    deliveryCharges = "1";
                }
                else {
                    binding.switchDelivery.setChecked(false);
                    deliveryCharges = "0";
                }
            }
        });

        binding.ivBack.setOnClickListener(v -> finish());

        binding.layoutProduct.setOnClickListener(view -> {
            //showPic();
        });


    }







    public void observeResponse() {
        viewProductViewModel.isResponse.observe(ViewProductAct.this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.ADDED_PRODUCT_SUBCAT) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response add shop product===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                selectSubCateModelArrayList.clear();
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for (int i =0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    SelectSubCateModel subCateModel = new SelectSubCateModel(object.getString("id"),object.getString("sub_category_name"));
                                    selectSubCateModelArrayList.add(subCateModel);
                                }

                                binding.rvSelectSubCate.setVisibility(View.VISIBLE);

                                selectSubCateAdapter = new ViewSelectSubCatAdapter(ViewProductAct.this, selectSubCateModelArrayList, ViewProductAct.this);
                                binding.rvSelectSubCate.setAdapter(selectSubCateAdapter);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(ViewProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ViewProductAct.this);
                            }


                        } else {
                            Toast.makeText(ViewProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.ADDED_PRODUCT_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response add shop product===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                AttributeModel attributeModel =new Gson().fromJson(stringResponse, AttributeModel.class);
                                selectAttributeFinalArrayList.clear();
                                selectAttributeFinalArrayList.addAll(attributeModel.getResult());
                                binding.rvAttri.setVisibility(View.VISIBLE);
                                ViewVariationAdapter variationAdapter = new ViewVariationAdapter(ViewProductAct.this,selectAttributeFinalArrayList);
                                binding.rvAttri.setAdapter(variationAdapter);
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(ViewProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ViewProductAct.this);
                            }

                        } else {
                            Toast.makeText(ViewProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        });
    }


    private void observeLoader() {
        viewProductViewModel.isLoading.observe(ViewProductAct.this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(ViewProductAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }



    public void showPic() {
        final Dialog dialog = new Dialog(ViewProductAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_product_pic);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutLogo = (LinearLayout) dialog.findViewById(R.id.layoutLogo);
        LinearLayout layout1Shop = (LinearLayout) dialog.findViewById(R.id.layout1Shop);
        ivLogo =  dialog.findViewById(R.id.ivLogo);
        iv1Pic = dialog.findViewById(R.id.iv1Pic);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        TextView btnOk = dialog.findViewById(R.id.btnOk);


        if(!str_image_path.equals("")){
            Glide.with(ViewProductAct.this)
                    .load(str_image_path)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            if(!product.getImage1().equals("")) {
                Glide.with(ViewProductAct.this)
                        .load(product.getImage1())
                        .centerCrop()
                        .into(ivLogo);
            }
            else {
                Glide.with(ViewProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo);
            }
        }


        if(!str_image_path1.equals("")){
            Glide.with(ViewProductAct.this)
                    .load(str_image_path1)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {

            if(!product.getImage2().equals("")){
                Glide.with(ViewProductAct.this)
                        .load(product.getImage2())
                        .centerCrop()
                        .into(iv1Pic);
            }else {
                Glide.with(ViewProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv1Pic);
            }

        }







        btnOk.setOnClickListener(v -> {
            banner_array_list.clear();
            if(!str_image_path.equals("")) banner_array_list.add(str_image_path);
            if(!str_image_path1.equals("")) banner_array_list.add(str_image_path1);
            //  else if(!str_image_path2.equals("")) banner_array_list.add(str_image_path2);

            if(banner_array_list.size()!=0) {
                binding.layoutProduct.setVisibility(View.GONE);
                binding.imageSlider.setVisibility(View.VISIBLE);
                binding.tvAdd.setVisibility(View.GONE);

                adapter = new ProductImageSliderAdapterCopy(ViewProductAct.this, banner_array_list,ViewProductAct.this);
                binding.imageSlider.setSliderAdapter(adapter);
                binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                //  binding.imageSlider.setIndicatorSelectedColor(R.color.colorPrimary);
                //  binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                binding.imageSlider.setScrollTimeInSec(3);
                binding.imageSlider.setAutoCycle(true);
                binding.imageSlider.startAutoCycle();
            }
            else {
                binding.layoutProduct.setVisibility(View.VISIBLE);
                binding.tvAdd.setVisibility(View.VISIBLE);
                binding.imageSlider.setVisibility(View.GONE);


            }
            dialog.dismiss();

        });


        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        layoutLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "1";
                if (checkPermisssionForReadStorage())
                    showImageSelection();
            }
        });
        layout1Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "2";
                if (checkPermisssionForReadStorage())
                    showImageSelection();
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void showImageSelection() {

        final Dialog dialog = new Dialog(ViewProductAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }






    private void getPhotoFromGallary() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(ViewProductAct.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(ViewProductAct.this,
                        "com.afaryseller.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" /*+ timeStamp + "_"*/;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if(chk.equals("1")) str_image_path = image.getAbsolutePath();
        else if(chk.equals("2")) str_image_path1 = image.getAbsolutePath();
        else if(chk.equals("3")) {
            if(position.equals("0")) {
                str_image_path = image.getAbsolutePath();
                if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                banner_array_list.add(Integer.parseInt(position),str_image_path);
                adapter.notifyDataSetChanged();
            }
            else {
                str_image_path1 =image.getAbsolutePath();
                if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                banner_array_list.add(Integer.parseInt(position),str_image_path1);
                adapter.notifyDataSetChanged();
            }

        }

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", resultCode + "");
            if (requestCode == SELECT_FILE) {
                if(chk.equals("1")) {
                    str_image_path = DataManager.getInstance().getRealPathFromURI(ViewProductAct.this, data.getData());
                    Glide.with(ViewProductAct.this)
                            .load(str_image_path)
                            .centerCrop()
                            .into(ivLogo);
                }
                else if(chk.equals("2")) {
                    str_image_path1 = DataManager.getInstance().getRealPathFromURI(ViewProductAct.this, data.getData());
                    Glide.with(ViewProductAct.this)
                            .load(str_image_path1)
                            .centerCrop()
                            .into(iv1Pic);
                }

                else if(chk.equals("3")) {
                    if(position.equals("0")) {
                        str_image_path = DataManager.getInstance().getRealPathFromURI(ViewProductAct.this, data.getData());
                        if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                        banner_array_list.add(Integer.parseInt(position),str_image_path);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        str_image_path1 = DataManager.getInstance().getRealPathFromURI(ViewProductAct.this, data.getData());
                        if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                        banner_array_list.add(Integer.parseInt(position),str_image_path1);
                        adapter.notifyDataSetChanged();
                    }

                }

            } else if (requestCode == REQUEST_CAMERA) {
                if(chk.equals("1")) {
                    Glide.with(ViewProductAct.this)
                            .load(str_image_path)
                            .centerCrop()
                            .into(ivLogo);
                }
                else if(chk.equals("2")) {

                    Glide.with(ViewProductAct.this)
                            .load(str_image_path1)
                            .centerCrop()
                            .into(iv1Pic);
                }

            }
        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(ViewProductAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(ViewProductAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(ViewProductAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(ViewProductAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(ViewProductAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(ViewProductAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(ViewProductAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(ViewProductAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(ViewProductAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ViewProductAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
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
        this.position = position+"";
        if(image.equals("change")) showImageSelection();
    }
}