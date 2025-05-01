package com.afaryseller.ui.subseller.signup;

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


public class SubSellerSignupViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;
    public SubSellerSignupViewModel() {
    }
    public void init(Context context){
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }



    public void  signupSubSeller(Context context ,
                                 Map<String, String> auth, RequestBody name, RequestBody username, RequestBody email,
                                 RequestBody password, RequestBody countryCode, RequestBody mobile,
                                 RequestBody parent_seller_id, RequestBody country, RequestBody state,
                                 RequestBody city, RequestBody address,RequestBody shopId,
                                 MultipartBody.Part image){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.subSellerSignupRepo( auth, name, username,  email,
                     password,  countryCode,  mobile,
                     parent_seller_id, country, state,
                     city, address,shopId,
                     image);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  getAllCountry(Context context){
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

    public void  getAllShops(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAllShopRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }





   /* public void  getAllCity(Context context,HashMap<String,String>map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getCity(map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }*/



}