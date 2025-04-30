package com.afaryseller.ui.uploadids;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.afaryseller.utility.DataManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class UploadManager {

/*
    public static void uploadDocuments(Context context, String userId, String filePath1, String filePath2) {
       Data inputData = new Data.Builder()
                .putString("user_id", userId)
                .putString("file_path_1", filePath1)
                .putString("file_path_2", filePath2)
                .build();

*/


        public static void uploadDocuments(Context context, String userId, Bitmap bitmap1, Bitmap bitmap2) {
            // Save bitmaps to files
           // File file1 = DataManager.saveBitmapToFile(context, bitmap1, "document_image_1.jpg");
          //  File file2 = DataManager.saveBitmapToFile(context, bitmap2, "document_image_2.jpg");


            File file1 =  persistImage(bitmap1,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);
            File file2 =  persistImage(bitmap2,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);


            if (file1 == null || file2 == null) {
                // Handle the case where file saving failed
                return;
            }

            Data inputData = new Data.Builder()
                    .putString("user_id", userId)
                    .putString("file_path_1", file1.getAbsolutePath())
                    .putString("file_path_2", file2.getAbsolutePath())
                    .build();




        OneTimeWorkRequest uploadRequest = new OneTimeWorkRequest.Builder(UploadWorker.class)
                .setInputData(inputData)
                .build();

        WorkManager.getInstance(context).enqueue(uploadRequest);
    }


    private  static File persistImage(Bitmap bitmap, String name, Context cOntext) {
        File filesDir = cOntext.getFilesDir();
        File imageFile = new File(filesDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e("TAG", "persistImage: "+e.getMessage() );
        }

        return  imageFile;

    }

}
