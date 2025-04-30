package com.afaryseller.ui.viewproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemMainAttributeBinding;
import com.afaryseller.ui.viewproduct.ShoppingProductModal;

import java.util.ArrayList;
import java.util.List;





public class MainAttributeAdapter2 extends RecyclerView.Adapter<MainAttributeAdapter2.MyViewHolder> {

    Context context;
    ArrayList<ShoppingProductModal.Result.ValidateName> arrayList;

    public MainAttributeAdapter2(Context context, ArrayList<ShoppingProductModal.Result.ValidateName> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMainAttributeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_main_attribute,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.binding.chkBox.setText(arrayList.get(position).getName());
        holder.binding.rvAddAttribute.setAdapter(new InnerAttributeAdapter(context, (ArrayList<ShoppingProductModal.Result.ValidateName.AttributeName>) arrayList.get(position).getAttributeName(),position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemMainAttributeBinding binding;
        public MyViewHolder(@NonNull ItemMainAttributeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }

    }


}