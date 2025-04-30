package com.afaryseller.ui.wallet.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.afaryseller.R;
import com.afaryseller.ui.splash.AskListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TransferMoneyBottomSheet extends BottomSheetDialogFragment implements AskListener {

    Context context;
    private RelativeLayout btnFirst,btnSecond;
    AskListener listener;
    public TransferMoneyBottomSheet(Context context) {
        this.context = context;
    }


    public TransferMoneyBottomSheet callBack(AskListener listener) {
        this.listener = listener;
        return this;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("RestrictedApi")
    public void setupDialog(Dialog dialog, int style) {
        View contentView = View.inflate(getContext(), R.layout.fragment_transfer, (ViewGroup) null);

        btnFirst = contentView.findViewById(R.id.btnFirst);
        btnSecond = contentView.findViewById(R.id.btnSecond);

        btnFirst.setOnClickListener(v -> {
            new TransferMOneyFragment(getActivity()).callBack(this::ask).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");

        });

        btnSecond.setOnClickListener(v -> {
            new RequestMoneyBottomSheet(getActivity()).callBack(this::ask).show(getActivity().getSupportFragmentManager(), "ModalBottomSheet");

        });

        dialog.setContentView(contentView);

        ((View) contentView.getParent()).setBackgroundColor(getResources().getColor(android.R.color.transparent));

    }

    @Override
    public void ask(String value, String status) {
        listener.ask("","transfer");
        dismiss();
    }
}
