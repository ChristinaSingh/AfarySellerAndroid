package com.afaryseller.ui.addtime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemTimeBinding;
import com.afaryseller.ui.addtime.TimeModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.MyViewHolder> {
    Context context;
    ArrayList<TimeModel.Result> arrayList;
    AddDateLister listener;

    public WeeklyAdapter(Context context, ArrayList<TimeModel.Result> arrayList,AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTimeBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_time, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvTime.setText(arrayList.get(position).getWeekName());
    }

    @Override
    public int getItemCount() {
     return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTimeBinding binding;

        public MyViewHolder(@NonNull ItemTimeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.layout.setOnClickListener(v -> {
                if(arrayList.get(getAdapterPosition()).getStatus().equals("0")){
                    arrayList.get(getAdapterPosition()).setStatus("1");
                    listener.onDate("1",getAdapterPosition(),"daily close day",true);
                    notifyDataSetChanged();
                }
                else {
                    arrayList.get(getAdapterPosition()).setStatus("1");
                    listener.onDate("0",getAdapterPosition(),"daily close day",false);
                    notifyDataSetChanged();
                }
            });
        }
    }
}