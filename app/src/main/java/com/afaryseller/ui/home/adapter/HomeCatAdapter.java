package com.afaryseller.ui.home.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemHomeCatBinding;
import com.afaryseller.ui.home.model.HomeCatModel;
import com.afaryseller.utility.DataManager;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HomeCatAdapter extends RecyclerView.Adapter<HomeCatAdapter.MyViewHolder> {
    Context context;
    ArrayList<HomeCatModel.Result> arrayList;

    public  HomeCatAdapter(Context context, ArrayList<HomeCatModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeCatBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_home_cat,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Glide.with(context).load(arrayList.get(position).getIcon()).into(holder.binding.ivCat);
       /* Drawable drawable = holder.binding.ivCat.getDrawable();

        // Set the color filter to tint the drawable
        if (drawable != null) {
            drawable.setColorFilter(DataManager.getColor(position), android.graphics.PorterDuff.Mode.SRC_IN);
        }*/


        holder.binding.tvName.setText(arrayList.get(position).getCategoryName());
        if(arrayList.get(position).getOrderCount().equals("0")){
            holder.binding.tvCount.setVisibility(View.GONE);
        }
        else {
            holder.binding.tvCount.setVisibility(View.VISIBLE);
            holder.binding.tvCount.setText(arrayList.get(position).getOrderCount());
        }


        holder.binding.mainRl.setBackgroundColor(DataManager.getColor(position));
        holder.binding.tvCount.setTextColor(DataManager.getColor(position));

    }

    @Override
    public int getItemCount() {
        int i =0;
        if (arrayList!=null) i = arrayList.size();

        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemHomeCatBinding binding;
        public MyViewHolder(@NonNull ItemHomeCatBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.mainRl.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
                bundle.putString("id",arrayList.get(getAdapterPosition()).getSkillsId());
                Navigation.findNavController(view).navigate(R.id.action_home_fragment_to_orderList,bundle);
                //  Navigation.findNavController(view).navigate(R.id.action_addShop_fragment_to_addTime);


            });

        }
    }
}
