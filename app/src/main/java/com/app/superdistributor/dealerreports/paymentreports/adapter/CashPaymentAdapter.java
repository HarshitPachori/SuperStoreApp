package com.app.superdistributor.dealerreports.paymentreports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.admin.paymenthistory.AccountBalanceOverviewAdapter;
import com.app.superdistributor.admin.paymenthistory.AmountOverviewModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.CashPaymentModel;

import java.util.ArrayList;

public class CashPaymentAdapter extends RecyclerView.Adapter<CashPaymentAdapter.MyViewHolder>{

    Context context;
    ArrayList<CashPaymentModel> list;
    public CashPaymentAdapter(Context context, ArrayList<CashPaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CashPaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cash_payment_history_item,parent,false);
        return  new CashPaymentAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CashPaymentAdapter.MyViewHolder holder, int position) {
        CashPaymentModel cashPaymentModel = list.get(position);
        holder.CashPaymentAmountTV.setText("Amount: "+cashPaymentModel.getAmount());
        holder.CashPaymentUserTV.setText("User: "+cashPaymentModel.getUser());
        holder.CashPaymentUserTypeTV.setText("User Type: "+cashPaymentModel.getUserType());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView CashPaymentAmountTV, CashPaymentUserTV, CashPaymentUserTypeTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            CashPaymentAmountTV = itemView.findViewById(R.id.cashpaymentamountTV);
            CashPaymentUserTV = itemView.findViewById(R.id.cashpaymentuserTV);
            CashPaymentUserTypeTV = itemView.findViewById(R.id.cashpaymentusertypeTV);
        }
    }
}
