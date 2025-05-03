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
import com.afaryseller.databinding.FragmenStoreBottomsheetBinding;
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
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StoreBottomSheet extends BottomSheetDialogFragment implements StoreListener {

    Context context;
    public String TAG = "StoreBottomSheet";
    BottomSheetDialog dialog;
    FragmenStoreBottomsheetBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    private AfarySeller apiInterface;
    AskListener listener;
    ArrayList<StoreModel.Datum> arrayList;
    private StoreAdapter adapter;

    public StoreBottomSheet(Context context) {
        this.context = context;
    }


    public StoreBottomSheet callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragmen_store_bottomsheet, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        apiInterface = ApiClient.getClient(context).create(AfarySeller.class);

        initBinding();
        return dialog;


    }

    private void initBinding() {
        arrayList = new ArrayList<>();

        binding.rlBack.setOnClickListener(v -> {
            dismiss();
        });

        adapter = new StoreAdapter(requireActivity(), arrayList, StoreBottomSheet.this);
        binding.rvStore.setAdapter(adapter);
        if (NetworkAvailablity.checkNetworkStatus(context)) {
            getAllShopList();
        } else {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
        }
    }


    private void getAllShopList() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        Log.e("MapMap", "get store REQUEST" + map);
        Call<ResponseBody> SignupCall = apiInterface.getShopsApi(map);

        SignupCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                //   binding.loader.setVisibility(View.GONE);

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "get store RESPONSE" + object);
                    if (object.getBoolean("status") == true) {
                        StoreModel data = new Gson().fromJson(responseData, StoreModel.class);
                        arrayList.clear();
                        arrayList.addAll(data.getData());
                        adapter.notifyDataSetChanged();


                    } else {
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
        listener.ask(id, "store");
    }
}

