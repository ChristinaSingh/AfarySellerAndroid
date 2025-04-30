package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemAttributeBinding;
import com.afaryseller.databinding.ItemBrandBinding;
import com.afaryseller.databinding.ItemSubAttributeBinding;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class SubAttributeAdapter extends RecyclerView.Adapter<SubAttributeAdapter.MyViewHolder> {
    Context context;
    ArrayList<AttributeModel.Result> arrayList;
    AddDateLister listener;

    public SubAttributeAdapter(Context context, ArrayList<AttributeModel.Result> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubAttributeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sub_attribute, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
     //   holder.binding.chkBox.setText(arrayList.get(position).getName());
        holder.binding.tvSubAttributeName.setText(arrayList.get(position).getName());


        if(arrayList.get(position).isChk()) holder.binding.chkBox.setChecked(true);
        else holder.binding.chkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubAttributeBinding binding;

        public MyViewHolder(@NonNull ItemSubAttributeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;


            binding.chkBox.setOnClickListener(v -> {

                if(arrayList.get(getAdapterPosition()).isChk() == false){
                   // arrayList.get(getAdapterPosition()).setChk(true);
                    listener.onDate("",getAdapterPosition(),"subAttribute",true);
                }
                else {
                   // arrayList.get(getAdapterPosition()).setChk(false);
                    listener.onDate("",getAdapterPosition(),"subAttribute",false);
                }


                binding.ivDelete.setOnClickListener(view -> {
                        listener.onDate("",getAdapterPosition(),"deleteSubAttribute",false);

                });




            });

        }
    }



}
