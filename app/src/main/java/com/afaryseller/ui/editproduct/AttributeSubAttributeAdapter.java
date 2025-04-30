package com.afaryseller.ui.editproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemVariationBinding;


import java.util.ArrayList;



public class AttributeSubAttributeAdapter extends RecyclerView.Adapter<AttributeSubAttributeAdapter.MyViewHolder> implements SubVariationListener {
    Context context;
    ArrayList<AttributeSubAttributeModel.Result> arrayList;
    VariationListener listener;
    public AttributeSubAttributeAdapter(Context context, ArrayList<AttributeSubAttributeModel.Result> arrayList,VariationListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener =listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemVariationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_variation, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.chkBox.setText(arrayList.get(position).getName());
        holder.binding.rvAddAttribute.setAdapter(new subVariationEditAdapter(context, (ArrayList<AttributeSubAttributeModel.Result.Attribute>) arrayList.get(position).getAttributes(),position, AttributeSubAttributeAdapter.this));
    }




    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }



    @Override
    public void subVariation(int mainPosition, int position) {
        listener.variation(mainPosition,position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemVariationBinding binding;

        public MyViewHolder(@NonNull ItemVariationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
