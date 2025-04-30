package com.afaryseller.ui.addproduct;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.databinding.ActivityAddProductBinding;
import com.afaryseller.databinding.DialogAddRemoveAttributeBinding;
import com.afaryseller.databinding.DialogAttributesBinding;
import com.afaryseller.databinding.DialogBandBinding;
import com.afaryseller.databinding.DialogCategoryBinding;
import com.afaryseller.databinding.DialogSubCategoryBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addproduct.adapter.AttributeAdapter;
import com.afaryseller.ui.addproduct.adapter.AttributeAddAdapter;
import com.afaryseller.ui.addproduct.adapter.BrandAdapter;
import com.afaryseller.ui.addproduct.adapter.MainCategoryAdapter;
import com.afaryseller.ui.addproduct.adapter.SelectCategoryAdapter;
import com.afaryseller.ui.addproduct.adapter.SelectSubCateAdapter;
import com.afaryseller.ui.addproduct.adapter.SubAttributeAdapter;
import com.afaryseller.ui.addproduct.adapter.SubCategoryAdapter;
import com.afaryseller.ui.addproduct.adapter.VariationAdapter;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.BrandModel;
import com.afaryseller.ui.addproduct.model.MainCateModel;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;
import com.afaryseller.ui.addproduct.model.SubCatModel;
import com.afaryseller.ui.addshop.AddShopFragment;
import com.afaryseller.ui.addshop.AddShopViewModel;
import com.afaryseller.ui.addshop.CountryAdapter;
import com.afaryseller.ui.addshop.StateAdapter;
import com.afaryseller.ui.addshop.adapter.CityAdapter;
import com.afaryseller.ui.addshop.model.CountryModel;
import com.afaryseller.ui.addshop.model.StateModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;
import com.afaryseller.ui.signup.CityModel;
import com.afaryseller.utility.DataManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Attr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProductNewAct extends AppCompatActivity implements AddDateLister {
    ActivityAddProductBinding binding;
    AddProductViewModel addProductViewModel;
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

    AttributeModel.Result data;
    MutableLiveData<List<AttributeModel.Result>> mutableAttributeArrayList;


    ArrayList<BrandModel.Result> brandArrayList;
    String shopId = "", catId = "",brandId="",validateId="";
    BrandAdapter brandAdapter;
    AttributeAddAdapter attributeAdapter;
    SubAttributeAdapter subAttributeAdapter;

    MainCategoryAdapter mainCategoryAdapter;
    SubCategoryAdapter subCategoryAdapter;
    SelectSubCateAdapter selectSubCateAdapter;
    SelectCategoryAdapter selectCategoryAdapter;
    Dialog categoryDialog;
    Dialog subCateDialog;
    public static TextView tvNotFound,tvNotFoundAttribute;
    ProgressDialog showProgressDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_product);
        addProductViewModel = new AddProductViewModel();
        showProgressDialog = new ProgressDialog(AddProductNewAct.this);

        initViews();

     //   observeLoader();
      //  observeResponse();

        //addProductViewModel.getMainCategory(AddProductNewAct.this);
        getAllMainCategoryRepo();
        getBrands();


    }


    public void getAllMainCategoryRepo() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        showProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showProgressDialog.setMessage("Please wait...");
        showProgressDialog.show();
        showProgressDialog.setCancelable(true);
        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.getMainCategoryApi(headerMap);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showProgressDialog.dismiss();
                Log.e("url===", response.toString());
                 try {
                     if (response.code() == 200) {
                         String stringResponse = response.body().string();
                         Log.e("response get main category===", stringResponse);

                         JSONObject jsonObject = new JSONObject(stringResponse);
                         if (jsonObject.getString("status").toString().equals("1")) {

                             MainCateModel mainCateModel = new Gson().fromJson(stringResponse, MainCateModel.class);
                             mainCateList.clear();
                             mainCateList.addAll(mainCateModel.getResult());
                         } else if (jsonObject.getString("status").toString().equals("0")) {
                             Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                         }
                     } else {


                     }
                 }catch (Exception e){
                     e.printStackTrace();
                 }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();
            }
        });
    }



    private void getBrands() {
        showProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showProgressDialog.setMessage("Please wait...");
        showProgressDialog.show();
        showProgressDialog.setCancelable(true);
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getId());
        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.getBrandApi(headerMap,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("url===", response.toString());
                showProgressDialog.dismiss();
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        Log.e("response get brands===", stringResponse);
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        if (jsonObject.getString("status").toString().equals("1")) {
                            BrandModel brandModel = new Gson().fromJson(stringResponse, BrandModel.class);
                            brandArrayList.clear();
                            brandArrayList.addAll(brandModel.getResult());
                            if (brandAdapter != null) brandAdapter.notifyDataSetChanged();


                        } else if (jsonObject.getString("status").toString().equals("0")) {
                            brandArrayList.clear();
                            brandAdapter.notifyDataSetChanged();
                            Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();

            }
        });

    }

    private void addBrands(String name, EditText editText) {
        showProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showProgressDialog.setMessage("Please wait...");
        showProgressDialog.show();
        showProgressDialog.setCancelable(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getId());
        map.put("name", name);
       // addProductViewModel.addBrand(AddProductNewAct.this, map);
        editText.setText("");
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.addBrandApi(headerMap,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("url===", response.toString());
                showProgressDialog.dismiss();
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        Log.e("response add brand===", stringResponse);
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        if (jsonObject.getString("status").toString().equals("1")) {
                            getBrands();
                        } else if (jsonObject.getString("status").toString().equals("0")) {
                            Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();

            }
        });
    }

    private void getAttribute() {
        showProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showProgressDialog.setMessage("Please wait...");
        showProgressDialog.show();
        showProgressDialog.setCancelable(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getId());
     //   addProductViewModel.getAttribute(AddProductNewAct.this, map);
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.getAttributeApi(headerMap,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("url===", response.toString());
                showProgressDialog.dismiss();
                try {
                    if (response.code()== 200) {
                        String stringResponse = response.body().string();
                        Log.e("response get brands===", stringResponse);
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        if (jsonObject.getString("status").toString().equals("1")) {
                            AttributeModel attributeModel = new Gson().fromJson(stringResponse, AttributeModel.class);
                            attributeArrayList.clear();
                            attributeArrayList.addAll(attributeModel.getResult());

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



                            if(attributeAdapter!=null)  attributeAdapter.notifyDataSetChanged();
                            else Log.e("adapter nul a rha hai","=====");

                        } else if (jsonObject.getString("status").toString().equals("0")) {
                            attributeArrayList.clear();
                            attributeAdapter.notifyDataSetChanged();
                            Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();

            }
        });
    }


    private void addAttribute(String name, EditText editText) {
        showProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showProgressDialog.setMessage("Please wait...");
        showProgressDialog.show();
        showProgressDialog.setCancelable(true);
        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getId());
        map.put("name", name);
        //addProductViewModel.addAttribute(AddProductNewAct.this, map);
        editText.setText("");
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");
        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.addAttributeApi(headerMap,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("url===", response.toString());
                showProgressDialog.dismiss();
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        Log.e("response get brands===", stringResponse);
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        if (jsonObject.getString("status").toString().equals("1")) {
                            getAttribute();
                        } else if (jsonObject.getString("status").toString().equals("0")) {
                            Toast.makeText(AddProductNewAct.this,"create successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();

            }
        });
    }


    private void getSubAttribute(String validate) {
        showProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showProgressDialog.setMessage("Please wait...");
        showProgressDialog.show();
        showProgressDialog.setCancelable(true);
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getId());
        map.put("validate_id", validate);
        //addProductViewModel.getSubAttribute(AddProductNewAct.this, map);

        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.getSubAttributeApi(headerMap,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("url===", response.toString());
                showProgressDialog.dismiss();
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        Log.e("response get brands===", stringResponse);

                        JSONObject jsonObject = new JSONObject(stringResponse);
                        if (jsonObject.getString("status").toString().equals("1")) {
                            AttributeModel attributeModel = new Gson().fromJson(stringResponse, AttributeModel.class);
                            subAttributeArrayList.clear();
                            subAttributeArrayList.addAll(attributeModel.getResult());


                            if(subAttributeAdapter!=null)  subAttributeAdapter.notifyDataSetChanged();
                            //else subAttributeDialog(AddProductNewAct.this,data);


                        } else if (jsonObject.getString("status").toString().equals("0")) {
                            subAttributeArrayList.clear();
                            subAttributeAdapter.notifyDataSetChanged();
                            Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();

            }
        });
    }


    private void addSubAttribute(String name,String validateId, EditText editText) {
        showProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        showProgressDialog.setMessage("Please wait...");
        showProgressDialog.show();
        showProgressDialog.setCancelable(true);
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getId());
        map.put("validate_id", validateId);
        map.put("name", name);
       // addProductViewModel.addSubAttribute(AddProductNewAct.this, map);
        editText.setText("");

        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.addSubAttributeApi(headerMap,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.e("url===", response.toString());
                showProgressDialog.dismiss();
                try {
                    if (response.code() == 200) {
                        String stringResponse = response.body().string();
                        Log.e("response add subAttributes===", stringResponse);
                        JSONObject jsonObject = new JSONObject(stringResponse);
                        if (jsonObject.getString("status").toString().equals("1")) {
                            getAttribute();
                        } else if (jsonObject.getString("status").toString().equals("0")) {
                            Toast.makeText(AddProductNewAct.this,"create successfully.", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();

            }
        });
    }



    private void getSubCate(String catId) {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(AddProductNewAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("cat_id", catId);
      //  addProductViewModel.getMainSubCategory(AddProductNewAct.this, map);


        AfarySeller apiInterface = ApiClient.getClient(AddProductNewAct.this).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.getSubAttributeApi(headerMap,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                showProgressDialog.dismiss();
                Log.e("url===", response.toString());
                try {
                    if (response.code() == 200) {
                        String stringResponse  = response.body().string();
                        Log.e("response get sub category===", stringResponse);
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

                            subCategoryDialog(AddProductNewAct.this);


                        } else if (jsonObject.getString("status").toString().equals("0")) {
                            Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                        }
                    } else {


                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                showProgressDialog.dismiss();
            }
        });
    }

    private void initViews() {

        if (getIntent() != null) shopId = getIntent().getStringExtra("shopId");

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



        binding.btnAddCategory.setOnClickListener(v -> {
            categoryDialog(AddProductNewAct.this);
        });

        binding.btnBrand.setOnClickListener(v -> {
            brandDialog(AddProductNewAct.this);
        });

        binding.btnAttributes.setOnClickListener(v -> {
            attributeDialog(AddProductNewAct.this);
        });

        binding.ivBack.setOnClickListener(v -> finish());
    }

   /* public void observeResponse() {
        addProductViewModel.isResponse.observe(AddProductNewAct.this, dynamicResponseModel -> {
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
                                Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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

                                subCategoryDialog(AddProductNewAct.this);


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                AttributeModel attributeModel = new Gson().fromJson(stringResponse, AttributeModel.class);
                                attributeArrayList.clear();
                                attributeArrayList.addAll(attributeModel.getResult());

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



                                if(attributeAdapter!=null)  attributeAdapter.notifyDataSetChanged();
                                else Log.e("adapter nul a rha hai","=====");

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                attributeArrayList.clear();
                                attributeAdapter.notifyDataSetChanged();
                                Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddProductNewAct.this,"create successfully.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                AttributeModel attributeModel = new Gson().fromJson(stringResponse, AttributeModel.class);
                                subAttributeArrayList.clear();
                                subAttributeArrayList.addAll(attributeModel.getResult());


                                if(subAttributeAdapter!=null)  subAttributeAdapter.notifyDataSetChanged();
                                //else subAttributeDialog(AddProductNewAct.this,data);


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                subAttributeArrayList.clear();
                                subAttributeAdapter.notifyDataSetChanged();
                                Toast.makeText(AddProductNewAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(AddProductNewAct.this,"create successfully.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AddProductNewAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }


    private void observeLoader() {
        addProductViewModel.isLoading.observe(AddProductNewAct.this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(AddProductNewAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }*/


    private void categoryDialog(Context mContext) {
        categoryDialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        DialogCategoryBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_category, null, false);
        categoryDialog.setContentView(dialogBinding.getRoot());


        dialogBinding.RRback.setOnClickListener(v -> categoryDialog.dismiss());

        mainCategoryAdapter = new MainCategoryAdapter(mContext, mainCateList, AddProductNewAct.this);
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

                selectSubCateAdapter = new SelectSubCateAdapter(mContext, selectSubCateModelArrayList, AddProductNewAct.this);
                binding.rvSelectSubCate.setAdapter(selectSubCateAdapter);

                selectCategoryAdapter = new SelectCategoryAdapter(mContext, selectCategoryModelArrayList, AddProductNewAct.this);
                binding.rvSelectCate.setAdapter(selectCategoryAdapter);

                categoryDialog.dismiss();
                subCateDialog.dismiss();
            }
        });


        subCategoryAdapter = new SubCategoryAdapter(mContext, subCateArrayList, AddProductNewAct.this);
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

        brandAdapter = new BrandAdapter(mContext, brandArrayList, AddProductNewAct.this);
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
        attributeAdapter = new AttributeAddAdapter(mContext,attributeArrayList,AddProductNewAct.this);
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
            selectAttributeFinalArrayList.clear();

            for(int i=0;i<attributeArrayList.size();i++){
                if(attributeArrayList.get(i).isChk()==true){
                    selectAttributeFinalArrayList.add(attributeArrayList.get(i)) ;
                }
            }

            Log.e("=====",DataManager.attributeArrayList.size() +"");

            if(selectAttributeFinalArrayList.size()==0) Toast.makeText(mContext, mContext.getString(R.string.please_select_attribute), Toast.LENGTH_SHORT).show();
            else {
                binding.rvAttri.setVisibility(View.VISIBLE);
                VariationAdapter variationAdapter = new VariationAdapter(mContext,selectAttributeFinalArrayList);
                binding.rvAttri.setAdapter(variationAdapter);

                dialog.dismiss();

            }
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

    private void subAttributeDialog(Context mContext,AttributeModel.Result data,int position) {
        Dialog dialog = new Dialog(mContext, WindowManager.LayoutParams.MATCH_PARENT);
        DialogAddRemoveAttributeBinding dialogBinding = DataBindingUtil
                .inflate(LayoutInflater.from(mContext), R.layout.dialog_add_remove_attribute, null, false);
        dialog.setContentView(dialogBinding.getRoot());


        dialogBinding.tvItemOptions.setText(data.getName() + " " +mContext.getString(R.string.options));
        subAttributeArrayList.clear();
        selectSubAttributeArrayList.clear();
        subAttributeAdapter = new SubAttributeAdapter(mContext,subAttributeArrayList,AddProductNewAct.this);
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

        dialogBinding.btnDone.setOnClickListener(v -> {
            if(selectSubAttributeArrayList.size()==0) Toast.makeText(mContext, mContext.getString(R.string.please_select_variations), Toast.LENGTH_SHORT).show();
            else {
                attributeArrayList.get(position).setModelList(selectSubAttributeArrayList);
                Log.e("adding=====",DataManager.attributeArrayList.size() +"");
            }
            dialog.dismiss();
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

            final ArrayList<AttributeModel.Result> filteredList = new ArrayList<AttributeModel.Result>();

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


        } else if (tag.equals("Cate")) {
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

        } else if (tag.equals("removeSubCategory")) {
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
            //attributeArrayList.get(position).setChk(chk);
            validateId = attributeArrayList.get(position).getId();
            data = attributeArrayList.get(position);
            subAttributeDialog(AddProductNewAct.this,data,position);
        }


        else if (tag.equals("attributeCheck")) {
            attributeArrayList.get(position).setChk(chk);
            attributeAdapter.notifyDataSetChanged();

        }


        else if (tag.equals("subAttribute")) {
            selectSubAttributeArrayList.clear();
            subAttributeArrayList.get(position).setChk(chk);
            subAttributeAdapter.notifyDataSetChanged();
            for(int i=0;i<subAttributeArrayList.size();i++) {
                if (subAttributeArrayList.get(i).isChk() == true)
                    selectSubAttributeArrayList.add(new SelectSubCateModel(subAttributeArrayList.get(i).getId(), subAttributeArrayList.get(i).getName()));

            }
        }
    }

    @Override
    public void onDate(String date, int position, String tag) {

    }





}




