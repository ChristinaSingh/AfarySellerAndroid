package com.afaryseller.ui.shopdetails.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemProductBinding;
import com.afaryseller.ui.shopdetails.model.ShopDetailModel;
import com.afaryseller.ui.shoplist.listerner.ShopListener;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    Context context;
    ArrayList<ShopDetailModel.Result.Product> arrayList;
    ShopListener listener;
    String currency="";

    public ProductAdapter(Context context,ArrayList<ShopDetailModel.Result.Product>arrayList,ShopListener listener,String currency) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
        this.currency = currency;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_product,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvProduct.setText(arrayList.get(position).getProductName());
        holder.binding.tvProductPrice.setText(currency + arrayList.get(position).getProductPrice());
        Glide.with(context).load(arrayList.get(position).getImage1())
                .override(250,250).into(holder.binding.ivImg);

        if(arrayList.get(position).getDeliveryCharges().equals("1")) holder.binding.tvFeeType.setVisibility(View.VISIBLE);
          else holder.binding.tvFeeType.setVisibility(View.GONE);

        if(arrayList.get(position).getApproval().equals("1")) holder.binding.tvProductApproval.setVisibility(View.GONE);
        else holder.binding.tvProductApproval.setVisibility(View.VISIBLE);

        if(arrayList.get(position).getStatus().equals("1")) {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.white));
            holder.binding.tvStatus.setText(context.getString(R.string.active));
        }
        else {
            holder.binding.tvStatus.setTextColor(context.getColor(R.color.red));
            holder.binding.tvStatus.setText(context.getString(R.string.deactive));        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;
        public MyViewHolder(@NonNull ItemProductBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
           binding.tvStatus.setOnClickListener(v -> {
               if (arrayList.get(getAdapterPosition()).getStatus().equals("1")) listener.editShop(arrayList.get(getAdapterPosition()).getProId(),v,null,"0");
               else listener.editShop(arrayList.get(getAdapterPosition()).getProId(),v,null,"1");

           });
            binding.tvEdit.setOnClickListener(v -> {
                listener.editShop(getAdapterPosition()+"",v,null,"edit");
            });

            binding.tvView.setOnClickListener(v -> {
                listener.editShop(getAdapterPosition()+"",v,null,"view");
            });

            binding.ivDelete.setOnClickListener(v -> {
                listener.editShop(getAdapterPosition()+"",v,null,"delete");
            });

        }
    }
}