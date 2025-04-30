package com.afaryseller.ui.subseller.home;

import static androidx.navigation.Navigation.findNavController;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.afaryseller.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SubSellerHomeAct extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_seller_home);

        BottomNavigationView navView = findViewById(R.id.nav_view_sub_seller);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home_sub_seller, R.id.navigation_report_sub_seller,  R.id.navigation_profile_sub_seller)
                .build();

        NavController navController = findNavController(this, R.id.nav_host_fragment);
        // NavigationUI.setupActionBarWithNavController(navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }
}
