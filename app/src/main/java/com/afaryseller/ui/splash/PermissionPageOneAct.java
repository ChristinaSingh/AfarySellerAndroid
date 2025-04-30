package com.afaryseller.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityShowsBinding;
import com.afaryseller.ui.login.LoginAct;

public class PermissionPageOneAct extends AppCompatActivity {
    ActivityShowsBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       binding = DataBindingUtil.setContentView(this,R.layout.activity_shows);
       initViews();
    }

    private void initViews() {
        binding.btnNext.setOnClickListener(view -> {
            startActivity(new Intent(PermissionPageOneAct.this, LoginAct.class).putExtra("type","").addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            finish();
        });
    }
}
