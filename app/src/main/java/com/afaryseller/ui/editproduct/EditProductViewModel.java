package com.afaryseller.ui.editproduct;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.afaryseller.core.BaseViewModel;
import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.repository.SellerRepository;
import com.afaryseller.utility.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditProductViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;
    public EditProductViewModel() {
    }


    public void init(Context context){
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }

    public void  getAddedSubCategory(Context context,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.addedProductSubCatRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  getAddedAttribute(Context context,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.addedProductAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  deleteAttribute(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.deleteAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  deleteSubAttribute(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.deleteSubAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getMainCategory(Context context,Map<String,String>auth){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAllMainCategoryRepo(auth);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }




    public void  getMainSubCategory(Context context,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAllMainSubCategoryRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getBrand(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getBrandsRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  addBrand(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.addBrandsRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getAttribute(Context context , Map<String,String> auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  addAttribute(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.addAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  getSubAttribute(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getSubAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  addSubAttribute(Context context ,Map<String ,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.addSubAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getAddedAttributeSubAttribute(Context context ,Map<String ,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAddedAttributeSubAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }




    public void  deleteSubAttributeInEditPage(Context context ,HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.deleteSubattributeInEditPageRepo(map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }



    public void  checkUncheckSubAttribute(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.checkUncheckSubAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  checkUncheckAttribute(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.checkUncheckAttributeRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }






    public void  editShopProduct(Context context , Map<String,String>auth, RequestBody proId, RequestBody productName,
                                 RequestBody product_price, RequestBody description, RequestBody sku,
                                 RequestBody delivery_charges,RequestBody inStock, RequestBody category_id, RequestBody sub_category, RequestBody product_brand, RequestBody attribute,
                                 RequestBody registerId,RequestBody userSellerId,RequestBody qty,  MultipartBody.Part img1, MultipartBody.Part img2,MultipartBody.Part img3, MultipartBody.Part img4){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.editSellerShopProduct(auth,proId,productName,product_price,description,sku,
                    delivery_charges,inStock,category_id,sub_category,product_brand,attribute,registerId,userSellerId,qty,img1,img2,img3,img4);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }
}
