package com.afaryseller.ui.shoplist;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.afaryseller.core.BaseViewModel;
import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.repository.SellerRepository;
import com.afaryseller.utility.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

public class ShopListViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;
    public ShopListViewModel() {
    }
    public void init(Context context){
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }



    public void  getAllShops(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAllShopRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  updateShopStatus(Context context , Map<String,String> auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.updateShopRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }



    public void  checkStatsPlan(Context context , Map<String,String> auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.checkPlanStatusRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


}