package com.afaryseller.ui.sellerreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemStoreBinding;

import java.util.ArrayList;



public class SubSellerStoreAdapter extends RecyclerView.Adapter<SubSellerStoreAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubSellerStoreModel.Datum> arrayList;
    StoreListener listener;

    public SubSellerStoreAdapter(Context context,ArrayList<SubSellerStoreModel.Datum>arrayList,StoreListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemStoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_store,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvStore.setText(arrayList.get(position).getName());


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemStoreBinding binding;
        public MyViewHolder(@NonNull ItemStoreBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.tvStore.setOnClickListener(v -> {
                listener.onStore(arrayList.get(getAdapterPosition()).getShopId());
            });







        }
    }
}
