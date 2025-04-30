package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSelectCategoryBinding;
import com.afaryseller.databinding.ItemSubCategoryBinding;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class SelectCategoryAdapter extends RecyclerView.Adapter<SelectCategoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<SelectSubCateModel> arrayList;
    AddDateLister listener;

    public SelectCategoryAdapter(Context context, ArrayList<SelectSubCateModel> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_select_category, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSelectCategoryBinding binding;

        public MyViewHolder(@NonNull ItemSelectCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;




        }
    }

}