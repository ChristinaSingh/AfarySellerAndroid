package com.afaryseller.ui.home.fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseFragment;
import com.afaryseller.databinding.FragmentProfileBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.changepassword.ChangePasswordAct;
import com.afaryseller.ui.chat.ChatListAct;
import com.afaryseller.ui.editprofile.EditProfileAct;
import com.afaryseller.ui.membershipplan.CurrentMemberShipPlanAct;
import com.afaryseller.ui.notifyadmin.NotifyAdminAct;
import com.afaryseller.ui.onlineorderhistory.onlineOrderHistoryAct;
import com.afaryseller.ui.sellerreport.SellerReportAct;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.ui.splash.WebViewAct;
import com.afaryseller.ui.subseller.changepassword.SubSellerChangePasswordAct;
import com.afaryseller.ui.subseller.subsellerlsit.SubSellerListAct;
import com.afaryseller.ui.subseller.updateprofile.UpdateSubSellerProfileAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends BaseFragment<FragmentProfileBinding, ProfileViewModel> {
    FragmentProfileBinding binding;
    ProfileViewModel profileViewModel;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private String str_image_path = "",latitude="",longitude="",countryId="",stateId="",cityId="",subSellerId="";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT2 = 5;
    Bitmap oneBitmap = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        profileViewModel = new ProfileViewModel();
        binding.setProfileViewModel(profileViewModel);
        binding.getLifecycleOwner();
        profileViewModel.init(getActivity());
        initViews();
        return binding.getRoot();
    }

    private void initViews() {

        binding.backNavigation.setOnClickListener(v -> getActivity().onBackPressed());


        binding.RRLogout.setOnClickListener(v -> {
                    //  SessionManager.clear(getActivity(),DataManager.getInstance().getUserData(getActivity()).getResult().getId())

                    showLogoutDialog();
                }
        );


        binding.RRSubLogout.setOnClickListener(v -> {
            showLogoutDialog();
                }
        );



        binding.txtTerms.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), WebViewAct.class)
                        .putExtra("url", ApiConstant.TERMS_AND_CONDITIONS)
                        .putExtra("title", getString(R.string.terms_and_conditions))));

        binding.txtSubTerms.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), WebViewAct.class)
                        .putExtra("url", ApiConstant.TERMS_AND_CONDITIONS)
                        .putExtra("title", getString(R.string.terms_and_conditions))));


        binding.txtPrivacy.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), WebViewAct.class)
                        .putExtra("url", ApiConstant.PRIVACY_POLICY)
                        .putExtra("title", getString(R.string.privacy_policy))));

        binding.txtSubPrivacy.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), WebViewAct.class)
                        .putExtra("url", ApiConstant.PRIVACY_POLICY)
                        .putExtra("title", getString(R.string.privacy_policy))));


        binding.txtChangePassword.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ChangePasswordAct.class)));

        binding.txtSubChangePassword.setOnClickListener(v ->{
                startActivity(new Intent(getActivity(), SubSellerChangePasswordAct.class)
                        .putExtra("subSellerId",subSellerId)
                        .putExtra("userType","seller"));
        });



        binding.txtupdate.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), EditProfileAct.class)));


        binding.txtSubupdate.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), UpdateSubSellerProfileAct.class)
                    .putExtra("subSellerId",subSellerId));
        });

        binding.txtChat.setOnClickListener(view -> startActivity(new Intent(getActivity(), ChatListAct.class)));

        binding.txtSubChat.setOnClickListener(view -> startActivity(new Intent(getActivity(), ChatListAct.class)));


        binding.rlOrderHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), onlineOrderHistoryAct.class)));

        binding.rlSubOrderHistory.setOnClickListener(v -> startActivity(new Intent(getActivity(), onlineOrderHistoryAct.class)));


        binding.txtMembership.setOnClickListener(v -> startActivity(new Intent(getActivity(), CurrentMemberShipPlanAct.class)));

        binding.txtSubMembership.setOnClickListener(v -> startActivity(new Intent(getActivity(), CurrentMemberShipPlanAct.class)));





        binding.tvNotifyAdmin.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotifyAdminAct.class)));

        binding.tvSubSeller.setOnClickListener(v -> startActivity(new Intent(getActivity(), SubSellerListAct.class)));

        binding.tvReport.setOnClickListener(v -> startActivity(new Intent(getActivity(), SellerReportAct.class)));








        binding.ivSeller.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= 33) {
                if (checkPermissionFor12Above()) showImageSelection();
            } else {
                if (checkPermissionForReadStorage11()) showImageSelection();
            }

        });



        binding.RRChangeLanguage.setOnClickListener(v -> {
            showLanguageChangeDialog();
        });

        observeLoader();
        observeResponse();



    }


    @Override
    public void onResume() {
        super.onResume();
        Log.e("type====",DataManager.getInstance().getUserData(requireActivity()).getResult().getType());

        if(DataManager.getInstance().getUserData(requireActivity()).getResult().getType().equals(Constant.SUBADMIN)) {
            binding.llSeller.setVisibility(View.VISIBLE);
            binding.rlNotifyAdmin.setVisibility(View.VISIBLE);
            binding.llSubSeller.setVisibility(View.GONE);

            getSellerProfile();
        }
        else {
            binding.llSeller.setVisibility(View.GONE);
            binding.rlNotifyAdmin.setVisibility(View.GONE);
            binding.llSubSeller.setVisibility(View.VISIBLE);
            subSellerId = DataManager.getInstance().getUserData(requireActivity()).getResult().getSub_seller_id();
            getSubSellerProfile();
        }


    }


    private void getSubSellerProfile() {
        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(requireActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("sub_seller_id", subSellerId);
        profileViewModel.getSubSellerProfile(requireActivity(),headerMap, map);
    }




    private void getSellerProfile() {
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + DataManager.getInstance().getUserData(getActivity()).getResult().getAccessToken());
        headerMap.put("Accept", "application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", DataManager.getInstance().getUserData(getActivity()).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(getActivity()).getResult().getRegisterId());

        profileViewModel.getSellerProfile(getActivity(), headerMap, map);
    }

    public void observeResponse() {
        profileViewModel.isResponse.observe(getActivity(), dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.GET_PROFILE) {
                    try {
                        pauseProgressDialog();
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("result");
                                // binding.tvName.setText(object.getString("name") + " " + object.getString("last_name"));
                                binding.tvName.setText(object.getString("user_name"));
                                showLang(object.getString("language"));
                                binding.tvEmail.setText(object.getString("email"));
                                binding.tvAddress.setText(object.getString("address"));
                                latitude = object.getString("lat");
                                longitude = object.getString("lon");
                                countryId = object.getString("country");
                                stateId = object.getString("state");
                                cityId = object.getString("city");



                                Glide.with(getActivity()).load(object.getString("image"))
                                        .error(R.drawable.user_default)
                                        .placeholder(R.drawable.user_default)
                                        .into(binding.ivSeller);

                                profileViewModel.isResponse = null;

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        pauseProgressDialog();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.GET_SUB_SELLER_PROFILE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            Log.e("get sub seller profile response===", dynamicResponseModel.getJsonObject().toString());

                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("data");
                                //  binding.tvName.setText(object.getString("name") + " " + object.getString("last_name"));
                                subSellerId = object.getString("id");
                                binding.tvName.setText(object.getString("name") );

                                binding.tvEmail.setText(object.getString("email"));
                                binding.tvAddress.setText(object.getString("address"));
                                //binding.tvEmail.setText(object.getString("email"));
                                showLang("");


                                Glide.with(requireActivity()).load(object.getString("image"))
                                        .error(R.drawable.user_default)
                                        .placeholder(R.drawable.user_default)
                                        .into(binding.ivSeller);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(requireActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }




                        } else {
                            Toast.makeText(requireActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }



                if (dynamicResponseModel.getApiName() == ApiConstant.UPDATE_PROFILE) {
                    try {
                        pauseProgressDialog();
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("1")) {
                                getSellerProfile();
                                profileViewModel.isResponse = null;
                            } else if (jsonObject.getString("status").equals("0")) {
                                Toast.makeText(requireActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").equals("5")) {
                                SessionManager.logout(requireActivity());
                            }


                        } else {
                            Toast.makeText(requireActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                        pauseProgressDialog();
                    }
                }

                if (dynamicResponseModel.getApiName() == ApiConstant.UPDATE_LANGUAGE) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                //showLang(language);
                                Intent intent = new Intent(requireActivity(), SplashAct.class);
                                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK));

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            } else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        } else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }


        });








