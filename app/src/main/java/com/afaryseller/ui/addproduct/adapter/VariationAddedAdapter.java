package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemVariationBinding;
import com.afaryseller.ui.addproduct.listener.ImageChangeListener;
import com.afaryseller.ui.addproduct.listener.UpdateSubAttributeListListener;
import com.afaryseller.ui.addproduct.model.AttributeAddedModel;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.SelectSubCateModel;

import java.util.ArrayList;



public class VariationAddedAdapter extends RecyclerView.Adapter<VariationAddedAdapter.MyViewHolder> implements ImageChangeListener {
    Context context;
    ArrayList<AttributeAddedModel.Result> arrayList;
    UpdateSubAttributeListListener listener;
    public VariationAddedAdapter(Context context, ArrayList<AttributeAddedModel.Result> arrayList,UpdateSubAttributeListListener listener) {
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
        holder.binding.rvAddAttribute.setAdapter(new SubVariationAdapter(context, (ArrayList<SelectSubCateModel>) arrayList.get(position).getModelList(),position, VariationAddedAdapter.this));
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
        if(arrayList.get(position).getModelList().isEmpty()){
           // arrayList.remove(position);
            listener.onList(position);
        }

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

