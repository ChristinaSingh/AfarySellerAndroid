package com.afaryseller.ui.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.afaryseller.R;
import com.afaryseller.databinding.FragmentWalletBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.editproduct.EditProductAct;
import com.afaryseller.ui.orderdetails.OrderDetailsAct;
import com.afaryseller.ui.splash.AskListener;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.ui.wallet.bottomsheet.BottomAddFragment;
import com.afaryseller.ui.wallet.bottomsheet.TransferMoneyBottomSheet;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletFragment extends Fragment implements AskListener {

    FragmentWalletBinding binding;

    WalletAdapter mAdapter;
    private AfarySeller apiInterface;
    private ArrayList<GetTransferDetails.Result> get_result = new ArrayList<>();
    GetProfileModal data;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wallet, container, false);
        apiInterface = ApiClient.getClient(getContext()).create(AfarySeller.class);

        GetProfile();
        // GetAvailableBal();
        GetTransactionAPI();

        binding.RRback.setOnClickListener(v -> {
            getFragmentManager().popBackStack();
        });


        //binding.recyclerWallet

        binding.txtAddMoney.setOnClickListener(v -> {
            // BottomAddFragment bottomSheetFragment = new BottomAddFragment(getActivity());
            new BottomAddFragment(getActivity()).callBack(this::ask).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
        });

        binding.txtWithdrawMoney.setOnClickListener(v -> {
            // WithDrawFragment bottomSheetFragment = new WithDrawFragment(getActivity());
            new WithDrawFragment(getActivity(), data.getResult().getWallet()).callBack(this::ask).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
        });

        binding.txtTransactMoney.setOnClickListener(v -> {
            //  TransferMOneyFragment bottomSheetFragment = new TransferMOneyFragment(getActivity());
            //  bottomSheetFragment.callBack(this::ask).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");
            new TransferMoneyBottomSheet(getActivity()).callBack(this::ask).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");

        });

        return binding.getRoot();
    }


    private void GetProfile() {

        DataManager.getInstance().showProgressMessage(getActivity(), getString(R.string.please_wait));
        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("country_id", DataManager.getInstance().getUserData(getActivity()).getResult().getCountry());
        Call<GetProfileModal> loginCall = apiInterface.get_profile(map);

        loginCall.enqueue(new Callback<GetProfileModal>() {
            @Override
            public void onResponse(Call<GetProfileModal> call,
                                   Response<GetProfileModal> response) {

                DataManager.getInstance().hideProgressMessage();

                try {

                    data = response.body();
                    String dataResponse = new Gson().toJson(response.body());
                    Log.e("MapMap", "GET RESPONSE" + dataResponse);

                    if (data.status.equals("1")) {
                        binding.textAmount.setText(data.result.getLocalCurrency() + data.result.getLocalPrice());
                    } else if (data.status.equals("0")) {
                        Toast.makeText(getActivity(), data.message /*getString(R.string.wrong_username_password)*/, Toast.LENGTH_SHORT).show();
                    } else if (data.status.equals("5")) {
                        SessionManager.logout(getActivity());
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<GetProfileModal> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });

    }



    private void GetTransactionAPI() {

        DataManager.getInstance().showProgressMessage((Activity) getContext(), getString(R.string.please_wait));
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        Map<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("country_id", DataManager.getInstance().getUserData(getActivity()).getResult().getCountry());

        map.put("type", "All");
        Log.e("MapMap", "" + map);

        Call<ResponseBody> loginCall = apiInterface.get_transfer_money(headerMap, map);

        loginCall.enqueue(new Callback<ResponseBody>() {

            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e("MapMap", "Exersice_List RESPONSE" + object);
                    if (object.getString("status").equals("1")) {
                        GetTransferDetails data = new Gson().fromJson(responseData, GetTransferDetails.class);
                        get_result.clear();
                        get_result.addAll(data.getResult());
                        setAdapter();

                    } else if (object.getString("status").equals("0")) {
                        Toast.makeText(getContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    } else if (object.getString("status").equals("5")) {
                        // Toast.makeText(getContext(), "No Data Found !!!!", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getActivity(), SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        getActivity().finish();

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                call.cancel();
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }

    private void setAdapter() {

        mAdapter = new WalletAdapter(getActivity(), get_result);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        binding.recyclerWallet.setLayoutManager(linearLayoutManager);
        binding.recyclerWallet.setAdapter(mAdapter);
    }

    @Override
    public void ask(String value, String status) {
        GetTransactionAPI();
        GetProfile();
    }


}
