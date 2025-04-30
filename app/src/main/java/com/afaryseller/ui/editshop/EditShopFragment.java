package com.afaryseller.ui.editshop;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.FragmentEditShopBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addshop.AddShopFragment;
import com.afaryseller.ui.addshop.CountryAdapter;
import com.afaryseller.ui.addshop.StateAdapter;
import com.afaryseller.ui.addshop.UploadShopImageManager;
import com.afaryseller.ui.addshop.adapter.CityAdapter;
import com.afaryseller.ui.addshop.model.CountryModel;
import com.afaryseller.ui.addshop.model.StateModel;
import com.afaryseller.ui.editprofile.EditProfileAct;
import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.ui.signup.CityModel;
import com.afaryseller.ui.splash.model.CurrencyModel;
import com.afaryseller.ui.viewproduct.ViewProductAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponent;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditShopFragment extends BaseFragment<FragmentEditShopBinding, EditShopViewModel> {
    FragmentEditShopBinding binding;
    EditShopViewModel editShopViewModel;
    ArrayList<CountryModel.Result> countryArrayList;
    ArrayList<StateModel.Result> stateArrayList;
    ArrayList<CityModel.Result> cityArrayList;

    String countryId = "", stateId = "", cityId = "", addressss = "", sub_categary_id = "", name = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    CircleImageView ivLogo, iv1Pic, iv2Pic;
    private String chk;
    private static String str_image_path = "", str_image_path1 = "", str_image_path2 = "";
   ShopModel.Result data;

   public static Bitmap oneBitmap=null,twoBitmap=null,threeBitmap=null;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_shop, container, false);
        editShopViewModel = new EditShopViewModel();
        binding.setEditShopViewModel(editShopViewModel);
        binding.getLifecycleOwner();
        editShopViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {

        if (this.getArguments() != null) {
            sub_categary_id = getArguments().getString("id");
            name = getArguments().getString("name");
            data = (ShopModel.Result) getArguments().getSerializable("shopData");
            binding.restorents.setText(name);
            setShopData();
        }

        binding.RRback.setOnClickListener(view -> getActivity().onBackPressed());

        if (!Places.isInitialized()) {
            Places.initialize(getActivity(), getString(R.string.place_api_key));
        }


        countryArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();

        observeLoader();
        observeResponse();
       // binding.ccp.setCountryForPhoneCode(241);

        editShopViewModel.getCountries(getActivity());


        binding.selectCountry.setOnClickListener(v -> {
            if(countryArrayList!=null){
                showDropDownCountry(v, binding.selectCountry,countryArrayList);
            }
        });


        binding.selectState.setOnClickListener(v -> {
            if(stateArrayList!=null){
                showDropDownState(v, binding.selectState,stateArrayList);
            }
        });


        binding.selectCity.setOnClickListener(v -> {
            if(cityArrayList!=null){
                showDropDownCity(v, binding.selectCity,cityArrayList);
            }
        });


    /*    binding.selectCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (countryArrayList != null) {
                    countryId = countryArrayList.get(i).getId();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("country_id", countryId);
                    editShopViewModel.getCountryStates(getActivity(), map);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.selectState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (stateArrayList != null) {
                    stateId = stateArrayList.get(i).getId();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("state_id", stateId);
                    editShopViewModel.getStateCity(getActivity(), map);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        binding.selectCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (stateArrayList != null) {
                    cityId = cityArrayList.get(i).getId();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/






        binding.address.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID,
                    Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS,
                    Place.Field.ADDRESS_COMPONENTS );

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    //.setTypeFilter(TypeFilter.ADDRESS)
                    //.setCountry("SA")
                    .build(getActivity());

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });


        binding.rrImage.setOnClickListener(view -> {
            showPic();
        });

        binding.addShops.setOnClickListener(v -> {
            validation();
        });

    }





    private void showDropDownCountry(View v, TextView textView, List<CountryModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    countryId = stringList.get(i).getId();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("country_id", countryId);
                    editShopViewModel.getCountryStates(getActivity(), map);

                }
            }
            return true;
        });
        popupMenu.show();
    }






    private void showDropDownState(View v, TextView textView, List<StateModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    stateId = stringList.get(i).getId();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("state_id", stateId);
                    editShopViewModel.getStateCity(getActivity(), map);
                }
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDropDownCity(View v, TextView textView, List<CityModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(requireActivity(), v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    cityId = stringList.get(i).getId();                }
            }
            return true;
        });
        popupMenu.show();
    }














    private void setShopData() {
       // String number[] =null;
        binding.name.setText(data.getName());
        binding.discription.setText(data.getDescription());
        binding.address.setText(data.getAddress());
        binding.streetLandmark.setText(data.getStreetLandmark());

        binding.niburhood.setText(data.getNeighbourhood());
        binding.ccp.setCountryForPhoneCode(Integer.parseInt(data.getCountryCode()));
       /* if(data.getPhone().contains("241")) {
            number = data.getPhone().split("241");
            binding.mobile.setText(number[1]);

        }else*/ binding.mobile.setText(data.getPhone());
        binding.mobileOne.setText(data.getPhonenumber());
        binding.mobileTwo.setText(data.getMobileaccount());

        // data.getCurrency();


       if(data.getLat().equals("")) latitude = 0.0;
           else latitude = Double.parseDouble(data.getLat());
       if(data.getLon().equals("")) longitude = 0.0;
           else longitude = Double.parseDouble(data.getLon());

        countryId = data.getCountry();
        stateId = data.getState();
        cityId = data.getCity();
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("country_id", countryId);
            editShopViewModel.getCountryStates(getActivity(), map);

          /*  HashMap<String, String> map1 = new HashMap<>();
            map1.put("state_id", stateId);
            editShopViewModel.getStateCity(getActivity(), map1);*/
        } catch (Exception e){
            e.printStackTrace();
        }

    }


    public void observeResponse() {
        editShopViewModel.isResponse.observe(getActivity(), dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_COUNTRY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get country===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {

                                CountryModel model = new Gson().fromJson(stringResponse, CountryModel.class);
                                countryArrayList.clear();
                                countryArrayList.addAll(model.getResult());
                           //     binding.selectCountry.setAdapter(new CountryAdapter(getActivity(), countryArrayList));
                             //   editShopViewModel.getCurrency(getActivity());


                               if(!countryId.equals("")){
                                   for(int i =0;i<countryArrayList.size();i++){
                                       if(countryId.equals(countryArrayList.get(i).getId())) {
                                           //binding.selectCountry.setSelection(i);
                                           binding.selectCountry.setText(countryArrayList.get(i).getName());
                                       }
                                   }
                               }


                                if (!countryId.equals("")) {
                                    HashMap<String, String> map = new HashMap<>();
                                    map.put("country_id", countryId);
                                    editShopViewModel.getCountryStates(getActivity(), map);
                                }



                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_STATE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get states===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                StateModel model = new Gson().fromJson(stringResponse, StateModel.class);
                                stateArrayList.clear();
                                stateArrayList.addAll(model.getResult());
                              //  binding.selectState.setAdapter(new StateAdapter(getActivity(), stateArrayList));
                                if(!stateId.equals("")){
                                    for(int i =0;i<stateArrayList.size();i++){
                                        if(stateId.equals(stateArrayList.get(i).getId())) {
                                          //  binding.selectState.setSelection(i);
                                            binding.selectState.setText(stateArrayList.get(i).getName());
                                        }
                                    }
                                }

                                HashMap<String, String> map1 = new HashMap<>();
                                map1.put("state_id", stateId);
                                editShopViewModel.getStateCity(getActivity(), map1);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_STATE_CITY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                           // Log.e("response get city===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response get city===", jsonObject.toString());
                            if (jsonObject.getString("status").toString().equals("1")) {
                                CityModel model = new Gson().fromJson(stringResponse, CityModel.class);
                                cityArrayList.clear();
                                cityArrayList.addAll(model.getResult());
                               // binding.selectCity.setAdapter(new CityAdapter(getActivity(), cityArrayList));
                                Log.e(" city id===", cityId);

                                if(!cityId.equals("")){
                                    for(int i =0;i<cityArrayList.size();i++){
                                        if(cityId.equals(cityArrayList.get(i).getId())) {
                                           // binding.selectCity.setSelection(i);
                                            binding.selectCity.setText(cityArrayList.get(i).getName());
                                            Log.e(" city id 11111===", cityId);
                                            Log.e(" city id 22222===", cityArrayList.get(i).getName());

                                        }
                                    }
                                }

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.EDIT_SHOP) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response edit shop===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {


                                JSONObject result = jsonObject.getJSONObject("result");
                                UploadShopImageManager.updateShopImages(requireActivity(),DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken()
                                        ,result.getString("id"), oneBitmap, twoBitmap,threeBitmap);
                                Bundle bundle = new Bundle();
                               // bundle.putString("shop_id", result.getString("id"));
                                bundle.putString("id", sub_categary_id);
                                bundle.putString("name", name);
                                // Navigation.findNavController(v).navigate(R.id.action_postsFragment_to_otherUserDetailFragment,bundle);
                                NavOptions.Builder navBuilder = new NavOptions.Builder();
                                NavOptions navOptions = navBuilder.build();
                                NavHostFragment.findNavController(EditShopFragment.this).navigate(R.id.action_editShop_fragment_to_shoplist, bundle, navOptions);

                                str_image_path = "";
                                str_image_path1 = "";
                                str_image_path2 = "";
                                oneBitmap= null;
                                twoBitmap = null;
                                threeBitmap = null;
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


/*
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_CURRENCY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response get currency===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                CurrencyModel currencyModel = new Gson().fromJson(stringResponse, CurrencyModel.class);
                                currencyArrayList.clear();
                                currencyArray.clear();
                                currencyArrayList.addAll(currencyModel.getResult());
                                for (int i = 0; i < currencyArrayList.size(); i++) {
                                    currencyArray.add(currencyArrayList.get(i).getName());
                                }
                                bb.notifyDataSetChanged();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                currencyArrayList.clear();
                                bb.notifyDataSetChanged();


                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
*/

            }
        });
    }

    private void observeLoader() {
        editShopViewModel.isLoading.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }


    public void showPic() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_add_pic);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutLogo = (LinearLayout) dialog.findViewById(R.id.layoutLogo);
        LinearLayout layout1Shop = (LinearLayout) dialog.findViewById(R.id.layout1Shop);
        LinearLayout layout2Shop = (LinearLayout) dialog.findViewById(R.id.layout2Shop);

        ivLogo = dialog.findViewById(R.id.ivLogo);
        iv1Pic = dialog.findViewById(R.id.iv1Pic);
        iv2Pic = dialog.findViewById(R.id.iv2Pic);
        ImageView ivCancel = dialog.findViewById(R.id.ivCancel);
        TextView btnOk = dialog.findViewById(R.id.btnOk);




        if (oneBitmap!=null) {
            Glide.with(getActivity())
                    .load(oneBitmap)
                    .centerCrop()
                    .into(ivLogo);
        }

        else {

            if(data.getImage1().contains(".png")||data.getImage1().contains(".jpg")){
                Glide.with(getActivity())
                        .load(data.getImage1())
                        .override(100,100)
                        .into(ivLogo);
            }

            else {
                Glide.with(getActivity())
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(ivLogo);
            }
        }


        if (twoBitmap!=null) {
            Glide.with(getActivity())
                    .load(twoBitmap)
                    .centerCrop()
                    .into(iv1Pic);
        }
        else {

            if(data.getImage2().contains(".png")||data.getImage2().contains(".jpg")){
                Glide.with(getActivity())
                        .load(data.getImage2())
                        .override(100,100)
                        .into(iv1Pic);

            }

            else {
                Glide.with(getActivity())
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv1Pic);
            }

        }

        if (threeBitmap!=null) {
            Glide.with(getActivity())
                    .load(threeBitmap)
                    .centerCrop()
                    .into(iv2Pic);
        }

        else {
            if(data.getImage3().contains(".png")||data.getImage3().contains(".jpg")){
                Glide.with(getActivity())
                        .load(data.getImage3())
                        .override(100,100)
                        .into(iv2Pic);
            }

            else {
                Glide.with(getActivity())
                        .load(R.drawable.img_default)
                        .centerCrop()
                        .into(iv2Pic);
            }
        }


        btnOk.setOnClickListener(v -> dialog.dismiss());

        ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        layoutLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "1";
                if (Build.VERSION.SDK_INT >= 33) {
                    if( checkPermissionFor12Above()) showImageSelection();
                }
                else {
                    if (checkPermisssionForReadStorage())   showImageSelection();
                }
            }
        });
        layout1Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // dialog.dismiss();
                //  dialog.cancel();
                chk = "2";
                if (Build.VERSION.SDK_INT >= 33) {
                    if (checkPermissionFor12Above()) showImageSelection();
                } else {
                    if (checkPermisssionForReadStorage()) showImageSelection();
                }
            }
        });
        layout2Shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                    //  dialog.dismiss();
                    //   dialog.cancel();
                    chk = "3";
                    if (Build.VERSION.SDK_INT >= 33) {
                        if (checkPermissionFor12Above()) showImageSelection();
                    } else {
                        if (checkPermisssionForReadStorage()) showImageSelection();
                    }
                }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void showImageSelection() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = dialog.getWindow();
        lp.copyFrom(window.getAttributes());
        //This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        LinearLayout layoutCamera = (LinearLayout) dialog.findViewById(R.id.layoutCemera);
        LinearLayout layoutGallary = (LinearLayout) dialog.findViewById(R.id.layoutGallary);
        layoutCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                openCamera();
            }
        });
        layoutGallary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.cancel();
                getPhotoFromGallary();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    private void getPhotoFromGallary() {
       /* Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);*/

        Intent intent = new Intent(Intent.ACTION_PICK,  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }


    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.afaryseller.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }

      //  Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      //  startActivityForResult(intent, REQUEST_CAMERA);
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" /*+ timeStamp + "_"*/;
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        if (chk.equals("1")) str_image_path = image.getAbsolutePath();
        else if (chk.equals("2")) str_image_path1 = image.getAbsolutePath();
        else if (chk.equals("3")) str_image_path2 = image.getAbsolutePath();

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", resultCode + "");
            if (requestCode == SELECT_FILE) {
                if (chk.equals("1")) {
                    str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());

                    try {
                        oneBitmap =    MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(getActivity())
                            .load(oneBitmap)
                            .centerCrop()
                            .into(ivLogo);
                } else if (chk.equals("2")) {
                    str_image_path1 = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                    try {
                        twoBitmap =    MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(getActivity())
                            .load(twoBitmap)
                            .centerCrop()
                            .into(iv1Pic);
                } else if (chk.equals("3")) {
                    str_image_path2 = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());
                    try {
                        threeBitmap =    MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Glide.with(getActivity())
                            .load(threeBitmap)
                            .centerCrop()
                            .into(iv2Pic);
                }

            } else if (requestCode == REQUEST_CAMERA) {
                if (chk.equals("1")) {
                 /*   oneBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",oneBitmap+"");
                    Glide.with(getActivity())
                            .load(oneBitmap)
                            .centerCrop()
                            .into(ivLogo);*/

                    Glide.with(requireActivity())
                            .asBitmap()
                            .load(str_image_path)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    oneBitmap = resource;
                                    ivLogo.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });


                } else if (chk.equals("2")) {
                    /*twoBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======", twoBitmap + "");
                    Glide.with(getActivity())
                            .load(twoBitmap)
                            .centerCrop()
                            .into(iv1Pic);*/


                    Glide.with(requireActivity())
                            .asBitmap()
                            .load(str_image_path1)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    twoBitmap = resource;
                                    iv1Pic.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });

                } else if (chk.equals("3")) {
                /*    threeBitmap = (Bitmap) data.getExtras().get("data");   //
                    Log.e("=======",threeBitmap+"");
                    Glide.with(getActivity())
                            .load(threeBitmap)
                            .centerCrop()
                            .into(iv2Pic);*/

                    Glide.with(requireActivity())
                            .asBitmap()
                            .load(str_image_path2)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    threeBitmap = resource;
                                    iv2Pic.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });
                }

            }

            if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {
                if (resultCode == RESULT_OK) {
                    Place place = Autocomplete.getPlaceFromIntent(data);
                    try {
                        Log.e("addressStreet====", place.getAddress());
                        Log.e("addressStreet====", place.getName());

                         addressss = place.getAddress();
                        latitude = place.getLatLng().latitude;
                        longitude = place.getLatLng().longitude;
                        //  city = DataManager.getInstance().getAddress(SignupAct.this,latitude,longitude);
                        //  binding.tvCity.setVisibility(View.VISIBLE);
                        //   binding.tvCity.setText(city);
                       // addressss = DataManager.getInstance().getAddress(requireActivity(),latitude,longitude);
                        latitude = place.getLatLng().latitude;
                        longitude = place.getLatLng().longitude;
                        binding.address.setText(addressss);

                      /*  StringBuilder fullAddress = new StringBuilder();
                        List<String> typesOrder = Arrays.asList(
                                "sublocality_level_2",
                                "sublocality_level_1",
                                "locality",
                                "administrative_area_level_2",
                                "administrative_area_level_3",
                                "administrative_area_level_1",
                                "country"
                        );

                        Set<String> addedTypes = new HashSet<>();

                        for (String type : typesOrder) {
                            for (AddressComponent component : place.getAddressComponents().asList()) {
                                if (component.getTypes().contains(type) && !addedTypes.contains(component.getName())) {
                                    fullAddress.append(component.getName()).append(", ");
                                    addedTypes.add(component.getName()); // Avoid duplicates
                                    break;
                                }
                            }
                        }

// Remove trailing comma and space
                        if (fullAddress.length() > 2) {
                            fullAddress.setLength(fullAddress.length() - 2);
                        }

                        addressss = fullAddress.toString();
                        Log.e("FormattedAddress", addressss);
                        binding.address.setText(addressss);*/


                    } catch (Exception e) {
                        e.printStackTrace();
                        //setMarker(latLng);
                    }

                } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                    // TODO: Handle the error.
                    Status status = Autocomplete.getStatusFromIntent(data);
                }

            }


        }
    }


    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }

    public boolean checkPermissionFor12Above() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED

        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.READ_MEDIA_IMAGES)
            ) {


                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean write_external_storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage && write_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(getActivity(), " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }

            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean camera = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean read_external_storage = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera && read_external_storage) {
                        showImageSelection();
                    } else {
                        Toast.makeText(getActivity(), "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }

    private void validation() {
        if (binding.name.getText().toString().trim().isEmpty()) {
            binding.name.setError(getString(R.string.can_not_empty));
            binding.name.setFocusable(true);
        }

      /* else if (binding.restorents.getText().toString().trim().isEmpty()) {
            binding.restorents.setError(getString(R.string.can_not_empty));
            binding.restorents.setFocusable(true);
        }*/

        else if (binding.discription.getText().toString().trim().isEmpty()) {
            binding.discription.setError(getString(R.string.can_not_empty));
            binding.discription.setFocusable(true);
        } else if (binding.address.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.please_enter_address), Toast.LENGTH_SHORT).show();

        }

        else if (binding.streetLandmark.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.please_enter_street_landmark), Toast.LENGTH_SHORT).show();

        }

        else if (binding.niburhood.getText().toString().trim().isEmpty()) {
            binding.niburhood.setError(getString(R.string.can_not_empty));
            binding.niburhood.setFocusable(true);
        } else if (countryId.equals("")) {
            Toast.makeText(getActivity(), getString(R.string.please_select_country), Toast.LENGTH_SHORT).show();
        } else if (stateId.equals("")) {
            Toast.makeText(getActivity(), getString(R.string.please_select_city), Toast.LENGTH_SHORT).show();

        } else if (binding.mobile.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.please_enter_phone_number), Toast.LENGTH_SHORT).show();
        } else if (binding.mobileOne.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.please_add_mobile_network_account_number1), Toast.LENGTH_SHORT).show();
        } /*else if (binding.mobileTwo.getText().toString().trim().isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.please_add_mobile_network_account_number2), Toast.LENGTH_SHORT).show();

        } *//*else if (str_image_path.equals("")) {
            Toast.makeText(getActivity(), getString(R.string.please_upload_shp_logo), Toast.LENGTH_SHORT).show();
        } else if (str_image_path1.equals("")) {
            Toast.makeText(getActivity(), getString(R.string.please_upload_profile_img), Toast.LENGTH_SHORT).show();
        } else if (str_image_path2.equals("")) {
            Toast.makeText(getActivity(), getString(R.string.please_upload_profile_img2), Toast.LENGTH_SHORT).show();
        }*/ else {
            AddShops();
        }
    }

    private void AddShops() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        MultipartBody.Part filePart, filePart2, filePart3;
        if (oneBitmap!=null) {
            File file = persistImage(oneBitmap,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),getActivity());//DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            // str_image_path = DataManager.compressImage(getActivity(),str_image_path);
            //File file = new File(str_image_path);
            filePart = MultipartBody.Part.createFormData("image_1", file.getName(), RequestBody.create(MediaType.parse("image_1"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        if (twoBitmap!=null) {
            File file = persistImage(twoBitmap,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),getActivity());//DataManager.getInstance().saveBitmapToFile(new File(str_image_path1));
            // str_image_path1 = DataManager.compressImage(getActivity(),str_image_path1);
            // File file =  //new File(str_image_path);
            filePart2 = MultipartBody.Part.createFormData("image_2", file.getName(), RequestBody.create(MediaType.parse("image_2"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        if (threeBitmap!=null) {
            File file = persistImage(threeBitmap,new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),getActivity());//DataManager.getInstance().saveBitmapToFile(new File(str_image_path2));
            //   str_image_path2 = DataManager.compressImage(getActivity(),str_image_path2);
            //  File file = new File(str_image_path);
            filePart3 = MultipartBody.Part.createFormData("image_3", file.getName(), RequestBody.create(MediaType.parse("image_3"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart3 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }


        RequestBody shopId = RequestBody.create(MediaType.parse("text/plain"), data.getShopId());
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        RequestBody shopName = RequestBody.create(MediaType.parse("text/plain"), binding.name.getText().toString());
        RequestBody subCateId = RequestBody.create(MediaType.parse("text/plain"), sub_categary_id);
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), binding.discription.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"), binding.address.getText().toString());
        RequestBody streetLandmark = RequestBody.create(MediaType.parse("text/plain"), binding.streetLandmark.getText().toString());

        RequestBody neighbourhood = RequestBody.create(MediaType.parse("text/plain"), binding.niburhood.getText().toString());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), countryId);
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), stateId);
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), cityId);
        RequestBody countryCode = RequestBody.create(MediaType.parse("text/plain"), binding.ccp.getSelectedCountryCode() +"");
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"),  binding.mobile.getText().toString());
        RequestBody phonenumber = RequestBody.create(MediaType.parse("text/plain"), binding.mobileTwo.getText().toString());
        RequestBody mobileaccount = RequestBody.create(MediaType.parse("text/plain"), binding.mobileTwo.getText().toString());
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), latitude + "");
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"), longitude + "");
        RequestBody registerId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());
        RequestBody userSellerId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(getActivity()).getResult().getId());

        editShopViewModel.editShop(getActivity(),headerMap,shopId, userId, shopName, subCateId, description, address,streetLandmark, neighbourhood, country,
                state, city,countryCode, phone, phonenumber, mobileaccount, lat, lon,registerId,userSellerId, filePart, filePart2, filePart3);
    }

    private  File persistImage(Bitmap bitmap, String name, Context cOntext) {
        File filesDir = cOntext.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");
        Log.e("image name===",name);

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "persistImage: "+e.getMessage() );
        }

        return  imageFile;

    }

}
