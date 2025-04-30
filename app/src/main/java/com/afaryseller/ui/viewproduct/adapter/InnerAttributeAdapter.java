package com.afaryseller.ui.viewproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemInnerBinding;
import com.afaryseller.ui.viewproduct.ShoppingProductModal;

import java.util.ArrayList;




public class InnerAttributeAdapter extends RecyclerView.Adapter<InnerAttributeAdapter.MyViewHolder> {
    Context context;
    ArrayList<ShoppingProductModal.Result.ValidateName.AttributeName> arrayList;
    int position;
    public InnerAttributeAdapter(Context context, ArrayList<ShoppingProductModal.Result.ValidateName.AttributeName> arrayList,int position) {
        this.context = context;
        this.arrayList = arrayList;
        this.position = position;
    }




    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemInnerBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_inner,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getName());



    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemInnerBinding binding;
        public MyViewHolder(@NonNull ItemInnerBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


        }
    }
}
