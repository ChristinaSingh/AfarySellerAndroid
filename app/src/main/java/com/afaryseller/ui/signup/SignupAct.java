package com.afaryseller.ui.signup;

import static com.afaryseller.retrofit.Constant.emailPattern;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySignupBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.addshop.model.StateModel;
import com.afaryseller.ui.login.LoginAct;
import com.afaryseller.ui.otp.OtpAct;
import com.afaryseller.ui.splash.ChooseAct;
import com.afaryseller.utility.CountryCodes;
import com.afaryseller.utility.DataManager;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupAct extends BaseActivity<ActivitySignupBinding, SignupViewModel> {
    public String TAG = "SignupAct";

    ActivitySignupBinding binding;
    SignupViewModel signupViewModel;
    double latitude = 0.0, longitude = 0.0;
    int AUTOCOMPLETE_REQUEST_CODE_ADDRESS = 101;
    String address = "", countryId = "", stateId = "", cityId = "", countryName = "";
    String firebaseToken = "", lang = "", currency = "";
    ArrayList<CountryModel.Result> countryArrayList;
    ArrayList<CityModel.Result> cityArrayList;
    ArrayList<StateModel.Result> stateArrayList;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean emailExit=false;
    private boolean  numberExit=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        signupViewModel = new SignupViewModel();
        binding.setSignupViewModel(signupViewModel);
        binding.getLifecycleOwner();
        signupViewModel.init(SignupAct.this);
        initViews();
        observeLoader();
        observeResponse();
    }

    private void initViews() {
        countryArrayList = new ArrayList<>();
        stateArrayList = new ArrayList<>();
        cityArrayList = new ArrayList<>();

        setCountryCodeFromLocation();

        if (getIntent() != null) {
            lang = getIntent().getStringExtra("lang");
            currency = getIntent().getStringExtra("currency");

        }

        binding.ccp.setCountryForPhoneCode(241);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);
            firebaseToken = token;

        });

        binding.btnContinue.setOnClickListener(view -> {
            startActivity(new Intent(this, OtpAct.class));
            //  finish();
        });
        binding.txtlogin.setOnClickListener(view -> {
            startActivity(new Intent(this, LoginAct.class).putExtra("type", ""));
            finish();
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
            Places.initialize(SignupAct.this, getString(R.string.place_api_key));
        }


        binding.tvAddress.setOnClickListener(v -> {
            List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS);

            // Start the autocomplete intent.
            Intent intent = new Autocomplete.IntentBuilder(
                    AutocompleteActivityMode.FULLSCREEN, fields)
                    //.setCountry("SA")
                    .build(SignupAct.this);

            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_ADDRESS);
        });

        binding.btnContinue.setOnClickListener(view -> validation());

        signupViewModel.getAllCountry(SignupAct.this);


        binding.email.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // When the user finishes entering the email, call API to check if the email exists
                if (binding.email.getText().toString().trim().isEmpty()) {
                    binding.email.setError(getString(R.string.please_enter_email));
                    binding.email.setFocusable(true);
                    Toast.makeText(SignupAct.this, getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                }
                else if (!binding.email.getText().toString().trim().matches(emailPattern)) {
                    binding.email.setError(getString(R.string.wrong_email));
                    binding.email.setFocusable(true);
                    Toast.makeText(SignupAct.this, getString(R.string.wrong_email), Toast.LENGTH_SHORT).show();
                }
                else {
                    checkEmailExistence(binding.email.getText().toString());

                }
            }
        });

        binding.phone.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                // When the user finishes entering the phone number, call API to check if the number exists
                checkNumberExistence(binding.ccp.getSelectedCountryCode(), binding.phone.getText().toString());
            }
        });


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
        }
        else if (!emailExit) {
            binding.email.setError(getString(R.string.email_already_exits));
            binding.email.setFocusable(true);
            Toast.makeText(SignupAct.this, getString(R.string.email_already_exits), Toast.LENGTH_SHORT).show();
        }
        else if (binding.phone.getText().toString().equals("")) {
            binding.phone.setError(getString(R.string.please_enter_phone_number));
            binding.phone.setFocusable(true);
        }

        else if (!numberExit) {
            binding.phone.setError(getString(R.string.mobile_number_already_exits));
            binding.phone.setFocusable(true);
            Toast.makeText(SignupAct.this, getString(R.string.mobile_number_already_exits), Toast.LENGTH_SHORT).show();
        }

        else if (binding.password.getText().toString().equals("")) {
            binding.password.setError(getString(R.string.enter_password));
            binding.password.setFocusable(true);
        } else if (binding.confirmPassword.getText().toString().equals("")) {
            binding.confirmPassword.setError(getString(R.string.please_enter_confirm_password));
            binding.confirmPassword.setFocusable(true);
        } else if (!binding.confirmPassword.getText().toString().equals(binding.password.getText().toString())) {
            Toast.makeText(this, getString(R.string.password_does_not_matched), Toast.LENGTH_SHORT).show();

        } else if (address.equals("")) {
            Toast.makeText(this, getString(R.string.please_select_address), Toast.LENGTH_SHORT).show();
        } else if (countryId.equals("")) {
            binding.tvSelectCountry.setError(getString(R.string.please_enter_country));
            binding.tvSelectCountry.setFocusable(true);
        } else if (stateId.equals("")) {
            binding.tvState.setError(getString(R.string.please_enter_state));
            binding.tvState.setFocusable(true);
        } else if (cityId.equals("")) {
            binding.etCity.setError(getString(R.string.please_enter_city));
            binding.etCity.setFocusable(true);
        } else {
            startActivity(new Intent(SignupAct.this, OtpAct.class)
                    .putExtra("user_name",binding.userName.getText().toString())
                    .putExtra("name", binding.name.getText().toString())
                    .putExtra("email", binding.email.getText().toString().trim())
                    .putExtra("mobile",binding.phone.getText().toString())
                    .putExtra("country_code", binding.ccp.getSelectedCountryCode() + "")
                    .putExtra("password", binding.password.getText().toString())
                    .putExtra("address",address)
                    .putExtra("lat",latitude + "")
                    .putExtra("lon",longitude + "")
                    .putExtra("country",countryId)
                    .putExtra("country_name",countryName)
                    .putExtra("state",stateId)
                    .putExtra("city",cityId)
                    .putExtra("language",lang)
                    .putExtra("type",Constant.SUBADMIN)
                    .putExtra("currency",currency)
                    .putExtra("zip_code",binding.etZipCode.getText().toString())
                    .putExtra("seller_register_id",firebaseToken));
            finish();



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
        PopupMenu popupMenu = new PopupMenu(SignupAct.this, v);
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
                    signupViewModel.getCountryStates(SignupAct.this, map);
                }
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDropDownState(View v, TextView textView, List<StateModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(SignupAct.this, v);
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
                    signupViewModel.getStateCity(SignupAct.this, map);
                }
            }
            return true;
        });
        popupMenu.show();
    }


    private void showDropDownCity(View v, TextView textView, List<CityModel.Result> stringList) {
        PopupMenu popupMenu = new PopupMenu(SignupAct.this, v);
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
        signupViewModel.isResponse.observe(this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.SIGNUP) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("result");
                                //  Toast.makeText(SignupAct.this, getString(R.string.p), Toast.LENGTH_SHORT).show()
                                startActivity(new Intent(SignupAct.this, OtpAct.class)
                                        .putExtra("mobile", binding.phone.getText().toString())
                                        .putExtra("countryCode", binding.ccp.getSelectedCountryCode())
                                        .putExtra("user_id", object.getString("id")));
                                finish();


                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                countryId = countryArrayList.get(0).getId();
                                map.put("country_id", countryId);
                                signupViewModel.getCountryStates(SignupAct.this, map);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                stateId = stateArrayList.get(0).getId();
                                HashMap<String, String> map = new HashMap<>();
                                map.put("state_id", stateId);
                                signupViewModel.getStateCity(SignupAct.this, map);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
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
                                cityId = cityArrayList.get(0).getId();
                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(SignupAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if (dynamicResponseModel.getApiName() == ApiConstant.CHECK_EMAIL_EXITS) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response===", stringResponse);
                            if (jsonObject.optString("status").equals("1")) {
                                emailExit = true;
                            } else if (jsonObject.optString("status").equals("0")) {
                                emailExit = false;
                                binding.email.setError(getString(R.string.email_already_exits));
                                binding.email.setFocusable(true);
                                Toast.makeText(SignupAct.this, getString(R.string.email_already_exits), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.CHECK_MOBILE_EXITS) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response===", stringResponse);
                            if (jsonObject.optString("status").equals("1")) {
                                numberExit =true;

                            } else if (jsonObject.optString("status").equals("0")) {
                                numberExit =false;
                                binding.phone.setError(getString(R.string.mobile_number_already_exits));
                                binding.phone.setFocusable(true);
                                Toast.makeText(SignupAct.this, getString(R.string.mobile_number_already_exits), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignupAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private void observeLoader() {
        signupViewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(SignupAct.this, false, getString(R.string.please_wait));
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setCountryCodeFromLocation();
            }
        }
    }



    private void checkEmailExistence(String email) {
        //binding.loader.setVisibility(View.VISIBLE);

        if (email.isEmpty()) {
            // Don't enable the next EditText if email is empty
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("email", email);
        Log.e("MapMap", "Check email Exit Request" + map);
        signupViewModel.checkEmailExits(SignupAct.this,map);


    }

    private void checkNumberExistence(String countryCode,String number) {
        //binding.loader.setVisibility(View.VISIBLE);

        if (number.isEmpty()) {
            // binding.phone.setEnabled(false); // Don't enable the next EditText if phone number is empty
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("mobile",number);
        map.put("country_code",countryCode );
        Log.e("MapMap", "Check number Exit Request" + map);
        signupViewModel.checkMobileExits(SignupAct.this,map);

    }

}
