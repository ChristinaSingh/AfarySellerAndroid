package com.afaryseller.ui.orderdetails;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemDetailsBinding;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MyViewHolder> {
    Context context;
    ArrayList<OrderDetailsModel.Result.Product> arrayList;
    OrderItemListener listener;

    public ItemsAdapter(Context context, ArrayList<OrderDetailsModel.Result.Product> arrayList,OrderItemListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemDetailsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_details, parent, false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvProductName.setText(arrayList.get(position).getProductSku() +" "+ arrayList.get(position).getProductName());
        //  holder.binding.tvProductPrice.setText(arrayList.get(position).getProductQut() + " X " + arrayList.get(position).getProductPrice() );
        //  holder.binding.tvProductTotal.setText(Integer.parseInt(arrayList.get(position).getProductQut()) * Integer.parseInt(arrayList.get(position).getProductPrice())+"");


        holder.binding.tvProductPrice.setText(arrayList.get(position).getQuantity() + " X " + arrayList.get(position).getPrice());
        holder.binding.tvProductTotal.setText(Integer.parseInt(arrayList.get(position).getQuantity()) * parseFrenchNumber(arrayList.get(position).getPrice()) + "");

        if(arrayList.get(position).getStatus().equals("Cancelled")){
            holder.binding.tvCancel.setVisibility(View.GONE);
            holder.binding.tvCancelledOrder.setVisibility(View.VISIBLE);


        }
        else  if(arrayList.get(position).getStatus().equals("Completed")){
            holder.binding.tvCancel.setVisibility(View.GONE);
            holder.binding.tvCancelledOrder.setVisibility(View.GONE);

        }

        else  if(arrayList.get(position).getStatus().equals("In_Transit")){
            holder.binding.tvCancel.setVisibility(View.GONE);
            holder.binding.tvCancelledOrder.setVisibility(View.GONE);

        }

        else  if(arrayList.get(position).getStatus().equals("Reached_shipping_company")){
            holder.binding.tvCancel.setVisibility(View.GONE);
            holder.binding.tvCancelledOrder.setVisibility(View.GONE);

        }


        else  if(arrayList.get(position).getStatus().equals("Cancelled_by_user")){

            if(arrayList.get(position).getReturnToSellerStatus().equals("Yes")){
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvCancelledOrder.setVisibility(View.GONE );

            }
            else {
                holder.binding.tvCancel.setVisibility(View.GONE);
                holder.binding.tvCancelledOrder.setVisibility(View.VISIBLE);
                holder.binding.tvCancelledOrder.setText(context.getString(R.string.order_return));
            }

        }




        else {
            holder.binding.tvCancel.setVisibility(View.VISIBLE);
            holder.binding.tvCancelledOrder.setVisibility(View.GONE);

        }

        holder.itemView.setOnClickListener(view -> productDetailDialog(arrayList.get(position)));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemDetailsBinding binding;

        public MyViewHolder(@NonNull ItemDetailsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.tvCancel.setOnClickListener(view -> listener.orderItem(getAdapterPosition(),arrayList.get(getAdapterPosition()),"Cancel"));

            binding.tvCancelledOrder.setOnClickListener(view -> {
                    if(arrayList.get(getAdapterPosition()).getStatus().equals("Cancelled_by_user"))
                        listener.orderItem(getAdapterPosition(),arrayList.get(getAdapterPosition()),"Received");
            });



        }
    }


    public void productDetailDialog(OrderDetailsModel.Result.Product productData) {
          ArrayList<MainInnerModel> mainInnerModelArrayList = new ArrayList<>();
        //   Dialog dialog = new Dialog(context, R.style.FullScreenDialog1);
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_product_info);



        TextView tvProductName = dialog.findViewById(R.id.tvProductName);
        TextView tvQuantity = dialog.findViewById(R.id.tvQuantity);
        TextView tvPrice = dialog.findViewById(R.id.tvPrice);
        ImageView ivProduct = dialog.findViewById(R.id.ivProduct);
        ImageView ivBack = dialog.findViewById(R.id.ivBack);
        TextView tvSku = dialog.findViewById(R.id.tvSku);
        TextView tvClose = dialog.findViewById(R.id.btnClose);

        RecyclerView rvMainInner = dialog.findViewById(R.id.rvMainInner);



        try {
            JSONArray jsonArray = new JSONArray(productData.getOption());
            if(jsonArray.length()>0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mainInnerModelArrayList.add(new MainInnerModel(jsonObject.getString("mainName"), jsonObject.getString("innerName")));
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }



        Glide.with(context).load(productData.getProductImages()).centerCrop().into(ivProduct);

        tvProductName.setText("Product Name : " + productData.getProductName());
        tvQuantity.setText("Quantity : " + productData.getQuantity());
        tvPrice.setText("Price : " + productData.getCurrency() + productData.getPrice());
        tvSku.setText("Sku : " + productData.getProductSku());

        rvMainInner.setAdapter(new MainInnerAdapter(context,mainInnerModelArrayList));



        tvClose.setOnClickListener(view -> dialog.dismiss());

        ivBack.setOnClickListener(view -> dialog.dismiss());

        WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
        dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setAttributes(layoutParams);

        dialog.show();
    }

    private int parseFrenchNumber(String number) {
        // Remove the commas and parse to an integer
        String cleanedNumber = number.replace(",", "");
        return Integer.parseInt(cleanedNumber);
    }

}