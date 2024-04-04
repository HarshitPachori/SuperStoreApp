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
import com.app.superdistributor.dealerreports.paymentreports.beans.ChequePaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.QRPaymentModel;

import java.util.ArrayList;

public class QRPaymentAdapter extends RecyclerView.Adapter<QRPaymentAdapter.MyViewHolder>{

    Context context;
    ArrayList<QRPaymentModel> list;
    public QRPaymentAdapter(Context context, ArrayList<QRPaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QRPaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.qr_payment_history_item,parent,false);
        return  new QRPaymentAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull QRPaymentAdapter.MyViewHolder holder, int position) {
        QRPaymentModel qrPaymentModel = list.get(position);
        holder.QRAmountUserTV.setText("User: "+qrPaymentModel.getAmount());

        holder.QRPaymentScreenshotTV.setMovementMethod(LinkMovementMethod.getInstance());
        holder.QRPaymentScreenshotTV.setText(Html.fromHtml("Screenshot: "+"<a href='"+qrPaymentModel.getPaymentScreenshotUrl()+"'> Click Here </a>"));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView QRAmountUserTV, QRPaymentScreenshotTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            QRAmountUserTV = itemView.findViewById(R.id.qrpaymentamountTV);
            QRPaymentScreenshotTV = itemView.findViewById(R.id.qrpaymentscreentshotTV);

        }
    }
}
