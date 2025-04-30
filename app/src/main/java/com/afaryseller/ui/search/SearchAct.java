package com.afaryseller.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.ActivitySearchBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.search.adapter.SearchShopAdapter;
import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchAct extends BaseFragment<ActivitySearchBinding,SearchViewModel> {
    public String TAG = "SearchAct";

    ActivitySearchBinding binding;
    SearchViewModel searchViewModel;
    SearchShopAdapter adapter;
    String queryString ="";
    ArrayList<ShopModel.Result> shopArrayList;

   /* @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(ge, R.layout.activity_search);
        searchViewModel = new SearchViewModel();
        binding.setSearchViewModel(searchViewModel);
        binding.getLifecycleOwner();
        searchViewModel.init(getActivity());
        initViews();

        observeLoader();
        observeResponse();
    }*/


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_search, container, false);
        searchViewModel = new SearchViewModel();
        binding.setSearchViewModel(searchViewModel);
        binding.getLifecycleOwner();
        searchViewModel.init(getActivity());
        initViews();
        return binding.getRoot() ;
    }

    private void initViews() {

        shopArrayList = new ArrayList<>();

        binding.search.setIconified(false);
        binding.search.setQueryHint(getString(R.string.search_shop_here));
        adapter = new SearchShopAdapter(getActivity(), shopArrayList);
        binding.recyList.setAdapter(adapter);
       // binding.swiperRefresh.setOnRefreshListener(this::getProduct);
        binding.search.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                return false;
            }
        });
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                queryString = s;
                if (queryString.length()>=3) searchShop();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                queryString = s;
                if (queryString.length()>=3) searchShop();
                return false;
            }
        });


        observeLoader();
        observeResponse();

    }

    private void searchShop() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("search", queryString);
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());


        searchViewModel.searchShop(getActivity(),headerMap,map);
    }


    public void observeResponse(){
        searchViewModel.isResponse.observe(getActivity(),dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.SEARCH_SHOP){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get daily close Day===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                binding.tvNotFound.setVisibility(View.GONE);
                                ShopModel shopModel = new Gson().fromJson(stringResponse, ShopModel.class);
                                shopArrayList.clear();
                                shopArrayList.addAll(shopModel.getResult());
                                adapter.notifyDataSetChanged();


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                shopArrayList.clear();
                                adapter.notifyDataSetChanged();
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                                binding.tvNotFound.setVisibility(View.VISIBLE);

                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());

                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        } );
    }

    private void observeLoader() {
        searchViewModel.isLoading.observe(getActivity(),aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

}
