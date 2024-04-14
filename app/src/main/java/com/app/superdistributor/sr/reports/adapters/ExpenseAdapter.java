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
import com.app.superdistributor.sr.reports.models.ConfirmedOrderModel;
import com.app.superdistributor.sr.reports.models.ExpenseModel;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    Context context;
    ArrayList<ExpenseModel> list,filterList;

    public ExpenseAdapter(Context context, ArrayList<ExpenseModel> list) {
        this.context = context;
        this.list = list;
        this.filterList = new ArrayList<>(list);
    }

    public void filter(String date) {
        filterList.clear();
        for (ExpenseModel item : list) {
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
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense_report_item, parent, false);
        return new ExpenseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        ExpenseModel model = filterList.get(position);
        holder.status.setText(model.getStatus());
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());
        holder.type.setText(model.getType());
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView type, amount, date, status,name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            type = itemView.findViewById(R.id.expense_type);
            amount = itemView.findViewById(R.id.expense_amount);
            date = itemView.findViewById(R.id.expense_date);
            status = itemView.findViewById(R.id.expense_status);
            name = itemView.findViewById(R.id.expense_doneby);
        }
    }
}
