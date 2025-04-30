package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSubAttributeAddedBinding;
import com.afaryseller.databinding.ItemSubAttributeBinding;
import com.afaryseller.ui.addproduct.model.AttributeModel;
import com.afaryseller.ui.addproduct.model.SubAttributeModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;



public class SubAttributeAddedAdapter extends RecyclerView.Adapter<SubAttributeAddedAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubAttributeModel.Result> arrayList;
    AddDateLister listener;

    public SubAttributeAddedAdapter(Context context, ArrayList<SubAttributeModel.Result> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubAttributeAddedBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sub_attribute_added, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
       // holder.binding.chkBox.setText(arrayList.get(position).getName());
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
        ItemSubAttributeAddedBinding binding;

        public MyViewHolder(@NonNull ItemSubAttributeAddedBinding itemView) {
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

            });

            binding.ivDelete.setOnClickListener(view -> {
                listener.onDate("",getAdapterPosition(),"deleteSubAttribute",false);

            });

        }
    }



}
