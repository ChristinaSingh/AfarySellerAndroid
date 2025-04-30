package com.afaryseller.ui.uploadids;

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

public class UploadIdViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;
    public UploadIdViewModel() {
    }

    public void init(Context context){
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();

    }




    public void  uploadIds(Context context , RequestBody userId, MultipartBody.Part img1,MultipartBody.Part img2){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.sellerUploadIds(userId,img1,img2);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void  sendAdminMsg(Context context , HashMap<String, String> map){
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.sendAdminMsgNewRepo(map);
        } else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }


}
