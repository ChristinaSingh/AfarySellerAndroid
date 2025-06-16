package com.afaryseller.ui.requestlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemPaymentReqBinding;

import java.util.ArrayList;

public class PaymentReqAdapter extends RecyclerView.Adapter<PaymentReqAdapter.MyViewHolder> {
    Context context;
    ArrayList<PaymentReqModel.Datum> arrayList;
    onPositionClickListener listener;

    public PaymentReqAdapter(Context context, ArrayList<PaymentReqModel.Datum> arrayList, onPositionClickListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPaymentReqBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_payment_req, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getMessage_noti());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemPaymentReqBinding binding;
        public MyViewHolder(@NonNull ItemPaymentReqBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.tvName.setOnClickListener(v -> {
                listener.onPosition(getAdapterPosition(),arrayList.get(getAdapterPosition()).getId(),arrayList.get(getAdapterPosition()).getId());
            });
        }
    }


}
