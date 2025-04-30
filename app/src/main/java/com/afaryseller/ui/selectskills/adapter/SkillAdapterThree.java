package com.afaryseller.ui.selectskills.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemAddSkillsBinding;
import com.afaryseller.ui.selectskills.listener.SkillsListener;
import com.afaryseller.ui.selectskills.model.SkillModel;

import java.util.ArrayList;

public class SkillAdapterThree extends RecyclerView.Adapter<SkillAdapterThree.MyViewHolder> {
    Context context;
    ArrayList<SkillModel.RealEstate> arrayList;
    SkillsListener listener;
    public SkillAdapterThree(Context context, ArrayList<SkillModel.RealEstate> arrayList,SkillsListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemAddSkillsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_add_skills,parent,false);
        return new MyViewHolder(binding) ;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.addSkillText.setText(arrayList.get(position).getName());
        if(arrayList.get(position).getStatus().equals("1")) holder.binding.addSkillText.setChecked(true);
        else  holder.binding.addSkillText.setChecked(false);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemAddSkillsBinding binding;
        public MyViewHolder(@NonNull ItemAddSkillsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.addSkillText.setOnClickListener(v -> {
                if(arrayList.get(getAdapterPosition()).getStatus().equals("0")) {
                    arrayList.get(getAdapterPosition()).setStatus("1");
                    listener.skills("real estate",arrayList.get(getAdapterPosition()).getId(),arrayList.get(getAdapterPosition()).getName(),"1",getAdapterPosition());

                }
                else{
                    arrayList.get(getAdapterPosition()).setStatus("0");
                    listener.skills("real estate",arrayList.get(getAdapterPosition()).getId(),arrayList.get(getAdapterPosition()).getName(),"0",getAdapterPosition());

                }
            });
        }
    }
}