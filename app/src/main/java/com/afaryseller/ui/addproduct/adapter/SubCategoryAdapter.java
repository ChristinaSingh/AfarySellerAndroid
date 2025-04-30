package com.afaryseller.ui.addproduct.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemMainCategoryBinding;
import com.afaryseller.databinding.ItemSubCategoryBinding;
import com.afaryseller.ui.addproduct.model.MainCateModel;
import com.afaryseller.ui.addproduct.model.SubCatModel;
import com.afaryseller.ui.addtime.listener.AddDateLister;

import java.util.ArrayList;

public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubCatModel.Result> arrayList;
    AddDateLister listener;

    public SubCategoryAdapter(Context context, ArrayList<SubCatModel.Result> arrayList, AddDateLister listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSubCategoryBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_sub_category, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.chkBox.setText(arrayList.get(position).getSubCatName());

        if(arrayList.get(position).isChk()==true)  {
            holder.binding.chkBox.setChecked(true);
            holder.binding.chkBox.setClickable(false);

        }
        else {
            holder.binding.chkBox.setChecked(false);
            holder.binding.chkBox.setClickable(true);

        }
    }

    @Override
    public int getItemCount() {
        int i = 0;
        if (arrayList != null) i = arrayList.size();
        return i;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemSubCategoryBinding binding;

        public MyViewHolder(@NonNull ItemSubCategoryBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.chkBox.setOnClickListener(v -> {
                if(arrayList.get(getAdapterPosition()).isChk() == false){
                    arrayList.get(getAdapterPosition()).setChk(true);
                    listener.onDate("",getAdapterPosition(),"subCate",true);
                }
                else {
                  //  arrayList.get(getAdapterPosition()).setChk(true);
                 //   listener.onDate("",getAdapterPosition(),"subCate",true);

                    Toast.makeText(context, "Already added...", Toast.LENGTH_SHORT).show();
                }

            });




        }
    }
}