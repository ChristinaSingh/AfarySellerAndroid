package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSelectCategoryBinding;
import com.afaryseller.databinding.ItemSelectSubCategoryBinding;
import com.afaryseller.databinding.ItemSubCategoryBinding;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;
import com.afaryseller.ui.addproduct.model.SubCatModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class SelectSubCateAdapter  extends RecyclerView.Adapter<SelectSubCateAdapter.MyViewHolder> {
    Context context;
    ArrayList<SelectSubCateModel> arrayList;
    AddDateLister listener;

    public SelectSubCateAdapter(Context context, ArrayList<SelectSubCateModel> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
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
                listener.onDate("",getAdapterPosition(),"removeSubCategory",false);
            });


        }
    }

}