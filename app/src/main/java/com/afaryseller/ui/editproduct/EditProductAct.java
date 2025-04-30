package com.afaryseller.ui.editproduct;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityEditProductBinding;
import com.afaryseller.databinding.DialogAddRemoveAttributeBinding;
import com.afaryseller.databinding.DialogAttributesBinding;
import com.afaryseller.databinding.DialogBandBinding;
import com.afaryseller.databinding.DialogCategoryBinding;
import com.afaryseller.databinding.DialogSubCategoryBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.addproduct.UploadProductImageManager;
import com.afaryseller.ui.addproduct.adapter.SubAttributeAddedAdapter;
import com.afaryseller.ui.addproduct.adapter.VariationAddedAdapter;
import com.afaryseller.ui.addproduct.listener.UpdateSubAttributeListListener;
import com.afaryseller.ui.addproduct.model.AttributeAddedModel;
import com.afaryseller.ui.addproduct.model.SubAttributeModel;
import com.afaryseller.ui.editproduct.EditProductAct;
import com.afaryseller.ui.editproduct.EditProductAct;
import com.afaryseller.ui.addproduct.adapter.AttributeAdapter;
import com.afaryseller.ui.addproduct.adapter.BrandAdapter;
import com.afaryseller.ui.addproduct.adapter.MainCategoryAdapter;
import com.afaryseller.ui.addproduct.adapter.ProductImageSliderAdapter;
import com.afaryseller.ui.addproduct.adapter.ProductImageSliderAdapterCopy;
import com.afaryseller.ui.addproduct.adapter.SelectCategoryAdapter;
import com.afaryseller.ui.addproduct.adapter.SelectSubCateAdapter;
import com.afaryseller.ui.addproduct.adapter.SubAttributeAdapter;
import com.afaryseller.ui.addproduct.adapter.SubCategoryAdapter;
import com.afaryseller.ui.addproduct.adapter.VariationAdapter;
import com.afaryseller.ui.addproduct.listener.ImageChangeListener;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.BrandModel;
import com.afaryseller.ui.addproduct.model.MainCateModel;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;
import com.afaryseller.ui.addproduct.model.SubCatModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;
import com.afaryseller.ui.shopdetails.ShopDetailsFragment;
import com.afaryseller.ui.shopdetails.model.ShopDetailModel;
import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.ui.subscription.SubscriptionAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProductAct  extends BaseActivity<ActivityEditProductBinding, EditProductViewModel> implements AddDateLister, ImageChangeListener, UpdateSubAttributeListListener, VariationListener {
    ActivityEditProductBinding binding;
    EditProductViewModel editProductViewModel;
    ArrayList<MainCateModel.Result> mainCateList;
    ArrayList<SubCatModel.Result> subCateArrayList;
    ArrayList<String> subCatIdArrayList;
    ArrayList<SelectSubCateModel> selectSubCateModelArrayList;
    ArrayList<SelectSubCateModel> selectCategoryModelArrayList;
    ArrayList<AttributeAddedModel.Result>attributeArrayList;
    ArrayList<AttributeModel.Result>selectedAttributeArrayList;
    ArrayList<AttributeAddedModel.Result>selectAttributeFinalArrayList;
    ArrayList<SubAttributeModel.Result>subAttributeArrayList;
    ArrayList<SelectSubCateModel>selectSubAttributeArrayList;
    ArrayList<String>categoryList = new ArrayList<>();
    ArrayList<String>subCategoryList = new ArrayList<>();
    ArrayList<String> numberList = new ArrayList<>();


    AttributeAddedModel.Result data;
    MutableLiveData<List<AttributeModel.Result>> mutableAttributeArrayList;


    ArrayList<BrandModel.Result> brandArrayList;
    String shopId = "", catId = "",brandId="",validateId="",position="",page="",productQty="";
    BrandAdapter brandAdapter;
    AttributeAdapter attributeAdapter;
    SubAttributeAddedAdapter subAttributeAdapter;




    MainCategoryAdapter mainCategoryAdapter;
    SubCategoryAdapter subCategoryAdapter;
    SelectSubCateAdapter selectSubCateAdapter;
    SelectCategoryAdapter selectCategoryAdapter;
    Dialog categoryDialog;
    Dialog subCateDialog;
    public static TextView tvNotFound,tvNotFoundAttribute;


    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    CircleImageView ivLogo,iv1Pic,ivLogo1,iv1Pic1;
    private String chk;
    private static String str_image_path="",str_image_path1="",str_image_path3="",str_image_path4="",productId="",shopIds="",deliveryCharges="0",inStock="No",selectsubCate;
    public JSONArray ja;
    ArrayList<BitmapUrlModel>banner_array_list;

    ProductImageSliderAdapterTwo adapter;
    ShopDetailModel.Result.Product product;

  //  public static Bitmap oneBitmap=null,twoBitmap=null;
    public static    Bitmap oneBitmap=null,twoBitmap=null,threeBitmap=null,fourBitmap=null;

    String NoImages ="";


    String currency="",shopName="";

    ArrayList<AttributeSubAttributeModel.Result>selectAttributeSubAttributeArrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_product);
        editProductViewModel = new EditProductViewModel();
        binding.setEditProductViewModel(editProductViewModel);
        binding.getLifecycleOwner();
        editProductViewModel.init(EditProductAct.this);

        for (int i = 1; i <= 50; i++) {
            numberList.add(String.valueOf(i)); // Convert the number to a String and add it to the ArrayList
        }

        initViews();

        observeLoader();
        observeResponse();

        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        editProductViewModel.getMainCategory(EditProductAct.this,headerMap);
        getBrands();
        getAddedSubCat();
       // getAddedAttribute();
        getAddedAttributeSubAttribute();
    }

    private void getAddedSubCat() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("pro_id", productId);
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        editProductViewModel.getAddedSubCategory(EditProductAct.this,headerMap, map);
    }

    private void getAddedAttribute() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("pro_id", productId);
        map.put("seller_id", DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        map.put("product_id", productId);
        editProductViewModel.getAddedAttribute(EditProductAct.this,headerMap, map);
    }


    private void getBrands() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        editProductViewModel.getBrand(EditProductAct.this,headerMap, map);
    }

    private void addBrands(String name, EditText editText) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("name", name);
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        editProductViewModel.addBrand(EditProductAct.this,headerMap, map);
        editText.setText("");
    }

    private void getAttribute() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        map.put("product_id", productId);
        editProductViewModel.getAttribute(EditProductAct.this,headerMap, map);
    }


    private void addAttribute(String name, EditText editText) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("name", name);
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        map.put("product_id",productId);
        editProductViewModel.addAttribute(EditProductAct.this,headerMap, map);
        editText.setText("");
    }


    private void getSubAttribute(String validate) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("validate_id", validate);
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        map.put("product_id", productId);
        editProductViewModel.getSubAttribute(EditProductAct.this,headerMap, map);
    }


    private void addSubAttribute(String name,String validateId, EditText editText) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("validate_id", validateId);
        map.put("name", name);
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        map.put("product_id",productId);


        editProductViewModel.addSubAttribute(EditProductAct.this,headerMap, map);
        editText.setText("");
    }



    private void getSubCate(String catId) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("cat_id", catId);
        map.put("user_seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("seller_register_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());

        editProductViewModel.getMainSubCategory(EditProductAct.this,headerMap, map);
        // editText.setText("");
    }



    private void getAddedAttributeSubAttribute() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id",DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        map.put("product_id",productId);

        editProductViewModel.getAddedAttributeSubAttribute(EditProductAct.this,headerMap, map);
        // editText.setText("");
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
        selectAttributeSubAttributeArrayList =new ArrayList<>();

        if (getIntent() != null) {
            product = (ShopDetailModel.Result.Product) getIntent().getSerializableExtra("data");
            Log.e("product image 1===",product.getProductImages());
            Log.e("product image 2===",product.getImage1());
            Log.e("product image 3===",product.getImage2());
            Log.e("product image 4===",product.getImage3());

            NoImages = getIntent().getStringExtra("images");
            shopName = getIntent().getStringExtra("shop_name");
            shopId =  getIntent().getStringExtra("shopId");
            productId =   product.getProId(); // getIntent().getStringExtra("shopId");
            if(getIntent().getStringExtra("currency").equals("XAF") || getIntent().getStringExtra("currency").equals("XOF")) currency = "FCFA";
            else currency =   getIntent().getStringExtra("currency");
         //   currency = getIntent().getStringExtra("currency");
         //   shop_name = getIntent().getStringExtra("shop_name");
          //  shopIds = shopId;
            binding.nameProduct.setText(product.getProductName());
            binding.edSellingPrice.setText(product.getProductPrice());
            binding.edDescription.setText(product.getProductDetails());
            binding.edSKU.setText(product.getSku());
            binding.tvBrand.setVisibility(View.VISIBLE);
            binding.tvBrand.setText(product.getProductBrand());


            binding.tvCurrency.setText(currency);
            productQty = product.getProductQut();
            binding.tvAvailableProduct.setText(product.getProductQut());

            if(Integer.parseInt(productQty)<=5){
                alertProductQuantity();
            }



            selectCategoryModelArrayList.clear();
            SelectSubCateModel hh = new SelectSubCateModel(product.getCategoryId(),product.getCategoryName());
            selectCategoryModelArrayList.add(hh);
            if(selectCategoryModelArrayList.size()!=0)   binding.layoutCategory.setVisibility(View.VISIBLE);
            selectCategoryAdapter = new SelectCategoryAdapter(EditProductAct.this, selectCategoryModelArrayList, EditProductAct.this);
            binding.rvSelectCate.setAdapter(selectCategoryAdapter);

           /* if(product.getDeliveryCharges().equals("0")) binding.switchDelivery.setChecked(false);
             else binding.switchDelivery.setChecked(true);*/

            if(product.getDeliveryCharges().equals("1")) binding.switchDelivery.setChecked(false);
            else binding.switchDelivery.setChecked(true);

            if(product.getInStock().equals("No")) {
                binding.switchProductStock.setChecked(false);
                binding.switchProductStock.setText(getString(R.string.product_out_of_stock));
                inStock = "No";

            }
            else {
                binding.switchProductStock.setChecked(true);
                binding.switchProductStock.setText(getString(R.string.in_stock));
                inStock = "Yes";

            }

            if(NoImages.equals("1")){
                banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Url",null));
            }
            else if(NoImages.equals("2")){
                banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Url",null));
                banner_array_list.add(new BitmapUrlModel(product.getImage1(),"Url",null));
            }
            else if(NoImages.equals("3")){
                banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Url",null));
                banner_array_list.add(new BitmapUrlModel(product.getImage1(),"Url",null));

                // change krna hai av
                banner_array_list.add(new BitmapUrlModel(product.getImage2(),"Url",null));
            }

            else if(NoImages.equals("4")){
                banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Url",null));
                banner_array_list.add(new BitmapUrlModel(product.getImage1(),"Url",null));

                ///// change krna hai av
                banner_array_list.add(new BitmapUrlModel(product.getImage2(),"Url",null));
                banner_array_list.add(new BitmapUrlModel(product.getImage3(),"Url",null));
            }



            binding.layoutProduct.setVisibility(View.GONE);
            binding.imageSlider.setVisibility(View.VISIBLE);
            binding.tvAdd.setVisibility(View.GONE);

            adapter = new ProductImageSliderAdapterTwo(EditProductAct.this, banner_array_list,EditProductAct.this);
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


        binding.switchProductStock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked==true){
                    binding.switchProductStock.setChecked(true);
                    binding.switchProductStock.setText(getString(R.string.in_stock));
                    inStock = "Yes";
                }
                else {
                    binding.switchProductStock.setChecked(false);
                    binding.switchProductStock.setText(getString(R.string.product_out_of_stock));

                    inStock = "No";
                }
            }
        });





        binding.btnAddCategory.setOnClickListener(v -> {
            categoryDialog(EditProductAct.this);
        });

        binding.btnBrand.setOnClickListener(v -> {
            brandDialog(EditProductAct.this);
        });

        binding.btnAttributes.setOnClickListener(v -> {
            attributeDialog(EditProductAct.this);
        });

        binding.ivBack.setOnClickListener(v -> finish());

        binding.layoutProduct.setOnClickListener(view -> {
           // showPic();

            if(NoImages.equalsIgnoreCase("1")) show1Pic();
            else if(NoImages.equalsIgnoreCase("2"))show2Pic();
            else if(NoImages.equalsIgnoreCase("3"))show3Pic();
            else if(NoImages.equalsIgnoreCase("4")) show4Pic();
        });

        binding.btnAddProduct.setOnClickListener(v -> {
            validation();
        });

        binding.tvAvailableProduct.setOnClickListener(v -> {
            if(!numberList.isEmpty()){
                showDropDownProductQty(v,binding.tvAvailableProduct,numberList);
            }
        });


    }

    private void alertProductQuantity() {
            AlertDialog.Builder builder = new AlertDialog.Builder(EditProductAct.this);
            builder.setMessage(getString(R.string.dear_partner_your_product_is_almost_out_of_stock_do_you_want_to_increase_the_stock))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();

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

    private void validation() {
        for (int i = 0;i<selectCategoryModelArrayList.size();i++){
            categoryList.add(selectCategoryModelArrayList.get(i).getId());
        }
        for (int i = 0;i<selectSubCateModelArrayList.size();i++){
            subCategoryList.add(selectSubCateModelArrayList.get(i).getId());
        }

        try {
            ja = new JSONArray();
            for (int i=0;i<selectAttributeFinalArrayList.size();i++){
                JSONObject jo = new JSONObject();
               // jo.put("id", selectAttributeFinalArrayList.get(i).getId());
                jo.put("name", selectAttributeFinalArrayList.get(i).getName());
                JSONArray  dataArray = new JSONArray();
                for (int j=0;j<selectAttributeFinalArrayList.get(i).getModelList().size();j++){
                    JSONObject joName = new JSONObject();
                  //  joName.put("id",selectAttributeFinalArrayList.get(i).getModelList().get(j).getId());
                    joName.put("dataname",selectAttributeFinalArrayList.get(i).getModelList().get(j).getName());
                    dataArray.put(joName);
                }
                jo.put("dataArray", dataArray);
                ja.put(jo);
            }
            Log.e("JsonArray====",ja.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (binding.nameProduct.getText().toString().trim().isEmpty()) {
            binding.nameProduct.setError(getString(R.string.can_not_empty));
            binding.nameProduct.setFocusable(true);
        }

        else if (productQty.equals("")) {
            Toast.makeText(EditProductAct.this, getString(R.string.please_select_number_of_available_product), Toast.LENGTH_SHORT).show();
        }

        else if (binding.edSellingPrice.getText().toString().trim().isEmpty()) {
            binding.edSellingPrice.setError(getString(R.string.can_not_empty));
            binding.edSellingPrice.setFocusable(true);
        }

        else if (binding.edDescription.getText().toString().trim().isEmpty()) {
            binding.edDescription.setError(getString(R.string.can_not_empty));
            binding.edDescription.setFocusable(true);
        }

        else if (binding.edSKU.getText().toString().trim().isEmpty()) {
            binding.edSKU.setError(getString(R.string.can_not_empty));
            binding.edSKU.setFocusable(true);
        }

        else if (subCategoryList.size()==0) {
            Toast.makeText(EditProductAct.this, getString(R.string.please_add_sub_category), Toast.LENGTH_SHORT).show();
        }

     /*   else if (binding.tvBrand.getText().toString().equals("")) {
            Toast.makeText(EditProductAct.this, getString(R.string.please_add_brand), Toast.LENGTH_SHORT).show();

        }

        else if (selectAttributeFinalArrayList.size()==0) {
            Toast.makeText(EditProductAct.this, getString(R.string.please_add_attribute), Toast.LENGTH_SHORT).show();
        }*/

       /* else if (str_image_path.isEmpty()) {
            Toast.makeText(EditProductAct.this, getString(R.string.please_add_1st_product_image), Toast.LENGTH_SHORT).show();
        }
        else if (str_image_path1.isEmpty()) {
            Toast.makeText(EditProductAct.this, getString(R.string.please_add_2nd_product_image), Toast.LENGTH_SHORT).show();
        }*/


        else {
            EditShopProduct();
        }


    }


    private void EditShopProduct() {
        //  attribute:[ { "name": "nee","dataArray": [{ "dataname": "n" }, { "dataname": "ne" }] }, { "name": "tum", "dataArray": [ { "dataname": "t" }, { "dataname": "tu" } ] }]
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        MultipartBody.Part filePart,filePart2,filePart3,filePart4;
        if (oneBitmap!=null) {
            //  File file1 = persistImage(oneBitmap,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),AddProductAct.this);//DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            // str_image_path = DataManager.compressImage(getActivity(),str_image_path);
            //File file = new File(str_image_path);
            //  filePart = MultipartBody.Part.createFormData("image", file1.getName(), RequestBody.create(MediaType.parse("image"), file1));


            String uniqueFileName = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString();
            File file1 = persistImage(oneBitmap, uniqueFileName, EditProductAct.this);
            filePart = MultipartBody.Part.createFormData("image", file1.getName(), RequestBody.create(MediaType.parse("image/*"), file1));



        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        if (twoBitmap!=null) {
            // File file2 = persistImage(twoBitmap,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),AddProductAct.this);//DataManager.getInstance().saveBitmapToFile(new File(str_image_path1));
            // str_image_path1 = DataManager.compressImage(getActivity(),str_image_path1);
            // File file =  //new File(str_image_path);
            // filePart2 = MultipartBody.Part.createFormData("image_1", file2.getName(), RequestBody.create(MediaType.parse("image_1"), file2));

            String uniqueFileName = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString();
            File file2 = persistImage(twoBitmap, uniqueFileName, EditProductAct.this);
            filePart2 = MultipartBody.Part.createFormData("image_1", file2.getName(), RequestBody.create(MediaType.parse("image_1/*"), file2));

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }


        if (threeBitmap!=null) {
            // File file3 = persistImage(threeBitmap,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),AddProductAct.this);//DataManager.getInstance().saveBitmapToFile(new File(str_image_path1));
            // str_image_path1 = DataManager.compressImage(getActivity(),str_image_path1);
            // File file =  //new File(str_image_path);
            // filePart3 = MultipartBody.Part.createFormData("image_2", file3.getName(), RequestBody.create(MediaType.parse("image_2"), file3));

            String uniqueFileName = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString();
            File file3 = persistImage(threeBitmap, uniqueFileName, EditProductAct.this);
            filePart3 = MultipartBody.Part.createFormData("image_2", file3.getName(), RequestBody.create(MediaType.parse("image_2/*"), file3));


        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart3 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }


        if (fourBitmap!=null) {
            // File file4 = persistImage(fourBitmap,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),AddProductAct.this);//DataManager.getInstance().saveBitmapToFile(new File(str_image_path1));
            // str_image_path1 = DataManager.compressImage(getActivity(),str_image_path1);
            // File file =  //new File(str_image_path);
            //   filePart4 = MultipartBody.Part.createFormData("image_3", file4.getName(), RequestBody.create(MediaType.parse("image_3"), file4));
            String uniqueFileName = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString();
            File file4 = persistImage(fourBitmap, uniqueFileName, EditProductAct.this);
            filePart4 = MultipartBody.Part.createFormData("image_3", file4.getName(), RequestBody.create(MediaType.parse("image_3/*"), file4));

        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart4 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

      //  SpannableString contentText = (SpannableString) binding.edDescription.getText();
     //   String htmlEncodedString = Html.toHtml(contentText);


        RequestBody proId = RequestBody.create(MediaType.parse("text/plain"),productId);
        RequestBody productName = RequestBody.create(MediaType.parse("text/plain"),binding.nameProduct.getText().toString());
        RequestBody sellingPrice = RequestBody.create(MediaType.parse("text/plain"),binding.edSellingPrice.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"),binding.edDescription.getText().toString());
        RequestBody sku = RequestBody.create(MediaType.parse("text/plain"),binding.edSKU.getText().toString());
        RequestBody deliveryCharge = RequestBody.create(MediaType.parse("text/plain"),deliveryCharges);
        RequestBody inStocks = RequestBody.create(MediaType.parse("text/plain"),inStock);

        RequestBody categoryId = RequestBody.create(MediaType.parse("text/plain"),commaSeprated(categoryList));
        RequestBody subcategory = RequestBody.create(MediaType.parse("text/plain"),commaSeprated(subCategoryList));

        RequestBody brand = RequestBody.create(MediaType.parse("text/plain"),binding.tvBrand.getText().toString());
        RequestBody attribute = RequestBody.create(MediaType.parse("text/plain"),ja.toString());

        RequestBody registerId = RequestBody.create(MediaType.parse("text/plain"),DataManager.getInstance().getUserData(EditProductAct.this).getResult().getRegisterId());
        RequestBody userSellerId = RequestBody.create(MediaType.parse("text/plain"),DataManager.getInstance().getUserData(EditProductAct.this).getResult().getId());
        RequestBody qty = RequestBody.create(MediaType.parse("text/plain"),productQty );



        editProductViewModel.editShopProduct(EditProductAct.this,headerMap, proId,productName,sellingPrice,description,sku,deliveryCharge,
                inStocks,categoryId,subcategory,brand,attribute,registerId,userSellerId,qty,filePart,filePart2,filePart3,filePart4);
    }


    public String commaSeprated(ArrayList<String> arrayList) {
        StringBuilder result = new StringBuilder();
        for (String string : arrayList) {
            result.append(string);
            result.append(",");
        }
        if(arrayList.size()==0)  selectsubCate = "";
        return result.length() > 0 ? /*result.substring(0, result.length() - 1)*/result.deleteCharAt(result.length() - 1).toString() : "";
    }


    public void observeResponse() {
        editProductViewModel.isResponse.observe(EditProductAct.this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_MAIN_CATEGORY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get main category===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                MainCateModel mainCateModel = new Gson().fromJson(stringResponse, MainCateModel.class);
                                mainCateList.clear();
                                mainCateList.addAll(mainCateModel.getResult());


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.GET_MAIN_SUB_CATEGORY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get main category===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                SubCatModel subCatModel = new Gson().fromJson(stringResponse, SubCatModel.class);
                                subCateArrayList.clear();


                                subCateArrayList.addAll(subCatModel.getResult());

                                if (selectSubCateModelArrayList.size() != 0) {
                                    for (int i = 0; i < subCateArrayList.size(); i++) {
                                        for (int j = 0; j < selectSubCateModelArrayList.size(); j++) {
                                            if (selectSubCateModelArrayList.get(j).getName().equals(subCateArrayList.get(i).getSubCatName()))
                                                subCateArrayList.get(i).setChk(true);
                                        }
                                    }
                                }

                                subCategoryDialog(EditProductAct.this);


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_BRAND) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get brands===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                BrandModel brandModel = new Gson().fromJson(stringResponse, BrandModel.class);
                                brandArrayList.clear();
                                brandArrayList.addAll(brandModel.getResult());
                                if(brandAdapter!=null)  brandAdapter.notifyDataSetChanged();


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                brandArrayList.clear();
                                brandAdapter.notifyDataSetChanged();
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }

                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.ADD_BRAND) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response add brand===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getBrands();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get brands===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                AttributeAddedModel attributeModel = new Gson().fromJson(stringResponse, AttributeAddedModel.class);
                                attributeArrayList.clear();
                                attributeArrayList.addAll(attributeModel.getResult());
                                Log.e("attribute list size===",attributeArrayList.size()+"");


                                if (selectAttributeFinalArrayList.size() != 0) {
                                    for (int i = 0; i < attributeArrayList.size(); i++) {
                                        for (int j = 0; j < selectAttributeFinalArrayList.size(); j++) {
                                            if (selectAttributeFinalArrayList.get(j).getName().equals(attributeArrayList.get(i).getName())) {
                                                attributeArrayList.get(i).setModelList(selectAttributeFinalArrayList.get(j).getModelList());
                                                attributeArrayList.get(i).setChk(true);
                                            }
                                        }
                                    }
                                }

                                Log.e("attribute list size===2",attributeArrayList.size()+"");



                                if(attributeAdapter!=null)  attributeAdapter.notifyDataSetChanged();
                                else Log.e("adapter nul a rha hai","=====");

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                attributeArrayList.clear();
                                attributeAdapter.notifyDataSetChanged();
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.ADD_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get brands===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getAttribute();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this,"create successfully.", Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }

                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SUB_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get brands===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                SubAttributeModel attributeModel = new Gson().fromJson(stringResponse, SubAttributeModel.class);
                                subAttributeArrayList.clear();
                                subAttributeArrayList.addAll(attributeModel.getResult());


                                if(subAttributeAdapter!=null)  subAttributeAdapter.notifyDataSetChanged();
                                //else subAttributeDialog(EditProductAct.this,data);


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                subAttributeArrayList.clear();
                                subAttributeAdapter.notifyDataSetChanged();
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }



                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.ADD_SUB_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get brands===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getSubAttribute(validateId);
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this,"create successfully.", Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.DELETE_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response delete attribute===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getAttribute();

/*
                                if (selectAttributeFinalArrayList.size() != 0) {
                                    for (int i = 0; i < attributeArrayList.size(); i++) {
                                        for (int j = 0; j < selectAttributeFinalArrayList.size(); j++) {
                                            if (selectAttributeFinalArrayList.get(j).getName().equals(attributeArrayList.get(i).getName())) {
                                                attributeArrayList.get(i).setModelList(selectAttributeFinalArrayList.get(j).getModelList());
                                                attributeArrayList.get(i).setChk(true);
                                            }
                                        }
                                    }
                                }
*/

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                attributeArrayList.clear();
                                attributeAdapter.notifyDataSetChanged();
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.DELETE_SUB_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response delete sub attribute===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);

                            if (jsonObject.getString("status").toString().equals("1")) {
                                if(page.equals("EditPage")){
                                    getAddedAttributeSubAttribute();
                                }else getSubAttribute(validateId);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                if(page.equals("EditPage")){
                                    getAddedAttributeSubAttribute();
                                }
                                else {
                                    subAttributeArrayList.clear();
                                    subAttributeAdapter.notifyDataSetChanged();
                                    Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }

                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.EDIT_SHOP_PRODUCT) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response add shop product===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                             //   oneBitmap =null;
                              //  twoBitmap =null;
                                JSONObject resObj = jsonObject.getJSONObject("result");

                                UploadProductImageManager.updateProductImages(EditProductAct.this,DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken()
                                        ,resObj.getString("pro_id"), oneBitmap, twoBitmap,threeBitmap,fourBitmap);
                               // productUploadDialog(resObj.getString("pro_id"));
                                oneBitmap=null;
                                twoBitmap=null;
                                threeBitmap=null;
                                fourBitmap=null;
                                Toast.makeText(EditProductAct.this, getString(R.string.product_edit_sucessfully), Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProductAct.this, ShopDetailsFragment.class)
                                        .putExtra("shop_id",shopId)
                                        .putExtra("name",shopName));
                                finish();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

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

                                selectSubCateAdapter = new SelectSubCateAdapter(EditProductAct.this, selectSubCateModelArrayList, EditProductAct.this);
                                binding.rvSelectSubCate.setAdapter(selectSubCateAdapter);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.ADDED_PRODUCT_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response added attribute product===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                AttributeAddedModel attributeModel =new Gson().fromJson(stringResponse, AttributeAddedModel.class);
                                selectAttributeFinalArrayList.clear();
                                selectAttributeFinalArrayList.addAll(attributeModel.getResult());
                                binding.rvAttri.setVisibility(View.VISIBLE);
                                VariationAddedAdapter variationAdapter = new VariationAddedAdapter(EditProductAct.this,selectAttributeFinalArrayList,EditProductAct.this);
                                binding.rvAttri.setAdapter(variationAdapter);
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }


                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ADDED_PRODUCT_ATTRIBUTE_SUB_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response get added attribute sub-attribute ===", stringResponse);
                            if (jsonObject.getString("status").equals("1")) {
                                AttributeSubAttributeModel attributeModel =new Gson().fromJson(stringResponse, AttributeSubAttributeModel.class);
                                selectAttributeSubAttributeArrayList.clear();
                                selectAttributeSubAttributeArrayList.addAll(attributeModel.getResult());
                                binding.rvAttri.setVisibility(View.VISIBLE);
                                AttributeSubAttributeAdapter attributeSubAttributeAdapter = new AttributeSubAttributeAdapter(EditProductAct.this,selectAttributeSubAttributeArrayList,EditProductAct.this);
                                binding.rvAttri.setAdapter(attributeSubAttributeAdapter);
                            } else if (jsonObject.getString("status").equals("0")) {
                                selectAttributeSubAttributeArrayList.clear();
                                AttributeSubAttributeAdapter attributeSubAttributeAdapter = new AttributeSubAttributeAdapter(EditProductAct.this,selectAttributeSubAttributeArrayList,EditProductAct.this);
                                binding.rvAttri.setAdapter(attributeSubAttributeAdapter);
                            }



                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.EDIT_PAGE_DELETE_SUB_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response delete sub attribute===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getAddedAttributeSubAttribute();
                              //  getAddedAttributeSubAttribute();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                getAddedAttributeSubAttribute();
                                //Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }

                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.CHECK_UNCHECK_VALIDATE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("response check uncheck validate===", dynamicResponseModel.getJsonObject().toString());
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getAttribute();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                               getAttribute();
                                //Toast.makeText(EditProductAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }

                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.CHECK_UNCHECK_ATTRIBUTE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("response check uncheck sub attribute===", dynamicResponseModel.getJsonObject().toString());
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                getSubAttribute(validateId);
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                getSubAttribute(validateId);
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(EditProductAct.this);
                            }

                        } else {
                            Toast.makeText(EditProductAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



            }
        });
    }


    private void observeLoader() {
        editProductViewModel.isLoading.observe(EditProductAct.this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(EditProductAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }



    public void showPic() {
        final Dialog dialog = new Dialog(EditProductAct.this);
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


        if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            if(!product.getImage1().equals("")) {
                Glide.with(EditProductAct.this)
                        .load(product.getImage1())
                        .centerCrop()
                        .into(ivLogo);
            }
            else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo);
            }
        }


        if(twoBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {

            if(!product.getImage2().equals("")){
                Glide.with(EditProductAct.this)
                        .load(product.getImage2())
                        .centerCrop()
                        .into(iv1Pic);
            }else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv1Pic);
            }

        }







        btnOk.setOnClickListener(v -> {
            banner_array_list.clear();
            if(oneBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Bitmap",oneBitmap));
            if(twoBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getImage1(),"Bitmap",twoBitmap));
            //  else if(!str_image_path2.equals("")) banner_array_list.add(str_image_path2);

            if(banner_array_list.size()!=0) {
                binding.layoutProduct.setVisibility(View.GONE);
                binding.imageSlider.setVisibility(View.VISIBLE);
                binding.tvAdd.setVisibility(View.GONE);

                adapter = new ProductImageSliderAdapterTwo(EditProductAct.this, banner_array_list,EditProductAct.this);
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
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });
        layout1Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "2";
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void showImageSelection() {

        final Dialog dialog = new Dialog(EditProductAct.this);
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
     /*   Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);*/
      //  Intent intent = new Intent(Intent.ACTION_PICK,  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    //    intent.setType("image/*");
     //   startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

        Intent intent = new Intent(Intent.ACTION_PICK,  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(EditProductAct.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(EditProductAct.this,
                        "com.afaryseller.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }

      //  Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     //   startActivityForResult(intent, REQUEST_CAMERA);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
    //    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    //    String imageFileName = "JPEG_" /*+ timeStamp + "_"*/;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
   //     File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
   //     File image = File.createTempFile(
   //             imageFileName,  /* prefix */
   //             ".jpg",         /* suffix */
    //            storageDir      /* directory */
   //     );

        // Save a file: path for use with ACTION_VIEW intents
      /*  if(chk.equals("1")) str_image_path = image.getAbsolutePath();
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

        }*/

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
        else if(chk.equals("3")) str_image_path3 = image.getAbsolutePath();
        else if(chk.equals("4")) str_image_path4 = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", resultCode + "");
         /*   if (requestCode == SELECT_FILE) {
                if(chk.equals("1")) {
                    str_image_path = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                    str_image_path = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                    try {
                        oneBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(EditProductAct.this)
                            .load(oneBitmap)
                            .centerCrop()
                            .into(ivLogo);
                }
                else if(chk.equals("2")) {
                    str_image_path1 = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                    try {
                        twoBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(EditProductAct.this)
                            .load(twoBitmap)
                            .centerCrop()
                            .into(iv1Pic);
                }

                else if(chk.equals("3")) {
                    if(position.equals("0")) {
                        str_image_path = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                        try {
                            oneBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                        banner_array_list.add(Integer.parseInt(position),new BitmapUrlModel(product.getProductImages(),"Bitmap",oneBitmap));
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        str_image_path1 = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                        try {
                            twoBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                        banner_array_list.add(Integer.parseInt(position),new BitmapUrlModel(product.getImage1(),"Bitmap",twoBitmap));
                        adapter.notifyDataSetChanged();
                    }

                }

            }
            else if (requestCode == REQUEST_CAMERA) {
                if(chk.equals("1")) {
                    oneBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",oneBitmap+"");
                    Glide.with(EditProductAct.this)
                            .load(oneBitmap)
                            .centerCrop()
                            .into(ivLogo);
                }
                else if(chk.equals("2")) {
                    twoBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",twoBitmap+"");
                    Glide.with(EditProductAct.this)
                            .load(twoBitmap)
                            .centerCrop()
                            .into(iv1Pic);
                }

                else if(chk.equals("3")) {
                    if(position.equals("0")) {
                        oneBitmap = (Bitmap) data.getExtras().get("data");   //
                        Log.e("=======",oneBitmap+"");
                        if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                        banner_array_list.add(Integer.parseInt(position),new BitmapUrlModel(product.getProductImages(),"Bitmap",oneBitmap));
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        twoBitmap = (Bitmap) data.getExtras().get("data");   //
                        Log.e("=======",twoBitmap+"");
                        if(banner_array_list.size()!=0) banner_array_list.remove(Integer.parseInt(position));
                        banner_array_list.add(Integer.parseInt(position),new BitmapUrlModel(product.getImage1(),"Bitmap",twoBitmap));
                        adapter.notifyDataSetChanged();
                    }

                }

            }*/

            if (requestCode == SELECT_FILE) {
                if(chk.equals("1")) {
                    str_image_path = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                    try {
                        oneBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    Glide.with(EditProductAct.this)
                            .load(str_image_path)
                            .centerCrop()
                            .into(ivLogo);
                }
                else if(chk.equals("2")) {
                    str_image_path1 = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                    try {
                        twoBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(EditProductAct.this)
                            .load(str_image_path1)
                            .centerCrop()
                            .into(iv1Pic);
                }

                else if(chk.equals("3")) {
                    str_image_path3 = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                    try {
                        threeBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(EditProductAct.this)
                            .load(str_image_path3)
                            .centerCrop()
                            .into(ivLogo1);
                }

                else if(chk.equals("4")) {
                    str_image_path4 = DataManager.getInstance().getRealPathFromURI(EditProductAct.this, data.getData());
                    try {
                        fourBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(EditProductAct.this)
                            .load(str_image_path4)
                            .centerCrop()
                            .into(iv1Pic1);
                }


/*
                else if(chk.equals("3")) {
                   if(position.equals("0")) {
                       str_image_path = DataManager.getInstance().getRealPathFromURI(AddProductAct.this, data.getData());
                       try {
                           oneBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }


                       banner_array_list.add(Integer.parseInt(position),oneBitmap);
                       adapter.notifyDataSetChanged();
                   }
                   else {
                       str_image_path1 = DataManager.getInstance().getRealPathFromURI(AddProductAct.this, data.getData());
                       try {
                           twoBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                       } catch (IOException e) {
                           throw new RuntimeException(e);
                       }

                       banner_array_list.add(Integer.parseInt(position),twoBitmap);
                       adapter.notifyDataSetChanged();
                   }

                }
*/

            }
            else if (requestCode == REQUEST_CAMERA) {
                if(chk.equals("1")) {
                  /*  oneBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",oneBitmap+"");
                    Glide.with(AddProductAct.this)
                            .load(oneBitmap)
                            .centerCrop()
                            .into(ivLogo);*/


                    Glide.with(EditProductAct.this)
                            .asBitmap()
                            .load(str_image_path)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    oneBitmap = resource;
                                    ivLogo.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });


                }
                else if(chk.equals("2")) {
                    /*twoBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",twoBitmap+"");
                    Glide.with(AddProductAct.this)
                            .load(twoBitmap)
                            .centerCrop()
                            .into(iv1Pic);*/


                    Glide.with(EditProductAct.this)
                            .asBitmap()
                            .load(str_image_path1)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    twoBitmap = resource;
                                    iv1Pic.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });

                }

                else if(chk.equals("3")) {
                   /* threeBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",threeBitmap+"");
                    Glide.with(AddProductAct.this)
                            .load(threeBitmap)
                            .centerCrop()
                            .into(ivLogo1);*/

                    Glide.with(EditProductAct.this)
                            .asBitmap()
                            .load(str_image_path3)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    threeBitmap = resource;
                                    ivLogo1.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });
                }

                else if(chk.equals("4")) {
                    /*fourBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",fourBitmap+"");
                    Glide.with(AddProductAct.this)
                            .load(fourBitmap)
                            .centerCrop()
                            .into(iv1Pic1);*/

                    Glide.with(EditProductAct.this)
                            .asBitmap()
                            .load(str_image_path4)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    fourBitmap = resource;
                                    iv1Pic1.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });
                }

/*
                else if(chk.equals("3")) {
                    if(position.equals("0")) {
                        oneBitmap = (Bitmap) data.getExtras().get("data");
                        banner_array_list.add(Integer.parseInt(position),oneBitmap);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        twoBitmap =(Bitmap) data.getExtras().get("data");
                        banner_array_list.add(Integer.parseInt(position),twoBitmap);
                        adapter.notifyDataSetChanged();
                    }

                }
*/

            }

        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(EditProductAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(EditProductAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(EditProductAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProductAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(EditProductAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(EditProductAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(EditProductAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(EditProductAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    public boolean checkPermissionFor12Above() {
        if (ContextCompat.checkSelfPermission(EditProductAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(EditProductAct.this,
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED

        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(EditProductAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(EditProductAct.this,
                            Manifest.permission.READ_MEDIA_IMAGES)
            ) {


                ActivityCompat.requestPermissions(EditProductAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(EditProductAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);
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
                        Toast.makeText(EditProductAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProductAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }

            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(EditProductAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditProductAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }

        }
    }


    private void categoryDialog(Context mContext) {
        categoryDialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        DialogCategoryBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_category, null, false);
        categoryDialog.setContentView(dialogBinding.getRoot());


        dialogBinding.RRback.setOnClickListener(v -> categoryDialog.dismiss());

        mainCategoryAdapter = new MainCategoryAdapter(mContext, mainCateList, EditProductAct.this);
        dialogBinding.rvCategory.setAdapter(mainCategoryAdapter);
        categoryDialog.show();
    }

    private void subCategoryDialog(Context mContext) {
        subCateDialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        DialogSubCategoryBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_sub_category, null, false);
        subCateDialog.setContentView(dialogBinding.getRoot());


        dialogBinding.RRback.setOnClickListener(v -> subCateDialog.dismiss());

        dialogBinding.btnAdd.setOnClickListener(v -> {
            if (selectSubCateModelArrayList.size() == 0)
                Toast.makeText(mContext, "Please select at least one sub-category", Toast.LENGTH_SHORT).show();
            else {
                binding.layoutCategory.setVisibility(View.VISIBLE);
                binding.rvSelectSubCate.setVisibility(View.VISIBLE);

                selectSubCateAdapter = new SelectSubCateAdapter(mContext, selectSubCateModelArrayList, EditProductAct.this);
                binding.rvSelectSubCate.setAdapter(selectSubCateAdapter);

                selectCategoryAdapter = new SelectCategoryAdapter(mContext, selectCategoryModelArrayList, EditProductAct.this);
                binding.rvSelectCate.setAdapter(selectCategoryAdapter);

                categoryDialog.dismiss();
                subCateDialog.dismiss();
            }
        });


        subCategoryAdapter = new SubCategoryAdapter(mContext, subCateArrayList, EditProductAct.this);
        dialogBinding.rvSubCategory.setAdapter(subCategoryAdapter);

        subCateDialog.show();
    }


    private void brandDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        DialogBandBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_band, null, false);
        dialog.setContentView(dialogBinding.getRoot());


        dialogBinding.ivBack.setOnClickListener(v -> dialog.dismiss());

        tvNotFound = dialogBinding.tvNotFound;

        dialogBinding.ivAdd.setOnClickListener(v -> {
            if (dialogBinding.etBrand.getText().toString().equals(""))
                Toast.makeText(mContext, mContext.getString(R.string.please_enter_brand_name), Toast.LENGTH_SHORT).show();
            else {
                addBrands(dialogBinding.etBrand.getText().toString(), dialogBinding.etBrand);
            }
        });

        dialogBinding.btnAddBrand.setOnClickListener(v -> {
            if(brandId.equals("")) Toast.makeText(mContext, mContext.getString(R.string.please_select_brand), Toast.LENGTH_SHORT).show();
            else {
                binding.tvBrand.setVisibility(View.VISIBLE);
                binding.tvBrand.setText(brandId);
                dialog.dismiss();

            }
        });

        dialogBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                getFilterSearch(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        for (int i = 0; i < brandArrayList.size(); i++) {
            Log.e("check dialog time ====", brandArrayList.get(i).isChk() + "");
        }

        brandAdapter = new BrandAdapter(mContext, brandArrayList, EditProductAct.this);
        dialogBinding.rvBrand.setAdapter(brandAdapter);

        dialog.show();
    }



    private void attributeDialog(Context mContext) {
        Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        DialogAttributesBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_attributes, null, false);
        dialog.setContentView(dialogBinding.getRoot());

        tvNotFoundAttribute = dialogBinding.tvNotFoundAttribute;

        attributeArrayList.clear();
        attributeAdapter = new AttributeAdapter(mContext,attributeArrayList,EditProductAct.this);
        dialogBinding.rvAttribute.setAdapter(attributeAdapter);
        Log.e("attribute list size==",attributeArrayList.size()+"");

        getAttribute();

        dialogBinding.ivBack.setOnClickListener(v -> dialog.dismiss());



        dialogBinding.ivAdd.setOnClickListener(v -> {
            if (dialogBinding.etAttribute.getText().toString().equals(""))
                Toast.makeText(mContext, mContext.getString(R.string.please_enter_attribute), Toast.LENGTH_SHORT).show();
            else {
                addAttribute(dialogBinding.etAttribute.getText().toString(),dialogBinding.etAttribute);
            }
        });

        dialogBinding.btnAddAttribute.setOnClickListener(v -> {
/*
          //  selectAttributeFinalArrayList.clear();
          *//*  for(int i=0;i<attributeArrayList.size();i++){
                if(attributeArrayList.get(i).isChk()){
                    selectAttributeFinalArrayList.add(attributeArrayList.get(i)) ;
                }
            }*//*

            for(int i=0;i<attributeArrayList.size();i++){
                if(attributeArrayList.get(i).isChk()){
                    selectAttributeFinalArrayList.add(attributeArrayList.get(i)) ;
                  //  Log.e("=====", attributeArrayList.get(i).getModelList().size()+"");
                }
            }


           // Log.e("=====",DataManager.attributeArrayList.size() +"");

            if(selectAttributeFinalArrayList.size()==0) Toast.makeText(mContext, mContext.getString(R.string.please_select_attribute), Toast.LENGTH_SHORT).show();
            else {
                binding.rvAttri.setVisibility(View.VISIBLE);
                Log.e("variation list===", selectAttributeFinalArrayList.size()+"");

                VariationAddedAdapter variationAdapter = new VariationAddedAdapter(mContext,selectAttributeFinalArrayList,EditProductAct.this);
                binding.rvAttri.setAdapter(variationAdapter);

                dialog.dismiss();

            }*/


        /*    selectAttributeFinalArrayList.clear();

            for (int i = 0; i < attributeArrayList.size(); i++) {
                if (attributeArrayList.get(i).isChk()) {
                    AttributeAddedModel.Result attribute = attributeArrayList.get(i);

                    // Check if an item with the same id and name is already in the list
                    boolean exists = false;
                    for (int j = 0; j < selectAttributeFinalArrayList.size(); j++) {
                        if (selectAttributeFinalArrayList.get(j).getId().equals(attribute.getId()) &&
                                selectAttributeFinalArrayList.get(j).getName().equals(attribute.getName())) {
                            exists = true;
                            break;
                        }
                    }

                    // If the item does not exist, add it to the final list
                    if (!exists) {
                        selectAttributeFinalArrayList.add(attribute);
                    }
                }
            }

            if (selectAttributeFinalArrayList.size() == 0) {
                Toast.makeText(mContext, mContext.getString(R.string.please_select_attribute), Toast.LENGTH_SHORT).show();
            } else {
                binding.rvAttri.setVisibility(View.VISIBLE);
                Log.e("variation list===", selectAttributeFinalArrayList.size() + "");

                VariationAddedAdapter variationAdapter = new VariationAddedAdapter(mContext, selectAttributeFinalArrayList, EditProductAct.this);
                binding.rvAttri.setAdapter(variationAdapter);

                dialog.dismiss();
            }*/

            dialog.dismiss();
            getAddedAttributeSubAttribute();

        });

        dialogBinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                getFilterSearchAttribute(query.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        dialog.show();
    }

    private void subAttributeDialog(Context mContext,AttributeAddedModel.Result data,int position) {
        Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        DialogAddRemoveAttributeBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_add_remove_attribute, null, false);
        dialog.setContentView(dialogBinding.getRoot());


        dialogBinding.tvItemOptions.setText(data.getName() + " " +mContext.getString(R.string.options));
        subAttributeArrayList.clear();
        selectSubAttributeArrayList.clear();
        subAttributeAdapter = new SubAttributeAddedAdapter(mContext,subAttributeArrayList,EditProductAct.this);
        dialogBinding.rvSubAttribute.setAdapter(subAttributeAdapter);

        getSubAttribute(data.getId());

        dialogBinding.ivBack.setOnClickListener(v -> {
            subAttributeAdapter = null;
            dialog.dismiss();
        });



        dialogBinding.btnAdd.setOnClickListener(v -> {
            if (dialogBinding.etOptions.getText().toString().equals(""))
                Toast.makeText(mContext, mContext.getString(R.string.please_enter_option), Toast.LENGTH_SHORT).show();
            else {
                addSubAttribute(dialogBinding.etOptions.getText().toString(),data.getId(),dialogBinding.etOptions);
            }
        });

/*
        dialogBinding.btnDone.setOnClickListener(v -> {
            if(selectSubAttributeArrayList.size()==0) Toast.makeText(mContext, mContext.getString(R.string.please_select_variations), Toast.LENGTH_SHORT).show();
            else {
                attributeArrayList.get(position).setModelList(selectSubAttributeArrayList);
                Log.e("adding=====",position+"");
            }
            dialog.dismiss();
        });
*/

        dialogBinding.btnDone.setOnClickListener(v -> {
                /*    if (selectSubAttributeArrayList.size() == 0) {
                        Toast.makeText(mContext, mContext.getString(R.string.please_select_variations), Toast.LENGTH_SHORT).show();
                    } else {
                        List<SelectSubCateModel> currentModelList = attributeArrayList.get(position).getModelList();
                        if (currentModelList == null) {
                            currentModelList = new ArrayList<>();
                        }

                        // Make sure not to add duplicates
                        for (SelectSubCateModel subAttribute : selectSubAttributeArrayList) {
                            if (!currentModelList.contains(subAttribute)) {
                                currentModelList.add(subAttribute);
                            }
                        }

                        attributeArrayList.get(position).setChk(true);
                        attributeArrayList.get(position).setModelList(currentModelList);
                        attributeAdapter.notifyDataSetChanged();
                    }

            dialog.dismiss();

                }*/

           /* if (selectSubAttributeArrayList.size() == 0) {
                Toast.makeText(mContext, mContext.getString(R.string.please_select_variations), Toast.LENGTH_SHORT).show();
            } else {
                List<SelectSubCateModel> currentModelList = attributeArrayList.get(position).getModelList();
                if (currentModelList == null) {
                    currentModelList = new ArrayList<>();
                }

                // Iterate through selectSubAttributeArrayList and check for duplicates
                for (SelectSubCateModel subAttribute : selectSubAttributeArrayList) {
                    boolean isDuplicate = false;

                    // Check if the current item already exists by ID or name
                    for (SelectSubCateModel existingSubAttribute : currentModelList) {
                        if (existingSubAttribute.getId().equals(subAttribute.getId()) ||
                                existingSubAttribute.getName().equals(subAttribute.getName())) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    // Add to the list only if it's not a duplicate
                    if (!isDuplicate) {
                        currentModelList.add(subAttribute);
                    }
                }

                // Set the updated list and mark it as checked
                attributeArrayList.get(position).setChk(true);
                attributeArrayList.get(position).setModelList(currentModelList);
                attributeAdapter.notifyDataSetChanged();
            }

            dialog.dismiss();*/


            if (selectSubAttributeArrayList.size() == 0) {
                Toast.makeText(mContext, mContext.getString(R.string.please_select_variations), Toast.LENGTH_SHORT).show();
            } else {
                // Retrieve the current model list for the selected attribute position
                List<SelectSubCateModel> currentModelList = attributeArrayList.get(position).getModelList();

                // If currentModelList is null, initialize it
                if (currentModelList == null) {
                    currentModelList = new ArrayList<>();
                }

                // Clear the selectSubAttributeArrayList to ensure only checked items are added
                selectSubAttributeArrayList.clear();

                // Add only the checked items to selectSubAttributeArrayList
                for (int i = 0; i < subAttributeArrayList.size(); i++) {
                    if (subAttributeArrayList.get(i).isChk()) {
                        // Only add checked items
                        SelectSubCateModel subAttribute = new SelectSubCateModel(
                                subAttributeArrayList.get(i).getId(),
                                subAttributeArrayList.get(i).getName()
                        );
                        selectSubAttributeArrayList.add(subAttribute);
                    }
                }

                // Now, update currentModelList with only selected (checked) items
                // Clear the currentModelList first to avoid stale data (optional, depends on your logic)
                currentModelList.clear();

                // Add selected sub-attributes to currentModelList
                for (SelectSubCateModel subAttribute : selectSubAttributeArrayList) {
                    // Add to currentModelList only if it's not already added
                    boolean isDuplicate = false;

                    for (SelectSubCateModel existingSubAttribute : currentModelList) {
                        if (existingSubAttribute.getId().equals(subAttribute.getId()) ||
                                existingSubAttribute.getName().equals(subAttribute.getName())) {
                            isDuplicate = true;
                            break;
                        }
                    }

                    if (!isDuplicate) {
                        currentModelList.add(subAttribute);
                    }
                }

                // Update the attribute with the new model list and mark it as checked
                attributeArrayList.get(position).setChk(true);  // Mark the attribute as checked
                attributeArrayList.get(position).setModelList(currentModelList);  // Set the updated model list
                attributeAdapter.notifyDataSetChanged();  // Notify the adapter to refresh the view
            }

            // Dismiss the dialog after processing
            dialog.dismiss();

                });

        dialogBinding.btnDone.setOnClickListener(v -> {
            dialog.dismiss();
            getAddedAttributeSubAttribute();

        });


        dialog.show();
    }



    public void getFilterSearch(String query) {
        try {
            query = query.toLowerCase();

            final ArrayList<BrandModel.Result> filteredList = new ArrayList<BrandModel.Result>();

            if (brandArrayList != null) {
                for (int i = 0; i < brandArrayList.size(); i++) {
                    String text = brandArrayList.get(i).getName().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(brandArrayList.get(i));
                    }

                }
                brandAdapter.filterList(filteredList);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getFilterSearchAttribute(String query) {
        try {
            query = query.toLowerCase();

            final ArrayList<AttributeAddedModel.Result> filteredList = new ArrayList<AttributeAddedModel.Result>();

            if (attributeArrayList != null) {
                for (int i = 0; i < attributeArrayList.size(); i++) {
                    String text = attributeArrayList.get(i).getName().toLowerCase();
                    if (text.contains(query)) {
                        filteredList.add(attributeArrayList.get(i));
                    }

                }
                attributeAdapter.filterList(filteredList);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public void onDate(String date, int position, String tag, boolean chk) {
        if (tag.equals("brandCheck")) {
            brandId =   brandArrayList.get(position).getName();
            brandArrayList.get(position).setChk(chk);
            brandAdapter.notifyDataSetChanged();
            for (int i = 0; i < brandArrayList.size(); i++) {
                Log.e("check select time ====", brandArrayList.get(i).isChk() + "");
            }


        }

        else if (tag.equals("Cate")) {
            catId = mainCateList.get(position).getId();
            mainCateList.get(position).setChk(chk);
            mainCategoryAdapter.notifyDataSetChanged();

            if(mainCateList.get(position).isChk() == true) {
                if (selectCategoryModelArrayList.size()==0) selectCategoryModelArrayList.add(new SelectSubCateModel(mainCateList.get(position).getId(), mainCateList.get(position).getCategoryName()));
                else {
                    for (int i=0;i<selectCategoryModelArrayList.size();i++){
                        if (!selectCategoryModelArrayList.get(i).getName().equals(mainCateList.get(position).getCategoryName())){
                            selectCategoryModelArrayList.add(new SelectSubCateModel(mainCateList.get(position).getId(), mainCateList.get(position).getCategoryName()));
                        }

                    }
                }
            }
            getSubCate(catId);
        }

        else if (tag.equals("subCate")) {
            subCateArrayList.get(position).setChk(chk);
            subCategoryAdapter.notifyDataSetChanged();
            if (subCateArrayList.get(position).isChk() == true)
                selectSubCateModelArrayList.add(new SelectSubCateModel(subCateArrayList.get(position).getId(), subCateArrayList.get(position).getSubCatName()));

        }

        else if (tag.equals("removeSubCategory")) {
            for (int i = 0; i < subCateArrayList.size(); i++) {
                if (selectSubCateModelArrayList.get(position).getId().equals(subCateArrayList.get(i).getId()))
                    subCateArrayList.get(i).setChk(false);
            }
            if (selectSubCateModelArrayList.size() != 0) {
                selectSubCateModelArrayList.remove(position);
                selectSubCateAdapter.notifyDataSetChanged();
                if (selectSubCateModelArrayList.size() == 0) {
                    selectCategoryModelArrayList.clear();
                    selectSubCateModelArrayList.clear();
                    selectCategoryAdapter.notifyDataSetChanged();
                    selectSubCateAdapter.notifyDataSetChanged();
                    binding.layoutCategory.setVisibility(View.GONE);
                    binding.rvSelectSubCate.setVisibility(View.GONE);
                }
            } else {
                selectCategoryModelArrayList.clear();
                selectSubCateModelArrayList.clear();
                selectCategoryAdapter.notifyDataSetChanged();
                selectSubCateAdapter.notifyDataSetChanged();
                binding.layoutCategory.setVisibility(View.GONE);
                binding.rvSelectSubCate.setVisibility(View.GONE);

            }

        }

        else if (tag.equals("attribute")) {
          //  attributeArrayList.get(position).setChk(chk);
            validateId = attributeArrayList.get(position).getId();
            data = attributeArrayList.get(position);
            subAttributeDialog(EditProductAct.this,data,position);
        }

        else if (tag.equals("attributeCheck")) {
           // attributeArrayList.get(position).setChk(chk);
           /* if(!attributeArrayList.get(position).isChk()) {
                attributeArrayList.get(position).getModelList().clear();
            }*/
            attributeAdapter.notifyDataSetChanged();

            checkUncheckAttribute(attributeArrayList.get(position).getId(),chk);
        }

        else if (tag.equals("subAttribute")) {
          /*  selectSubAttributeArrayList.clear();
            subAttributeArrayList.get(position).setChk(chk);
            Log.e("sub attribute selection===",chk+"");
            subAttributeAdapter.notifyDataSetChanged();
            for(int i=0;i<subAttributeArrayList.size();i++) {
                Log.e("sub attribute selection===", subAttributeArrayList.get(i).isChk() + "");
                if (subAttributeArrayList.get(i).isChk()==true) {
                    selectSubAttributeArrayList.add(new SelectSubCateModel(subAttributeArrayList.get(i).getId(), subAttributeArrayList.get(i).getName()));

                }
            }*/

            checkUncheckSubAttribute(subAttributeArrayList.get(position).getId(),chk);


            Log.e("sub attribute selection size===", selectSubAttributeArrayList.size() + "");


        }

        else if (tag.equals("deleteAttribute")) {
            deleteAttributeDialog(attributeArrayList.get(position).getId());

        }

        else if (tag.equals("deleteSubAttribute")) {
            deleteSubAttributeDialog(subAttributeArrayList.get(position).getId());
        }
    }

    private void deleteSubAttributeDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductAct.this);
        builder.setMessage(R.string.are_you_sure_you_want_to_delete_this_sub_attribute)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        deleteSubAttribute(id);
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



    private void checkUncheckSubAttribute(String id,boolean chk) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("attribute_id", id);
       if(chk) map.put("status","1");
       else map.put("status","0");
        editProductViewModel.checkUncheckSubAttribute(EditProductAct.this,headerMap, map);
    }


    private void checkUncheckAttribute(String id,boolean chk) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("validate_id", id);
        if(chk) map.put("status","1");
        else map.put("status","0");
        editProductViewModel.checkUncheckAttribute(EditProductAct.this,headerMap, map);
    }






    private void deleteSubAttribute(String id) {
        page = "SubAttribute";
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("attribute_id", id);
        editProductViewModel.deleteSubAttribute(EditProductAct.this,headerMap, map);
    }

    private void deleteAttributeDialog(String id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProductAct.this);
        builder.setMessage(getString(R.string.are_you_sure_you_want_to_delete_this_attribute))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        deleteAttribute(id);
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

    private void deleteAttribute(String id) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("validate_id", id);
        editProductViewModel.deleteAttribute(EditProductAct.this,headerMap, map);

    }



    @Override
    public void onDate(String date, int position, String tag) {

    }


    @Override
    public void imageChange(int position, String image) {
        chk = "3";
        this.position = position+"";
       // if(image.equals("change")) showImageSelection();
        if(image.equals("change")) {
           // showImageSelection();

            if (NoImages.equalsIgnoreCase("1")) show1Pic();
            else if (NoImages.equalsIgnoreCase("2")) show2Pic();
            else if (NoImages.equalsIgnoreCase("3")) show3Pic();
            else if (NoImages.equalsIgnoreCase("4")) show4Pic();
        }
    }

    private  File persistImage(Bitmap bitmap, String name, Context cOntext) {
        File filesDir = cOntext.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "persistImage: "+e.getMessage() );
        }

        return  imageFile;

    }


    public void show1Pic() {

        final Dialog dialog = new Dialog(EditProductAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_product_pic_one);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutLogo = (LinearLayout) dialog.findViewById(R.id.layoutLogo);
        ivLogo =  dialog.findViewById(R.id.ivLogo);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        TextView btnOk = dialog.findViewById(R.id.btnOk);


       /* if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            Glide.with(EditProductAct.this)
                    .load(R.drawable.img_default)
                    .centerCrop()
                    .into(ivLogo);
        }*/


        if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            if(!product.getImage1().equals("")) {
                Glide.with(EditProductAct.this)
                        .load(product.getProductImages())
                        .centerCrop()
                        .into(ivLogo);
            }
            else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo);
            }
        }




        btnOk.setOnClickListener(v -> {
            banner_array_list.clear();
          //  if(oneBitmap!=null) banner_array_list.add(oneBitmap);

            if(oneBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Bitmap",oneBitmap));

            //  else if(!str_image_path2.equals("")) banner_array_list.add(str_image_path2);

            if(banner_array_list.size()!=0) {
                binding.layoutProduct.setVisibility(View.GONE);
                binding.imageSlider.setVisibility(View.VISIBLE);
                binding.tvAdd.setVisibility(View.GONE);

                adapter = new ProductImageSliderAdapterTwo(EditProductAct.this, banner_array_list,EditProductAct.this);
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
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void show2Pic() {
        final Dialog dialog = new Dialog(EditProductAct.this);
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


       /* if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            Glide.with(EditProductAct.this)
                    .load(R.drawable.img_default)
                    .centerCrop()
                    .into(ivLogo);
        }


        if(twoBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {
            Glide.with(EditProductAct.this)
                    .load(R.drawable.img_default)
                    .centerCrop()
                    .into(iv1Pic);
        }*/



        if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            if(!product.getProductImages().equals("")) {
                Glide.with(EditProductAct.this)
                        .load(product.getProductImages())
                        .centerCrop()
                        .into(ivLogo);
            }
            else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo);
            }
        }


        if(twoBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {

            if(!product.getImage1().equals("")){
                Glide.with(EditProductAct.this)
                        .load(product.getImage1())
                        .centerCrop()
                        .into(iv1Pic);
            }else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv1Pic);
            }

        }







        btnOk.setOnClickListener(v -> {
            banner_array_list.clear();
           // if(oneBitmap!=null) banner_array_list.add(oneBitmap);
           // if(twoBitmap!=null) banner_array_list.add(twoBitmap);
            //  else if(!str_image_path2.equals("")) banner_array_list.add(str_image_path2);

            if(oneBitmap!=null) banner_array_list.add(new BitmapUrlModel("","Bitmap",oneBitmap));
            else banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Url",oneBitmap));

            if(twoBitmap!=null) banner_array_list.add(new BitmapUrlModel("","Bitmap",twoBitmap));
            //  else if(!str_image_path2.equals("")) banner_array_list.add(str_image_path2);
            else banner_array_list.add(new BitmapUrlModel(product.getImage1(),"Url",twoBitmap));

            if(banner_array_list.size()!=0) {
                binding.layoutProduct.setVisibility(View.GONE);
                binding.imageSlider.setVisibility(View.VISIBLE);
                binding.tvAdd.setVisibility(View.GONE);

                adapter = new ProductImageSliderAdapterTwo(EditProductAct.this, banner_array_list,EditProductAct.this);
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
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });
        layout1Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "2";
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void show3Pic() {
        final Dialog dialog = new Dialog(EditProductAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_product_3_pic);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutLogo = (LinearLayout) dialog.findViewById(R.id.layoutLogo);
        LinearLayout layout1Shop = (LinearLayout) dialog.findViewById(R.id.layout1Shop);

        LinearLayout layoutLogo1 = (LinearLayout) dialog.findViewById(R.id.layoutLogo1);


        ivLogo =  dialog.findViewById(R.id.ivLogo);
        iv1Pic = dialog.findViewById(R.id.iv1Pic);
        ivLogo1 =  dialog.findViewById(R.id.ivLogo1);


        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        TextView btnOk = dialog.findViewById(R.id.btnOk);


    /*    if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            Glide.with(EditProductAct.this)
                    .load(R.drawable.img_default)
                    .centerCrop()
                    .into(ivLogo);
        }


        if(twoBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {
            Glide.with(EditProductAct.this)
                    .load(R.drawable.img_default)
                    .centerCrop()
                    .into(iv1Pic);
        }*/


        if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            if(!product.getImage1().equals("")) {
                Glide.with(EditProductAct.this)
                        .load(product.getProductImages())
                        .centerCrop()
                        .into(ivLogo);
            }
            else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo);
            }
        }


        if(twoBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {

            if(!product.getImage2().equals("")){
                Glide.with(EditProductAct.this)
                        .load(product.getImage1())
                        .centerCrop()
                        .into(iv1Pic);
            }else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv1Pic);
            }

        }

        if(threeBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(threeBitmap)
                    .centerCrop()
                    .into(ivLogo1);
        }
        else {

            if(!product.getImage2().equals("")){
                Glide.with(EditProductAct.this)
                        .load(product.getImage2())
                        .centerCrop()
                        .into(ivLogo1);
            }else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo1);
            }

        }






        btnOk.setOnClickListener(v -> {
            banner_array_list.clear();
          //  if(oneBitmap!=null) banner_array_list.add(oneBitmap);
         //   if(twoBitmap!=null) banner_array_list.add(twoBitmap);
         //   if(threeBitmap!=null) banner_array_list.add(threeBitmap);

            if(oneBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Bitmap",oneBitmap));
            if(twoBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getImage1(),"Bitmap",twoBitmap));
            if(threeBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getImage2(),"Bitmap",threeBitmap));



            //  else if(!str_image_path2.equals("")) banner_array_list.add(str_image_path2);

            if(banner_array_list.size()!=0) {
                binding.layoutProduct.setVisibility(View.GONE);
                binding.imageSlider.setVisibility(View.VISIBLE);
                binding.tvAdd.setVisibility(View.GONE);

                adapter = new ProductImageSliderAdapterTwo(EditProductAct.this, banner_array_list,EditProductAct.this);
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
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });
        layout1Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "2";
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });


        layoutLogo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "3";
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void show4Pic() {
        final Dialog dialog = new Dialog(EditProductAct.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_product_4_pic);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutLogo = (LinearLayout) dialog.findViewById(R.id.layoutLogo);
        LinearLayout layout1Shop = (LinearLayout) dialog.findViewById(R.id.layout1Shop);

        LinearLayout layoutLogo1 = (LinearLayout) dialog.findViewById(R.id.layoutLogo1);
        LinearLayout layout1Shop1 = (LinearLayout) dialog.findViewById(R.id.layout1Shop1);


        ivLogo =  dialog.findViewById(R.id.ivLogo);
        iv1Pic = dialog.findViewById(R.id.iv1Pic);
        ivLogo1 =  dialog.findViewById(R.id.ivLogo1);
        iv1Pic1 =  dialog.findViewById(R.id.iv1Pic1);


        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        TextView btnOk = dialog.findViewById(R.id.btnOk);


     /*   if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            Glide.with(EditProductAct.this)
                    .load(R.drawable.img_default)
                    .centerCrop()
                    .into(ivLogo);
        }


        if(twoBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {
            Glide.with(EditProductAct.this)
                    .load(R.drawable.img_default)
                    .centerCrop()
                    .into(iv1Pic);
        }*/


        if(oneBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }
        else {
            if(!product.getImage1().equals("")) {
                Glide.with(EditProductAct.this)
                        .load(product.getProductImages())
                        .centerCrop()
                        .into(ivLogo);
            }
            else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo);
            }
        }


        if(twoBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {

            if(!product.getImage2().equals("")){
                Glide.with(EditProductAct.this)
                        .load(product.getImage1())
                        .centerCrop()
                        .into(iv1Pic);
            }else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv1Pic);
            }

        }

        if(threeBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo1);
        }
        else {
            if(!product.getImage1().equals("")) {
                Glide.with(EditProductAct.this)
                        .load(product.getImage2())
                        .centerCrop()
                        .into(ivLogo1);
            }
            else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo1);
            }
        }


        if(fourBitmap!=null){
            Glide.with(EditProductAct.this)
                    .load(fourBitmap)
                    .centerCrop()
                    .into(iv1Pic1);
        }
        else {

            if(!product.getImage2().equals("")){
                Glide.with(EditProductAct.this)
                        .load(product.getImage3())
                        .centerCrop()
                        .into(iv1Pic1);
            }else {
                Glide.with(EditProductAct.this)
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv1Pic1);
            }

        }




        btnOk.setOnClickListener(v -> {
            banner_array_list.clear();
           // if(oneBitmap!=null) banner_array_list.add(oneBitmap);
          // if(twoBitmap!=null) banner_array_list.add(twoBitmap);
          //  if(threeBitmap!=null) banner_array_list.add(threeBitmap);
         //   if(fourBitmap!=null) banner_array_list.add(fourBitmap);

            if(oneBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getProductImages(),"Bitmap",oneBitmap));
            if(twoBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getImage1(),"Bitmap",twoBitmap));
            if(threeBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getImage2(),"Bitmap",threeBitmap));
            if(fourBitmap!=null) banner_array_list.add(new BitmapUrlModel(product.getImage3(),"Bitmap",fourBitmap));




            //  else if(!str_image_path2.equals("")) banner_array_list.add(str_image_path2);

            if(banner_array_list.size()!=0) {
                binding.layoutProduct.setVisibility(View.GONE);
                binding.imageSlider.setVisibility(View.VISIBLE);
                binding.tvAdd.setVisibility(View.GONE);

                adapter = new ProductImageSliderAdapterTwo(EditProductAct.this, banner_array_list,EditProductAct.this);
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
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });
        layout1Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "2";
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });


        layoutLogo1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "3";
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });
        layout1Shop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "4";
                if (Build.VERSION.SDK_INT >= 33) {
                    if(checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });




        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        pauseProgressDialog();
    }

    @Override
    public void onList(int position) {
        if(selectAttributeFinalArrayList.size()>1){
            selectAttributeFinalArrayList.remove(position);
            VariationAddedAdapter variationAdapter = new VariationAddedAdapter(EditProductAct.this,selectAttributeFinalArrayList,EditProductAct.this);
            binding.rvAttri.setAdapter(variationAdapter);
        }
    }

    @Override
    public void variation(int mainPosition, int position) {
         if(!selectAttributeSubAttributeArrayList.isEmpty()){
             page = "EditPage";
             deleteAddedSubAttribute(selectAttributeSubAttributeArrayList.get(mainPosition).getAttributes().get(position).getId());
         }
    }


    private void deleteAddedSubAttribute(String subAttributeId) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(EditProductAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("attribute_id",subAttributeId);
        editProductViewModel.deleteSubAttributeInEditPage(EditProductAct.this, map);
        // editText.setText("");
    }


    private void showDropDownProductQty(View v, TextView textView, List<String> stringList) {
        PopupMenu popupMenu = new PopupMenu(EditProductAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i));
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).equalsIgnoreCase(menuItem.getTitle().toString())) {
                    productQty = stringList.get(i);
                }
            }
            return true;
        });
        popupMenu.show();
    }



}
