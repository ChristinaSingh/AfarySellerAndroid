package com.afaryseller.ui.home.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import com.afaryseller.databinding.FragmentHomeBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.changepassword.ChangePasswordAct;
import com.afaryseller.ui.home.adapter.HomeCatAdapter;
import com.afaryseller.ui.home.adapter.SliderAdapterExample;
import com.afaryseller.ui.home.model.BannerModal1;
import com.afaryseller.ui.home.model.HomeCatModel;
import com.afaryseller.ui.login.model.LoginModel;
import com.afaryseller.ui.notifications.NotificationAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class HomeFragment extends BaseFragment<FragmentHomeBinding, HomeViewModel> {
    FragmentHomeBinding binding;
    HomeViewModel homeViewModel;
    private ArrayList<String> banner_array_list;
    SliderAdapterExample adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeViewModel = new HomeViewModel();
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
        }
        else {
            binding.tvName.setText(getString(R.string.hello) + " " + DataManager.getInstance().getUserData(getActivity()).getResult().getSubSellerName());

        }

        binding.imgSearch.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_home_fragment_to_search);
        });

        binding.rlNotification.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotificationAct.class)));


        observeLoader();
        observeResponse();
        callHomeCat();
    }

    @Override
    public void onResume() {
        super.onResume();
        callCounterNotification();

    }

    private void callHomeCat() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        map.put("sub_seller_id", DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id());
        map.put("shop_id",SessionManager.readString(requireActivity(), Constant.shopId, ""));
        homeViewModel.homeCat(getActivity(), headerMap, map);

    }


    public void callProfile() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        homeViewModel.getSellerProfile(getActivity(), headerMap, map);
    }


    private void callCounterNotification() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        homeViewModel.getNotificationCounter(getActivity(), headerMap, map);

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



    private void changeUserPasswordStatus() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        homeViewModel.changeUserPasswordStatus(getActivity(), map);

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


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SELLER_CAT) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            callSliderImg();
                            if (jsonObject.getString("status").toString().equals("1")) {
                                binding.tvNotFount.setVisibility(View.GONE);
                                HomeCatModel model = new Gson().fromJson(stringResponse, HomeCatModel.class);
                                binding.skillRecycler.setAdapter(new HomeCatAdapter(getActivity(), (ArrayList<HomeCatModel.Result>) model.getResult()));
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        } else {
                            binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.NOTIFICATIONS_COUNTER) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            callProfile();
                            if (jsonObject.getString("status").toString().equals("1")) {
                                if (!jsonObject.getString("result").equals("0")) {
                                    binding.tvCounter.setVisibility(View.VISIBLE);
                                    binding.tvCounter.setText(jsonObject.getString("result"));
                                } else binding.tvCounter.setVisibility(View.GONE);


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                binding.tvCounter.setVisibility(View.GONE);
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        } else {
                            binding.tvCounter.setVisibility(View.GONE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_PROFILE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response===", jsonObject.toString());
                            JSONObject object = jsonObject.getJSONObject("result");
                            LoginModel loginModel = new Gson().fromJson(stringResponse, LoginModel.class);
                            if (DataManager.getInstance().getUserData(requireActivity()).getResult().getType().equals(Constant.SUBADMIN)) {
                                SessionManager.writeString(requireActivity(), Constant.SELLER_INFO, stringResponse);
                                SessionManager.writeString(requireActivity(), Constant.shopId, loginModel.getResult().getShopId());


                            }
                            SessionManager.writeString(requireActivity(), Constant.SELLER_LANGUAGE, loginModel.getResult().getLanguage());

                              /*  if(object.getString("language")!=null){
                                    if(object.getString("language").equalsIgnoreCase("fr")) updateResources(requireActivity(),"fr");
                                        else updateResources(requireActivity(),"en");
                                }*/
                            if (jsonObject.getString("status").toString().equals("1")) {
                                if (jsonObject.getJSONObject("result").getString("seller_status").equals("Deactive")) {
                                    if (DataManager.getInstance().getUserData(getActivity()) != null) {
                                        SessionManager.logout(getActivity());
                                        Toast.makeText(getActivity(), getString(R.string.currenty_your_account_deactive), Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    if (object.getString("password_request_status").equalsIgnoreCase("CHANGE_BY_ADMIN"))
                                        openResetPasswordAlert();

                                }

                            } else if (jsonObject.getString("status").toString().equals("0")) {
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



                if (dynamicResponseModel.getApiName() == ApiConstant.CHANGE_USER_PASSWORD_STATUS) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response===", stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                            }

                        } else {
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }




            }
        });
    }


    private void updateResources(Context wellcomeScreen, String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Resources resources = wellcomeScreen.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

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


    private void openResetPasswordAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getString(R.string.please_change_your_password))
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        startActivity(new Intent(getActivity(), ChangePasswordAct.class));
                    }
                }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        changeUserPasswordStatus();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}
