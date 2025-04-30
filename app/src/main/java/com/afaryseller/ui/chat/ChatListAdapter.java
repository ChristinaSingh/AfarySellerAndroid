package com.afaryseller.ui.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.afaryseller.R;
import com.afaryseller.databinding.ItemChatListBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.MyViewHolder> {
    Context context;
    ArrayList<ChatListModel.Result>arrayList;
    ChatOnListener listener;

    public ChatListAdapter(Context context, ArrayList<ChatListModel.Result>arrayList, ChatOnListener listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemChatListBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_chat_list,parent,false);
        return new MyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.binding.tvName.setText(arrayList.get(position).getUserName());
        holder.binding.tvMsg.setText(arrayList.get(position).getLastChatMessage());
      //  holder.binding.tvTime.setText(arrayList.get(position).getName());
        Glide.with(context).load(arrayList.get(position).getImage()).error(R.drawable.user_default).
                override(50,50).into(holder.binding.img1);

        holder.itemView.setOnClickListener(view -> {
            listener.onChat(position,arrayList.get(position));
        });

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ItemChatListBinding binding;
        public MyViewHolder(@NonNull ItemChatListBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;

        }
    }
}
