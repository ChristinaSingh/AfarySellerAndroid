package com.afaryseller.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityAskBinding;
import com.afaryseller.ui.login.LoginAct;

public class AskAct extends AppCompatActivity implements AskListener {
    ActivityAskBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_ask);
        initViews();
    }

    private void initViews() {
        new AskBottomSheet().callBack(this::ask).show(getSupportFragmentManager(),"");

    }

    @Override
    public void ask(String value,String status) {
        if(value.equalsIgnoreCase("No")) {
            startActivity(new Intent(AskAct.this, ReadAct.class));
            finish();
        } else {
            startActivity(new Intent(AskAct.this, LoginAct.class).putExtra("type","Become a Seller"));
            finish();
        }
    }
}
