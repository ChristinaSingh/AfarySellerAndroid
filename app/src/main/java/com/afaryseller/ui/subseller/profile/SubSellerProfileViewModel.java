package com.afaryseller.ui.subseller.profile;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.afaryseller.core.BaseViewModel;
import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.repository.SellerRepository;
import com.afaryseller.utility.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;


public class SubSellerProfileViewModel  extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;

    public SubSellerProfileViewModel() {
    }

    public void init(Context context) {
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }


    public void getSubSellerProfile(Context context, Map<String,String> auth, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getSubSellerProfileRepo(auth,map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void deleteSubSellerProfile(Context context, Map<String,String> auth, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.deleteSubSellerRepo(auth,map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

}