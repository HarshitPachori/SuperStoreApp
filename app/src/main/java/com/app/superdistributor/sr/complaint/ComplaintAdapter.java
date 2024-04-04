package com.app.superdistributor.sr.complaint;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.MyViewHolder> {


    Context context;
    ArrayList<ComplaintModel> list;

    public ComplaintAdapter(Context context, ArrayList<ComplaintModel> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ComplaintAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ComplaintAdapter.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.complaint_item, parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ComplaintAdapter.MyViewHolder holder, int position) {
        holder.dealer.setText(list.get(position).getDealer());
        holder.tag.setText(list.get(position).getTag());
        holder.desc.setText(list.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView tag, dealer, desc;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dealer = itemView.findViewById(R.id.dealer_name);
            tag = itemView.findViewById(R.id.tag);
            desc = itemView.findViewById(R.id.complaint_desc);
        }
    }
}
