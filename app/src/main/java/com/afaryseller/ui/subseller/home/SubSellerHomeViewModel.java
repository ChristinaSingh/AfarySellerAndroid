package com.afaryseller.ui.subseller.home;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.afaryseller.core.BaseViewModel;
import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.repository.SellerRepository;
import com.afaryseller.utility.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;




public class SubSellerHomeViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;
    public SubSellerHomeViewModel() {
    }
    public void init(Context context){
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }



    public void  getSliderImg(Context context , Map<String,String> headerMap, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.sliderHomeImg(headerMap,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  homeCat(Context context , Map<String,String> headerMap, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAddedCat(headerMap,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getNotificationCounter(Context context ,Map<String,String> headerMap, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getNotificationCounterRepo(headerMap,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  getSellerProfile(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getSellerProfile(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }






}

