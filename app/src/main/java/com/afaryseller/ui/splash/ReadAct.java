package com.afaryseller.ui.splash;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.ActivityReadBinding;

public class ReadAct extends AppCompatActivity {
    ActivityReadBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_read);
        initViews();
    }

    private void initViews() {
        binding.btn.setOnClickListener(view -> {
            startActivity(new Intent(this, ChooseAct.class));
            finish();
        });
    }
}
