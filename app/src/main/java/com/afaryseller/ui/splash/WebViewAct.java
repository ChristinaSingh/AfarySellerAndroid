package com.afaryseller.ui.splash;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityWebviewBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;


import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebViewAct extends AppCompatActivity {
    public String TAG = "WebViewAct";

    ActivityWebviewBinding binding;
    String url = "", title = "";
    private AfarySeller apiInterface;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this, R.layout.activity_webview);
        apiInterface = ApiClient.getClient(WebViewAct.this).create(AfarySeller.class);

        initViews();
    }

    private void initViews() {
        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");

        }

        setUrlView();

        binding.ivBack.setOnClickListener(v -> finish());

        binding.btnOk.setOnClickListener(v -> finish());

        getTermsConditions();

        binding.btnOne.setOnClickListener(v -> {
          //  url = Constant.TERMS_AND_CONDITIONS;
            getTermsConditions();

        });

        binding.btnTwo.setOnClickListener(v -> {
            //url = Constant.USE_CONDITIONS;
            getUseConditions();

        });

        binding.btnThree.setOnClickListener(v -> {
           // url = Constant.PRIVACY_POLICY;
           getPrivacyPolicy();
        });



    }


    public void getTermsConditions() {
        DataManager.getInstance().showProgressMessage(WebViewAct.this, "Please wait...");
        Call<ResponseBody> loginCall = apiInterface.getTermsConditions();

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.e(TAG,"get terms conditions Response = " + responseString);
                    if(jsonObject.getString("status").equals("1")) {
                        JSONObject jsonObject11 = jsonObject.getJSONObject("result");
                        binding.tvDetails.setMovementMethod(new ScrollingMovementMethod());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                            else if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr"), Html.FROM_HTML_MODE_COMPACT));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                        } else {

                            if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                            else if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr")));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                        }
                    } else {
                        // binding.tvNotFound.setVisibility(View.VISIBLE);

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


    public void getPrivacyPolicy() {
        DataManager.getInstance().showProgressMessage(WebViewAct.this, "Please wait...");
        Call<ResponseBody> loginCall = apiInterface.getPrivacyPolicy();

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.e(TAG,"get privacy policy Response = " + responseString);
                    if(jsonObject.getString("status").equals("1")) {
                        JSONObject jsonObject11 = jsonObject.getJSONObject("result");
                        binding.tvDetails.setMovementMethod(new ScrollingMovementMethod());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                            else if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr"), Html.FROM_HTML_MODE_COMPACT));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                        } else {

                            if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                            else if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr")));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                        }
                    } else {
                        // binding.tvNotFound.setVisibility(View.VISIBLE);

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


    public void getUseConditions() {
        DataManager.getInstance().showProgressMessage(WebViewAct.this, "Please wait...");
        Call<ResponseBody> loginCall = apiInterface.getUseConditions();

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.e(TAG,"get use conditions Response = " + responseString);
                    if(jsonObject.getString("status").equals("1")) {
                        JSONObject jsonObject11 = jsonObject.getJSONObject("result");
                        binding.tvDetails.setMovementMethod(new ScrollingMovementMethod());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                            else if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr"), Html.FROM_HTML_MODE_COMPACT));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                        } else {

                            if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                            else if(SessionManager.readString(WebViewAct.this, Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr")));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                        }
                    } else {
                        // binding.tvNotFound.setVisibility(View.VISIBLE);

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


    private void setUrlView() {
        binding.tvTitle.setText(title);
        binding.webView.getSettings().setJavaScriptEnabled(true);
        binding.webView.getSettings().setLoadWithOverviewMode(true);
        binding.webView.getSettings().setUseWideViewPort(true);
        binding.webView.setWebViewClient(new WebViewClient());
        binding.webView.loadUrl(url);
    }
}
