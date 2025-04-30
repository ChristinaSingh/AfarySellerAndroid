package com.afaryseller.ui.home.fragment;

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

public class ProfileViewModel extends BaseViewModel {
        SellerRepository sellerRepository;
        LiveData<Boolean> isLoading;
        LiveData<DynamicResponseModel> isResponse;
    public ProfileViewModel() {
        }
        public void init(Context context){
            sellerRepository = new SellerRepository();
            isLoading = sellerRepository.getIsLoading();
            isResponse = sellerRepository.getIsResponse();
        }



      /*  public void  getSliderImg(Context context , HashMap<String,String> map){
            if (NetworkAvailablity.checkNetworkStatus(context)) {
                sellerRepository.sliderHomeImg(map);
            } else {
                Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
            }
        }*/

        public void  getSellerProfile(Context context , Map<String,String>auth, HashMap<String, String> map){
            if (NetworkAvailablity.checkNetworkStatus(context)) {
                sellerRepository.getSellerProfile(auth,map);
            } else {
                Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
            }
        }

    public void  getSellerProfileUpdate(Context context , Map<String,String>auth, HashMap<String, String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getSellerProfileUpdateRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void updateProfile(Context context, Map<String,String>auth, RequestBody userId, RequestBody user_name, RequestBody address,
                              RequestBody lat, RequestBody lon, RequestBody country,
                              RequestBody city, RequestBody registerId, RequestBody userSellerId, MultipartBody.Part img1) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.updateProfileRepo(auth,userId,user_name,address,lat,lon,country,city,registerId,userSellerId,img1);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }





    public void  updateLanguage(Context context , HashMap<String, String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.updateLanguageRepo(map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void getSubSellerProfile(Context context, Map<String,String> auth, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getSubSellerProfileRepo(auth,map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }


}
