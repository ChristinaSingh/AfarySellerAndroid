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
import com.afaryseller.databinding.ItemBrandBinding;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.addproduct.AddProductNewAct;
import com.afaryseller.ui.addproduct.model.AttributeAddedModel;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.BrandModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class AttributeAdapter extends RecyclerView.Adapter<AttributeAdapter.MyViewHolder> {
    Context context;
    ArrayList<AttributeAddedModel.Result> arrayList;
    AddDateLister listener;

    public AttributeAdapter(Context context, ArrayList<AttributeAddedModel.Result> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAttributeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_attribute, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvAttributeName.setText(arrayList.get(position).getName());


        if(arrayList.get(position).isChk()) {
          //  holder.binding.chkBox.setVisibility(View.VISIBLE);
            holder.binding.chkBox.setChecked(true);
        }
        else {
           // holder.binding.chkBox.setVisibility(View.GONE);
            holder.binding.chkBox.setChecked(false);
        }



    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAttributeBinding binding;

        public MyViewHolder(@NonNull ItemAttributeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.tvAttributeName.setOnClickListener(v -> {
                listener.onDate("",getAdapterPosition(),"attribute",true);
            });


            binding.ivDelete.setOnClickListener(view -> {
                listener.onDate("",getAdapterPosition(),"deleteAttribute",false);

            });



            binding.chkBox.setOnClickListener(v -> {
                if(arrayList.get(getAdapterPosition()).isChk() == false){
                   // arrayList.get(getAdapterPosition()).setChk(true);
                    listener.onDate("",getAdapterPosition(),"attributeCheck",true);
                }
                else {
                    //arrayList.get(getAdapterPosition()).setChk(false);
                    listener.onDate("",getAdapterPosition(),"attributeCheck",false);
                }


            });


        }
    }

    public void filterList(ArrayList<AttributeAddedModel.Result> filterlist) {
        if(filterlist.size()==0)
            AddProductNewAct.tvNotFoundAttribute.setVisibility(View.VISIBLE);
        else
            AddProductNewAct.tvNotFoundAttribute.setVisibility(View.GONE);
        arrayList = filterlist;
        notifyDataSetChanged();
    }

}
