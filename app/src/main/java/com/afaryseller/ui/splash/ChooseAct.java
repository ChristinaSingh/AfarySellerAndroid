package com.afaryseller.ui.splash;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityChooseBinding;
import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.editprofile.EditProfileAct;
import com.afaryseller.ui.login.LoginAct;
import com.afaryseller.ui.signup.CountryModel;
import com.afaryseller.ui.signup.SignupAct;
import com.afaryseller.ui.splash.adapter.SliderTextAdapter;
import com.afaryseller.ui.splash.model.CountryModelNew;
import com.afaryseller.ui.splash.model.CurrencyModel;
import com.afaryseller.ui.splash.model.TitleDesModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.gson.Gson;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChooseAct extends AppCompatActivity {
    private String TAG = "ChooseAct";
    ActivityChooseBinding binding;
    private AfarySeller apiInterface;
    ArrayList<TitleDesModel.Result> arrayList;

    ArrayList<CurrencyModel.Result> currencyArrayList;
   // String[] country = {"English", "Français"};
    ArrayList<CountryModelNew> country = new ArrayList<>();
    ArrayList<String> currencyArray = new ArrayList<>();
    private boolean isFirstSelection = true;
    SliderTextAdapter adapter1;
   // private Object item,currency;
    private String item="",currency="";


    ArrayAdapter bb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_choose);
        apiInterface = ApiClient.getClient(ChooseAct.this).create(AfarySeller.class);

        initViews();
    }

    private void initViews() {
        arrayList = new ArrayList<>();
        currencyArrayList = new ArrayList<>();
        country.add(new CountryModelNew("1","English","Anglais"));
        country.add(new CountryModelNew("2","French","Français"));



        binding.loginBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginAct.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });


        binding.registerBtn.setOnClickListener(view -> {
            startActivity(new Intent(this, SignupAct.class).putExtra("lang",item)
                    .putExtra("currency",currency).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });


       /* ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.langougeChange.setAdapter(aa);

         bb = new ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyArray);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.currencyChange.setAdapter(bb);*/




         binding.tvCurrency.setOnClickListener(v -> {
             showDropCurrency(v,binding.tvCurrency,currencyArray);

         });

        binding.tvLanguage.setOnClickListener(v -> {
            showDropLanguage(v,binding.tvLanguage,country);
        });




      /*  binding.langougeChange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if (isFirstSelection) {
                    isFirstSelection = false;
                    return; // Skip initial selection
                }


                item = (String) parent.getSelectedItem();
                if (item.equals("English")) {
                   // Locale englishLocale = new Locale("en");
                 //   DataManager.updateLocale(ChooseAct.this,englishLocale);

                     updateResources(ChooseAct.this, "en");


                } else if (item.equals("Français")) {
                   // Locale frenchLocale = new Locale("fr");
                   // DataManager.updateLocale(ChooseAct.this,frenchLocale);

                     updateResources(ChooseAct.this, "fr");

                }

            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.currencyChange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                currency = (String) parent.getSelectedItem();
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/





        getDescriptionTitle();
    }

    public void getDescriptionTitle() {
        DataManager.getInstance().showProgressMessage(ChooseAct.this, getString(R.string.please_wait));
        Call<ResponseBody> loginCall = apiInterface.getTitleDes();

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.e(TAG,"get title description Response = " + responseString);
                    getCurrency();
                    if(jsonObject.getString("status").equals("1")) {
                        TitleDesModel titleDesModel = new Gson().fromJson(responseString,TitleDesModel.class);
                        arrayList.clear();
                        arrayList.addAll(titleDesModel.getResult());

                        adapter1 = new SliderTextAdapter(ChooseAct.this, arrayList);
                        binding.imageSlider.setSliderAdapter(adapter1);
                        binding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
                        binding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
                        binding.imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                        //     binding.imageSlider.setIndicatorSelectedColor(R.color.colorPrimary);
                        //      binding.imageSlider.setIndicatorUnselectedColor(Color.GRAY);
                        binding.imageSlider.setScrollTimeInSec(3);
                        binding.imageSlider.setAutoCycle(true);
                        binding.imageSlider.startAutoCycle();


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


    public void getCurrency() {
        DataManager.getInstance().showProgressMessage(ChooseAct.this, getString(R.string.please_wait));
        Call<ResponseBody> loginCall = apiInterface.getCurrency();

        loginCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                DataManager.getInstance().hideProgressMessage();

                try {
                    String responseString = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseString);
                    Log.e(TAG,"get title description Response = " + responseString);
                    if(jsonObject.getString("status").equals("1")) {
                        CurrencyModel currencyModel = new Gson().fromJson(responseString,CurrencyModel.class);
                        currencyArrayList.clear();
                        currencyArray.clear();

                        currencyArrayList.addAll(currencyModel.getResult());
                        for (int i=0;i<currencyArrayList.size();i++){
                            currencyArray.add(currencyArrayList.get(i).getName());
                        }
                       // bb.notifyDataSetChanged();

                        currency = currencyArray.get(0);
                        item = "en";
                        binding.tvLanguage.setText(country.get(0).getName());
                        binding.tvCurrency.setText(currency);


                    } else {
                        currencyArrayList.clear();
                      //  bb.notifyDataSetChanged();

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





    private void updateResources(ChooseAct wellcomeScreen, String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Resources resources = wellcomeScreen.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        SessionManager.writeString(ChooseAct.this, Constant.SELLER_LANGUAGE, en);
        adapter1.notifyDataSetChanged();
        Log.e("changalalal","=====");
    }


    public void restartActivity(Activity activity) {
        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);
    }




    private void showDropLanguage(View v, TextView textView, List<CountryModelNew> stringList) {
        PopupMenu popupMenu = new PopupMenu(ChooseAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }


        popupMenu.setOnMenuItemClickListener(menuItem -> {
         //   textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    if(stringList.get(i).getId().equalsIgnoreCase("1")) {
                        item = "en"; //stringList.get(i).getName();
                       updateResources(ChooseAct.this, "en");
                       textView.setText(stringList.get(i).getName());
                        binding.tvText.setText("Next");
                   }
                   else {
                       item = "fr";
                       textView.setText(stringList.get(i).getNameFr());
                       updateResources(ChooseAct.this, "fr");
                       binding.tvText.setText("Suivant");
                   }
                }
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDropCurrency(View v, TextView textView, List<String> stringList) {
        PopupMenu popupMenu = new PopupMenu(ChooseAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i));
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).equalsIgnoreCase(menuItem.getTitle().toString())) {
                    currency = stringList.get(i);

                }
            }
            return true;
        });
        popupMenu.show();
    }



}
