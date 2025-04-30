package com.afaryseller.ui.addproduct;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.afaryseller.ui.addshop.UploadShopImageWorker;
import com.afaryseller.utility.DataManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class UploadProductImageManager {

    public static void updateProductImages(Context context, String token, String proId, Bitmap bitmap1, Bitmap bitmap2, Bitmap bitmap3
    ,Bitmap bitmap4) {
        // Save bitmaps to files
        try{
            if (bitmap1 != null && bitmap2 != null && bitmap3 != null && bitmap4!=null) {
             //   File file1 = DataManager.saveBitmapToFile(context, bitmap1, "image_1.jpg");
             //   File file2 = DataManager.saveBitmapToFile(context, bitmap2, "image_2.jpg");
             //   File  file3 = DataManager.saveBitmapToFile(context, bitmap3, "image_3.jpg");
            //    File  file4 = DataManager.saveBitmapToFile(context, bitmap4, "image_4.jpg");

                File file1 =  persistImage(bitmap1,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);
                File file2 =  persistImage(bitmap2,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);
                File file3 =  persistImage(bitmap3,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);
                File file4 =  persistImage(bitmap4,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);



                Data inputData = new Data.Builder()
                        .putString("token", token)
                        .putString("pro_id", proId)
                        .putString("file_path_1", file1.getAbsolutePath())
                        .putString("file_path_2", file2.getAbsolutePath())
                        .putString("file_path_3", file3.getAbsolutePath())
                        .putString("file_path_4", file4.getAbsolutePath())
                        .build();
                OneTimeWorkRequest uploadRequest = new OneTimeWorkRequest.Builder(UploadProductImageWorker.class)
                        .setInputData(inputData)
                        .build();

                WorkManager.getInstance(context).enqueue(uploadRequest);
            }
            else {
                String file1Path="",file2Path="",file3Path="",file4Path="";
                if (bitmap1==null){
                    file1Path="";
                }
                else {
                   // File file1 = DataManager.saveBitmapToFile(context, bitmap1, "image_1.jpg");
                    File file1 =  persistImage(bitmap1,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);


                    file1Path = file1.getAbsolutePath();
                }


                if (bitmap2==null){
                    file2Path="";
                }
                else {
                  //  File file2 = DataManager.saveBitmapToFile(context, bitmap2, "image_2.jpg");
                    File file2 =  persistImage(bitmap2,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);

                    file2Path = file2.getAbsolutePath();
                }

                if (bitmap3==null){
                    file3Path="";
                }
                else {
                   // File  file3 = DataManager.saveBitmapToFile(context, bitmap3, "image_3.jpg");
                    File file3 =  persistImage(bitmap3,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);

                    file3Path = file3.getAbsolutePath();
                }

                if (bitmap4==null){
                    file4Path="";
                }
                else {
                   // File  file4 = DataManager.saveBitmapToFile(context, bitmap4, "image_4.jpg");
                    File file4 =  persistImage(bitmap4,new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date()) + "_" + UUID.randomUUID().toString(),context);

                    file4Path = file4.getAbsolutePath();
                }


                Data inputData = new Data.Builder()
                        .putString("token", token)
                        .putString("pro_id", proId)
                        .putString("file_path_1", file1Path)
                        .putString("file_path_2", file2Path)
                        .putString("file_path_3", file3Path)
                        .putString("file_path_4", file4Path)
                        .build();
                OneTimeWorkRequest uploadRequest = new OneTimeWorkRequest.Builder(UploadProductImageWorker.class)
                        .setInputData(inputData)
                        .build();

                WorkManager.getInstance(context).enqueue(uploadRequest);


            }







        }catch (Exception e){
            e.printStackTrace();
        }


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