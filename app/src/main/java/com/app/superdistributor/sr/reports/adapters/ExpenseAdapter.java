package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.models.ExpenseModel;

import java.util.ArrayList;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ViewHolder> {

    Context context;
    ArrayList<ExpenseModel> list;

    public ExpenseAdapter(Context context, ArrayList<ExpenseModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ExpenseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.expense_report_item, parent, false);
        return new ExpenseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseAdapter.ViewHolder holder, int position) {
        ExpenseModel model = list.get(position);
        holder.status.setText(model.getStatus());
        holder.amount.setText(model.getAmount());
        holder.date.setText(model.getDate());
        holder.type.setText(model.getType());
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
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
