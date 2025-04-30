package com.afaryseller.ui.chat;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.core.BaseActivity;
import com.afaryseller.databinding.ActivityChatBinding;
import com.afaryseller.retrofit.ApiConstant;
import com.afaryseller.utility.DataManager;
import com.afaryseller.utility.SessionManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChatAct extends BaseActivity<ActivityChatBinding, ChatViewModel> {
    public String TAG = "ChatAct";
    ActivityChatBinding binding;
    ChatViewModel chatViewModel;
    DatabaseReference reference1;
    String request_id="", user_name, user_image, receiverId = "", receiverName = "", receiverImage = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat);
        chatViewModel = new ChatViewModel();
        binding.setChatViewModel(chatViewModel);
        binding.getLifecycleOwner();
        chatViewModel.init(ChatAct.this);
        //   initViews();


        initializeViews();
        if (getIntent() != null) {
            setUserInfo(getIntent().getStringExtra("UserId"));
            receiverId = getIntent().getStringExtra("UserId");
            receiverName = getIntent().getStringExtra("UserName");
            receiverImage = getIntent().getStringExtra("UserImage");
           // request_id = getIntent().getStringExtra("request_id");


        }

        observeLoader();
        observeResponse();

    }

    private void initializeViews() {
        binding.backNavigation.setOnClickListener(v -> finish());
        binding.ChatLayout.imgSendIcon.setOnClickListener(v -> {
            if (binding.ChatLayout.tvMessage.getText().toString().length() > 0 ) {
                String messageText = binding.ChatLayout.tvMessage.getText().toString().trim();
                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", DataManager.getInstance().toBase64(messageText));
                    map.put("user", DataManager.getInstance().getUserData(ChatAct.this).getResult().getUserName());
                    map.put("date", DataManager.getInstance().getCurrent());
                    map.put("msg_type", "1");
                    map.put("sender_id", DataManager.getInstance().getUserData(ChatAct.this).getResult().getId());
                    Log.e("send mdg===", map.toString());
                    reference1.push().setValue(map);

                    sendPushNotifi(messageText);

                }
                binding.ChatLayout.tvMessage.setText("");
            } else {
                binding.ChatLayout.tvMessage.setError("Field is Blank");
            }
        });

        disablesend_button();

        binding.ChatLayout.tvMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    binding.ChatLayout.imgSendIcon.setEnabled(true);
                    binding.ChatLayout.imgSendIcon.setAlpha((float) 1.0);
                } else {
                    disablesend_button();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void sendPushNotifi(String messageText) {
        Map<String,String>headerMap = new HashMap<>();
        headerMap.put("Authorization","Bearer " +DataManager.getInstance().getUserData(ChatAct.this).getResult().getAccessToken());
        headerMap.put("Accept","application/json");

        HashMap<String, String> map = new HashMap<>();
        map.put("sender_id", DataManager.getInstance().getUserData(ChatAct.this).getResult().getId());
        map.put("receiver_id",receiverId);
        map.put("chat_message",messageText);
        map.put("user_id", DataManager.getInstance().getUserData(ChatAct.this).getResult().getId());
        map.put("seller_register_id", DataManager.getInstance().getUserData(ChatAct.this).getResult().getRegisterId());

        Log.e(TAG, "Chat msg Request :" + map);
        chatViewModel.sendNotification(ChatAct.this,headerMap,map);
   }

    private void disablesend_button() {
        binding.ChatLayout.imgSendIcon.setEnabled(false);
        binding.ChatLayout.imgSendIcon.setAlpha((float) 0.3);
    }


    private void setUserInfo(String pid) {
        // user_id = DataManager.getInstance().getUserData(MsgChatActivity.this).result.id;
        user_name = DataManager.getInstance().getUserData(ChatAct.this).getResult().getUserName();
        user_image = DataManager.getInstance().getUserData(ChatAct.this).getResult().getImage();
        String self_user = DataManager.getInstance().getUserData(this).getResult().getId();
        String other_user = pid;
        if (Double.parseDouble(self_user) > Double.parseDouble(other_user)) {
            reference1 = FirebaseDatabase.getInstance()
                    .getReferenceFromUrl("https://decoded-reducer-294611.firebaseio.com/" + "messages" + "_" + self_user + "_" + other_user);

            // .getReferenceFromUrl("https://afarycodeseller-default-rtdb.firebaseio.com/" + "messages" + "_" + self_user + "_" + other_user);
        } else {
            reference1 = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://decoded-reducer-294611.firebaseio.com/" + "messages" + "_" + other_user + "_" + self_user);

            //.getReferenceFromUrl("https://afarycodeseller-default-rtdb.firebaseio.com/" + "messages" + "_" + other_user + "_" + self_user);
        }
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> map = dataSnapshot.getValue(genericTypeIndicator);
                String message = map.get("message").toString();
                Log.e("ttttt",map+"");

                if (map != null) {
                    String sender_id = map.get("sender_id").toString();
                    String userName = map.get("user").toString();
                    String time = map.get("date").toString();
                    String msg_type = map.get("msg_type").toString();
                    if (userName.equals(DataManager.getInstance().getUserData(ChatAct.this).getResult().getUserName())) {
                        addMessageBox(message, 1, time, msg_type, "");
                    } else {
                        addMessageBox(message, 2, time, msg_type, "");
                    }
                }
                Log.e("mSgChat", "onChildAdded: " + message);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void addMessageBox(final String message, int type, String date, String msg_type, final String url) {
        TextView tv_time = null, tv_message = null,tvName;
        RelativeLayout relative_img_message = null;
        ImageView img_message = null;
        View view = null;

        if (type == 1) {
            view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_chat_white_bg, null);
            tv_time = view.findViewById(R.id.tv_time);
            tv_message = view.findViewById(R.id.tv_message);
            relative_img_message = view.findViewById(R.id.relative_img_message);
            img_message = view.findViewById(R.id.ivImg);
            tvName = view.findViewById(R.id.tvName);
            tvName.setText(user_name);
            tv_time.setText(DataManager.getInstance().getCurrent());//DataManager.getInstance().dateformatenew(date)
            Glide.with(getApplicationContext())
                    .load(user_image)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .into(img_message);
            tv_message.setText(DataManager.getInstance().fromBase64(message));


        } else {
            view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_chat_left_bg, null);
            tv_time = view.findViewById(R.id.tv_time1);
            tv_message = view.findViewById(R.id.tv_message1);
            relative_img_message = view.findViewById(R.id.relative_img_message1);
            img_message = view.findViewById(R.id.ivImg1);
            tv_time.setText(DataManager.getInstance().getCurrent());//DataManager.getInstance().dateformatenew(date)
            tvName = view.findViewById(R.id.tvName1);
            tvName.setText(receiverName);
            Glide.with(getApplicationContext())
                    .load(receiverImage)
                    .apply(new RequestOptions().placeholder(R.drawable.user_default))
                    .into(img_message);
            tv_message.setText(DataManager.getInstance().fromBase64(message));


        }
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        view.setLayoutParams(lp);

        binding.layout1.addView(view);
        binding.scrollView.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });
    }



    public void observeResponse(){
        chatViewModel.isResponse.observe(this,dynamicResponseModel -> {
            if(dynamicResponseModel.getJsonObject()!=null){
                pauseProgressDialog();
                if(dynamicResponseModel.getApiName()== ApiConstant.SEND_PUSH_NOTIFICATION){
                    try {
                        if(dynamicResponseModel.getCode()==200){
                            Log.e("response===",dynamicResponseModel.getJsonObject().toString());
                            String stringResponse = dynamicResponseModel.getJsonObject().string();
                            JSONObject jsonObject = new JSONObject(stringResponse);
                            if (jsonObject.getString("status").toString().equals("1")) {
                                // binding.tvNotFount.setVisibility(View.GONE);

                            } else if (jsonObject.getString("status").toString().equals("0")) {
                                // binding.tvNotFount.setVisibility(View.VISIBLE);
                                Toast.makeText(ChatAct.this, jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
                            }

                            else if (jsonObject.getString("status").toString().equals("5")) {
                                SessionManager.logout(ChatAct.this);
                            }
                        }
                        else {
                            //  binding.tvNotFount.setVisibility(View.VISIBLE);
                            Toast.makeText(ChatAct.this, dynamicResponseModel.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }




            }


        } );
    }

    private void observeLoader() {
        chatViewModel.isLoading.observe(ChatAct.this,aBoolean -> {
            if (aBoolean) {
                showProgressDialog(ChatAct.this, false, getString(R.string.please_wait));
            }else{
                pauseProgressDialog();
            }
        });
    }

}
