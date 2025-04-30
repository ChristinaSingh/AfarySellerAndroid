package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemBrandBinding;
import com.afaryseller.databinding.ItemMainCategoryBinding;
import com.afaryseller.ui.addproduct.AddProductAct;
import com.afaryseller.ui.addproduct.AddProductNewAct;
import com.afaryseller.ui.addproduct.model.BrandModel;
import com.afaryseller.ui.addproduct.model.MainCateModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.MyViewHolder> {
    Context context;
    ArrayList<BrandModel.Result> arrayList;
    AddDateLister listener;

    public BrandAdapter(Context context, ArrayList<BrandModel.Result> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBrandBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_brand, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.chkBox.setText(arrayList.get(position).getName());

        if(arrayList.get(position).isChk()==true)  holder.binding.chkBox.setChecked(true);
        else  holder.binding.chkBox.setChecked(false);
    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemBrandBinding binding;

        public MyViewHolder(@NonNull ItemBrandBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.chkBox.setOnClickListener(v -> {
                for (int i = 0 ;i< arrayList.size();i++){
                    arrayList.get(i).setChk(false);
                }

                arrayList.get(getAdapterPosition()).setChk(true);
                listener.onDate("",getAdapterPosition(),"brandCheck",true);
            });
        }
    }

    public void filterList(ArrayList<BrandModel.Result> filterlist) {
        if(filterlist.size()==0)
            AddProductNewAct.tvNotFound.setVisibility(View.VISIBLE);
        else
            AddProductNewAct.tvNotFound.setVisibility(View.GONE);
        arrayList = filterlist;
        notifyDataSetChanged();
    }

}