package com.afaryseller.ui.viewproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSelectSubCategoryBinding;
import com.afaryseller.databinding.ItemViewSelectSubCategoryBinding;
import com.afaryseller.ui.addproduct.adapter.SubVariationAdapter;
import com.afaryseller.ui.addproduct.listener.ImageChangeListener;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;

import java.util.ArrayList;

public class ViewSubVariationAdapter extends RecyclerView.Adapter<ViewSubVariationAdapter.MyViewHolder> {
    Context context;
    ArrayList<SelectSubCateModel> arrayList;
    ImageChangeListener listener;
    int  mainPosition;
    public ViewSubVariationAdapter(Context context, ArrayList<SelectSubCateModel> arrayList, int  mainPosition, ImageChangeListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.mainPosition = mainPosition;
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


        }
    }
}