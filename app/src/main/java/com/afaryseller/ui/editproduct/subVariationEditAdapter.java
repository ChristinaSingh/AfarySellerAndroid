package com.afaryseller.ui.editproduct;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSelectSubCategoryBinding;

import java.util.ArrayList;



public class subVariationEditAdapter extends RecyclerView.Adapter<subVariationEditAdapter.MyViewHolder> {
    Context context;
    ArrayList<AttributeSubAttributeModel.Result.Attribute> arrayList;
    SubVariationListener listener;
    int  mainPosition;
    public subVariationEditAdapter(Context context, ArrayList<AttributeSubAttributeModel.Result.Attribute> arrayList, int  mainPosition, SubVariationListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.mainPosition = mainPosition;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSelectSubCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_select_sub_category, parent, false);
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
        ItemSelectSubCategoryBinding binding;

        public MyViewHolder(@NonNull ItemSelectSubCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivRemove.setOnClickListener(v -> {
                if(arrayList.size()!=0){
                    listener.subVariation(mainPosition,getAdapterPosition());
                }
            });
        }
    }
}