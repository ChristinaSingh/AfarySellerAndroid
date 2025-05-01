package com.afaryseller.ui.subseller.report;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemReportBinding;
import com.afaryseller.ui.bookedorder.OrderListener;
import com.afaryseller.ui.bookedorder.OrderModel;
import com.afaryseller.ui.orderdetails.OrderDetailsAct;
import com.afaryseller.utility.DataManager;

import java.util.ArrayList;


public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderModel.Result> arrayList;
    OrderListener listener;

    public ReportAdapter(Context context, ArrayList<OrderModel.Result> arrayList, OrderListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReportBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_report, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.orderId.setText(arrayList.get(position).getOrderId());
        holder.binding.tvDate.setText(DataManager.formatDate(arrayList.get(position).getDateTime()));
        //  holder.binding.orderPrice.setText("FCFA"+ arrayList.get(position).getTotalAmount());

        try {
            //  double taxN1 = Double.parseDouble(arrayList.get(position).getTaxN1());
            //   double taxN2 = Double.parseDouble(arrayList.get(position).getTaxN2());
            //  double platFormsFees = Double.parseDouble(arrayList.get(position).getPlatFormsFees());
            //   double deliveryFees = Double.parseDouble(arrayList.get(position).getDeliveryCharges());
            int n1=0;
            if (arrayList.get(position).getTotalAmount().contains(",")) n1 = parseFrenchNumber(arrayList.get(position).getTotalAmount().replace(",",""));
            else n1 = parseFrenchNumber(arrayList.get(position).getTotalAmount());

            int n2 = parseFrenchNumber(arrayList.get(position).getTaxN1())
                    + parseFrenchNumber(arrayList.get(position).getTaxN2())
                    + parseFrenchNumber(arrayList.get(position).getPlatFormsFees())
                    + parseFrenchNumber(arrayList.get(position).getDeliveryCharges());
            int n3 = n1 - n2;
            String subTotal = n3 + "";
            holder.binding.orderPrice.setText("FCFA" + subTotal);
        } catch (Exception e) {
            e.printStackTrace();
        }








    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ItemReportBinding binding;

        public MyViewHolder(@NonNull ItemReportBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.cardViewMain.setOnClickListener(v ->
            {
                context.startActivity(new Intent(context, OrderDetailsAct.class)
                        .putExtra("id", arrayList.get(getAdapterPosition()).getOrderId() + ""));
            });




        }
    }

    private int parseFrenchNumber(String number) {
        // Remove the commas and parse to an integer
        String cleanedNumber = number.replace(",", "");
        return Integer.parseInt(cleanedNumber);
    }

}

