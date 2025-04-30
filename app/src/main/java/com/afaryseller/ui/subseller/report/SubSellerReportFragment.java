package com.afaryseller.ui.subseller.report;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.afaryseller.R;
import com.afaryseller.databinding.FragmentSubSellerPeriodicReportBinding;

public class SubSellerReportFragment extends Fragment {
    FragmentSubSellerPeriodicReportBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sub_seller_periodic_report, container, false);
        //initViews();
        return binding.getRoot();
    }


}