/*
        profileViewModel.isResponse.observe(getActivity(),dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.GET_PROFILE_UPDATE){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").equals("1")) {
                                JSONObject object = jsonObject.getJSONObject("result");
                                LoginModel loginModel = new Gson().fromJson(stringResponse,LoginModel.class);
                                SessionManager.writeString(requireActivity(), Constant.SELLER_INFO, stringResponse);

                                profileViewModel.isResponse = null;

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(getActivity(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(getActivity());
                            }

                        }
                        else {
                            Toast.makeText(getActivity(), dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }




            }
        } );
*/


    }

    private void observeLoader() {
        profileViewModel.isLoading.observe(getActivity(), aBoolean -> {
            if (aBoolean) {
                showProgressDialog(getActivity(), false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }


    private void showLanguageChangeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(R.string.change_language)
                .setItems(new CharSequence[]{
                        getString(R.string.english),
                        getString(R.string.french)
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // English
                                changeLocale("en");
                                break;
                            case 1:
                                // French
                                changeLocale("fr");
                                break;
                        }
                    }
                });
        builder.create().show();
    }


    private void changeLocale(String en) {
        updateResources(requireActivity(), en);
        updateLanguage(DataManager.getInstance().getUserData(getActivity()).getResult().getId(), en);

    }

    private void updateLanguage(String id, String en) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", id);
        map.put("language", en);
        SessionManager.writeString(requireActivity(), Constant.SELLER_LANGUAGE, en);
        profileViewModel.updateLanguage(getActivity(), map);
    }


    private void updateResources(Context wellcomeScreen, String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Resources resources = wellcomeScreen.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

    private void showLang(String language) {
        if (language.equalsIgnoreCase("en"))
        {

            if(DataManager.getInstance().getUserData(requireActivity()).getResult().getType().equals(Constant.SUBADMIN))
                binding.txtLang.setText(getString(R.string.english));
            else binding.txtSubLang.setText(getString(R.string.english));

        }
        else if (language.equalsIgnoreCase("fr"))
        {
            if(DataManager.getInstance().getUserData(requireActivity()).getResult().getType().equals(Constant.SUBADMIN))
                binding.txtLang.setText(getString(R.string.french));
            else binding.txtSubLang.setText(getString(R.string.french));
        }
        else {
            if(DataManager.getInstance().getUserData(requireActivity()).getResult().getType().equals(Constant.SUBADMIN))
                binding.txtLang.setText(getString(R.string.english));
            else binding.txtSubLang.setText(getString(R.string.english));
        }

    }


    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        builder.setTitle(getString(R.string.logout))
                .setMessage(getString(R.string.are_you_sure_logout))
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        SessionManager.clearSession(requireActivity());
                        Intent intent = new Intent(requireActivity(), SplashAct.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle cancel action
                        dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



    public void showImageSelection() {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().getAttributes().windowAnimations = android.R.style.Widget_Material_ListPopupWindow;
        dialog.setContentView(R.layout.dialog_show_image_selection1);
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


        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
        str_image_path = image.getAbsolutePath();


        return image;
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermissionForReadStorage11() {
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
                        MY_PERMISSION_CONSTANT2);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT2);
            }
            return false;
        } else {

            //  explain("Please Allow Location Permission");
            return true;
        }
    }


    public boolean checkPermissionFor12Above() {
        if (ContextCompat.checkSelfPermission(requireActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(requireActivity(),
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED

        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(),
                            Manifest.permission.READ_MEDIA_IMAGES)
            ) {


                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(requireActivity(),
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
            case MY_PERMISSION_CONSTANT2: {
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
                        Toast.makeText(requireActivity(), "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(requireActivity(), "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
/*
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
*/
            if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
            } else if (requestCode == SELECT_FILE) {
                str_image_path = DataManager.getInstance().getRealPathFromURI(getActivity(), data.getData());

                try {
                    oneBitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), data.getData());
                       /* if(oneBitmap!=null) {
                            oneBitmap = resizeBitmap(oneBitmap, 3000, 3000);
                        }*/

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Log.e("bitmap===", oneBitmap + "");
                Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.ivSeller);

                updateProfile();


            } else if (requestCode == REQUEST_CAMERA) {
               /* Glide.with(getActivity())
                        .load(str_image_path)
                        .centerCrop()
                        .into(binding.imgUser);*/



              /*  Uri imageUri = Uri.parse(str_image_path);
                ContentResolver contentResolver = requireActivity().getContentResolver(); // Get the content resolver from your activity

                oneBitmap = getBitmapFromUri(imageUri, contentResolver);
                binding.imgUser.setImageBitmap(oneBitmap);*/



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
                                binding.ivSeller.setImageBitmap(resource);
                                updateProfile();
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                // Handle cleanup if necessary
                            }
                        });




            }


        }


    }



    private void updateProfile() {

        Map<String,String> headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(requireActivity()).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        MultipartBody.Part filePart;
        if (oneBitmap!=null) {
            File file = persistImage(oneBitmap,"",requireActivity());//DataManager.getInstance().saveBitmapToFile(new File(str_image_path));
            // str_image_path = DataManager.compressImage(getActivity(),str_image_path);
            //File file = new File(str_image_path);
            filePart = MultipartBody.Part.createFormData("image", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }
        RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(requireActivity()).getResult().getId());
        RequestBody userName = RequestBody.create(MediaType.parse("text/plain"),binding.tvName.getText().toString());
        RequestBody address = RequestBody.create(MediaType.parse("text/plain"),binding.tvAddress.getText().toString());
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"),latitude+"");
        RequestBody lon = RequestBody.create(MediaType.parse("text/plain"),longitude+"");
        RequestBody country = RequestBody.create(MediaType.parse("text/plain"),countryId);
        RequestBody city = RequestBody.create(MediaType.parse("text/plain"),cityId);
        RequestBody registerId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(requireActivity()).getResult().getRegisterId());
        RequestBody userSellerId = RequestBody.create(MediaType.parse("text/plain"), DataManager.getInstance().getUserData(requireActivity()).getResult().getId());


        profileViewModel.updateProfile(requireActivity(),headerMap,  userId,userName,address,lat,lon,country,city,registerId,userSellerId,filePart);
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
