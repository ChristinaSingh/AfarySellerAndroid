package com.afaryseller.ui.subseller.updateprofile;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySubSellerEditProfileBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.shoplist.ShopModel;
import com.afaryseller.ui.signup.CityModel;
import com.afaryseller.ui.signup.CountryModel;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
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
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UpdateSubSellerProfileAct extends BaseActivity<ActivitySubSellerEditProfileBinding, UpdateSubSellerProfileViewModel> {
    public String TAG = "UpdateSubSellerProfileAct";

    ActivitySubSellerEditProfileBinding binding;
    UpdateSubSellerProfileViewModel updateSubSellerProfileViewModel;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String address = "", countryId = "", cityId = "",str_image_path="",subSellerId="",shopId="";
    ArrayList<CountryModel.Result> countryArrayList;
    ArrayList<CityModel.Result> cityArrayList;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    SubSellerProfileModel subSellerProfileModel;
    Bitmap oneBitmap=null;
    ArrayList<ShopModel.Result>shopArrayList;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_seller_edit_profile);
        updateSubSellerProfileViewModel = new UpdateSubSellerProfileViewModel();
        binding.setUpdateSubSellerProfileViewModel(updateSubSellerProfileViewModel);
        binding.getLifecycleOwner();
        updateSubSellerProfileViewModel.init(UpdateSubSellerProfileAct.this);
        initViews();
        observeLoader();
        observeResponse();
    }

    private void initViews() {
        countryArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();
        shopArrayList = new ArrayList<>();

        if(getIntent()!=null){
            subSellerId = getIntent().getStringExtra("subSellerId");
        }


        binding.ivSeller.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 33) {
                if(checkPermissionFor12Above()) showImageSelection();
            }
            else {
                if (checkPermisssionForReadStorage()) showImageSelection();
            }
        });


        binding.backNavigation.setOnClickListener(v ->finish());

        binding.tvCountry.setOnClickListener(view -> {

            if (countryArrayList != null)
                showDropDownCountry(view, binding.tvCountry, countryArrayList);
        });

        binding.tvCity.setOnClickListener(view -> {

            if (cityArrayList != null) showDropDownCity(view, binding.tvCity, cityArrayList);
        });



        binding.tvShop.setOnClickListener(v -> {

            if(DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getType().equals(Constant.SUBADMIN)) {
                if (!shopArrayList.isEmpty())
                    showDropDownShops(v, binding.tvShop, shopArrayList);

            }
        });


        if (!Places.isInitialized()) {
            Places.initialize(UpdateSubSellerProfileAct.this, getString(R.string.place_api_key));
        }


        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    //.setCountry("SA")
                    .build(UpdateSubSellerProfileAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });

        binding.btnUpdate.setOnClickListener(view -> validation());

      //  updateSubSellerProfileViewModel.getAllCountry(UpdateSubSellerProfileAct.this);

     getSubSellerProfile();


    }


    private void getSubSellerProfile() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("sub_seller_id", subSellerId);
        updateSubSellerProfileViewModel.getSubSellerProfile(UpdateSubSellerProfileAct.this,headerMap, map);
    }

    private void getShops() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String,String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getId());
        // map.put("category_id", sub_categary_id);
        map.put("merchant_id", "1");
        map.put("seller_register_id", DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getRegisterId());
        map.put("user_seller_id", DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getId());

        updateSubSellerProfileViewModel.getAllShops(UpdateSubSellerProfileAct.this,headerMap,map);

    }



    private void validation() {
        if(binding.tvName.getText().toString().equals("")){
            binding.tvName.setError(getString(R.string.can_not_empty));
            binding.tvName.setFocusable(true);
        }

        else if(binding.tvAddress.getText().toString().equals("")){
            binding.tvAddress.setError(getString(R.string.can_not_empty));
            binding.tvAddress.setFocusable(true);
        }

       /* else  if(binding.tvCountry.getText().toString().equals("")){
            binding.tvCountry.setError(getString(R.string.can_not_empty));
            binding.tvCountry.setFocusable(true);
        }

        else  if(binding.tvCity.getText().toString().equals("")){
            binding.tvCity.setError(getString(R.string.can_not_empty));
            binding.tvCity.setFocusable(true);
        }*/

       /* else  if(str_image_path.equals("")){
            Toast.makeText(this, getString(R.string.please_upload_profile_pic), Toast.LENGTH_SHORT).show();
        }*/
        else {
            updateProfile();
        }
    }


    private void showDropDownCountry(View v, TextView textView, List<CountryModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(UpdateSubSellerProfileAct.this, v);
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
                    updateSubSellerProfileViewModel.getAllCity(UpdateSubSellerProfileAct.this, map);
                }
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDropDownCity(View v, TextView textView, List<CityModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(UpdateSubSellerProfileAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    cityId = stringList.get(i).getId();
                }
            }
            return true;
        });
        popupMenu.show();
    }

    public void observeResponse() {
        updateSubSellerProfileViewModel.isResponse.observe(this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();

                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SUB_SELLER_PROFILE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("get sub seller profile response===", dynamicResponseModel.getJsonObject().toString());
                            JSONObject jsonObject = new JSONObject(stringResponse);

                            if (jsonObject.getString("status").toString().equals("1")) {
                                 subSellerProfileModel = new Gson().fromJson(stringResponse, SubSellerProfileModel.class);
                                 JSONObject object = jsonObject.getJSONObject("data");
                                //  binding.tvName.setText(object.getString("name") + " " + object.getString("last_name"));
                                subSellerId = object.getString("id");
                                binding.tvName.setText(object.getString("name") );
                                address = object.getString("address");
                                binding.tvEmail.setText(object.getString("email"));
                                binding.tvAddress.setText(object.getString("address"));
                                //binding.tvEmail.setText(object.getString("email"));
                                 shopId = object.getString("shop_id");

                                Glide.with(UpdateSubSellerProfileAct.this).load(object.getString("image"))
                                        .error(R.drawable.user_default)
                                        .placeholder(R.drawable.user_default)
                                        .into(binding.ivSeller);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(UpdateSubSellerProfileAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(UpdateSubSellerProfileAct.this);
                            }

                            getShops();


                        } else {
                            Toast.makeText(UpdateSubSellerProfileAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.UPDATE_SUB_SELLER_PROFILE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("update sub seller profile response===", stringResponse);
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                SessionManager.writeString(UpdateSubSellerProfileAct.this, Constant.shopId, shopId);
                                Toast.makeText(UpdateSubSellerProfileAct.this, getString(R.string.update_successfully), Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(UpdateSubSellerProfileAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(UpdateSubSellerProfileAct.this);
                            }


                        } else {
                            Toast.makeText(UpdateSubSellerProfileAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_COUNTRY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                CountryModel countryModel = new Gson().fromJson(stringResponse, CountryModel.class);
                                countryArrayList.clear();
                                countryArrayList.addAll(countryModel.getResult());
                                HashMap<String, String> map = new HashMap<>();
                                map.put("country_id", "1");
                                updateSubSellerProfileViewModel.getAllCity(UpdateSubSellerProfileAct.this, map);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(UpdateSubSellerProfileAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UpdateSubSellerProfileAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_CITY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                CityModel cityModel = new Gson().fromJson(stringResponse, CityModel.class);
                                cityArrayList.clear();
                                cityArrayList.addAll(cityModel.getResult());
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(UpdateSubSellerProfileAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UpdateSubSellerProfileAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(dynamicResponseModel.getApiName()== ApiConstant.GET_ALL_SHOP){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response get daily close Day===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                ShopModel shopModel = new Gson().fromJson(stringResponse, ShopModel.class);
                                shopArrayList.clear();
                                shopArrayList.addAll(shopModel.getResult());
                                for(int i =0;i<shopArrayList.size();i++){
                                    if(shopId.equals(shopArrayList.get(i).getShopId())){
                                        binding.tvShop.setText(shopArrayList.get(i).getName());
                                    }
                                }

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                shopArrayList.clear();
                            }



                        }
                        else {
                            Toast.makeText(UpdateSubSellerProfileAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private void observeLoader() {
        updateSubSellerProfileViewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(UpdateSubSellerProfileAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }


    public void showImageSelection() {

        final Dialog dialog = new Dialog(UpdateSubSellerProfileAct.this);
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
      /*  Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);*/
        Intent intent = new Intent(Intent.ACTION_PICK,  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);
    }

    private void openCamera() {
        /*Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(UpdateSubSellerProfileAct.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(UpdateSubSellerProfileAct.this,
                        "com.afaryseller.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }*/

        Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
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
        //   str_image_path = image.getAbsolutePath();


        return image;
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(UpdateSubSellerProfileAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(UpdateSubSellerProfileAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(UpdateSubSellerProfileAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateSubSellerProfileAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(UpdateSubSellerProfileAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(UpdateSubSellerProfileAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(UpdateSubSellerProfileAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(UpdateSubSellerProfileAct.this,
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
        if (ContextCompat.checkSelfPermission(UpdateSubSellerProfileAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(UpdateSubSellerProfileAct.this,
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED

        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UpdateSubSellerProfileAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(UpdateSubSellerProfileAct.this,
                            Manifest.permission.READ_MEDIA_IMAGES)
            ) {


                ActivityCompat.requestPermissions(UpdateSubSellerProfileAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(UpdateSubSellerProfileAct.this,
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
                        Toast.makeText(UpdateSubSellerProfileAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateSubSellerProfileAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UpdateSubSellerProfileAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateSubSellerProfileAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {

                Place place = Autocomplete.getPlaceFromIntent(data);
                try {
                    Log.e("addressStreet====", place.getAddress());
                    address = place.getAddress();
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                    //  city = DataManager.getInstance().getAddress(SignupAct.this,latitude,longitude);
                    //  binding.tvCity.setVisibility(View.VISIBLE);
                    //   binding.tvCity.setText(city);
                    binding.tvAddress.setText(place.getAddress());
                    latitude = place.getLatLng().latitude;
                    longitude = place.getLatLng().longitude;
                } catch (Exception e) {
                    e.printStackTrace();
                    //setMarker(latLng);
                }

            }
            else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }
            else if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(UpdateSubSellerProfileAct.this, data.getData());
                try {
                    oneBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Glide.with(UpdateSubSellerProfileAct.this)
                        .load(oneBitmap)
                        .centerCrop()
                        .into(binding.ivSeller);


            }
            else if (requestCode == REQUEST_CAMERA) {
                oneBitmap = (Bitmap) data.getExtras().get("data");   //
                Log.e("=======",oneBitmap+"");
                Glide.with(UpdateSubSellerProfileAct.this)
                        .load(oneBitmap)
                        .centerCrop()
                        .into(binding.ivSeller);
            }

        }


    }


    private void updateProfile() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        MultipartBody.Part filePart;
        if (oneBitmap!=null) {
            File file = persistImage(oneBitmap,"",UpdateSubSellerProfileAct.this);//DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            // str_image_path = DataManager.compressImage(getActivity(),str_image_path);
            //File file = new File(str_image_path);
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), binding.tvName.getText().toString());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"),subSellerProfileModel.getData().getUsername());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"),binding.tvEmail.getText().toString().trim());
        RequestBody subSellerID = RequestBody.create(MediaType.parse("text/plain"),subSellerId);
        RequestBody countryCode = RequestBody.create(MediaType.parse("text/plain"), subSellerProfileModel.getData().getCountryCode());
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"),subSellerProfileModel.getData().getMobile());
        RequestBody parent_seller_id = RequestBody.create(MediaType.parse("text/plain"),DataManager.getInstance().getUserData(UpdateSubSellerProfileAct.this).getResult().getId());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), subSellerProfileModel.getData().getCountry());
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"),subSellerProfileModel.getData().getState() );
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), subSellerProfileModel.getData().getCity());
        RequestBody addresss = RequestBody.create(MediaType.parse("text/plain"),address );
        RequestBody shop_Id = RequestBody.create(MediaType.parse("text/plain"),shopId );



        updateSubSellerProfileViewModel.updateSubSellerProfile(UpdateSubSellerProfileAct.this,headerMap,  name,userName,email,subSellerID,countryCode,mobile,parent_seller_id,country,state,city,addresss,shop_Id,filePart);
    }

    private static File persistImage(Bitmap bitmap, String name, Context cOntext) {
        File filesDir = cOntext.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "persistImage: "+e.getMessage() );
        }

        return  imageFile;

    }


    private void showDropDownShops(View v, TextView textView, List<ShopModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(UpdateSubSellerProfileAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if(stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    shopId = stringList.get(i).getShopId();
                }
            }
            return true;
        });
        popupMenu.show();
    }

}
