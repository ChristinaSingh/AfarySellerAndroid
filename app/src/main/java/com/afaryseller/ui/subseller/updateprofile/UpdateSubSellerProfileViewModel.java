package com.afaryseller.ui.subseller.updateprofile;

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



public class UpdateSubSellerProfileViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;

    public UpdateSubSellerProfileViewModel() {
    }

    public void init(Context context) {
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }


    public void  getSubSellerProfile(Context context , Map<String,String> auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getSubSellerProfileRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }



    public void  updateSubSellerProfile(Context context ,
                                 Map<String, String> auth, RequestBody name, RequestBody username, RequestBody email,
                                 RequestBody subSellerID, RequestBody countryCode, RequestBody mobile,
                                 RequestBody parent_seller_id, RequestBody country, RequestBody state,
                                 RequestBody city, RequestBody address,RequestBody shop_Id,
                                 MultipartBody.Part image){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.subSellerUpdateProfileRepo( auth, name, username,  email,
                    subSellerID,  countryCode,  mobile,
                    parent_seller_id, country, state,
                    city, address,shop_Id,
                    image);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }




    public void getAllCountry(Context context) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getCountry();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void getAllCity(Context context, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getCity(map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void  getAllShops(Context context ,Map<String,String>auth, HashMap<String,String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getAllShopRepo(auth,map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


}
