package com.afaryseller.ui.uploadids;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivitySelectSkillsBinding;
import com.afaryseller.databinding.ActivityUploadIdBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.login.LoginAct;
import com.afaryseller.ui.notifyadmin.NotifyAdminAct;
import com.afaryseller.ui.selectskills.SelectSkillsAct;
import com.afaryseller.ui.selectskills.SkillsViewModel;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterFour;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterOne;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterThree;
import com.afaryseller.ui.selectskills.adapter.SkillAdapterTwo;
import com.afaryseller.ui.selectskills.model.SkillModel;
import com.afaryseller.ui.splash.PermissionPageOneAct;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.utility.DataManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.vilborgtower.user.utils.RealPathUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadIdsAct extends BaseActivity<ActivityUploadIdBinding, UploadIdViewModel> {
    ActivityUploadIdBinding binding;
    UploadIdViewModel uploadIdViewModel;
    String str_image_path = "", onePath = "", twoPath = "";
    private static final int REQUEST_CAMERA = 1;
    private static final int SELECT_FILE = 2;
    private static final int MY_PERMISSION_CONSTANT = 5;
    String chk = "", userId = "";

    Bitmap oneBitmap=null,twoBitmap=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_upload_id);
        uploadIdViewModel = new UploadIdViewModel();
        binding.setUploadIdViewModel(uploadIdViewModel);
        binding.getLifecycleOwner();
        uploadIdViewModel.init(UploadIdsAct.this);
        initViews();
        observeLoader();
        observeResponse();
    }

    public void observeResponse() {
        uploadIdViewModel.isResponse.observe(this, dynamicResponseModel -> {
            if (dynamicResponseModel.getJsonObject() != null) {
                pauseProgressDialog();
                if (dynamicResponseModel.getApiName() == ApiConstant.SELLER_UPDATE_DOCUMENT) {
                    try {
                        if (dynamicResponseModel.getCode() == 200) {
                            Log.e("response upload ids===", dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                notifyToAdmin(userId,"Documents uploaded");

                            } else if (jsonObject.getString("status").toString().equals("0")) {

                                Toast.makeText(UploadIdsAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(UploadIdsAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                if(dynamicResponseModel.getApiName()== ApiConstant.SEND_ADMIN_MSG){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            Log.e("response===",stringResponse);

                            if (jsonObject.getString("status").equals("1")) {
                                Toast.makeText(UploadIdsAct.this, getString(R.string.register_successfully), Toast.LENGTH_SHORT).show();
                                UploadManager.uploadDocuments(UploadIdsAct.this, userId, oneBitmap, twoBitmap);
                                startActivity(new Intent(UploadIdsAct.this, PermissionPageOneAct.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                finish();

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            }



                        }
                        else {
                            Toast.makeText(this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }



        });
    }

    private void observeLoader() {
        uploadIdViewModel.isLoading.observe(this, aBoolean -> {
            if (aBoolean) {
                showProgressDialog(UploadIdsAct.this, false, getString(R.string.please_wait));
            } else {
                pauseProgressDialog();
            }
        });
    }


    private void initViews() {

        if (getIntent() != null) userId = getIntent().getStringExtra("user_id");

        binding.btnRegister.setOnClickListener(view -> {
            if (oneBitmap==null)
                Toast.makeText(this, getString(R.string.please_upload_id), Toast.LENGTH_SHORT).show();
            else if (twoBitmap == null)
                Toast.makeText(this,getString(R.string.please_upload_id) , Toast.LENGTH_SHORT).show();
            else if (!binding.chkTerm.isChecked())
                Toast.makeText(this, getString(R.string.please_check_terms_and_condition), Toast.LENGTH_SHORT).show();
            else uploadDoc();

        });

        binding.imageLinear.setOnClickListener(view -> {
            chk = "one";
            if (Build.VERSION.SDK_INT >= 33) {
                if(checkPermissionFor12Above()) showImageSelection();
            }
            else {
                if (checkPermisssionForReadStorage()) showImageSelection();
            }
        });

        binding.linearImage1.setOnClickListener(view -> {
            chk = "two";
            if (Build.VERSION.SDK_INT >= 33) {
                if(checkPermissionFor12Above()) showImageSelection();
            }
            else {
                if (checkPermisssionForReadStorage()) showImageSelection();
            }
        });





    }

    public void showImageSelection() {

        final Dialog dialog = new Dialog(UploadIdsAct.this);
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
        //    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent intent = new Intent(Intent.ACTION_PICK,  android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), SELECT_FILE);

    }


    /* if (Build.VERSION.SDK_INT >= 33) {

          if (oneBitmap!=null) {
              File file = persistImage(oneBitmap,"",UploadIdsAct.this);                 //  DataManager.getInstance().saveBitmapToFile(new File(onePath));
              //  File file = new File(onePath);
              filePart = MultipartBody.Part.createFormData("document_image_1", file.getName(), RequestBody.create(MediaType.parse("document_image_1/*"), file));
          } else {
              RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
              filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
          }


      }
      else {
          if (!onePath.equalsIgnoreCase("")) {
              File file = DataManager.getInstance().saveBitmapToFile(new File(onePath));
              //  File file = new File(onePath);
              filePart = MultipartBody.Part.createFormData("document_image_1", file.getName(), RequestBody.create(MediaType.parse("document_image_1/*"), file));
          } else {
              RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
              filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
          }
      }

        if (Build.VERSION.SDK_INT >= 33) {

           if (twoBitmap!=null) {
               File file =    persistImage(twoBitmap,"",UploadIdsAct.this);          //DataManager.getInstance().compressFile(new File(twoPath));
               // File file = new File(twoPath);
               filePart2 = MultipartBody.Part.createFormData("document_image_2", file.getName(), RequestBody.create(MediaType.parse("document_image_2/*"), file));
           } else {
               RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
               filePart2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
           }

       }

       else {
           if (!twoPath.equalsIgnoreCase("")) {
               File file = DataManager.getInstance().compressFile(new File(twoPath));
               // File file = new File(twoPath);
               filePart2 = MultipartBody.Part.createFormData("document_image_2", file.getName(), RequestBody.create(MediaType.parse("document_image_2/*"), file));
           } else {
               RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
               filePart2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
           }
       }



      */
    public void uploadDoc() {
        MultipartBody.Part filePart, filePart2;






        if (oneBitmap!=null) {
           // File file = persistImage(oneBitmap,"",UploadIdsAct.this);                 //  DataManager.getInstance().saveBitmapToFile(new File(onePath));
            String uniqueFileName = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString();

            File file =  persistImage(oneBitmap,uniqueFileName,UploadIdsAct.this);
            //  File file = new File(onePath);
            filePart = MultipartBody.Part.createFormData("document_image_1", file.getName(), RequestBody.create(MediaType.parse("document_image_1/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        if (twoBitmap!=null) {
            String uniqueFileName = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString();
            File file =    persistImage(twoBitmap,uniqueFileName,UploadIdsAct.this);          //DataManager.getInstance().compressFile(new File(twoPath));
            // File file = new File(twoPath);
            filePart2 = MultipartBody.Part.createFormData("document_image_2", file.getName(), RequestBody.create(MediaType.parse("document_image_2/*"), file));
        } else {
            RequestBody attachmentEmpty = RequestBody.create(MediaType.parse("text/plain"), "");
            filePart2 = MultipartBody.Part.createFormData("attachment", "", attachmentEmpty);
        }

        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), userId);

        uploadIdViewModel.uploadIds(UploadIdsAct.this, user_id, filePart, filePart2);
    }

    private void openCamera() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(UploadIdsAct.this,
                        "com.afaryseller.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }

        //  Intent intent = new Intent(Intent.ACTION_PICK,
            //    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
      //  Intent intent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
     //   startActivityForResult(intent, REQUEST_CAMERA);

    }



    private File saveMediaToStorage( Bitmap bitmap) throws  NullPointerException {
        Uri tempUri = getImageUri(UploadIdsAct.this, bitmap);
        String imag = RealPathUtil.INSTANCE.getRealPath(UploadIdsAct.this, tempUri);
        File profileImage = new File(imag);
        //UploadQR(profileImage)
return profileImage;

    }

      private Uri getImageUri( Context  inContext,  Bitmap  inImage){
          ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String  path = MediaStore.Images.Media.insertImage(
                inContext.getContentResolver(),
                inImage,
                "Title" + System.currentTimeMillis(),
                null
        );
        return Uri.parse(path);
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

        if (chk.equalsIgnoreCase("one")) onePath = str_image_path;
        else twoPath = str_image_path;
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.e("Result_code", resultCode + "");
            binding.img1.setVisibility(View.VISIBLE);
            binding.img2.setVisibility(View.VISIBLE);
            if (requestCode == SELECT_FILE) {

                str_image_path = DataManager.getInstance().getPath(UploadIdsAct.this, data.getData());
                if (chk.equalsIgnoreCase("one")) {
                    binding.llOne.setVisibility(View.GONE);
                    binding.llTwo.setVisibility(View.VISIBLE);
                    onePath = str_image_path;
                    try {
                        oneBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                       /* if(oneBitmap!=null) {
                            oneBitmap = resizeBitmap(oneBitmap, 3000, 3000);
                        }*/

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Log.e("bitmap===",oneBitmap+"");
                    Glide.with(UploadIdsAct.this)
                            .load(oneBitmap)
                            .centerCrop()
                            .into(binding.img1);

                } else {
                    binding.llOne.setVisibility(View.VISIBLE);
                    binding.llTwo.setVisibility(View.GONE);
                    twoPath = str_image_path;
                    //twoBitmap = (Bitmap) data.getExtras().get("data");
                    try {
                        twoBitmap =    MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                       /* if(twoBitmap!=null) {
                            twoBitmap = resizeBitmap(twoBitmap, 3000, 3000);
                        }*/


                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }


                    Glide.with(UploadIdsAct.this)
                            .load(twoBitmap)
                            .centerCrop()
                            .into(binding.img2);
                }


            } else if (requestCode == REQUEST_CAMERA) {
                if (chk.equalsIgnoreCase("one")) {
                    binding.llOne.setVisibility(View.GONE);
                    binding.llTwo.setVisibility(View.VISIBLE);
                    onePath = str_image_path;
                    /*oneBitmap = (Bitmap) data.getExtras().get("data");
                   *//* if(oneBitmap!=null) {
                        oneBitmap = resizeBitmap(oneBitmap, 3000, 3000);
                    }*//*

                    Log.e("=======",oneBitmap+"");
                    Glide.with(UploadIdsAct.this)
                            .load(oneBitmap)
                            .centerCrop()
                            .into(binding.img1);*/



                    Glide.with(UploadIdsAct.this)
                            .asBitmap()
                            .load(onePath)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    oneBitmap = resource;
                                    binding.img1.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });





                } else {
                    binding.llOne.setVisibility(View.VISIBLE);
                    binding.llTwo.setVisibility(View.GONE);
                    twoPath = str_image_path;

                   /* twoBitmap = (Bitmap) data.getExtras().get("data");   //MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                   *//* if(twoBitmap!=null) {
                        twoBitmap = resizeBitmap(twoBitmap, 3000, 3000);
                    }*//*
                    Log.e("=======",oneBitmap+"");
                    Glide.with(UploadIdsAct.this)
                            .load(twoBitmap)
                            .centerCrop()
                            .into(binding.img2);*/



                    Glide.with(UploadIdsAct.this)
                            .asBitmap()
                            .load(twoPath)  // URL or file path
                            .centerCrop()
                            .into(new CustomTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    // Bitmap is now ready to use
                                    // Do something with the Bitmap
                                    twoBitmap = resource;
                                    binding.img2.setImageBitmap(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                    // Handle cleanup if necessary
                                }
                            });

                }
            }


        }
    }


    public static Bitmap ConvertUrlToBitmap(String src)
    {
        try{
            URL url = new URL(src);
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            return image;
        }
        catch (IOException e)
        {
            return null;
        }
    }

    //CHECKING FOR Camera STATUS
    public boolean checkPermisssionForReadStorage() {
        if (ContextCompat.checkSelfPermission(UploadIdsAct.this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(UploadIdsAct.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
                ||

                ContextCompat.checkSelfPermission(UploadIdsAct.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED
        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadIdsAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(UploadIdsAct.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    ||
                    ActivityCompat.shouldShowRequestPermissionRationale(UploadIdsAct.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)


            ) {


                ActivityCompat.requestPermissions(UploadIdsAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_CONSTANT);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(UploadIdsAct.this,
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
        if (ContextCompat.checkSelfPermission(UploadIdsAct.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED

                ||

                ContextCompat.checkSelfPermission(UploadIdsAct.this,
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED

        ) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(UploadIdsAct.this,
                    Manifest.permission.CAMERA)

                    ||

                    ActivityCompat.shouldShowRequestPermissionRationale(UploadIdsAct.this,
                            Manifest.permission.READ_MEDIA_IMAGES)
            ) {


                ActivityCompat.requestPermissions(UploadIdsAct.this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_MEDIA_IMAGES},
                        101);

            } else {

                //explain("Please Allow Location Permission");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(UploadIdsAct.this,
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
                        Toast.makeText(UploadIdsAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UploadIdsAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(UploadIdsAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UploadIdsAct.this, "12 permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }



    private static File persistImage(Bitmap bitmap, String name, Context cOntext) {
        File filesDir = cOntext.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

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


    private Bitmap resizeBitmap(Bitmap bitmap, int maxWidth, int maxHeight) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float ratio = (float) width / height;

        if (width > maxWidth) {
            width = maxWidth;
            height = (int) (width / ratio);
        }
        if (height > maxHeight) {
            height = maxHeight;
            width = (int) (height * ratio);
        }

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }



    private void notifyToAdmin(String userId,String messageText) {
        HashMap<String, String> map = new HashMap<>();
        map.put("message",messageText);
        map.put("user_id", userId);

        Log.e("NotifyAdminAct", "Notify admin msg Request :" + map);
        uploadIdViewModel.sendAdminMsg(UploadIdsAct.this,map);
    }



}
