package com.afaryseller.ui.splash;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.afaryseller.R;
import com.afaryseller.databinding.FragmentAskBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class AskBottomSheet extends BottomSheetDialogFragment {
    public String TAG = "AskBottomSheet";
    BottomSheetDialog dialog;
    FragmentAskBinding binding;
    private BottomSheetBehavior<View> mBehavior;
    public AskListener listener;




    public AskBottomSheet() {
    }


    public AskBottomSheet callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getActivity()), R.layout.fragment_ask, null, false);
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        mBehavior = BottomSheetBehavior.from((View) binding.getRoot().getParent());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        initBinding();
        return  dialog;
    }

    private void initBinding() {


        String sourceString = getString(R.string.do_you_have_account)+" "+"<b>" + getString(R.string.customer) + "</b> " + " " + getString(R.string.with_afary_code);
        binding.tvText.setText(Html.fromHtml(sourceString));

        binding.btnYes.setOnClickListener(view -> {
            listener.ask("Yes","");
        });

        binding.btnNo.setOnClickListener(view -> {
            listener.ask("No","");
        });
    }
}
