package com.afaryseller.ui.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityChatListBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.ui.signup.CountryModel;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.utility.DataManager;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatListAct extends AppCompatActivity implements ChatOnListener{
    public String TAG ="ChatListAct";
    ActivityChatListBinding binding;
    AfarySeller apiInterface;
    ChatListAdapter adapter;
    ArrayList<ChatListModel.Result>arrayList;
    ArrayList<CountryModel.Result>countryArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_list);
        apiInterface = ApiClient.getClient(this).create(AfarySeller.class);
        initViews();

    }

    private void initViews() {

        arrayList = new ArrayList<>();
        countryArrayList = new ArrayList<>();

        adapter = new ChatListAdapter(ChatListAct.this,arrayList,ChatListAct.this);
        binding.rvChatList.setAdapter(adapter);

        binding.backNavigation.setOnClickListener(view -> finish());


        getCountry();

    }


    private void getAllSeller(String CountryId) {
        DataManager.getInstance().showProgressMessage(ChatListAct.this,"Please wait...");
        Map<String, String> map = new HashMap<>();
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " + DataManager.getInstance().getUserData(ChatListAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");


        map.put("user_id", DataManager.getInstance().getUserData(ChatListAct.this).getResult().getId());
        map.put("country_id",CountryId);
        map.put("register_id", DataManager.getInstance().getUserData(ChatListAct.this).getResult().getRegisterId());

        Log.e(TAG, "Chat msg Request :" + map);
        Call<ResponseBody> loginCall = apiInterface.getSellerChatListApi(headerMap,map);
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Chat msg Response :" + object);
                    if (object.optString("status").equals("1")) {
                        ChatListModel model = new Gson().fromJson(responseData,ChatListModel.class);
                        arrayList.clear();
                        arrayList.addAll(model.getResult());
                        adapter.notifyDataSetChanged();

                    } else if (object.optString("status").equals("0")) {
                        arrayList.clear();
                        adapter.notifyDataSetChanged();

                        // Toast.makeText(OrderDetailsAct.this, data.message /*getString(R.string.wrong_username_password)*/, Toast.LENGTH_SHORT).show();
                    }

                    else if (object.getString("status").equals("5")) {
                        startActivity(new Intent(ChatListAct.this, SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
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

    private void getCountry( ) {
        DataManager.getInstance().showProgressMessage(ChatListAct.this,"Please wait...");
        Call<ResponseBody> loginCall = apiInterface.getCountry();
        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();
                try {
                    String responseData = response.body() != null ? response.body().string() : "";
                    JSONObject object = new JSONObject(responseData);
                    Log.e(TAG, "Get Country Response :" + object);
                    if (object.optString("status").equals("1")) {
                        CountryModel countryModel = new Gson().fromJson(responseData, CountryModel.class);
                        countryArrayList.clear();
                        countryArrayList.addAll(countryModel.getResult());
                        for (int i=0;i<countryArrayList.size();i++){
                            if(DataManager.getInstance().getUserData(ChatListAct.this).getResult().getCountryName().equals(countryArrayList.get(i).getName()))
                            {
                                getAllSeller(countryArrayList.get(i).getId());

                            }
                        }

                    } else if (object.optString("status").equals("0")) {
                        countryArrayList.clear();

                        // Toast.makeText(OrderDetailsAct.this, data.message /*getString(R.string.wrong_username_password)*/, Toast.LENGTH_SHORT).show();
                    }

                    else if (object.getString("status").equals("5")) {
                        startActivity(new Intent(ChatListAct.this, SplashAct.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
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




    @Override
    public void onChat(int Position, ChatListModel.Result result) {
            startActivity(new Intent(this, ChatAct.class)
                    .putExtra("UserId", result.getId())
                    .putExtra("UserName",result.getUserName())
                    .putExtra("UserImage",result.getImage())
                    .putExtra("id",DataManager.getInstance().getUserData(ChatListAct.this).getResult().getId())
                    .putExtra("name",DataManager.getInstance().getUserData(ChatListAct.this).getResult().getName())
                    .putExtra("img",DataManager.getInstance().getUserData(ChatListAct.this).getResult().getImage()));

    }



}
