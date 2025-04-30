package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemAttributeBinding;
import com.afaryseller.databinding.ItemVariationBinding;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.addproduct.listener.ImageChangeListener;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class VariationAdapter extends RecyclerView.Adapter<VariationAdapter.MyViewHolder> implements  ImageChangeListener {
    Context context;
    ArrayList<AttributeModel.Result> arrayList;

    public VariationAdapter(Context context, ArrayList<AttributeModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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
        holder.binding.rvAddAttribute.setAdapter(new SubVariationAdapter(context, (ArrayList<SelectSubCateModel>) arrayList.get(position).getModelList(),position, VariationAdapter.this));
    }




    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    @Override
    public void imageChange(int position, String image) {
        arrayList.get(position).getModelList().remove(Integer.parseInt(image));
        if( arrayList.get(position).getModelList().size()==0) arrayList.remove(position);

        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemVariationBinding binding;

        public MyViewHolder(@NonNull ItemVariationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}