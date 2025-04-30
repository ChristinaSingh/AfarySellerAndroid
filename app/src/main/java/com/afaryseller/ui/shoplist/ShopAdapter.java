package com.afaryseller.ui.shoplist;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemShopBinding;
import com.afaryseller.ui.shopdetails.ShopDetailsFragment;
import com.afaryseller.ui.shoplist.listerner.ShopListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ShopAdapter extends RecyclerView.Adapter<ShopAdapter.MyViewHolder> {
    Context context;
    ArrayList<ShopModel.Result>arrayList;
    ShopListener listener;

    public ShopAdapter(Context context,ArrayList<ShopModel.Result>arrayList,ShopListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemShopBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_shop,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       holder.binding.shopsName.setText(arrayList.get(position).getName());
        holder.binding.description.setText(arrayList.get(position).getDescription());
        Glide.with(context).load(arrayList.get(position).getImage1())
                .override(250,250).into(holder.binding.shopsImage);


        if(arrayList.get(position).getShopStatus().equals("Active")) {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.colorGreen));
            holder.binding.tvStatus.setText(context.getString(R.string.active));
        }
        else {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.red));
            holder.binding.tvStatus.setText(context.getString(R.string.deactive));        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemShopBinding binding;
        public MyViewHolder(@NonNull ItemShopBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.edit.setOnClickListener(v -> {
                listener.editShop(arrayList.get(getAdapterPosition()).getShopId(),v,arrayList.get(getAdapterPosition()),"Edit");
            });

            binding.product.setOnClickListener(v -> {
                listener.editShop(arrayList.get(getAdapterPosition()).getShopId(),v,arrayList.get(getAdapterPosition()),"Add");
            });

            binding.view.setOnClickListener(v -> {
                context.startActivity(new Intent(context, ShopDetailsFragment.class)
                        .putExtra("shop_id",arrayList.get(getAdapterPosition()).getShopId())
                        .putExtra("name",arrayList.get(getAdapterPosition()).getName()));
            });

            binding.tvStatus.setOnClickListener(v -> {
                if (arrayList.get(getAdapterPosition()).getShopStatus().equals("Active")) listener.editShop(arrayList.get(getAdapterPosition()).getShopId(),v,null,"Deactive");
                else listener.editShop(arrayList.get(getAdapterPosition()).getShopId(),v,null,"Active");

            });



        }
    }
}
