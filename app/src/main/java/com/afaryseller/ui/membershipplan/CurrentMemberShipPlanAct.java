package com.afaryseller.ui.membershipplan;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityCurrentMembershipPlanBinding;
import com.afaryseller.databinding.ActivitySubscriptionBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.subscription.SubscriptionAct;
import com.afaryseller.ui.subscription.SubscriptionViewModel;
import com.afaryseller.ui.subscription.adapter.SubscriptionAdapter;
import com.afaryseller.ui.subscription.model.SubscriptionModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CurrentMemberShipPlanAct extends BaseActivity<ActivityCurrentMembershipPlanBinding,MembershipViewModel> {
    public String TAG = "CurrentMemberShipPlanAct";
    ActivityCurrentMembershipPlanBinding binding;
    MembershipViewModel membershipViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_membership_plan);
        membershipViewModel = new MembershipViewModel();
        binding.setMembershipViewModel(membershipViewModel);
        binding.getLifecycleOwner();
        membershipViewModel.init(CurrentMemberShipPlanAct.this);

        observeLoader();
        observeResponse();


        binding.ivBack.setOnClickListener(v -> finish());


        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(CurrentMemberShipPlanAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        Map<String,String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(CurrentMemberShipPlanAct.this).getResult().getId());

        membershipViewModel.getCurrentMembershipPlan(CurrentMemberShipPlanAct.this,headerMap,map);
    }


    public void observeResponse(){
        membershipViewModel.isResponse.observe(CurrentMemberShipPlanAct.this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();

                if(dynamicResponseModel.getApiName()== ApiConstant.GET_CURRENT_MEMBERSHIP_PLAN){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get current membership plan===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject dataObj = jsonObject.getJSONObject("data");
                                JSONObject currentObj = dataObj.getJSONObject("current");

                                binding.tvNoPlanActive.setVisibility(View.GONE);
                                binding.llActivePlan.setVisibility(View.VISIBLE);


                                binding.tvTitle.setText(currentObj.getString("plan_name"));
                                binding.tvPeriod.setText(currentObj.getString("valid_days")+" " + getString(R.string.days));
                                binding.tvPrice.setText(currentObj.getString("currency")+currentObj.getString("price"));

                                binding.tvNumberOfDayRemaining.setText( getString(R.string.numner_of_days_remaining)+ " : "+currentObj.getInt("number_of_day_remaing"));



                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                    binding.tvQuantity.setText(Html.fromHtml(  getString(R.string.product_quantity) +  " : " + currentObj.getString("product_quantity")+ " " + getString(R.string.remaining) ,Html.FROM_HTML_MODE_COMPACT));
                                    binding.tvCommission.setText(Html.fromHtml(currentObj.getString("features2"),Html.FROM_HTML_MODE_COMPACT));
                                    binding.tvAgent3.setText(Html.fromHtml(currentObj.getString("features3"),Html.FROM_HTML_MODE_COMPACT));

                                } else {
                                    binding.tvQuantity.setText(Html.fromHtml(getString(R.string.product_quantity) +  " : " +currentObj.getString("product_quantity") + " " + getString(R.string.remaining)));
                                    binding.tvCommission.setText(Html.fromHtml(currentObj.getString("features2")));
                                    binding.tvAgent3.setText(Html.fromHtml(currentObj.getString("features3")));


                                }

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                binding.tvNoPlanActive.setVisibility(View.VISIBLE);
                                binding.llActivePlan.setVisibility(View.GONE);

                               // Toast.makeText(CurrentMemberShipPlanAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(CurrentMemberShipPlanAct.this);
                            }

                        }
                        else {
                            Toast.makeText(CurrentMemberShipPlanAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } );
    }

    private void observeLoader() {
        membershipViewModel.isLoading.observe(CurrentMemberShipPlanAct.this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(CurrentMemberShipPlanAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

}
