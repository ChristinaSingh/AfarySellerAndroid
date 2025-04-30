package com.afaryseller.ui.addshop;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;
import com.afaryseller.utility.DataManager;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;


public class UploadShopImageWorker extends Worker {

    public UploadShopImageWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Retrieve input data
        String token = getInputData().getString("token");
        String shopId = getInputData().getString("shop_id");
        String filePath1 = getInputData().getString("file_path_1");
        String filePath2 = getInputData().getString("file_path_2");
        String filePath3 = getInputData().getString("file_path_3");
        Map<String, String> headerMap = new HashMap<>();
        headerMap.put("Authorization", "Bearer " + token);
        headerMap.put("Accept", "application/json");

        Log.e("Token====",headerMap.toString());

        // Prepare files
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);
        File file3 = new File(filePath3);

        // Prepare Retrofit API service
        AfarySeller apiService = ApiClient.getClient(getApplicationContext()).create(AfarySeller.class);

        // Prepare MultipartBody.Part

        MultipartBody.Part part1 = file1.exists() ? MultipartBody.Part.createFormData("image_1", file1.getName(), RequestBody.create(file1, okhttp3.MediaType.parse("image/jpeg"))) : MultipartBody.Part.createFormData("attachment", "", RequestBody.create(MediaType.parse("text/plain"), ""));
        MultipartBody.Part part2 = file2.exists() ? MultipartBody.Part.createFormData("image_2", file2.getName(), RequestBody.create(file2, okhttp3.MediaType.parse("image/jpeg"))) : MultipartBody.Part.createFormData("attachment", "", RequestBody.create(MediaType.parse("text/plain"), ""));
        MultipartBody.Part part3 = file3.exists() ? MultipartBody.Part.createFormData("image_3", file3.getName(), RequestBody.create(file3, okhttp3.MediaType.parse("image/jpeg"))) : MultipartBody.Part.createFormData("attachment", "", RequestBody.create(MediaType.parse("text/plain"), ""));



        RequestBody shopIdBody = RequestBody.create(shopId, MediaType.parse("text/plain"));

        // Perform the upload
        Call<ResponseBody> call = apiService.updateShopImageApi(headerMap,shopIdBody, part1, part2,part3);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                Log.e("response upload shop images===", response.body().string());
                // String stringResponse = dynamicResponseModel.getJsonObject().string();
                Log.e("upload shop images =====","uploaded");
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
