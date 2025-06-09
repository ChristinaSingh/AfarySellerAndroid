package com.afaryseller.ui.wallet;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemWalletBinding;

import java.util.ArrayList;

public class WalletAdapter extends
        RecyclerView.Adapter<WalletAdapter.MyViewHolder> {

    private ArrayList<GetTransferDetails.Result> all_category_subcategory;
    private final Context activity;

    public WalletAdapter(Context a, ArrayList<GetTransferDetails.Result> all_category_subcategory) {
        this.activity = a;
        this.all_category_subcategory = all_category_subcategory;
        // adapterCallback = ((CategoryAdapter.AdapterCallback1) activity);
    }

    @Override
    public WalletAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        ItemWalletBinding progressAdapterBinding = DataBindingUtil.inflate(LayoutInflater.from(activity)
                , R.layout.item_wallet, parent, false);

        return new MyViewHolder(progressAdapterBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        holder.progressAdapterBinding.userName.setText(all_category_subcategory.get(position).getDatetime());
        holder.progressAdapterBinding.amount.setText(all_category_subcategory.get(position).getLocalCurrency() + String.format("%.2f",Double.parseDouble(all_category_subcategory.get(position).getLocalPrice())));
      //  holder.progressAdapterBinding.tvWalletBal.setText(all_category_subcategory.get(position).getWalletLocalCurrency()+ String.format("%.2f",Double.parseDouble(all_category_subcategory.get(position).getWalletLocalPrice())));


        holder.progressAdapterBinding.tvTransId.setText(Html.fromHtml("<font color='#000'>"+ "<b>"+"Transaction ID :" + "</b>" + all_category_subcategory.get(position).getTransactionId()   +"</font>")  );
        holder.progressAdapterBinding.tvRefInfo.setText(Html.fromHtml("<font color='#000'>"+ "<b>"+"Reference Info :"+ "</b>"+  all_category_subcategory.get(position).getReferenceInfo()    +"</font>")  );
        holder.progressAdapterBinding.tvTemps.setText(Html.fromHtml("<font color='#000'>"+"<b>"+"Temps :" + "</b>" + all_category_subcategory.get(position).getDatetime() +"</font>"));
        holder.progressAdapterBinding.tvAmount.setText(Html.fromHtml("<font color='#000'>" +  "<b>" +"Amount : "+ "</b>"  + all_category_subcategory.get(position).getLocalCurrency()+String.format("%.2f",Double.parseDouble(all_category_subcategory.get(position).getLocalPrice()))  + "</font>"));
        holder.progressAdapterBinding.tvWalletBal.setText(Html.fromHtml("<font color='#000'>" +  "<b>" +"New Balance : "+ "</b>"  +all_category_subcategory.get(position).getWalletLocalCurrency()+ String.format("%.2f",Double.parseDouble(all_category_subcategory.get(position).getWalletLocalPrice())) + "</font>"));



        if (all_category_subcategory.get(position).getTransactionType().equalsIgnoreCase("Wallet")) {
            holder.progressAdapterBinding.tvProductType.setText(Html.fromHtml("<font color='#000'>"+"<b>"+"Description : purchase"+ "</b>"+"</font>"));
            holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"  + "<b>"  + "Transaction type :" + "</b>" + "Wallet"+"</font>")  );
            holder.progressAdapterBinding.ivType.setImageResource(R.drawable.ic_payment);



        } else if (all_category_subcategory.get(position).getTransactionType().equalsIgnoreCase("Booking")) {
            if(all_category_subcategory.get(position).getOperateur().equalsIgnoreCase("AM"))
            {
                holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"+ "<b>" +  "Transaction type : " + "</b>"+"Airtel money number "  + all_category_subcategory.get(position).getClient()+ "</font>" ));

            }
            else if (all_category_subcategory.get(position).getOperateur().equalsIgnoreCase("MC"))
            {
                holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"+ "<b>" +  "Transaction type : " + "</b>"+"Moov money number "  + all_category_subcategory.get(position).getClient()+ "</font>" ));
            }

            else  {
                holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"  + "<b>"  + "Transaction type :" + "</b>" + "Wallet"+    "</font>")  );
            }

            holder.progressAdapterBinding.userName2.setText("To " + all_category_subcategory.get(position).getReferenceInfo());

            holder.progressAdapterBinding.tvProductType.setText(Html.fromHtml("<font color='#000'>"+"<b>"+"Description : purchase" +"</b>"+"</font>"));
            holder.progressAdapterBinding.tvTransId.setText(Html.fromHtml("<font color='#000'>"+ "<b>"+"Transaction ID :" + "</b>" + all_category_subcategory.get(position).getTransactionId()   +"</font>")  );
            holder.progressAdapterBinding.tvRefInfo.setText(Html.fromHtml("<font color='#000'>"+ "<b>"+"Reference Info :"+ "</b>"+  all_category_subcategory.get(position).getReferenceInfo()    +"</font>")  );
            holder.progressAdapterBinding.ivType.setImageResource(R.drawable.ic_payment);


        } else if (all_category_subcategory.get(position).getTransactionType().equalsIgnoreCase("AddMoney")) {
            holder.progressAdapterBinding.ivType.setImageResource(R.drawable.received);
            holder.progressAdapterBinding.userName2.setText("From " +all_category_subcategory.get(position).getTransactionBy());


            if(all_category_subcategory.get(position).getOperateur().equalsIgnoreCase("AM"))
                holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"+ "<b>" +  "Transaction type : " + "</b>"+"Airtel money number "  + all_category_subcategory.get(position).getClient()+ "</font>" ));

            else if (all_category_subcategory.get(position).getOperateur().equalsIgnoreCase("MC"))
                holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"+ "<b>" +  "Transaction type : " + "</b>"+"Moov money number "  + all_category_subcategory.get(position).getClient()+ "</font>" ));




            holder.progressAdapterBinding.tvProductType.setText(Html.fromHtml("<font color='#000'>"+ "<b>"+"Description : recharge"+ "</b>"+"</font>"));
            holder.progressAdapterBinding.rlOne.setVisibility(View.VISIBLE);
            holder.progressAdapterBinding.rlTwo.setVisibility(View.GONE);


        }

        else if (all_category_subcategory.get(position).getTransactionType().equalsIgnoreCase("Withdraw")) {
            holder.progressAdapterBinding.ivType.setImageResource(R.drawable.withdraw);
            holder.progressAdapterBinding.userName2.setText("To " + " withdraw money");
            holder.progressAdapterBinding.tvProductType.setText(Html.fromHtml("<font color='#000'>"+ "<b>"+"Description : withdraw"+ "</b>"+"</font>"));
            holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"  + "<b>"  + "Transaction type :" + "</b>" + "wallet"+"</font>")  );
            //    holder.progressAdapterBinding.userName2.setText("To " + all_category_subcategory.get(position).getReferenceInfo());

            //  holder.progressAdapterBinding.rlOne.setVisibility(View.VISIBLE);
            //  holder.progressAdapterBinding.rlTwo.setVisibility(View.GONE);


        }

        else if (all_category_subcategory.get(position).getTransactionType().equalsIgnoreCase("TransferMoney")) {
            holder.progressAdapterBinding.ivType.setImageResource(R.drawable.withdraw);
            holder.progressAdapterBinding.userName2.setText("To " + all_category_subcategory.get(position).getReferenceInfo());
            holder.progressAdapterBinding.tvProductType.setText(Html.fromHtml("<font color='#000'>"+ "<b>"+"Description : transfer"+ "</b>"+"</font>"));
            holder.progressAdapterBinding.tvTransactonType.setText(Html.fromHtml("<font color='#000'>"  + "<b>"  + "Transaction type :" + "</b>" + "wallet "+"</font>")  );
            holder.progressAdapterBinding.userName2.setText("To " + all_category_subcategory.get(position).getReferenceInfo());

            //  holder.progressAdapterBinding.rlOne.setVisibility(View.VISIBLE);
            //  holder.progressAdapterBinding.rlTwo.setVisibility(View.GONE);


        }


        if(all_category_subcategory.get(position).isChk()==true){

            holder.progressAdapterBinding.ivExpen.setImageResource(R.drawable.ic_expen_minus);
            holder.progressAdapterBinding.rlDetail.setVisibility(View.GONE);
            holder.progressAdapterBinding.rlTwo.setVisibility(View.VISIBLE);

        }
        else {

            holder.progressAdapterBinding.ivExpen.setImageResource(R.drawable.ic_expen_plus);
            holder.progressAdapterBinding.rlDetail.setVisibility(View.VISIBLE);
            holder.progressAdapterBinding.rlTwo.setVisibility(View.GONE);

        }

    }

    @Override
    public int getItemCount() {
        return all_category_subcategory.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemWalletBinding progressAdapterBinding;

        public MyViewHolder(ItemWalletBinding itemView) {
            super(itemView.getRoot());
            progressAdapterBinding = itemView;

            progressAdapterBinding.ivExpen.setOnClickListener(view -> {
                if(all_category_subcategory.get(getAdapterPosition()).isChk()==false)
                    all_category_subcategory.get(getAdapterPosition()).setChk(true);
                else all_category_subcategory.get(getAdapterPosition()).setChk(false);
                notifyDataSetChanged();

            });

            progressAdapterBinding.tvTransId.setOnClickListener(view -> setClipboard(activity,all_category_subcategory.get(getAdapterPosition()).getTransactionId()));

        }
    }


    private void setClipboard(Context context, String text) {
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }

        Toast.makeText(activity,"copy...",Toast.LENGTH_SHORT).show();
    }

}
