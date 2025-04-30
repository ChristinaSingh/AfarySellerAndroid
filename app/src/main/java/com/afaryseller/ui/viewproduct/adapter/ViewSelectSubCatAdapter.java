package com.afaryseller.ui.viewproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemViewSelectSubCategoryBinding;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class ViewSelectSubCatAdapter extends RecyclerView.Adapter<ViewSelectSubCatAdapter.MyViewHolder> {
    Context context;
    ArrayList<SelectSubCateModel> arrayList;
    AddDateLister listener;

    public ViewSelectSubCatAdapter(Context context, ArrayList<SelectSubCateModel> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewSelectSubCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_view_select_sub_category, parent, false);
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
        ItemViewSelectSubCategoryBinding binding;

        public MyViewHolder(@NonNull ItemViewSelectSubCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

         /*   binding.ivRemove.setOnClickListener(v -> {
                listener.onDate("",getAdapterPosition(),"removeSubCategory",false);
            });*/


        }
    }

}