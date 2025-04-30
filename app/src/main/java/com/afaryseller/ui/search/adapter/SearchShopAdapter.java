package com.afaryseller.ui.search.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSearchShopBinding;
import com.afaryseller.databinding.ItemShopBinding;
import com.afaryseller.ui.shopdetails.ShopDetailsFragment;
import com.afaryseller.ui.shoplist.ShopAdapter;
import com.afaryseller.ui.shoplist.ShopModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchShopAdapter extends RecyclerView.Adapter<SearchShopAdapter.MyViewHolder> {
    Context context;
    ArrayList<ShopModel.Result> arrayList;

    public SearchShopAdapter(Context context,ArrayList<ShopModel.Result>arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSearchShopBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_search_shop,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.shopsName.setText(arrayList.get(position).getName());
        holder.binding.description.setText(arrayList.get(position).getDescription());
        Glide.with(context).load(arrayList.get(position).getImage1())
                .override(250, 250).into(holder.binding.shopsImage);


        holder.itemView.setOnClickListener(v -> {
            context.startActivity(new Intent(context, ShopDetailsFragment.class)
                    .putExtra("shop_id",arrayList.get(position).getId())
                    .putExtra("name",arrayList.get(position).getName()));
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSearchShopBinding binding;
        public MyViewHolder(@NonNull ItemSearchShopBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;







        }
    }
}