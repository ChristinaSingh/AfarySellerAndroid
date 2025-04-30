package com.afaryseller.ui.subscription;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.afaryseller.core.BaseViewModel;
import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.repository.SellerRepository;
import com.afaryseller.utility.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

public class SubscriptionViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;
    public SubscriptionViewModel() {
    }
    public void init(Context context){
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }




    public void  getSubscriptionPlan(Context context,Map<String,String>auth,String countryId){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getSubscriptionPlanRepo(auth,countryId);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  purchaseSubscriptionPlan(Context context,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.purchaseSubscriptionPlanRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  purchaseZeroSubscriptionPlan(Context context, Map<String,String>auth, HashMap<String, String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.purchaseZeroSubscriptionPlanRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


}