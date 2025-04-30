package com.afaryseller.ui.splash;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivitySplashBinding;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.home.HomeAct;
import com.afaryseller.ui.login.LoginAct;
import com.afaryseller.ui.otp.OtpAct;
import com.afaryseller.ui.subseller.home.SubSellerHomeAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Locale;

public class SplashAct extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_splash);

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            Log.e("token>>>>>", token);

        });


        Intent intent11 = getIntent();
        if (intent11 != null) {
            String from = intent11.getStringExtra("from");
            String msg = intent11.getStringExtra("msg");
            String title = intent11.getStringExtra("title");

            if ("notification".equalsIgnoreCase(from)) {
                if (msg != null) {
                    AlertDialogAdminStatus(title, msg);
                } else {
                    // Handle the case where 'msg' is null
                    Log.e("MainActivity", "Message is null");
                    // Optionally, you might show a default message or take some other action
                }
            }
            else {
                Log.e("SplashActivity", "Notification is null");
                if (Build.VERSION.SDK_INT >= 33) {
                    if (ContextCompat.checkSelfPermission(SplashAct.this,
                            android.Manifest.permission.POST_NOTIFICATIONS) !=
                            PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(SplashAct.this,
                                new String[]{android.Manifest.permission.POST_NOTIFICATIONS}
                                ,101);
                    }
                    else {

                        processNextAct();
                    }
                }
                else processNextAct();

            }
        } else {
            // Handle the case where 'intent' is null
            Log.e("SplashActivity", "Intent is null");
            if (Build.VERSION.SDK_INT >= 33) {
                if (ContextCompat.checkSelfPermission(SplashAct.this,
                        android.Manifest.permission.POST_NOTIFICATIONS) !=
                        PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SplashAct.this,
                            new String[]{android.Manifest.permission.POST_NOTIFICATIONS}
                            ,101);
                }
                else {

                    processNextAct();
                }
            }
            else processNextAct();
        }




        /*if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(SplashAct.this,
                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                    PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(SplashAct.this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}
                        ,101);
            }
            else {

                processNextAct();
            }
        }
        else processNextAct();*/


    }

    private void processNextAct() {

        String lang = SessionManager.readString(SplashAct.this, Constant.SELLER_LANGUAGE, "");
        Log.e("Language====",lang);
       switch (lang) {
            case "en":
                changeLocale("en");
                break;
            case "fr":
                changeLocale("fr");
                break;
           default:
               changeLocale("en");
               break;

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (DataManager.getInstance().getUserData(getApplicationContext()) != null&&
                        DataManager.getInstance().getUserData(getApplicationContext()).getResult() != null &&
                        DataManager.getInstance().getUserData(getApplicationContext()).getResult().getId()!=null) {

                    if (DataManager.getInstance().getUserData(SplashAct.this).getResult().getType().equals(Constant.SUBADMIN)) {

                        startActivity(new Intent(SplashAct.this, HomeAct.class)
                                .putExtra("status", "")
                                .putExtra("msg", "").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }
                    else {
                        startActivity(new Intent(SplashAct.this, SubSellerHomeAct.class)
                                .putExtra("status", "")
                                .putExtra("msg", "").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                        finish();
                    }

                }else {

                    startActivity(new Intent(SplashAct.this, LoginAct.class).putExtra("type","")
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();

                 /*   Intent intent = new Intent(SplashAct.this, OtpAct.class).putExtra("type","");
                    startActivity(intent);
                    finish();*/

                     }
            }
        },5000);





    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0) {
                    boolean postNotification = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                  ;
                    if (postNotification ) {
                        processNextAct();
                    } else {
                        Toast.makeText(SplashAct.this, " permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(SplashAct.this, "  permission denied, boo! Disable the functionality that depends on this permission.", Toast.LENGTH_SHORT).show();
                }
                // return;
            }


        }
    }



    private void changeLocale(String en) {
        updateResources(SplashAct.this,en);

    }


    private void updateResources(Context wellcomeScreen, String en) {
        Locale locale = new Locale(en);
        Locale.setDefault(locale);
        Resources resources = wellcomeScreen.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

    }

    private void AlertDialogAdminStatus(String title,String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashAct.this);
        builder
                //.setTitle(getString(R.string.password_change_by_admin))
                .setTitle(title)
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        if (Build.VERSION.SDK_INT >= 33) {
                            if (ContextCompat.checkSelfPermission(SplashAct.this,
                                    android.Manifest.permission.POST_NOTIFICATIONS) !=
                                    PackageManager.PERMISSION_GRANTED) {
                                ActivityCompat.requestPermissions(SplashAct.this,
                                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS}
                                        ,101);
                            }
                            else {

                                processNextAct();
                            }
                        }
                        else processNextAct();
                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();



    }



}
