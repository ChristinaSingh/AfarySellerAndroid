package com.afaryseller.ui.subscription.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemSubscriptionBinding;
import com.afaryseller.ui.shoplist.listerner.ShopListener;
import com.afaryseller.ui.subscription.model.SubscriptionModel;

import java.util.ArrayList;

public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubscriptionModel.Result> arrayList;
    ShopListener listener;

    public SubscriptionAdapter(Context context,ArrayList<SubscriptionModel.Result>arrayList,ShopListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubscriptionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_subscription,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvTitle.setText(arrayList.get(position).getPlanName());
        holder.binding.tvPeriod.setText(arrayList.get(position).getValidDays()+" "+context.getString(R.string.days));
        holder.binding.tvPrice.setText(arrayList.get(position).getSymbol()+arrayList.get(position).getCurrencyCode()
                +arrayList.get(position).getSymbol()+arrayList.get(position).getPrice());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.binding.tvQuantity.setText(Html.fromHtml(arrayList.get(position).getFeatures1(),Html.FROM_HTML_MODE_COMPACT));
            holder.binding.tvCommission.setText(Html.fromHtml(arrayList.get(position).getFeatures2(),Html.FROM_HTML_MODE_COMPACT));
            holder.binding.tvAgent3.setText(Html.fromHtml(arrayList.get(position).getFeatures3(),Html.FROM_HTML_MODE_COMPACT));

        } else {
            holder.binding.tvQuantity.setText(Html.fromHtml(arrayList.get(position).getFeatures1()));
            holder.binding.tvCommission.setText(Html.fromHtml(arrayList.get(position).getFeatures2()));
            holder.binding.tvAgent3.setText(Html.fromHtml(arrayList.get(position).getFeatures3()));


        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubscriptionBinding binding;
        public MyViewHolder(@NonNull ItemSubscriptionBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.tvSubscribe.setOnClickListener(v -> {
                listener.editShop(getAdapterPosition()+"",v,null,"subscribe");
            });

        }
    }
}