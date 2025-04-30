package com.afaryseller.ui.uploadids;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.afaryseller.retrofit.AfarySeller;
import com.afaryseller.retrofit.ApiClient;


import org.json.JSONObject;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class UploadWorker extends Worker {

    public UploadWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Retrieve input data
        String userId = getInputData().getString("user_id");
        String filePath1 = getInputData().getString("file_path_1");
        String filePath2 = getInputData().getString("file_path_2");

        // Prepare files
        File file1 = new File(filePath1);
        File file2 = new File(filePath2);

        // Prepare Retrofit API service
        AfarySeller apiService = ApiClient.getClient(getApplicationContext()).create(AfarySeller.class);

        // Prepare MultipartBody.Part
        MultipartBody.Part part1 = file1.exists() ? MultipartBody.Part.createFormData("document_image_1", file1.getName(), RequestBody.create(file1, okhttp3.MediaType.parse("document_image_1/*"))) : null;
        MultipartBody.Part part2 = file2.exists() ? MultipartBody.Part.createFormData("document_image_2", file2.getName(), RequestBody.create(file2, okhttp3.MediaType.parse("document_image_2/*"))) : null;

        RequestBody userIdBody = RequestBody.create(userId, MediaType.parse("text/plain"));

        // Perform the upload
        Call<ResponseBody> call = apiService.sellerAddSkillsApi(userIdBody, part1, part2);
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                Log.e("response upload ids===", response.body().string());
               // String stringResponse = dynamicResponseModel.getJsonObject().string();
                Log.e("upload idss=====","uploaded");
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
