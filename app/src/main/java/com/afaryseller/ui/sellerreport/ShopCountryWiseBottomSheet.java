package com.afaryseller.ui.sellerreport;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.FragmentStoreCountryWiseBottomsheetBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.NetworkAvailablity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopCountryWiseBottomSheet extends BottomSheetDialogFragment implements StoreListener {

    Context context;
    public String TAG = "ShopCountryWiseBottomSheet";
    BottomSheetDialog dialog;
    FragmentStoreCountryWiseBottomsheetBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    private AfarySeller apiInterface;
    AskListener listener;
    ArrayList<StoreCountryWiseModel.Datum> arrayList;
    private StoreCountryWiseAdapter adapter;

    public ShopCountryWiseBottomSheet(Context context) {
        this.context = context;
    }


    public ShopCountryWiseBottomSheet callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_store_country_wise_bottomsheet, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        apiInterface = ApiClient.getClient(context).create(AfarySeller.class);

        initBinding();
        return  dialog;







    }

    private void initBinding() {
        arrayList = new ArrayList<>();

        binding.rlBack.setOnClickListener(v -> {
            dismiss();
        });

        adapter = new StoreCountryWiseAdapter(requireActivity(),arrayList,ShopCountryWiseBottomSheet.this);
        binding.rvStoreCountryWise.setAdapter(adapter);

        if (NetworkAvailablity.checkNetworkStatus(context)) {
            getAllSubSellerShopCountryWiseList();
        }

        else {
            Toast.makeText(context,"No internet connection", Toast.LENGTH_LONG).show();
        }
    }




    private void getAllSubSellerShopCountryWiseList() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        Log.e("MapMap", "get store country wise REQUEST" + map);
        Call<ResponseBody> SignupCall = apiInterface.getSubSellerShopsCountryWiseApi(map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                //   binding.loader.setVisibility(View.GONE);

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "get store country wise RESPONSE" + object);
                    if (object.getBoolean("status")==true) {
                        StoreCountryWiseModel data = new Gson().fromJson(responseData, StoreCountryWiseModel.class);
                        arrayList.clear();
                        arrayList.addAll(data.getData());
                        adapter.notifyDataSetChanged();


                    } else  {
                        // binding.rlPayment.setVisibility(View.VISIBLE);
                        // binding.rlPaymentStatus.setVisibility(View.GONE);
                        dismiss();
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
                // binding.loader.setVisibility(View.GONE);

            }
        });
    }


    @Override
    public void onStore(String id) {
        dismiss();
        listener.ask(commaSepShopId( arrayList.get(Integer.parseInt(id)).getShops()),"subSellerCountry");
    }


    public String commaSepShopId(List<StoreCountryWiseModel.Datum.Shop> arrayList){
        StringBuilder shopIds = new StringBuilder();

        for (int i = 0; i < arrayList.size(); i++) {
            shopIds.append(arrayList.get(i).getShopId()); // or arrayList.get(i).shop_id if it's a public field

            if (i < arrayList.size() - 1) {
                shopIds.append(",");
            }
        }

        Log.e("comma sep shop id==",shopIds+"");

        return shopIds+"";

    }
}