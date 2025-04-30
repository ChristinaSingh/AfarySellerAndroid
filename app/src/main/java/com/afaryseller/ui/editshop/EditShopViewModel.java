package com.afaryseller.ui.editshop;

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

public class EditShopViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;
    public EditShopViewModel() {
    }
    public void init(Context context){
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }

    public void  getCountries(Context context){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getCountry();
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getCountryStates(Context context , HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getStates(map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  getStateCity(Context context , HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getStatesCity(map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }



    public void  editShop(Context context , Map<String,String>auth,RequestBody shopId, RequestBody userId, RequestBody shopName, RequestBody subCateId,
                          RequestBody description, RequestBody address,RequestBody streetLandmark, RequestBody neighbourhood,
                          RequestBody country, RequestBody state, RequestBody city, RequestBody countryCode, RequestBody phone,
                          RequestBody phonenumber, RequestBody mobileaccount,
                          RequestBody lat, RequestBody lon,RequestBody registerId,RequestBody userSellerId, MultipartBody.Part img1, MultipartBody.Part img2, MultipartBody.Part img3){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.editSellerShop(auth,shopId,userId,shopName,subCateId,description,address,streetLandmark,neighbourhood,country,
                    state,city,countryCode,phone,phonenumber,mobileaccount,lat,lon,registerId,userSellerId,img1,img3,img3);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getCurrency(Context context){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getCurrencyRepo();
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

}