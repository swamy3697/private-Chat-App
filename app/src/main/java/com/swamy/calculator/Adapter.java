package com.swamy.calculator;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.swamy.calculator.Database.Message;

import java.util.ArrayList;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.MViewHolder> {
    Context context;
    List<Message> messages;

    public Adapter(Context context) {
        this.context = context;
        this.messages=new ArrayList<>();

    }

    @NonNull
    @Override
    public MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.message,parent,false);

        return new MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MViewHolder holder, int position) {

        Message message=messages.get(position);
        holder.message.setText(message.getMessage());
        holder.timeStamp.setText(message.getTime());
        holder.readReceipt.setText("😎");
        if(message.isMe()){
            holder.messageLayout.setGravity(Gravity.END);
        }
        else {
            holder.messageLayout.setGravity(Gravity.START);
        }



    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public static class MViewHolder  extends RecyclerView.ViewHolder{
        TextView message;
        TextView timeStamp;
        TextView readReceipt;
        LinearLayout messageLayout;

        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            message=itemView.findViewById(R.id.messageView);
            timeStamp=itemView.findViewById(R.id.timeStamp);
            readReceipt=itemView.findViewById(R.id.readReceipt);
            messageLayout=itemView.findViewById(R.id.messageLayout);
        }
    }
    public void insertMessge(Message message){


        this.messages.add(message);
        notifyItemInserted(this.messages.size());

    }
}
