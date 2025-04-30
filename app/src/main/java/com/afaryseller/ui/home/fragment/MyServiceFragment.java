package com.afaryseller.ui.home.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.FragmentHomeBinding;
import com.afaryseller.databinding.FragmentServiceBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.home.adapter.HomeCatAdapter;
import com.afaryseller.ui.home.adapter.MyServiceAdapter;
import com.afaryseller.ui.home.adapter.SliderAdapterExample;
import com.afaryseller.ui.home.model.BannerModal1;
import com.afaryseller.ui.home.model.HomeCatModel;
import com.afaryseller.ui.selectskills.SelectSkillsAct;
import com.afaryseller.utility.DataManager;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyServiceFragment extends BaseFragment<FragmentServiceBinding,MyServiceViewModel> {
    FragmentServiceBinding binding;
    MyServiceViewModel myServiceViewModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_service, container, false);
        myServiceViewModel = new MyServiceViewModel();
        binding.setMyServiceViewModel(myServiceViewModel);
        binding.getLifecycleOwner();
        myServiceViewModel.init(getActivity());
        initViews();
        return binding.getRoot() ;
    }

    private void initViews() {

        observeLoader();
        observeResponse();


        binding.ivBack.setOnClickListener(view -> getActivity().onBackPressed());

        binding.addProduct.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), SelectSkillsAct.class)
                   .putExtra("user_id",DataManager.getInstance().getUserData(getActivity()).getResult().getId())
                    .putExtra("type","home"));

           /* Bundle bundle = new Bundle();
            bundle.putString("id","1");
            bundle.putString("name","Online Store");
            Navigation.findNavController(view).navigate(R.id.action_myservice_fragment_to_addShopFragment,bundle);*/
        });


    }


    private void callHomeCat() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        myServiceViewModel.homeCat(getActivity(),headerMap,map);
    }

    public void observeResponse(){
        myServiceViewModel.isResponse.observe(getActivity(),dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();

                if(dynamicResponseModel.getApiName()== ApiConstant.GET_SELLER_CAT){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                binding.tvNotFount.setVisibility(View.GONE);
                                HomeCatModel model = new Gson().fromJson(stringResponse,HomeCatModel.class);
                                binding.productListRecycler.setAdapter(new MyServiceAdapter(getActivity(), (ArrayList<HomeCatModel.Result>) model.getResult()));
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            binding.tvNotFount.setVisibility(View.VISIBLE);
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
        myServiceViewModel.isLoading.observe(getActivity(),aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        callHomeCat();

    }
}
