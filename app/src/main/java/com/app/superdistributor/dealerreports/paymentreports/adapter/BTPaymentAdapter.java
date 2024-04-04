package com.app.superdistributor.dealerreports.paymentreports.adapter;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.dealerreports.paymentreports.beans.BTPaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.ChequePaymentModel;

import java.util.ArrayList;

public class BTPaymentAdapter extends RecyclerView.Adapter<BTPaymentAdapter.MyViewHolder>{

    Context context;
    ArrayList<BTPaymentModel> list;
    public BTPaymentAdapter(Context context, ArrayList<BTPaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public BTPaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.bt_payment_history_item,parent,false);
        return  new BTPaymentAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull BTPaymentAdapter.MyViewHolder holder, int position) {
        BTPaymentModel btPaymentModel = list.get(position);
        holder.BTAmountTV.setText("Amount: "+btPaymentModel.getAmount());
        holder.BtPaymentDateTV.setText("Date: "+btPaymentModel.getDate());

        holder.BTScreenshotTV.setMovementMethod(LinkMovementMethod.getInstance());
        holder.BTScreenshotTV.setText(Html.fromHtml("Screenshot: "+"<a href='"+btPaymentModel.getTransactionScreenshotUrl()+"'> Click Here </a>"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView BTAmountTV, BtPaymentDateTV, BTScreenshotTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            BTAmountTV = itemView.findViewById(R.id.btamountTV);
            BtPaymentDateTV = itemView.findViewById(R.id.btdateTV);
            BTScreenshotTV = itemView.findViewById(R.id.btscreentshotTV);


        }
    }
}
