package com.afaryseller.ui.viewproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemVariationBinding;
import com.afaryseller.databinding.ItemViewVariationBinding;
import com.afaryseller.ui.addproduct.adapter.SubVariationAdapter;
import com.afaryseller.ui.addproduct.adapter.VariationAdapter;
import com.afaryseller.ui.addproduct.listener.ImageChangeListener;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;

import java.util.ArrayList;

public class ViewVariationAdapter extends RecyclerView.Adapter<ViewVariationAdapter.MyViewHolder> implements ImageChangeListener {
    Context context;
    ArrayList<AttributeModel.Result> arrayList;

    public ViewVariationAdapter(Context context, ArrayList<AttributeModel.Result> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemViewVariationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_view_variation, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.chkBox.setText(arrayList.get(position).getName());
        holder.binding.rvAddAttribute.setAdapter(new ViewSubVariationAdapter(context, (ArrayList<SelectSubCateModel>) arrayList.get(position).getModelList(),position, ViewVariationAdapter.this));
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
        ItemViewVariationBinding binding;

        public MyViewHolder(@NonNull ItemViewVariationBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}