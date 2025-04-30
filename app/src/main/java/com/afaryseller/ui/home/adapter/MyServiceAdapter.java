package com.afaryseller.ui.home.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemHomeCatBinding;
import com.afaryseller.databinding.ItemMyServicesBinding;
import com.afaryseller.ui.home.model.HomeCatModel;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.MyViewHolder> {
    Context context;
    ArrayList<HomeCatModel.Result> arrayList;

    public MyServiceAdapter(Context context, ArrayList<HomeCatModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMyServicesBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_my_services,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(arrayList.get(position).getIcon()).into(holder.binding.ivCat);
        holder.binding.tvName.setText(arrayList.get(position).getCategoryName());

    }

    @Override
    public int getItemCount() {
        int i =0;
        if (arrayList!=null) i = arrayList.size();

        return i;    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemMyServicesBinding binding;
        public MyViewHolder(@NonNull ItemMyServicesBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.mainRl.setOnClickListener(view -> {
                Bundle bundle = new Bundle();
              //  bundle.putString("id",arrayList.get(getAdapterPosition()).getId());
                bundle.putString("id",arrayList.get(getAdapterPosition()).getSkillsId());
                bundle.putString("name",arrayList.get(getAdapterPosition()).getCategoryName());
                Navigation.findNavController(view).navigate(R.id.action_myservice_fragment_to_shoplist,bundle);
              //  Navigation.findNavController(view).navigate(R.id.action_addShop_fragment_to_addTime);


            });


        }
    }
}