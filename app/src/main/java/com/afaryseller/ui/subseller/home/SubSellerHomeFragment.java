package com.afaryseller.ui.subseller.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;

import com.afaryseller.databinding.FragmentSubSellerHomeBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.home.adapter.SliderAdapterExample;
import com.afaryseller.ui.home.model.BannerModal1;
import com.afaryseller.ui.login.model.LoginModel;
import com.afaryseller.ui.notifications.NotificationAct;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubSellerHomeFragment extends BaseFragment<FragmentSubSellerHomeBinding, SubSellerHomeViewModel> {

    FragmentSubSellerHomeBinding binding;
    SubSellerHomeViewModel homeViewModel;
    private ArrayList<String> banner_array_list;
    SliderAdapterExample adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_seller_home, container, false);
        homeViewModel = new SubSellerHomeViewModel();
        binding.setHomeViewModel(homeViewModel);
        binding.getLifecycleOwner();
        homeViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {
        banner_array_list = new ArrayList<>();

        binding.tvFullAddress.setText(DataManager.getInstance().getUserData(getActivity()).getResult().getAddress()
              /*  DataManager.getInstance().getUserData(getActivity()).getResult().getCityName() + "," +
                DataManager.getInstance().getUserData(getActivity()).getResult().getStateName()+"," +
                DataManager.getInstance().getUserData(getActivity()).getResult().getCountryName()*/);


        if (DataManager.getInstance().getUserData(requireActivity()).getResult().getType().equals(Constant.SUBADMIN)) {
            binding.tvName.setText(getString(R.string.hello) + " " + DataManager.getInstance().getUserData(getActivity()).getResult().getUserName());
            binding.rlShop.setVisibility(View.GONE);
            binding.tvNotFount.setVisibility(View.GONE);

        }
        else {
            binding.tvName.setText(getString(R.string.hello) + " " + DataManager.getInstance().getUserData(getActivity()).getResult().getSubSellerName());
            if(!SessionManager.readString(requireContext(),Constant.shopName,"").equals("")) {
                binding.rlShop.setVisibility(View.VISIBLE);
                binding.tvNotFount.setVisibility(View.GONE);
                binding.tvShopName.setText(SessionManager.readString(requireContext(), Constant.shopName, ""));
            }
            else {
                binding.rlShop.setVisibility(View.GONE);
                binding.tvNotFount.setVisibility(View.VISIBLE);
            }
        }

        binding.imgSearch.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_fragment_to_search);
        });

        binding.rlNotification.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotificationAct.class)));

        binding.rlShop.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("id","");
            Navigation.findNavController(v).navigate(R.id.action_sub_seller_home_fragment_to_orderList,bundle);
        });

        observeLoader();
        observeResponse();
        callSliderImg();





    }



    private void callSliderImg() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        //  headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("country_id", DataManager.getInstance().getUserData(getActivity()).getResult().getCountry());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        homeViewModel.getSliderImg(getActivity(), headerMap, map);

    }



    public void observeResponse() {
        homeViewModel.isResponse.observe(getActivity(), dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.SLIDER_HOME) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);

                            if (jsonObject.getString("status").toString().equals("1")) {
                                BannerModal1 bannerModal1 = new Gson().fromJson(stringResponse, BannerModal1.class);
                                binding.imageSlider.setVisibility(View.VISIBLE);
                                for (int i = 0; bannerModal1.getResult().size() > i; i++) {
                                    banner_array_list.add(bannerModal1.getResult().get(i).image);
                                }
                                adapter = new SliderAdapterExample(getActivity(), banner_array_list);
                                binding.imageSlider.setSliderAdapter(adapter);
                                binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                                binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                                binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                                //    binding.imageSlider2.setIndicatorSelectedColor(R.color.colorPrimary);
                                //    binding.imageSlider2.setIndicatorUnselectedColor(Color.GRAY);
                                binding.imageSlider.setScrollTimeInSec(3);
                                binding.imageSlider.setAutoCycle(true);
                                binding.imageSlider.startAutoCycle();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                binding.imageSlider.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }













            }
        });
    }

    private void observeLoader() {
        homeViewModel.isLoading.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }





}
