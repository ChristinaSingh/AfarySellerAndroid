package com.afaryseller.ui.subseller.subsellerlsit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSubSellerBinding;

import com.afaryseller.ui.subseller.changepassword.SubSellerChangePasswordAct;
import com.afaryseller.ui.subseller.profile.SubSellerProfileAct;
import com.bumptech.glide.Glide;

import java.util.ArrayList;



public class SubSellerAdapter extends RecyclerView.Adapter<SubSellerAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubSellerListModel.Datum> arrayList;
    SubSellerListListener listener;

    public SubSellerAdapter(Context context,ArrayList<SubSellerListModel.Datum>arrayList,SubSellerListListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubSellerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sub_seller,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getName());
        holder.binding.tvEmail.setText(arrayList.get(position).getEmail());
        holder.binding.tvMobile.setText(arrayList.get(position).getMobile());

        Glide.with(context).load(arrayList.get(position).getImage())
                .override(100,100)
                .placeholder(R.drawable.user_default)
                .error(R.drawable.user_default).into(holder.binding.ivImg);


        holder.itemView.setOnClickListener(v -> context.startActivity(new Intent(context, SubSellerProfileAct.class)
                .putExtra("subSellerId",arrayList.get(position).getId())));


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubSellerBinding binding;
        public MyViewHolder(@NonNull ItemSubSellerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
          /*  binding.edit.setOnClickListener(v -> {
                listener.editShop(arrayList.get(getAdapterPosition()).getShopId(),v,arrayList.get(getAdapterPosition()),"Edit");
            });*/








        }
    }
}
