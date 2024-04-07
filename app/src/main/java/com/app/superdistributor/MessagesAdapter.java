package com.app.superdistributor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

//import kotlinx.coroutines.channels.Send;
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.MyViewHolder> {

    String username;
    Context context;
    ArrayList<MessageModel> list;

static final int SENDER_TYPE= 0;
static final int RECEIVER_TYPE= 1;
    public MessagesAdapter(String username, Context context, ArrayList<MessageModel> list) {
        this.username = username;
        this.context = context;
        this.list = list != null ? list : new ArrayList<>();
    }

    @NonNull
    @Override
    public MessagesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == SENDER_TYPE) view = LayoutInflater.from(context).inflate(R.layout.sender_message_item, parent, false);
        else view = LayoutInflater.from(context).inflate(R.layout.reciever_message_item, parent, false);
        return new MessagesAdapter.MyViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        // Determine sender or receiver type based on message sender
        MessageModel message = list.get(position);
        return message.getSender().equals(username) ? SENDER_TYPE : RECEIVER_TYPE;
    }
    @Override
    public void onBindViewHolder(@NonNull MessagesAdapter.MyViewHolder holder, int position) {
        MessageModel messageModel = list.get(position);
        if (list.isEmpty())
            Toast.makeText(context, messageModel.toString(), Toast.LENGTH_SHORT).show();
        holder.senderNameTv.setText(messageModel.getSender());
        holder.messageBodyTv.setText(messageModel.getMessage());
        if (messageModel.getTimestamp() == null) {
            holder.dateTimeTv.setVisibility(View.GONE);
        } else {
            holder.dateTimeTv.setVisibility(View.VISIBLE);
            holder.dateTimeTv.setText(messageModel.getTimestamp().toString());
        }
        holder.markAsReadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        if(messageModel.getSender().equals(username)){
            holder.inner_cont.setBackgroundColor(ContextCompat.getColor(context,R.color.sender_msg));
        }else{
            holder.inner_cont.setBackgroundColor(ContextCompat.getColor(context,R.color.reciever_msg));

        }

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView senderNameTv, messageBodyTv, dateTimeTv;
        Button markAsReadBtn;
        ConstraintLayout inner_cont;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            senderNameTv = itemView.findViewById(R.id.sender);
            messageBodyTv = itemView.findViewById(R.id.message_body);
            dateTimeTv = itemView.findViewById(R.id.date_timeTv);
            markAsReadBtn = itemView.findViewById(R.id.read_btn);
            inner_cont = itemView.findViewById(R.id.inner_cont);
        }
    }
}
