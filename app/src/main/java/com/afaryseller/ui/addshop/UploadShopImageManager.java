package com.afaryseller.ui.addshop;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.afaryseller.ui.uploadids.UploadWorker;
import com.afaryseller.utility.DataManager;

import java.io.File;

public class UploadShopImageManager {

/*
    public static void uploadDocuments(Context context, String userId, String filePath1, String filePath2) {
       Data inputData = new Data.Builder()
                .putString("user_id", userId)
                .putString("file_path_1", filePath1)
                .putString("file_path_2", filePath2)
                .build();

*/


    public static void updateShopImages(Context context,String token, String shopId, Bitmap bitmap1, Bitmap bitmap2,Bitmap bitmap3) {
        // Save bitmaps to files
        try{
            if (bitmap1 != null && bitmap2 != null && bitmap3 != null) {
                File file1 = DataManager.saveBitmapToFile(context, bitmap1, "image_1.jpg");
                File file2 = DataManager.saveBitmapToFile(context, bitmap2, "image_2.jpg");
                File  file3 = DataManager.saveBitmapToFile(context, bitmap3, "image_3.jpg");
                Data inputData = new Data.Builder()
                        .putString("token", token)
                        .putString("shop_id", shopId)
                        .putString("file_path_1", file1.getAbsolutePath())
                        .putString("file_path_2", file2.getAbsolutePath())
                        .putString("file_path_3", file3.getAbsolutePath())
                        .build();
                OneTimeWorkRequest uploadRequest = new OneTimeWorkRequest.Builder(UploadShopImageWorker.class)
                        .setInputData(inputData)
                        .build();

                WorkManager.getInstance(context).enqueue(uploadRequest);
            }
            else {
                String file1Path="",file2Path="",file3Path="";
                if (bitmap1==null){
                    file1Path="";
                }
                else {
                    File file1 = DataManager.saveBitmapToFile(context, bitmap1, "image_1.jpg");
                    file1Path = file1.getAbsolutePath();
                }


                if (bitmap2==null){
                    file2Path="";
                }
                else {
                    File file2 = DataManager.saveBitmapToFile(context, bitmap2, "image_2.jpg");
                    file2Path = file2.getAbsolutePath();
                }

                if (bitmap3==null){
                    file3Path="";
                }
                else {
                    File  file3 = DataManager.saveBitmapToFile(context, bitmap3, "image_3.jpg");
                    file3Path = file3.getAbsolutePath();
                }


                Data inputData = new Data.Builder()
                        .putString("token", token)
                        .putString("shop_id", shopId)
                        .putString("file_path_1", file1Path)
                        .putString("file_path_2", file2Path)
                        .putString("file_path_3", file3Path)
                        .build();
                OneTimeWorkRequest uploadRequest = new OneTimeWorkRequest.Builder(UploadShopImageWorker.class)
                        .setInputData(inputData)
                        .build();

                WorkManager.getInstance(context).enqueue(uploadRequest);


            }







        }catch (Exception e){
            e.printStackTrace();
        }


    }
}


