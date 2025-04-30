package com.afaryseller.ui.changepassword;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.afaryseller.core.BaseViewModel;
import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.repository.SellerRepository;
import com.afaryseller.utility.NetworkAvailablity;

import java.util.HashMap;
import java.util.Map;

public class ChangePassViewModel  extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;

    public ChangePassViewModel() {
    }

    public void init(Context context) {
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }


    public void changePass(Context context, Map<String,String>auth,HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.changeSellePass(auth,map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

}
