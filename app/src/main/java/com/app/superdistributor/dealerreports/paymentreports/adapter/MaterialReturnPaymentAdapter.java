package com.app.superdistributor.dealerreports.paymentreports.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.dealerreports.paymentreports.beans.BTPaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.MaterialReturnPaymentModel;

import java.util.ArrayList;

public class MaterialReturnPaymentAdapter extends RecyclerView.Adapter<MaterialReturnPaymentAdapter.MyViewHolder>{

    Context context;
    ArrayList<MaterialReturnPaymentModel> list;
    public MaterialReturnPaymentAdapter(Context context, ArrayList<MaterialReturnPaymentModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MaterialReturnPaymentAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.mr_payment_history_item,parent,false);
        return  new MaterialReturnPaymentAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MaterialReturnPaymentAdapter.MyViewHolder holder, int position) {
        MaterialReturnPaymentModel materialReturnPaymentModel = list.get(position);
        holder.MaterialReturnDescriptionTV.setText("Description: "+materialReturnPaymentModel.getDescription());
        holder.MaterialReturnValueTV.setText("Value: "+materialReturnPaymentModel.getValue());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView MaterialReturnDescriptionTV, MaterialReturnValueTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            MaterialReturnDescriptionTV = itemView.findViewById(R.id.descriptionmaterialreturnTV);
            MaterialReturnValueTV = itemView.findViewById(R.id.valuematerialreturnTV);
        }
    }
}
