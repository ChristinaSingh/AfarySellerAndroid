package com.afaryseller.ui.orderdetails;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemMaininnerBinding;

import java.util.ArrayList;

public class MainInnerAdapter extends RecyclerView.Adapter<MainInnerAdapter.MyViewHolder> {
    Context context;
    ArrayList<MainInnerModel>arrayList;

    public MainInnerAdapter(Context context, ArrayList<MainInnerModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMaininnerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_maininner,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).mainName + " : " + arrayList.get(position).innerName);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemMaininnerBinding binding;
        public MyViewHolder(@NonNull ItemMaininnerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
