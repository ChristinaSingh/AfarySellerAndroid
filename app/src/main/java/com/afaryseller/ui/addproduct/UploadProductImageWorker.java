package com.afaryseller.ui.addproduct;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;




public class UploadProductImageWorker extends Worker {

    public UploadProductImageWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Retrieve input data
        String token = getInputData().getString("token");
        String proIdId = getInputData().getString("pro_id");
        String filePath1 = getInputData().getString("file_path_1");
        String filePath2 = getInputData().getString("file_path_2");
        String filePath3 = getInputData().getString("file_path_3");
        String filePath4 = getInputData().getString("file_path_4");

        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + token);
        headerMap.put("Accept", "application/json");

        Log.e("Token====",headerMap.toString());

        // Prepare files
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        File file3 = new File(filePath3);
        File file4 = new File(filePath4);

        // Prepare Retrofit API service
        AfarySeller apiService = ApiClient.getClient(getApplicationContext()).create(AfarySeller.class);

        // Prepare MultipartBody.Part


        MultipartBody.Part part1 = file1.exists() ? MultipartBody.Part.createFormData("image", file1.getName(), RequestBody.create(file1, okhttp3.MediaType.parse("image/*"))) : null;
        MultipartBody.Part part2 = file2.exists() ? MultipartBody.Part.createFormData("image_1", file2.getName(), RequestBody.create(file2, okhttp3.MediaType.parse("image_1/*"))) : null;
        MultipartBody.Part part3 = file3.exists() ? MultipartBody.Part.createFormData("image_2", file3.getName(), RequestBody.create(file3, okhttp3.MediaType.parse("image_2/*"))) : null;
        MultipartBody.Part part4 = file4.exists() ? MultipartBody.Part.createFormData("image_3", file4.getName(), RequestBody.create(file4, okhttp3.MediaType.parse("image_3/*"))) : null;


      //  MultipartBody.Part part1 = file1.exists() ? MultipartBody.Part.createFormData("image", file1.getName(), RequestBody.create(file1, okhttp3.MediaType.parse("image/*"))) : MultipartBody.Part.createFormData("attachment", "", RequestBody.create(MediaType.parse("text/plain"), ""));
       // MultipartBody.Part part2 = file2.exists() ? MultipartBody.Part.createFormData("image_1", file2.getName(), RequestBody.create(file2, okhttp3.MediaType.parse("image_1/*"))) : MultipartBody.Part.createFormData("attachment", "", RequestBody.create(MediaType.parse("text/plain"), ""));
     //   MultipartBody.Part part3 = file3.exists() ? MultipartBody.Part.createFormData("image_2", file3.getName(), RequestBody.create(file3, okhttp3.MediaType.parse("image_2/*"))) : MultipartBody.Part.createFormData("attachment", "", RequestBody.create(MediaType.parse("text/plain"), ""));
     //   MultipartBody.Part part4 = file4.exists() ? MultipartBody.Part.createFormData("image_3", file4.getName(), RequestBody.create(file4, okhttp3.MediaType.parse("image_3/*"))) : MultipartBody.Part.createFormData("attachment", "", RequestBody.create(MediaType.parse("text/plain"), ""));



        RequestBody shopIdBody = RequestBody.create(proIdId, MediaType.parse("text/plain"));

        // Perform the upload
        Call<ResponseBody> call = apiService.updateProductImageApi(headerMap,shopIdBody, part1, part2,part3,part4);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                Log.e("response upload product images===", response.body().string());
                // String stringResponse = dynamicResponseModel.getJsonObject().string();
                Log.e("upload product images =====","uploaded");
                return Result.success();
            } else {
                return Result.failure();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return Result.retry();
        }
    }
}
