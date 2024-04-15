package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.models.PostMessageModel;
import com.app.superdistributor.sr.reports.models.RaiseComplaintModel;

import java.util.ArrayList;

public class PostMessageAdapter extends RecyclerView.Adapter<PostMessageAdapter.ViewHolder> {

    Context context;
    ArrayList<PostMessageModel> list,filterList;

    public PostMessageAdapter(Context context, ArrayList<PostMessageModel> list) {
        this.context = context;
        this.list = list;
        this.filterList = new ArrayList<>(list);
    }
    public void filter(String date) {
        filterList.clear();
        for (PostMessageModel item : list) {
            Log.d("item",item.getDate());
            Log.d("item",date);
            if (item.getDate().toLowerCase().contains(date.toLowerCase())) {
                filterList.add(item);
            }
        }
        Log.d("item",filterList.toString());
        Log.d("item",list.toString());
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public PostMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sr_post_msg_report_item, parent, false);
        return new PostMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostMessageAdapter.ViewHolder holder, int position) {
        PostMessageModel model = filterList.get(position);
        holder.messageTv.setText(model.getMessage());
        holder.dealersTv.setText(model.getDealers().toString());
        holder.date.setText(model.getDate());;
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageTv, dealersTv,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTv = itemView.findViewById(R.id.post_msg_name);
            dealersTv = itemView.findViewById(R.id.post_msg_to_dealer);
            date = itemView.findViewById(R.id.postmsgDate);
        }
    }
}
