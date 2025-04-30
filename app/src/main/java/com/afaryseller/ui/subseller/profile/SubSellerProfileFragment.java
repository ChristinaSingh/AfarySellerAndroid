package com.afaryseller.ui.subseller.profile;

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

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.FragmentSubSellerProfileBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SubSellerProfileFragment extends BaseFragment<FragmentSubSellerProfileBinding, SubSellerProfileViewModel> {
    public String TAG = "SubSellerProfileFrag";
    FragmentSubSellerProfileBinding binding;
    SubSellerProfileViewModel subSellerProfileViewModel;
    String subSellerId="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_seller_profile, container, false);
        subSellerProfileViewModel = new SubSellerProfileViewModel();
        binding.setSubSellerProfileViewModel(subSellerProfileViewModel);
        binding.getLifecycleOwner();
        subSellerProfileViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }


    private void initViews() {




        /*binding.backNavigation.setOnClickListener(v -> finish());


        binding.txtupdate.setOnClickListener(v -> startActivity(new Intent(SubSellerProfileAct.this, UpdateSubSellerProfileAct.class)
                .putExtra("subSellerId",subSellerId)));


        binding.txtChangePassword.setOnClickListener(v -> startActivity(new Intent(SubSellerProfileAct.this, SubSellerChangePasswordAct.class)
                .putExtra("subSellerId",subSellerId)));

        binding.txtDeleteAccount.setOnClickListener(v -> deleteAccountDialog());*/

        observeLoader();
        observeResponse();
        getSubSellerProfile();

        binding.RRSubLogout.setOnClickListener(v -> {
                    showLogoutDialog();
                }
        );

    }


    public void observeResponse() {
        subSellerProfileViewModel.isResponse.observe(requireActivity(), dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();

                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SUB_SELLER_PROFILE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("get sub seller profile response===", dynamicResponseModel.getJsonObject().toString());

                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("data");
                                //  binding.tvName.setText(object.getString("name") + " " + object.getString("last_name"));
                                subSellerId = object.getString("id");
                                binding.tvName.setText(object.getString("name") );

                                binding.tvEmail.setText(object.getString("email"));
                                binding.tvAddress.setText(object.getString("address"));
                                //binding.tvEmail.setText(object.getString("email"));


                                Glide.with(requireActivity()).load(object.getString("image"))
                                        .error(R.drawable.user_default)
                                        .placeholder(R.drawable.user_default)
                                        .into(binding.ivSeller);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(requireActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }




                        } else {
                            Toast.makeText(requireActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }






            }
        });
    }



    private void observeLoader() {
        subSellerProfileViewModel.isLoading.observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                showProgressDialog(requireActivity(), false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }



    private void getSubSellerProfile() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(requireActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("sub_seller_id", SessionManager.readString(requireActivity(), Constant.SUB_SELLER_ID,""));
        subSellerProfileViewModel.getSubSellerProfile(requireActivity(),headerMap, map);
    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.are_you_sure_logout))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SessionManager.clearSession(requireActivity());
                        Intent intent = new Intent(requireActivity(), SplashAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
