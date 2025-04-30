package com.afaryseller.ui.editTime.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemTimeBinding;
import com.afaryseller.ui.addtime.TimeModel;
import com.afaryseller.ui.addtime.adapter.CloseAdapter;
import com.afaryseller.ui.addtime.listener.AddDateLister;
import com.afaryseller.ui.editTime.model.CloseTimeModel;

import java.util.ArrayList;

public class CloseTimeAdapter extends RecyclerView.Adapter<CloseTimeAdapter.MyViewHolder> {
    Context context;
    ArrayList<CloseTimeModel.Result> arrayList;
    AddDateLister listener;

    public CloseTimeAdapter(Context context, ArrayList<CloseTimeModel.Result> arrayList, AddDateLister listener) {
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
        if(!arrayList.get(position).getCloseTime().equals("")) holder.binding.tvTime.setText(arrayList.get(position).getWeekName() + "\n" + arrayList.get(position).getCloseTime());
        else holder.binding.tvTime.setText(arrayList.get(position).getWeekName());    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemTimeBinding binding;

        public MyViewHolder(@NonNull ItemTimeBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.layout.setOnClickListener(v -> listener.onDate("",getAdapterPosition(),"close",true));

        }
    }
}
