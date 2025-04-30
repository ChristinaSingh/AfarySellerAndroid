package com.afaryseller.firebase;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.afaryseller.R;
import com.afaryseller.retrofit.Constant;
import com.afaryseller.ui.chat.ChatAct;
import com.afaryseller.ui.home.HomeAct;
import com.afaryseller.ui.splash.ChooseAct;
import com.afaryseller.ui.splash.PermissionPageTwoAct;
import com.afaryseller.ui.splash.SplashAct;
import com.afaryseller.ui.wallet.PaymentByAnotherAct;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "";
    private static final String CHANNEL_ID = "my_channel_id";
    JSONObject notificationObj;
    String result="",key="",message="",type="",msg="",keyEng="";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        try {
        Log.e("remoteMessage>>", "" + remoteMessage);
        if (!remoteMessage.getData().isEmpty()) {
            Log.e(TAG, "Message data payload : " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();


                notificationObj = new JSONObject(data.get("data"));
                result = notificationObj.getString("result");
                type = notificationObj.getString("type");
                msg = notificationObj.getString("message");
                keyEng = notificationObj.getString("key");

            Log.d("FCMService", "data====: " + notificationObj.toString());



            // Process the data as needed
            Log.d("FCMService", "Result: " + result);
            Log.d("FCMService", "Key: " + keyEng);
            Log.d("FCMService", "Message: " + message);
            Log.d("FCMService", "Type: " + type);

            if(SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                    || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("") ) {
                key = notificationObj.getString("key");
                message = notificationObj.getString("message");

            }
            else {
                if(notificationObj.has("key_fr") && notificationObj.has("message_fr")){
                    key = notificationObj.getString("key_fr");
                    message = notificationObj.getString("message_fr");
                    Log.e("French ka param=====","======");

                }
                else{
                    key = notificationObj.getString("key");
                    message = notificationObj.getString("message");
                    Log.e("French ka param not available=====","======");
                }


            }



          if(type.equalsIgnoreCase("validated")){

              if(SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                      || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("") ) {
                  sendNotification(key, result, "", message, type, notificationObj,msg,keyEng);
              }

              else {
                  sendNotification(notificationObj.getString("key_fr"), result,""
                          ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
              }

          }
          else if(type.equalsIgnoreCase("disprove")) {


              if(SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                      || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("") ) {
                  sendNotification(getString(R.string.product_not_validate)
                          , getString(R.string.doesnt_validate),""
                          ,message,type,notificationObj,msg,keyEng);
              }

              else {
               //   sendNotification(notificationObj.getString("key_fr"), result,""
                     //     ,notificationObj.getString("message_fr"),type,notificationObj);

                  sendNotification(notificationObj.getString("key_fr")
                          , getString(R.string.doesnt_validate),""
                          ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
                  Log.e("check fr notification====","=======");
              }



          }

          else if(type.equalsIgnoreCase("Statuschange")) {

              if(SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                      || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("") ) {
                  sendNotification(key
                          , "",""
                          ,message,type,notificationObj,msg,keyEng);
              }
              else {
                  sendNotification(notificationObj.getString("key_fr")
                          , "",""
                          ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
              }

          }

          else if(type.equalsIgnoreCase("status_updated")) sendNotification(key
                  , "",""
                  ,message,type,notificationObj,msg,keyEng);

          else if(type.equalsIgnoreCase("Registration")) {
             if(keyEng.equalsIgnoreCase("Your account has been Deactive by AfaryCode Team.")) {
                 if(SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                         || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("") ) {
                     sendNotification(key
                             , key,""
                             ,message,type,notificationObj,msg,keyEng);
                 }

                 else {
                     sendNotification(notificationObj.getString("key_fr")
                             , notificationObj.getString("key_fr"),""
                             ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
                 }

                 Log.e("====","Your account has been Deactive by AfaryCode Team.");
                  if (DataManager.getInstance().getUserData(getApplicationContext()) != null) {
                      SessionManager.logout(getApplicationContext());
                  }
              }


             else if(keyEng.equals("Your account is deactivated, Please contact AfaryCode administration!")){

             }

           else {
                 if(SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                 || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("") ){
                     sendNotification(getString(R.string.welcome_to_afarycode), "",""
                             ,message,type,notificationObj,msg,keyEng);
                 }
                           else {
                         sendNotification(notificationObj.getString("key_fr"), "",""
                             ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
                 }

             }
          }

         /* else if(key.equalsIgnoreCase("Your account has been Deactive by AfaryCode Team.")) {
              sendNotification("Your account has been Deactive by AfaryCode Team."
                      , key,""
                      ,message,type,notificationObj);
              Log.e("====","Your account has been Deactive by AfaryCode Team.");
              if (DataManager.getInstance().getUserData(getApplicationContext()) != null) {
                  SessionManager.clear(getApplicationContext(), "");
              }
          }*/

/*
          else if(type.equalsIgnoreCase("Statuschange")) {
              if(keyEng.equalsIgnoreCase("Now your accout status is Active by admin")) {
                  if(SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                          || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("") ) {
                      sendNotification(key
                              , key,""
                              ,message,type,notificationObj,msg,keyEng);
                  }

                  else {
                      sendNotification(notificationObj.getString("key_fr")
                              , notificationObj.getString("key_fr"),""
                              ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
                  }


              }

          }
*/



          else if(keyEng.equalsIgnoreCase("Now your account status is Active by admin")) sendNotification(getString(R.string.your_account_is_activated)
                  , key,""
                  ,message,type,notificationObj,msg,keyEng);


        /*  else if(key.equalsIgnoreCase("You have been logged out because you have logged in on another device"))
              sendNotification("You have been logged out because you have logged in on another device"
                  , key,""
                  ,message,type,notificationObj);*/


          else if(keyEng.contains("Dear service provider")) {
              if (SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                      || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("")) {

                  sendNotification(notificationObj.getString("title")
                          , key,""
                          ,message,type,notificationObj,msg,keyEng);

              } else {
               /*   sendNotification(notificationObj.getString("title")
                          , key,""
                          ,message,type,notificationObj);*/
                  sendNotification(notificationObj.getString("title_fr"), notificationObj.getString("key_fr"),""
                          ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
              }
          }







          else if(keyEng.contains("Seller")){
              if(keyEng.equals("Seller Accout Validate Now")){
                  if (SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("en")
                          || SessionManager.readString(getApplicationContext(), Constant.SELLER_LANGUAGE, "").equals("")) {

                      sendNotification(key
                              , "",notificationObj.getString("step")
                              ,message,type,notificationObj,msg,keyEng);
                  }

                  else {
                      sendNotification(notificationObj.getString("key_fr")
                              , "",notificationObj.getString("step")
                              ,notificationObj.getString("message_fr"),type,notificationObj,msg,keyEng);
                  }
              }
              else {
                  sendNotification(getString(R.string.delivery_person_accept_request)
                          , key, ""
                          , message, type, notificationObj,msg,keyEng);
              }
          }


          else if(keyEng.contains("Wallet Recharge Failed"))
              sendNotification(getString(R.string.wallet_recharge_failed)
                      , key,""
                      ,message,type,notificationObj,msg,keyEng);

          else if(keyEng.contains("Wallet Recharge"))
              sendNotification(getString(R.string.wallet_recharge)
                      , key,""
                      ,message,type,notificationObj,msg,keyEng);


          else if (type.equals("InvoiceToOtherUserForWallet")) {
              //  if (!remoteMessage.getString("other_user_id").equals(PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.User_id, ""))) {
              sendNotification(getString(R.string.wallet_recharge)
                      , key,""
                      ,message,type,notificationObj,msg,keyEng);

              //  }
              //  else {

              //  }

          }

          else sendNotification(key
                  , result,notificationObj.getString("step")
                  ,message,type,notificationObj,msg,keyEng);


        }

        if (remoteMessage.getData() != null) {
            if(type.equalsIgnoreCase("validated") || type.equalsIgnoreCase("disprove")) {
                Intent intent1 = new Intent("check_product");
                Log.e("SendData=====", type);
                intent1.putExtra("type", notificationObj.getString("type"));
                intent1.putExtra("productId", notificationObj.getString("product_id"));
                intent1.putExtra("productName",notificationObj.getString("product_name"));
                sendBroadcast(intent1);
            } /*else if (key.equalsIgnoreCase("You have been logged out because you have logged in on another device")) {
                Intent intent1 = new Intent("check_statusLogout");
                Log.e("SendData=====", notificationObj.getString("type"));
                intent1.putExtra("status", notificationObj.getString("key"));
                intent1.putExtra("msg",notificationObj.getString("message"));
                sendBroadcast(intent1);

            }*/

        }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendNotification(String title, String messageBody,String step,String msg,String type,JSONObject remoteMessage,String msgEng,String keyEng) {

        try {
            Intent intent = null;

            if (type.equals("Seller verification account")) {
                if (step.equalsIgnoreCase("5"))
                    intent = new Intent(getApplicationContext(), PermissionPageTwoAct.class).putExtra("step", "5");
            } else if (type.equals("insert_chat")) {
                intent = new Intent(getApplicationContext(), ChatAct.class)
                        .putExtra("UserId", remoteMessage.getString("userid"))
                        .putExtra("UserName", remoteMessage.getString("user_name"))
                        .putExtra("UserImage", remoteMessage.getString("userimage"));
            }/* else if (title.equalsIgnoreCase("Your account has been Deactive by AfaryCode Team.")) {
                intent = new Intent();
                if (DataManager.getInstance().getUserData(getApplicationContext()) != null) {
                    SessionManager.logout(this);
                }
            }*/ else if (keyEng.equalsIgnoreCase("Your account is Activated")) {
                intent = new Intent();
            } else if (msgEng.contains("Dear partner,Customer") || msgEng.contains("Dear Partner, You have  accepted an") || msgEng.contains("want your product")
                    || msgEng.contains("Deleted a order order id is")) {
                intent = new Intent(getApplicationContext(), HomeAct.class)
                        .putExtra("status", "cancelByUser").putExtra("msg", msg);
            }
            //A customer wants to know the availability of the product
            else if (msgEng.contains("A customer wants to know the availability of the product")) {
                intent = new Intent(getApplicationContext(), HomeAct.class)
                        .putExtra("status", "updateStock").putExtra("msg", msg)
                        .putExtra("user_id", remoteMessage.getString("userid"))
                        .putExtra("product_id", remoteMessage.getString("product_id"))
                        .putExtra("product_name", remoteMessage.getString("product_name"))
                        .putExtra("product_sku", remoteMessage.getString("product_sku"))
                        .putExtra("product_image", remoteMessage.getString("product_image"));
            } else if (msgEng.contains("Dear service provider")) {
                intent = new Intent(getApplicationContext(), HomeAct.class)
                        .putExtra("status", "ProductValidate").putExtra("msg", msg);

            } /*else if (msg.contains("You have been logged out because you have logged in on another device")) {
                SessionManager.clearSession(getApplicationContext());
                intent = new Intent(getApplicationContext(), SplashAct.class);
            }*/
            else if(type.equalsIgnoreCase("Statuschange")){
                intent = new Intent(getApplicationContext(), SplashAct.class)
                        .putExtra("title",title)
                        .putExtra("msg",msg)
                        .putExtra("from","notification");
            }

            else if (type.equals("InvoiceToOtherUserForWallet")) {
                //  if (!remoteMessage.getString("other_user_id").equals(PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.User_id, ""))) {
                intent = new Intent(getApplicationContext(), PaymentByAnotherAct.class)
                        .putExtra("paymentInsertId", remoteMessage.getString("invoice_id"))
                        .putExtra("user_id",remoteMessage.getString("userid"))
                        .putExtra("type","InvoiceToOtherUserForWallet");
                //  }
                //  else {

                //  }

            }

            else if (type.equals("InvoiceToOtherUser")) {
                //  if (!remoteMessage.getString("other_user_id").equals(PreferenceConnector.readString(getApplicationContext(), PreferenceConnector.User_id, ""))) {
                intent = new Intent(getApplicationContext(), PaymentByAnotherAct.class)
                        .putExtra("paymentInsertId", remoteMessage.getString("invoice_id"))
                        .putExtra("user_id",remoteMessage.getString("userid"))
                        .putExtra("type","InvoiceToOtherUser");
                //  }
                //  else {

                //  }

            }

            else if (type.equals("InvoiceToUser")) {
                intent = new Intent(getApplicationContext(), HomeAct.class)
                        .putExtra("status", "cancelByUser")
                        .putExtra("msg", "");


                /* .putExtra("status", "")
                        .putExtra("msg", "");*/
            }

            else if (type.equals("InvoiceToUserForWallet")) {
                intent = new Intent(getApplicationContext(), HomeAct.class)
                        .putExtra("status", "cancelByUser")
                        .putExtra("msg", msg);

               /*  .putExtra("status", "")
                        .putExtra("msg", "");*/
            }


            else {
                intent = new Intent(getApplicationContext(), HomeAct.class)
                        .putExtra("status",
                                "cancelByUser").putExtra("msg", msg);

                 /*  .putExtra("status",
                        title).putExtra("msg", msg);*/
            }


            if (messageBody != null) {
                Bitmap bmp = null;
                try {
                    InputStream in = new URL("http://placehold.it/120x120&text=image3").openStream();
                    bmp = BitmapFactory.decodeStream(in);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final int not_nu = generateRandom();
           /* PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), not_nu, intent, PendingIntent.FLAG_IMMUTABLE);
            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder noBuilder = new NotificationCompat.Builder(this, getString(R.string.default_channel_id))
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.logo_well1))
                    .setSmallIcon(R.drawable.logo_well1)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))

                    //  .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(sound)
                    .setContentIntent(contentIntent)
                    // .setLargeIcon(bmp)
                    //.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bmp))
                    .setVibrate(new long[]{1000, 1000});




            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel notificationChannel = new NotificationChannel(getString(R.string.default_channel_id), "NOTIFICATION_CHANNEL_NAME", importance);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.enableVibration(true);
                notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                assert notificationManager != null;
                noBuilder.setChannelId(getString(R.string.default_channel_id));
                notificationManager.createNotificationChannel(notificationChannel);
            }
            notificationManager.notify(not_nu, noBuilder.build());*/

                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), not_nu, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                        .setSmallIcon(R.drawable.logo_well1)
                        .setContentTitle(title)
                        //.setContentText(msg)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Channel name", NotificationManager.IMPORTANCE_HIGH);
                    notificationManager.createNotificationChannel(channel);
                }

                notificationManager.notify(0, notificationBuilder.build());


            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }
    public int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }
}
