package com.afaryseller.ui.sellerreport;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemPeriodicReportBinding;
import com.afaryseller.ui.bookedorder.OrderListener;


import java.util.ArrayList;



public class PeriodicReportAdapter extends RecyclerView.Adapter<PeriodicReportAdapter.MyViewHolder> {
    Context context;
    ArrayList<PeriodicReportModel.Result> arrayList;
    OrderListener listener;

    public PeriodicReportAdapter(Context context, ArrayList<PeriodicReportModel.Result> arrayList, OrderListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemPeriodicReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_periodic_report, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getName());
        holder.binding.rvPeriodicReport.setAdapter(new PeriodicListAdapter(context, (ArrayList<PeriodicReportModel.Result.OrderDetail>) arrayList.get(position).getOrderDetails()));
        holder.binding.reportTotal.setText(context.getString(R.string.total) + " FCFA" + arrayList.get(position).getReportTotal());

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ItemPeriodicReportBinding binding;

        public MyViewHolder(@NonNull ItemPeriodicReportBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }



}
