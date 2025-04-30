package com.afaryseller.ui.subseller.signup;

import static com.afaryseller.retrofit.Constant.emailPattern;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.afaryseller.databinding.ActivitySubSellerSignupBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addshop.model.StateModel;
import com.afaryseller.ui.editprofile.EditProfileAct;
import com.afaryseller.ui.otp.OtpAct;
import com.afaryseller.ui.signup.CityModel;
import com.afaryseller.ui.signup.CountryModel;
import com.afaryseller.utility.CountryCodes;
import com.afaryseller.utility.DataManager;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.messaging.FirebaseMessaging;
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
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class SubSellerSignupAct extends BaseActivity<ActivitySubSellerSignupBinding, SubSellerSignupViewModel> {
    public String TAG = "SubSellerSignupAct";

    ActivitySubSellerSignupBinding binding;
    SubSellerSignupViewModel subSellersignupViewModel;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String address = "", countryId = "", stateId = "", cityId = "", countryName = "",str_image_path="";
    String firebaseToken = "", lang = "", currency = "";
    ArrayList<CountryModel.Result> countryArrayList;
    ArrayList<CityModel.Result> cityArrayList;
    ArrayList<StateModel.Result> stateArrayList;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;

    Bitmap oneBitmap=null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sub_seller_signup);
        subSellersignupViewModel = new SubSellerSignupViewModel();
        binding.setSubSellersignupViewModel(subSellersignupViewModel);
        binding.getLifecycleOwner();
        subSellersignupViewModel.init(SubSellerSignupAct.this);
        initViews();
        observeLoader();
        observeResponse();
    }

    private void initViews() {
        countryArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();

        setCountryCodeFromLocation();

       /* if (getIntent() != null) {
            lang = getIntent().getStringExtra("lang");
            currency = getIntent().getStringExtra("currency");

        }*/

        binding.ivSeller.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 33) {
                if(checkPermissionFor12Above()) showImageSelection();
            }
            else {
                if (checkPermisssionForReadStorage()) showImageSelection();
            }
        });


        binding.backNavigation.setOnClickListener(v ->finish());



        binding.ccp.setCountryForPhoneCode(241);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);
            firebaseToken = token;

        });

        binding.btnContinue.setOnClickListener(view -> {
            startActivity(new Intent(this, OtpAct.class));
            //  finish();
        });


        binding.tvSelectCountry.setOnClickListener(view -> {

            if (countryArrayList != null)
                showDropDownCountry(view, binding.tvSelectCountry, countryArrayList);
        });

        binding.tvState.setOnClickListener(view -> {

            if (stateArrayList != null) showDropDownState(view, binding.tvState, stateArrayList);
        });

        binding.etCity.setOnClickListener(view -> {

            if (cityArrayList != null) showDropDownCity(view, binding.etCity, cityArrayList);
        });

        if (!Places.isInitialized()) {
            Places.initialize(SubSellerSignupAct.this, getString(R.string.place_api_key));
        }


        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    //.setCountry("SA")
                    .build(SubSellerSignupAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });

        binding.btnContinue.setOnClickListener(view -> validation());

        subSellersignupViewModel.getAllCountry(SubSellerSignupAct.this);

    }


    public void validation() {
        if (binding.userName.getText().toString().equals("")) {
            binding.userName.setError(getString(R.string.please_enter_username));
            binding.userName.setFocusable(true);
        } else if (binding.name.getText().toString().equals("")) {
            binding.name.setError(getString(R.string.please_enter_name));
            binding.name.setFocusable(true);
        } else if (binding.email.getText().toString().trim().equals("")) {
            binding.email.setError(getString(R.string.please_enter_email));
            binding.email.setFocusable(true);
        } else if (!binding.email.getText().toString().trim().matches(emailPattern)) {
            binding.email.setError(getString(R.string.wrong_email));
            binding.email.setFocusable(true);
        } else if (binding.phone.getText().toString().equals("")) {
            binding.phone.setError(getString(R.string.please_enter_phone_number));
            binding.phone.setFocusable(true);
        } else if (binding.password.getText().toString().equals("")) {
            binding.password.setError(getString(R.string.enter_password));
            binding.password.setFocusable(true);
        } else if (binding.confirmPassword.getText().toString().equals("")) {
            binding.confirmPassword.setError(getString(R.string.please_enter_confirm_password));
            binding.confirmPassword.setFocusable(true);
        } else if (!binding.confirmPassword.getText().toString().equals(binding.password.getText().toString())) {
            Toast.makeText(this, getString(R.string.password_does_not_matched), Toast.LENGTH_SHORT).show();

        } else if (address.equals("")) {
            Toast.makeText(this, getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
        } /*else if (countryId.equals("")) {
            binding.tvSelectCountry.setError(getString(R.string.please_enter_country));
            binding.tvSelectCountry.setFocusable(true);
        } else if (stateId.equals("")) {
            binding.tvState.setError(getString(R.string.please_enter_state));
            binding.tvState.setFocusable(true);
        } else if (cityId.equals("")) {
            binding.etCity.setError(getString(R.string.please_enter_city));
            binding.etCity.setFocusable(true);
        }*/ else {
            updateProfile();
        }


    }


    public void ShowHidePass(View view) {

        if (view.getId() == R.id.show_pass_btn) {

            if (binding.password.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.eye1);

                //Show Password
                binding.password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.blind);

                //Hide Password
                binding.password.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }

    public void ShowHidePass11(View view) {

        if (view.getId() == R.id.show_pass_btn11) {

            if (binding.confirmPassword.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                ((ImageView) (view)).setImageResource(R.drawable.eye1);

                //Show Password
                binding.confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                ((ImageView) (view)).setImageResource(R.drawable.blind);

                //Hide Password
                binding.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

            }
        }
    }


    private void showDropDownCountry(View v, TextView textView, List<CountryModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(SubSellerSignupAct.this, v);
        for (int i = 0; i < stringList.size(); i++) {
            popupMenu.getMenu().add(stringList.get(i).getName());
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            textView.setText(menuItem.getTitle());
            for (int i = 0; i < stringList.size(); i++) {
                if (stringList.get(i).getName().equalsIgnoreCase(menuItem.getTitle().toString())) {
                    countryId = stringList.get(i).getId();
                    countryName = stringList.get(i).getName();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("country_id", countryId);
                    // signupViewModel.getStateCity(SignupAct.this,map);
                    subSellersignupViewModel.getCountryStates(SubSellerSignupAct.this, map);
                }
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDropDownState(View v, TextView textView, List<StateModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(SubSellerSignupAct.this, v);
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
                    // signupViewModel.getStateCity(SignupAct.this,map);
                    subSellersignupViewModel.getStateCity(SubSellerSignupAct.this, map);
                }
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDropDownCity(View v, TextView textView, List<CityModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(SubSellerSignupAct.this, v);
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
        subSellersignupViewModel.isResponse.observe(this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.SUB_SELLER_SIGNUP) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("create sub seller response===", stringResponse);
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("1")) {
                                //JSONObject object = jsonObject.getJSONObject("result");
                                  Toast.makeText(SubSellerSignupAct.this, getString(R.string.sub_seller_register_sucessfully), Toast.LENGTH_SHORT).show();
                                 finish();


                            } else if (jsonObject.getString("status").equals("0")) {
                                Toast.makeText(SubSellerSignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubSellerSignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                               // countryId = countryArrayList.get(0).getId();
                                map.put("country_id", countryArrayList.get(0).getId());
                                subSellersignupViewModel.getCountryStates(SubSellerSignupAct.this, map);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubSellerSignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubSellerSignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_STATE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                StateModel stateModel = new Gson().fromJson(stringResponse, StateModel.class);
                                stateArrayList.clear();
                                stateArrayList.addAll(stateModel.getResult());
                               // stateId = stateArrayList.get(0).getId();
                                HashMap<String, String> map = new HashMap<>();
                                map.put("state_id", stateArrayList.get(0).getId());
                                subSellersignupViewModel.getStateCity(SubSellerSignupAct.this, map);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubSellerSignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubSellerSignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.GET_ALL_STATE_CITY) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                CityModel cityModel = new Gson().fromJson(stringResponse, CityModel.class);
                                cityArrayList.clear();
                                cityArrayList.addAll(cityModel.getResult());
                               // cityId = cityArrayList.get(0).getId();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SubSellerSignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SubSellerSignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    private void observeLoader() {
        subSellersignupViewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SubSellerSignupAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_ADDRESS) {
            if (resultCode == RESULT_OK) {
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

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            }

        }

        else if (requestCode == SELECT_FILE) {
            str_image_path = DataManager.getInstance().getRealPathFromURI(SubSellerSignupAct.this, data.getData());
            try {
                oneBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            Glide.with(SubSellerSignupAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(binding.ivSeller);


        }
        else if (requestCode == REQUEST_CAMERA) {
            oneBitmap = (Bitmap) data.getExtras().get("data");   //
            Log.e("=======",oneBitmap+"");
            Glide.with(SubSellerSignupAct.this)
                    .load(oneBitmap)
                    .centerCrop()
                    .into(binding.ivSeller);
        }

    }


    private void setCountryCodeFromLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            try {
                FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            if (addresses != null && !addresses.isEmpty()) {
                                String countryCode = addresses.get(0).getCountryCode();
                                if (countryCode != null && !countryCode.isEmpty()) {
                                    Log.e("country code===", CountryCodes.getPhoneCode(countryCode) + "");
                                    binding.ccp.setCountryForPhoneCode(/*getCountryPhoneCode(countryCode)*/
                                            CountryCodes.getPhoneCode(countryCode));
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Error determining location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private int getCountryPhoneCode(String countryCode) {
        switch (countryCode) {
            case "IN":
                return 91; // India
            case "US":
                return 1;  // United States
            case "GA":
                return 241;

            // Add other countries as needed
            default:
                return 1; // Default to US if unknown
        }
    }




    public void showImageSelection() {

        final Dialog dialog = new Dialog(SubSellerSignupAct.this);
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
        if (takePictureIntent.resolveActivity(EditProfileAct.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(EditProfileAct.this,
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
        if (ContextCompat.checkSelfPermission(SubSellerSignupAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(SubSellerSignupAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(SubSellerSignupAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SubSellerSignupAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(SubSellerSignupAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(SubSellerSignupAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(SubSellerSignupAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SubSellerSignupAct.this,
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
        if (ContextCompat.checkSelfPermission(SubSellerSignupAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(SubSellerSignupAct.this,
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED

        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SubSellerSignupAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(SubSellerSignupAct.this,
                            Manifest.permission.READ_MEDIA_IMAGES)
            ) {


                ActivityCompat.requestPermissions(SubSellerSignupAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(SubSellerSignupAct.this,
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
                        Toast.makeText(SubSellerSignupAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SubSellerSignupAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(SubSellerSignupAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SubSellerSignupAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }

            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION : {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setCountryCodeFromLocation();
                }
            }
        }
    }





    private void updateProfile() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(SubSellerSignupAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        MultipartBody.Part filePart;
        if (oneBitmap!=null) {
            File file = persistImage(oneBitmap,"",SubSellerSignupAct.this);//DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            // str_image_path = DataManager.compressImage(getActivity(),str_image_path);
            //File file = new File(str_image_path);
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), binding.userName.getText().toString());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"),binding.name.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"),binding.email.getText().toString().trim());
        RequestBody password = RequestBody.create(MediaType.parse("text/plain"),binding.password.getText().toString());
        RequestBody countryCode = RequestBody.create(MediaType.parse("text/plain"),binding.ccp.getSelectedCountryCode() + "");
        RequestBody mobile = RequestBody.create(MediaType.parse("text/plain"),binding.phone.getText().toString());
        RequestBody parent_seller_id = RequestBody.create(MediaType.parse("text/plain"),DataManager.getInstance().getUserData(SubSellerSignupAct.this).getResult().getId());
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"),countryId );
        RequestBody state = RequestBody.create(MediaType.parse("text/plain"),stateId );
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"), cityId);
        RequestBody addresss = RequestBody.create(MediaType.parse("text/plain"),address );

        subSellersignupViewModel.signupSubSeller(SubSellerSignupAct.this,headerMap,  name,userName,email,password,countryCode,mobile,parent_seller_id,country,state,city,addresss,filePart);
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



}

