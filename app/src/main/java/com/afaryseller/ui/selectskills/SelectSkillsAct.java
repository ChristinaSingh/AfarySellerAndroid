package com.afaryseller.ui.selectskills;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySelectSkillsBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.otp.OtpAct;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterFour;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterOne;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterThree;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterTwo;
import com.afaryseller.ui.selectskills.listener.SkillsListener;
import com.afaryseller.ui.selectskills.model.SkillModel;
import com.afaryseller.ui.signup.SignupAct;
import com.afaryseller.ui.signup.SignupViewModel;
import com.afaryseller.ui.uploadids.UploadIdsAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.NetworkAvailablity;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectSkillsAct extends BaseActivity<ActivitySelectSkillsBinding,SkillsViewModel> implements SkillsListener {
    public String TAG = "SelectSkillsAct";
    ActivitySelectSkillsBinding binding;
    SkillsViewModel skillsViewModel;
    private ArrayList<SkillModel.Result> arrayList;
    private ArrayList<SkillModel.TravelTicket> travelTicketArrayList;
    private ArrayList<SkillModel.RealEstate> realEstateArrayList;
    private ArrayList<SkillModel.HomeService> homeServiceArrayList;
    private ArrayList<String> arrayListOne;
    private ArrayList<String> arrayListTwo;
    private ArrayList<String> arrayListThree;
    private ArrayList<String> arrayListFour;
    SkillAdapterOne adapterOne;
    SkillAdapterTwo adapterTwo;
    SkillAdapterThree adapterThree;
    SkillAdapterFour adapterFour;

  //  private AfarySeller apiInterface;
    String main="",travel="",realEstate="",home="",userId="",type="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_select_skills);
      //  apiInterface = ApiClient.getClient(SelectSkillsAct.this).create(AfarySeller.class);
        skillsViewModel = new SkillsViewModel();
        binding.setSkillsViewModel(skillsViewModel);
        binding.getLifecycleOwner();
        skillsViewModel.init(SelectSkillsAct.this);
        initViews();

        observeLoader();
        observeResponse();

        HashMap<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        skillsViewModel.getSkills(SelectSkillsAct.this,map);

    }

    private void initViews() {

        if(getIntent()!=null) {
            userId = getIntent().getStringExtra("user_id");
            type = getIntent().getStringExtra("type");

        }

        arrayList = new ArrayList<>();
        travelTicketArrayList = new ArrayList<>();
        realEstateArrayList = new ArrayList<>();
        homeServiceArrayList = new ArrayList<>();

        arrayListOne = new ArrayList<>();
        arrayListTwo = new ArrayList<>();
        arrayListThree = new ArrayList<>();
        arrayListFour = new ArrayList<>();

        binding.rrBack.setOnClickListener(view -> finish());

        binding.nextSkill.setOnClickListener(view -> {
          /*  HashMap<String,String> map = new HashMap<>();
            map.put("user_id",userId);
            map.put("travel_ticket",travel);
            map.put("real_estate",realEstate);
            map.put("home_services",home);
            map.put("main_services",main);

            //map.put("password",binding.password.getText().toString());

            Log.e(TAG, " Add Skills Request ==="+ map);
            skillsViewModel.addSkills(SelectSkillsAct.this,map);*/

            if(type.equalsIgnoreCase("reg")) {
                startActivity(new Intent(this, UploadIdsAct.class)
                        .putExtra("user_id", userId));
                finish();
            }
            else finish();


        });


    /*    if (NetworkAvailablity.checkNetworkStatus(SelectSkillsAct.this)) getAllSkills();
        else Toast.makeText(this, getString(R.string.network_failure), Toast.LENGTH_SHORT).show();*/

    }


    public void observeResponse(){
        skillsViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.SELLER_GET_ALL_SKILLS){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get skills===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                SkillModel model = new Gson().fromJson(stringResponse, SkillModel.class);
                                arrayList.clear();

                                binding.text2.setVisibility(View.VISIBLE);
                                binding.text3.setVisibility(View.VISIBLE);
                                binding.text4.setVisibility(View.VISIBLE);
                                binding.nextSkill.setVisibility(View.VISIBLE);

                                travelTicketArrayList.clear();
                                realEstateArrayList.clear();
                                homeServiceArrayList.clear();




                                arrayList.addAll(model.getResult());
                                travelTicketArrayList.addAll(model.getTravelTicket());
                                realEstateArrayList.addAll(model.getRealEstate());
                                homeServiceArrayList.addAll(model.getHomeServices());

                                adapterOne = new SkillAdapterOne(SelectSkillsAct.this,arrayList,SelectSkillsAct.this);
                                binding.recyclerViewSkill.setAdapter(adapterOne);

                                adapterTwo = new SkillAdapterTwo(SelectSkillsAct.this,travelTicketArrayList,SelectSkillsAct.this);
                                binding.recyclerViewSkill2.setAdapter(adapterTwo);

                                adapterThree = new SkillAdapterThree(SelectSkillsAct.this,realEstateArrayList,SelectSkillsAct.this);
                                binding.recyclerViewSkill3.setAdapter(adapterThree);

                                adapterFour = new SkillAdapterFour(SelectSkillsAct.this,homeServiceArrayList,SelectSkillsAct.this);
                                binding.recyclerViewSkill4.setAdapter(adapterFour);


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                arrayList.clear();
                                travelTicketArrayList.clear();
                                realEstateArrayList.clear();
                                homeServiceArrayList.clear();
                                adapterOne.notifyDataSetChanged();
                                adapterTwo.notifyDataSetChanged();
                                adapterThree.notifyDataSetChanged();
                                adapterFour.notifyDataSetChanged();
                                binding.text2.setVisibility(View.GONE);
                                binding.text3.setVisibility(View.GONE);
                                binding.text4.setVisibility(View.GONE);
                                binding.nextSkill.setVisibility(View.GONE);

                                Toast.makeText(SelectSkillsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SelectSkillsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.SELLER_SKILLS){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response add skills===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                               // JSONObject object = jsonObject.getJSONObject("result");
                                //  Toast.makeText(SignupAct.this, getString(R.string.p), Toast.LENGTH_SHORT).show()

                           /*  if(type.equalsIgnoreCase("reg")) {
                                 startActivity(new Intent(this, UploadIdsAct.class)
                                         .putExtra("user_id", userId));
                                 finish();
                             }
                             else finish();*/


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SelectSkillsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SelectSkillsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        } );
    }

    private void observeLoader() {
        skillsViewModel.isLoading.observe(this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SelectSkillsAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

    @Override
    public void skills(String type, String id, String value,String selectValue,int position) {
        if(type.equalsIgnoreCase("main")){
            arrayListOne.add(id);
            arrayList.get(position).setStatus(selectValue);
            main = Constant.commaSeprated(arrayListOne);
            adapterOne.notifyDataSetChanged();
            Log.e("main item===",main);
            addSkills(id,selectValue);
        }
        if(type.equalsIgnoreCase("travel")){
            arrayListTwo.add(id);
            travelTicketArrayList.get(position).setStatus(selectValue);
            adapterTwo.notifyDataSetChanged();
            travel = Constant.commaSeprated(arrayListTwo);
            Log.e("travel item===",travel);
            addSkills(id,selectValue);

        }
        if(type.equalsIgnoreCase("real estate")){
            arrayListThree.add(id);
            realEstateArrayList.get(position).setStatus(selectValue);
            adapterThree.notifyDataSetChanged();
            realEstate = Constant.commaSeprated(arrayListThree);
            Log.e("realEstate item===",realEstate);
            addSkills(id,selectValue);

        }
        if(type.equalsIgnoreCase("home")){
            arrayListFour.add(id);
            homeServiceArrayList.get(position).setStatus(selectValue);
            adapterFour.notifyDataSetChanged();
           home = Constant.commaSeprated(arrayListFour);
            Log.e("home item===",home);
            addSkills(id,selectValue);

        }

    }

    public void addSkills(String skillsId,String status){
        HashMap<String,String> map = new HashMap<>();
        map.put("user_id",userId);
        map.put("skills_id",skillsId);
        map.put("status",status);
        skillsViewModel.addSkills(SelectSkillsAct.this,map);

    }


/*
    private void getAllSkills() {
        DataManager.getInstance().showProgressMessage(SelectSkillsAct.this, getString(R.string.please_wait));
        Call<ResponseBody> call = apiInterface.getSkillApi();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.e(TAG,"getProduct Item Response = " + responseString);
                    if(jsonObject.getString("status").equals("1")) {
                        SkillModel model = new Gson().fromJson(responseString, SkillModel.class);
                        arrayList.clear();

                        binding.text2.setVisibility(View.VISIBLE);
                        binding.text3.setVisibility(View.VISIBLE);
                        binding.text4.setVisibility(View.VISIBLE);

                        travelTicketArrayList.clear();
                        realEstateArrayList.clear();
                        homeServiceArrayList.clear();




                        arrayList.addAll(model.getResult());
                        travelTicketArrayList.addAll(model.getTravelTicket());
                        realEstateArrayList.addAll(model.getRealEstate());
                        homeServiceArrayList.addAll(model.getHomeServices());

                        binding.recyclerViewSkill.setAdapter(new SkillAdapterOne(SelectSkillsAct.this,arrayList));
                        binding.recyclerViewSkill2.setAdapter(new SkillAdapterTwo(SelectSkillsAct.this,travelTicketArrayList));
                        binding.recyclerViewSkill3.setAdapter(new SkillAdapterThree(SelectSkillsAct.this,realEstateArrayList));
                        binding.recyclerViewSkill4.setAdapter(new SkillAdapterFour(SelectSkillsAct.this,homeServiceArrayList));


                    } else {
                        arrayList.clear();
                        travelTicketArrayList.clear();
                        realEstateArrayList.clear();
                        homeServiceArrayList.clear();
                        binding.text2.setVisibility(View.GONE);
                        binding.text3.setVisibility(View.GONE);
                        binding.text4.setVisibility(View.GONE);
                    }

                } catch (Exception e) {
                    // Toast.makeText(mContext, "Exception = " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Exception","Exception = " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                DataManager.getInstance().hideProgressMessage();
            }
        });
    }
*/



}
