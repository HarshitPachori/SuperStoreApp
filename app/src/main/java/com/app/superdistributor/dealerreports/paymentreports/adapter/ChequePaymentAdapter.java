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
import com.app.superdistributor.dealerreports.paymentreports.beans.CashPaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.ChequePaymentModel;

import java.util.ArrayList;

public class ChequePaymentAdapter extends RecyclerView.Adapter<ChequePaymentAdapter.MyViewHolder>{

    Context context;
    ArrayList<ChequePaymentModel> list;
    public ChequePaymentAdapter(Context context, ArrayList<ChequePaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ChequePaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.cheque_payment_history_item,parent,false);
        return  new ChequePaymentAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ChequePaymentAdapter.MyViewHolder holder, int position) {

        ChequePaymentModel chequePaymentModel = list.get(position);
        holder.ChequePaymentUserTV.setText("User: "+chequePaymentModel.getUser());
        holder.ChequePaymentUserTypeTV.setText("UserType: "+chequePaymentModel.getUserType());
        holder.ChequePaymentDateTV.setText("Date: "+chequePaymentModel.getDate());

        holder.ChequePaymentScreenshotTV.setMovementMethod(LinkMovementMethod.getInstance());
        holder.ChequePaymentScreenshotTV.setText(Html.fromHtml("Screenshot: "+"<a href='"+chequePaymentModel.getChequeScreenshotUrl()+"'> Click Here </a>"));
//        holder.ChequePaymentScreenshotTV.setText("Screenshot: "+);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ChequePaymentUserTV, ChequePaymentUserTypeTV, ChequePaymentDateTV, ChequePaymentScreenshotTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ChequePaymentUserTV = itemView.findViewById(R.id.chequepaymentuserTV);
            ChequePaymentUserTypeTV = itemView.findViewById(R.id.chequepaymentuserTypeTV);
            ChequePaymentDateTV = itemView.findViewById(R.id.chequepaymentdateTV);
            ChequePaymentScreenshotTV = itemView.findViewById(R.id.chequepaymentscreentshotTV);


        }
    }
}
