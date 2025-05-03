package com.afaryseller.repository;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.afaryseller.core.DynamicResponseModel;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.retrofit.ApiConstant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Part;

public class SellerRepository {
    private AfarySeller apiInterface;
    private MutableLiveData<Boolean> isLoadingData = new MutableLiveData();
    private MutableLiveData<DynamicResponseModel> apiResponseData = new MutableLiveData();
    Context context;
   public LiveData<Boolean> isLoading;
    public LiveData<DynamicResponseModel> isResponse;


    public SellerRepository() {
        apiInterface = ApiClient.getClient(context).create(AfarySeller.class);
       // isLoadingData = new MutableLiveData();
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoadingData;
    }


    public LiveData<DynamicResponseModel> getIsResponse() {
        return apiResponseData;
    }

    public void sellerLogin(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerLogin(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SELLER_LOGIN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SELLER_LOGIN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void subSellerLogin(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.subSellerLoginApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SUB_SELLER_LOGIN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SUB_SELLER_LOGIN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }





    public void sellerRegister(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerSignup(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SIGNUP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SIGNUP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void subSellerSignupRepo(Map<String, String> auth,RequestBody name, RequestBody username, RequestBody email,
                                  RequestBody password, RequestBody countryCode, RequestBody mobile,
                                  RequestBody parent_seller_id,RequestBody country,RequestBody state,
                                  RequestBody city,RequestBody address,RequestBody shopId,
                                  MultipartBody.Part image) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.subSellerSignup(auth,name,username,email,password,countryCode,mobile,parent_seller_id,country,state,city,address,shopId,image);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SUB_SELLER_SIGNUP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SUB_SELLER_SIGNUP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void getCity(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllCity(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ALL_CITY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ALL_CITY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getAllSkills(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSkillApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SELLER_GET_ALL_SKILLS, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SELLER_GET_ALL_SKILLS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void SendOtpRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sendOtpApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SEND_OTP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SEND_OTP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void SendWhatsappOtpRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sendWhatsappOtpApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SEND_WHATSAPP_OTP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SEND_WHATSAPP_OTP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }







    public void verifyOtpRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.verifyOtpApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.VERIFY_OTP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.VERIFY_OTP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }





    public void sellerSkills(HashMap<String,String> map) {
       // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerAddSkillsApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SELLER_SKILLS, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SELLER_SKILLS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void sellerUploadIds(RequestBody userId, MultipartBody.Part img1, MultipartBody.Part img2) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerAddSkillsApi(userId,img1,img2);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SELLER_UPDATE_DOCUMENT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SELLER_UPDATE_DOCUMENT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void sliderHomeImg(Map<String, String> auth,HashMap<String, String> map) {
      //  isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSlider(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SLIDER_HOME, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SLIDER_HOME,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getAddedCat(Map<String,String>auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSellerCat(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_SELLER_CAT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_SELLER_CAT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getCurrencyRepo() {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getCurrency();
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_CURRENCY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_CURRENCY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getCountry() {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getCountry();
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ALL_COUNTRY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ALL_COUNTRY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getStates(HashMap<String,String> map) {
      // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllState(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ALL_STATE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ALL_STATE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getStatesCity(HashMap<String,String> map) {
       // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllStateCity(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
             //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ALL_STATE_CITY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ALL_STATE_CITY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void getSellerProfile(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSellerProfileApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_PROFILE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_PROFILE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void getSellerProfile22(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSellerProfileApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_PROFILE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_PROFILE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getSellerProfileUpdateRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSellerProfileUpdateApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_PROFILE_UPDATE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_PROFILE_UPDATE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void updateLanguageRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.updateLanguageApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.UPDATE_LANGUAGE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.UPDATE_LANGUAGE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }





    public void changeSellePass(Map<String,String>auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.changeSellerPassApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.CHANGE_PASSWORD, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.CHANGE_PASSWORD,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void addSellerShop(Map<String, String> auth,RequestBody userId,RequestBody shopName,RequestBody subCateId,
                              RequestBody description,RequestBody address,RequestBody streetLandmark,RequestBody neighbourhood,
                              RequestBody country,RequestBody state,RequestBody city,RequestBody currency,RequestBody countryCode,RequestBody phone,
                              RequestBody phonenumber, RequestBody mobileaccount,
                              RequestBody lat, RequestBody lon,RequestBody registerId,RequestBody userSellerId,MultipartBody.Part img1,MultipartBody.Part img2,MultipartBody.Part img3) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerAddShopApi(auth,userId,shopName,subCateId,description,address,streetLandmark,neighbourhood,country,
                state,city,currency,countryCode,phone,phonenumber,mobileaccount,lat,lon,registerId,userSellerId,img1,img2,img3);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_SHOP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_SHOP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getDailyCloseDay(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getDailyCloseDayApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_DAILY_CLOSE_DAY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_DAILY_CLOSE_DAY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void AddDailyCloseDayRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addDailyCloseDayApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_DAILY_CLOSE_DAY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_DAILY_CLOSE_DAY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void AddOpenTimeRepo(Map<String, String> auth,HashMap<String,String> map) {
       // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addOpenTimeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
              //  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_OPEN_TIME, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_OPEN_TIME,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
           //     isLoadingData.setValue(false);
            }
        });
    }

    public void UpdateOpenTimeRepo(Map<String, String> auth,HashMap<String,String> map) {
        // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.updateOpenTimeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.UPDATE_OPEN_TIME, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.UPDATE_OPEN_TIME,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //     isLoadingData.setValue(false);
            }
        });
    }



    public void getEditOpenTimeRepo(Map<String, String> auth,HashMap<String,String> map) {
        // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getEditOpenTimeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_EDIT_OPEN_TIME, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_EDIT_OPEN_TIME,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //     isLoadingData.setValue(false);
            }
        });
    }

    public void AddCloseTimeRepo(Map<String, String> auth,HashMap<String,String> map) {
        // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addCloseTimeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_CLOSE_TIME, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_CLOSE_TIME,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //     isLoadingData.setValue(false);
            }
        });
    }

    public void updateCloseTimeRepo(Map<String, String> auth,HashMap<String,String> map) {
        // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.updateCloseTimeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.UPDATE_CLOSE_TIME, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.UPDATE_CLOSE_TIME,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //     isLoadingData.setValue(false);
            }
        });
    }


    public void getEditCloseTimeRepo(Map<String, String> auth,HashMap<String,String> map) {
        // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getEditCloseTimeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_EDIT_CLOSE_TIME, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_EDIT_CLOSE_TIME,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                //     isLoadingData.setValue(false);
            }
        });
    }



    public void getAllHolidaysRepo(Map<String, String> auth,HashMap<String,String> map) {
         isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllHolidaysApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_HOLIDAY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_HOLIDAY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                    isLoadingData.setValue(false);
            }
        });
    }


    public void addHolidaysRepo(Map<String, String> auth,HashMap<String,String> map) {
         isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addAllHolidaysApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_HOLIDAY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_HOLIDAY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                     isLoadingData.setValue(false);
            }
        });
    }

    public void deleteHolidaysRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.deleteHolidaysApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.DELETE_HOLIDAY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.DELETE_HOLIDAY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void getAllShopRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllShopApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ALL_SHOP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ALL_SHOP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void editSellerShop(Map<String, String> auth,RequestBody shopId,RequestBody userId,RequestBody shopName,RequestBody subCateId,
                              RequestBody description,RequestBody address,RequestBody streetLandmark,RequestBody neighbourhood,
                              RequestBody country,RequestBody state,RequestBody city,RequestBody countryCode,RequestBody phone,
                              RequestBody phonenumber, RequestBody mobileaccount,
                              RequestBody lat, RequestBody lon,RequestBody registerId,RequestBody userSellerId,MultipartBody.Part img1,MultipartBody.Part img2,MultipartBody.Part img3) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerEditShopApi(auth,shopId,userId,shopName,subCateId,description,address,streetLandmark,neighbourhood,country,
                state,city,countryCode,phone,phonenumber,mobileaccount,lat,lon,registerId,userSellerId,img1,img2,img3);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.EDIT_SHOP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.EDIT_SHOP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getAllMainCategoryRepo(Map<String, String> auth) {
        //isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getMainCategoryApi(auth);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_MAIN_CATEGORY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_MAIN_CATEGORY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getAllMainSubCategoryRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getMainSubCategoryApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_MAIN_SUB_CATEGORY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_MAIN_SUB_CATEGORY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void getBrandsRepo(Map<String, String> auth,HashMap<String,String> map) {
        // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getBrandApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_BRAND, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_BRAND,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void addBrandsRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addBrandApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_BRAND, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_BRAND,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void addAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void getAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
         isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getAttributeNewRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAttributeNewApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ATTRIBUTE_NEW, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ATTRIBUTE_NEW,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void deleteAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.deleteAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.DELETE_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.DELETE_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }





    public void addSubAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addSubAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_SUB_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_SUB_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void getSubAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSubAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_SUB_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_SUB_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void deleteSubAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.deleteSubAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.DELETE_SUB_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.DELETE_SUB_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }







    public void addSellerShopProduct(Map<String, String> auth,RequestBody userId, RequestBody shopId, RequestBody productName,
                                     RequestBody product_price, RequestBody description, RequestBody sku,
                                     RequestBody delivery_charges,RequestBody inStock, RequestBody category_id, RequestBody sub_category, RequestBody product_brand, RequestBody attribute
                                     ,RequestBody registerId,RequestBody userSellerId,RequestBody qty, MultipartBody.Part img1, MultipartBody.Part img2,MultipartBody.Part img3,MultipartBody.Part img4) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerAddShopProductApi(auth,userId,shopId,productName,product_price,description,sku,delivery_charges,inStock,
                category_id,sub_category,product_brand,attribute,registerId,userSellerId,qty,img1,img2,img3,img4);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_SHOP_PRODUCT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_SHOP_PRODUCT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getShopDetailsRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getShopDetailApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_SHOP_DETAIL_PRODUCT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_SHOP_DETAIL_PRODUCT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void updateProductRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.updateProductApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.PRODUCT_ACTIVE_DEACTIVE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.PRODUCT_ACTIVE_DEACTIVE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void updateProfileRepo(Map<String, String> auth,RequestBody userId, RequestBody user_name, RequestBody address,
                                     RequestBody lat, RequestBody lon, RequestBody country,
                                     RequestBody city,RequestBody registerId,RequestBody userSellerId,MultipartBody.Part img1) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.updateProfileApi(auth,userId,user_name,address,lat,lon,country,city,registerId,userSellerId,img1);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.UPDATE_PROFILE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.UPDATE_PROFILE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void updateShopRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.updateShopApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SHOP_ACTIVE_DEACTIVE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SHOP_ACTIVE_DEACTIVE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getSubscriptionPlanRepo(Map<String, String> auth,String countryId) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSubscriptionApi(auth,countryId);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_SUBSCRIPTION_PLAN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_SUBSCRIPTION_PLAN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


 /*   public void purchaseSubscriptionPlanRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.purchaseSubscriptionPayApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.PAY_SUBSCRIPTION_PLAN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.PAY_SUBSCRIPTION_PLAN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }*/


    public void purchaseSubscriptionPlanRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.purchaseSubscriptionPayApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.PAY_SUBSCRIPTION_PLAN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.PAY_SUBSCRIPTION_PLAN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void currentMembershipPlanRepo(Map<String, String> auth,Map<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.currentMemberShipPlanApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_CURRENT_MEMBERSHIP_PLAN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_CURRENT_MEMBERSHIP_PLAN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



/*
    public void purchaseZeroSubscriptionPlanRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.purchaseZeroSubscriptionPayApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }
*/

    public void purchaseZeroSubscriptionPlanRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.purchaseZeroSubscriptionPayApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void addedProductSubCatRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addedProductSubCatApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADDED_PRODUCT_SUBCAT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADDED_PRODUCT_SUBCAT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void addedProductAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.addedProductAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADDED_PRODUCT_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADDED_PRODUCT_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getProductDetailRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getProductDetailApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_PRODUCT_DETAILS, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_PRODUCT_DETAILS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }






    public void editSellerShopProduct( Map<String, String> auth,RequestBody proId, RequestBody productName,
                                     RequestBody product_price, RequestBody description, RequestBody sku,
                                     RequestBody delivery_charges,RequestBody inStockS, RequestBody category_id, RequestBody sub_category, RequestBody product_brand, RequestBody attribute
            ,RequestBody registerId,RequestBody userSellerId,RequestBody qty, MultipartBody.Part img1, MultipartBody.Part img2,MultipartBody.Part img3,MultipartBody.Part img4) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sellerEditShopProductApi(auth,proId,productName,product_price,description,sku,delivery_charges,inStockS,
                category_id,sub_category,product_brand,attribute,registerId,userSellerId,qty,img1,img2,img3,img4);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.EDIT_SHOP_PRODUCT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.EDIT_SHOP_PRODUCT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getAllNotificationsRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllNotificationApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_NOTIFICATIONS, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_NOTIFICATIONS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void getNotificationCounterRepo(Map<String, String> auth,HashMap<String,String> map) {
      //  isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getNotificationCounterApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            //    isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.NOTIFICATIONS_COUNTER, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.NOTIFICATIONS_COUNTER,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getAllOrderRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllOrderApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ALL_ORDER, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ALL_ORDER,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getOrderDetailsRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getOrderDetailsApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ORDER_DETAIL, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ORDER_DETAIL,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void internationalDeliveryTransitRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.internationalDeliveryTransitApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.INTERNATIONAL_TRANSIT_DELIVERY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.INTERNATIONAL_TRANSIT_DELIVERY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }









    public void completeSelfCollectOrderRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.completeSelfCollectOrderApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.COMPLETE_SELF_COLLECT_ORDER, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.COMPLETE_SELF_COLLECT_ORDER,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void acceptDeclineOrderRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.acceptDeclineOrderApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ACCEPT_DECLINE_ORDER, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ACCEPT_DECLINE_ORDER,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void sendPushNotificationRepo(Map<String, String> auth,HashMap<String,String> map) {
       // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.sendNotificationApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SEND_PUSH_NOTIFICATION, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SEND_PUSH_NOTIFICATION,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void generateTokenRepo(HashMap<String,String> map) {
        // isLoadingData.setValue(true);
        AfarySeller  apiInterface = ApiClient.getClient1(context).create(AfarySeller.class);
        Call<ResponseBody> call = apiInterface.getTokenApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GENERATE_TOKEN, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GENERATE_TOKEN,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }

    public void returnOrderRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.productReturnApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.RETURN_SELLER_PRODUCT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.RETURN_SELLER_PRODUCT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void addDeliveryRepo(Map<String,String> auth, RequestBody package_name, RequestBody owner,
                                RequestBody command_date, RequestBody adresse_source, RequestBody adresse_destination,
                                RequestBody delivery_type, RequestBody latitude_source, RequestBody longitude_source, RequestBody longitude_destination, RequestBody longitude_destination1,
                                RequestBody command_number, RequestBody delivery_amount , RequestBody command_amount, RequestBody customer_code, MultipartBody.Part img1) {
        isLoadingData.setValue(false);
       // AfarySeller  apiInterface = ApiClient.getClient1(context).create(AfarySeller.class);

        Call<ResponseBody> call = apiInterface.addDeliveryApi(auth,package_name,owner,command_date,adresse_source,adresse_destination,
                delivery_type,latitude_source,longitude_source,longitude_destination,longitude_destination1,command_number,delivery_amount,command_amount,customer_code,img1);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ADD_DELIVERY, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ADD_DELIVERY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void deleteProductRepo(Map<String, String> auth,HashMap<String,String> map) {
         isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.deleteProductApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.DELETE_PRODUCT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.DELETE_PRODUCT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void searchShopRepo(Map<String, String> auth,HashMap<String,String> map) {
       // isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.searchShopApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SEARCH_SHOP, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SEARCH_SHOP,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void checkPlanStatusRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.checkPlanStatusApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.CHECK_PLAN_STATUS, response.body(), response.code());
                     Log.e("response===",response.body()+"");
                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                        Log.e("responseError===",response.errorBody()+"");


                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.CHECK_PLAN_STATUS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getOnlineOrderHistoryRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getOnlineOrderHistoryApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.ONLINE_ORDER_HISTORY, response.body(), response.code());
                    Log.e("response===",response.body()+"");
                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                        Log.e("responseError===",response.errorBody()+"");


                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.ONLINE_ORDER_HISTORY,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }





    public void getAdminMsgRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAdminMsgApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ADMIN_MSG, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ADMIN_MSG,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void sendAdminMsgRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.notifyAdminApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SEND_ADMIN_MSG, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SEND_ADMIN_MSG,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }





    public void sendAdminMsgNewRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.notifyAdminNewApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SEND_ADMIN_MSG, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SEND_ADMIN_MSG,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void getAllSubSellerRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAllSubSellerApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ALL_SUB_SELLER, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ALL_SUB_SELLER,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void changeSubSellePassword(Map<String,String>auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.changeSubSellerPassApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SUB_SELLER_CHANGE_PASSWORD, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SUB_SELLER_CHANGE_PASSWORD,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getSubSellerProfileRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSubSellerProfileApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_SUB_SELLER_PROFILE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_SUB_SELLER_PROFILE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void deleteSubSellerRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.deleteSubSellerProfileApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.DELETE_SUB_SELLER_PROFILE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.DELETE_SUB_SELLER_PROFILE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void subSellerUpdateProfileRepo(Map<String, String> auth,RequestBody name, RequestBody username, RequestBody email,
                                    RequestBody subSellerId, RequestBody countryCode, RequestBody mobile,
                                    RequestBody parent_seller_id,RequestBody country,RequestBody state,
                                    RequestBody city,RequestBody address,RequestBody shop_Id,
                                    MultipartBody.Part image) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.updateSubSellerProfileApi(auth,name,username,email,subSellerId,countryCode,mobile,parent_seller_id,country,state,city,address,shop_Id,image);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.UPDATE_SUB_SELLER_PROFILE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.UPDATE_SUB_SELLER_PROFILE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getSellerRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSellerApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_SELLER_COUNTRY_WISE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_SELLER_COUNTRY_WISE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getAddedAttributeSubAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getAddedAttributeSubAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_ADDED_PRODUCT_ATTRIBUTE_SUB_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_ADDED_PRODUCT_ATTRIBUTE_SUB_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void deleteSubattributeInEditPageRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.deleteSubAttributeInEditPageApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.EDIT_PAGE_DELETE_SUB_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.EDIT_PAGE_DELETE_SUB_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void checkUncheckAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.checkUncheckAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.CHECK_UNCHECK_VALIDATE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.CHECK_UNCHECK_VALIDATE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void checkUncheckSubAttributeRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.checkUncheckSubAttributeApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //   isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.CHECK_UNCHECK_ATTRIBUTE, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.CHECK_UNCHECK_ATTRIBUTE,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void   informDeliveryOrderCancelRepo(HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.informDeliveryOrderCancelApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.INFORM_DELIVERY_CANCEL_ORDER, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.INFORM_DELIVERY_CANCEL_ORDER,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void checkEmailExitsRepo(HashMap<String,String> map) {
        isLoadingData.setValue(false);
        Call<ResponseBody> call = apiInterface.checkEmailExitApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.CHECK_EMAIL_EXITS, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.CHECK_EMAIL_EXITS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void checkMobileExitsRepo(HashMap<String,String> map) {
        isLoadingData.setValue(false);
        Call<ResponseBody> call = apiInterface.checkNumberExitApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.CHECK_MOBILE_EXITS, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.CHECK_MOBILE_EXITS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }




    public void changeUserPasswordStatusRepo(HashMap<String,String> map) {
        isLoadingData.setValue(false);
        Call<ResponseBody> call = apiInterface.changeUserPasswordStatusApi(map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.CHANGE_USER_PASSWORD_STATUS, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.CHANGE_USER_PASSWORD_STATUS,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }





    public void getPeriodicReportRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getPeriodicReportApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_PERIODIC_REPORT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_PERIODIC_REPORT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }



    public void getSellerPeriodicReportRepo(Map<String, String> auth,HashMap<String,String> map) {
        isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSellerPeriodicReportApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.GET_SELLER_PERIODIC_REPORT, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.GET_SELLER_PERIODIC_REPORT,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


    public void getSubSellerOrderCounterRepo(Map<String, String> auth,HashMap<String,String> map) {
        //  isLoadingData.setValue(true);
        Call<ResponseBody> call = apiInterface.getSubSellerOrderCounterApi(auth,map);
        call.enqueue(new Callback<ResponseBody>() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                //    isLoadingData.setValue(false);
                DynamicResponseModel dynamicResponseModel;
                Log.e("url===", response.toString());
                if (response.code() == 200) {
                    dynamicResponseModel = new DynamicResponseModel(ApiConstant.SUB_SELLER_ORDER_COUNTER, response.body(), response.code());

                } else {

                    JSONObject jsonObject;
                    String message = "";
                    try {
                        jsonObject = new JSONObject(response.errorBody().string());
                        message = jsonObject.getString("error");
                    } catch (JSONException e) {
                        message = "";
                        e.printStackTrace();
                    } catch (IOException e) {
                        message = "";
                        e.printStackTrace();
                    }
                    dynamicResponseModel = new DynamicResponseModel(
                            ApiConstant.SUB_SELLER_ORDER_COUNTER,
                            response.body(),
                            response.code());
                }
                apiResponseData.setValue(dynamicResponseModel);


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                isLoadingData.setValue(false);
            }
        });
    }


}
