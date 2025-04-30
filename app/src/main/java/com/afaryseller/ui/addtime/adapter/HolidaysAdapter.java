package com.afaryseller.ui.addtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemHolidaysBinding;
import com.afaryseller.databinding.ItemTimeBinding;
import com.afaryseller.ui.addtime.TimeModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;
import com.afaryseller.ui.addtime.model.HolidaysModel;

import java.util.ArrayList;

public class HolidaysAdapter extends RecyclerView.Adapter<HolidaysAdapter.MyViewHolder> {
    Context context;
    ArrayList<HolidaysModel.Result> arrayList;
    AddDateLister listener;

    public HolidaysAdapter(Context context, ArrayList<HolidaysModel.Result> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHolidaysBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_holidays, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
         holder.binding.tvTime.setText(arrayList.get(position).getHolidayDate());

    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemHolidaysBinding binding;

        public MyViewHolder(@NonNull ItemHolidaysBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.ivRemove.setOnClickListener(v -> listener.onDate(arrayList.get(getAdapterPosition()).getId(),getAdapterPosition(),""));

        }
    }

}