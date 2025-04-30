package com.afaryseller.ui.bookedorder;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemOrderBinding;
import com.afaryseller.ui.orderdetails.OrderDetailsAct;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderModel.Result> arrayList;
    OrderListener listener;

    public OrderAdapter(Context context, ArrayList<OrderModel.Result> arrayList, OrderListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemOrderBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_order, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.orderId.setText(arrayList.get(position).getOrderId());
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





      /*  double total = Double.parseDouble(arrayList.get(position).getPrice())
                + Double.parseDouble(arrayList.get(position).getPlatFormsFees())
                + Double.parseDouble(arrayList.get(position).getDeliveryCharges())
                + Double.parseDouble(arrayList.get(position).getTaxN1())
                + Double.parseDouble(arrayList.get(position).getTaxN2());*/
        //   holder.binding.orderPrice.setText("Rs"+String.format("%.2f",total));


        if (arrayList.get(position).getProductList() != null) {
            if (arrayList.get(position).getProductList().size() == 1) {
                holder.binding.llOne.setVisibility(View.VISIBLE);
                holder.binding.llTwo.setVisibility(View.GONE);
                holder.binding.llThree.setVisibility(View.GONE);
                holder.binding.llFour.setVisibility(View.GONE);
                Glide.with(context).load(arrayList.get(position).getProductList().get(0).getProductImages()).into(holder.binding.productImage);

            } else if (arrayList.get(position).getProductList().size() == 2) {
                holder.binding.llOne.setVisibility(View.GONE);
                holder.binding.llTwo.setVisibility(View.VISIBLE);
                holder.binding.llThree.setVisibility(View.GONE);
                holder.binding.llFour.setVisibility(View.GONE);
                Glide.with(context).load(arrayList.get(position).getProductList().get(0).getProductImages()).into(holder.binding.productImageTw01);
                Glide.with(context).load(arrayList.get(position).getProductList().get(1).getProductImages()).into(holder.binding.productImageTwo2);

            } else if (arrayList.get(position).getProductList().size() == 3) {
                holder.binding.llOne.setVisibility(View.GONE);
                holder.binding.llTwo.setVisibility(View.GONE);
                holder.binding.llThree.setVisibility(View.VISIBLE);
                holder.binding.llFour.setVisibility(View.GONE);
                Glide.with(context).load(arrayList.get(position).getProductList().get(0).getProductImages()).into(holder.binding.productImageThree1);
                Glide.with(context).load(arrayList.get(position).getProductList().get(1).getProductImages()).into(holder.binding.productImageThree2);
                Glide.with(context).load(arrayList.get(position).getProductList().get(2).getProductImages()).into(holder.binding.productImageThree3);

            } else if (arrayList.get(position).getProductList().size() <= 4) {
                holder.binding.llOne.setVisibility(View.GONE);
                holder.binding.llTwo.setVisibility(View.GONE);
                holder.binding.llThree.setVisibility(View.GONE);
                holder.binding.llFour.setVisibility(View.VISIBLE);
                Glide.with(context).load(arrayList.get(position).getProductList().get(0).getProductImages()).into(holder.binding.productImageFour1);
                Glide.with(context).load(arrayList.get(position).getProductList().get(1).getProductImages()).into(holder.binding.productImageFour2);
                Glide.with(context).load(arrayList.get(position).getProductList().get(2).getProductImages()).into(holder.binding.productImageFour3);
                holder.binding.tvImgCount.setText("+" + (arrayList.get(position).getProductList().size() - 3));

            }
        }

        if (arrayList.get(position).getStatus().equals("Pending")) {
            holder.binding.rlAllBtn.setVisibility(View.VISIBLE);
            holder.binding.rlBtn.setVisibility(View.GONE);
            holder.binding.orderStatus.setVisibility(View.GONE);
            holder.binding.cardViewMain.setCardBackgroundColor(context.getColor(R.color.red));
            holder.binding.orderId.setTextColor(context.getColor(R.color.white));
            holder.binding.orderPrice.setTextColor(context.getColor(R.color.purple_700));

        } else if (arrayList.get(position).getStatus().equals("Accepted") || arrayList.get(position).getStatus().equalsIgnoreCase("PickedUp")
        || arrayList.get(position).getStatus().equalsIgnoreCase("In_Transit")) {
            holder.binding.rlAllBtn.setVisibility(View.VISIBLE);
            holder.binding.rlBtn.setVisibility(View.GONE);
            holder.binding.orderStatus.setVisibility(View.VISIBLE);
            holder.binding.orderStatus.setText(arrayList.get(position).getStatus());
            holder.binding.orderStatus.setTextColor(context.getColor(R.color.colorGreen));

            holder.binding.cardViewMain.setCardBackgroundColor(context.getColor(R.color.yelllow));
            holder.binding.orderId.setTextColor(context.getColor(R.color.white));
            holder.binding.orderPrice.setTextColor(context.getColor(R.color.purple_700));


        } else if (arrayList.get(position).getStatus().equals("Cancelled")) {
            holder.binding.rlAllBtn.setVisibility(View.GONE);
            holder.binding.rlBtn.setVisibility(View.VISIBLE);
            holder.binding.btnView.setBackgroundResource(R.drawable.btn_shape_capsule);
            holder.binding.orderStatus.setVisibility(View.VISIBLE);
            holder.binding.orderStatus.setText(arrayList.get(position).getStatus());
            holder.binding.orderStatus.setTextColor(context.getColor(R.color.white));

            holder.binding.cardViewMain.setCardBackgroundColor(context.getColor(R.color.colorGray));
            holder.binding.orderId.setTextColor(context.getColor(R.color.white));
            holder.binding.orderPrice.setTextColor(context.getColor(R.color.purple_700));


        } else if (arrayList.get(position).getStatus().equals("Cancelled_by_user")) {
            holder.binding.rlAllBtn.setVisibility(View.GONE);
            holder.binding.rlBtn.setVisibility(View.VISIBLE);
            holder.binding.btnView.setBackgroundResource(R.drawable.btn_shape_capsule);
            holder.binding.orderStatus.setVisibility(View.VISIBLE);
            holder.binding.orderStatus.setText(context.getString(R.string.cancelled));
            holder.binding.orderStatus.setTextColor(context.getColor(R.color.white));

            holder.binding.cardViewMain.setCardBackgroundColor(context.getColor(R.color.colorGray));
            holder.binding.orderId.setTextColor(context.getColor(R.color.white));
            holder.binding.orderPrice.setTextColor(context.getColor(R.color.purple_700));


        } else if (arrayList.get(position).getStatus().equals("Completed")
        || arrayList.get(position).getStatus().equalsIgnoreCase("Reached_shipping_company")) {
            holder.binding.rlAllBtn.setVisibility(View.GONE);
            holder.binding.rlBtn.setVisibility(View.VISIBLE);
            holder.binding.btnView.setBackgroundResource(R.drawable.rounded_shap_gray);
            holder.binding.orderStatus.setVisibility(View.VISIBLE);
            holder.binding.orderStatus.setText(context.getString(R.string.completed));
            holder.binding.orderStatus.setTextColor(context.getColor(R.color.white));
            holder.binding.cardViewMain.setCardBackgroundColor(context.getColor(R.color.colorGreen));
            holder.binding.orderId.setTextColor(context.getColor(R.color.white));
            holder.binding.orderPrice.setTextColor(context.getColor(R.color.purple_700));


        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        ItemOrderBinding binding;

        public MyViewHolder(@NonNull ItemOrderBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

            binding.btnAccept.setOnClickListener(v ->
            {
                if (!arrayList.get(getAdapterPosition()).getStatus().equals("Cancelled")) {
                    context.startActivity(new Intent(context, OrderDetailsAct.class)
                            .putExtra("id", arrayList.get(getAdapterPosition()).getOrderId() + ""));
                }
            });


            binding.btnView.setOnClickListener(v ->
            {
                context.startActivity(new Intent(context, OrderDetailsAct.class)
                        .putExtra("id", arrayList.get(getAdapterPosition()).getOrderId() + ""));
            });


            binding.btnDecline.setOnClickListener(v -> {
                listener.onOrder(arrayList.get(getAdapterPosition()));
            });

        }
    }

    private int parseFrenchNumber(String number) {
        // Remove the commas and parse to an integer
        String cleanedNumber = number.replace(",", "");
        return Integer.parseInt(cleanedNumber);
    }

}
