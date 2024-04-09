package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.models.PostMessageModel;

import java.util.ArrayList;

public class PostMessageAdapter extends RecyclerView.Adapter<PostMessageAdapter.ViewHolder> {

    Context context;
    ArrayList<PostMessageModel> list;

    public PostMessageAdapter(Context context, ArrayList<PostMessageModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PostMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sr_post_msg_report_item, parent, false);
        return new PostMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostMessageAdapter.ViewHolder holder, int position) {
        PostMessageModel model = list.get(position);
        holder.messageTv.setText(model.getMessage());
        holder.dealersTv.setText(model.getDealers().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTv, dealersTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTv = itemView.findViewById(R.id.post_msg_name);
            dealersTv = itemView.findViewById(R.id.post_msg_to_dealer);
        }
    }
}
