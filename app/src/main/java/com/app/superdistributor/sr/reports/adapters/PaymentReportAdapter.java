package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.models.PaymentModel;

import java.util.ArrayList;

public class PaymentReportAdapter extends RecyclerView.Adapter<PaymentReportAdapter.ViewHolder> {

    Context context;
    ArrayList<PaymentModel> list;

    public PaymentReportAdapter(Context context, ArrayList<PaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public PaymentReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_report_item, parent, false);
        return new PaymentReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentReportAdapter.ViewHolder holder, int position) {

        PaymentModel model = list.get(position);
        holder.paymentMethod.setText(model.getPaymentMethod());
        holder.user.setText(model.getUser());
        holder.usertype.setText(model.getUserType());
        holder.amount.setText(model.getAmount());
        holder.status.setText(model.getStatus());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView amount, usertype, user, paymentMethod, status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.payment_amount);
            usertype = itemView.findViewById(R.id.payment_usertype);
            user = itemView.findViewById(R.id.payment_user);
            status = itemView.findViewById(R.id.payment_status);
            paymentMethod = itemView.findViewById(R.id.payment_Method);
        }
    }
}
