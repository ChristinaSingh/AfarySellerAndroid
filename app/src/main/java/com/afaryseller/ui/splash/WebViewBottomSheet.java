package com.afaryseller.ui.splash;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.FragmentAskBinding;
import com.afaryseller.databinding.FragmentWebViewBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WebViewBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "WebViewBottomSheet";
    BottomSheetDialog dialog;
    FragmentWebViewBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public AskListener listener;
    private AfarySeller apiInterface;

    String url = "", title = "";



    public WebViewBottomSheet(String url,String title) {
        this.url = url;
        this.title = title;
    }


    public WebViewBottomSheet callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_web_view, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return  dialog;
    }

    private void initBinding() {
        apiInterface = ApiClient.getClient(getActivity()).create(AfarySeller.class);


            setUrlView();

            binding.ivBack.setOnClickListener(v -> dialog.dismiss());

            binding.btnOk.setOnClickListener(v -> dialog.dismiss());

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
        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");
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
                            if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                            else if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr"), Html.FROM_HTML_MODE_COMPACT));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                        } else {

                            if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                            else if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
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
        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");
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
                            if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                            else if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr"), Html.FROM_HTML_MODE_COMPACT));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                        } else {

                            if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                            else if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
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
        DataManager.getInstance().showProgressMessage(getActivity(), "Please wait...");
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
                            if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                            else if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details_fr"), Html.FROM_HTML_MODE_COMPACT));
                            else
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details"), Html.FROM_HTML_MODE_COMPACT));
                        } else {

                            if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("en"))
                                binding.tvDetails.setText(Html.fromHtml(jsonObject11.getString("details")));
                            else if(SessionManager.readString(requireActivity(), Constant.SELLER_LANGUAGE, "").equalsIgnoreCase("fr"))
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
