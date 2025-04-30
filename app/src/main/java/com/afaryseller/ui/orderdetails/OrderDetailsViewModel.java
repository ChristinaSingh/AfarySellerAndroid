package com.afaryseller.ui.orderdetails;

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

public class OrderDetailsViewModel extends BaseViewModel {
    SellerRepository sellerRepository;
    LiveData<Boolean> isLoading;
    LiveData<DynamicResponseModel> isResponse;

    public OrderDetailsViewModel() {
    }

    public void init(Context context) {
        sellerRepository = new SellerRepository();
        isLoading = sellerRepository.getIsLoading();
        isResponse = sellerRepository.getIsResponse();
    }


    public void getOrderDetails(Context context,Map<String, String> auth, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.getOrderDetailsRepo(auth,map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }

    public void acceptDeclineOrder(Context context,Map<String, String> auth, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.acceptDeclineOrderRepo(auth,map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void informDeliveryOrderCancel(Context context, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.informDeliveryOrderCancelRepo(map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }




    public void getToken(Context context, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.generateTokenRepo(map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    public void addDeliveryToken(Context context, Map<String,String> auth, RequestBody package_name, RequestBody owner,
                                 RequestBody command_date, RequestBody adresse_source, RequestBody adresse_destination,
                                 RequestBody delivery_type, RequestBody latitude_source, RequestBody longitude_source, RequestBody longitude_destination, RequestBody longitude_destination1,
                                 RequestBody command_number, RequestBody delivery_amount , RequestBody command_amount, RequestBody customer_code, MultipartBody.Part img1) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.addDeliveryRepo(auth,package_name,owner,command_date,adresse_source,adresse_destination,
                    delivery_type,latitude_source,longitude_source,longitude_destination,longitude_destination1,command_number,delivery_amount,command_amount,customer_code,img1);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }



    public void completeSelfCollectOrder(Context context,Map<String, String> auth, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.completeSelfCollectOrderRepo(auth,map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }



    public void returnOrder(Context context, HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.returnOrderRepo(map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }



    public void internationalDeliveryTransit(Context context,HashMap<String, String> map) {
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            sellerRepository.internationalDeliveryTransitRepo(map);
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }


}