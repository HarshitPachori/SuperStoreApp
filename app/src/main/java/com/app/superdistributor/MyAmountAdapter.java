package com.app.superdistributor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.models.AmountModel;

import java.util.ArrayList;

public class MyAmountAdapter extends RecyclerView.Adapter<MyAmountAdapter.MyViewHolder>{

    Context context;
    ArrayList<AmountModel> list;

    public MyAmountAdapter(Context context, ArrayList<AmountModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyAmountAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.amount_item,parent,false);
        return  new MyAmountAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAmountAdapter.MyViewHolder holder, int position) {
        AmountModel amountModel = list.get(position);

        //amount
        if(amountModel.getAmount().contains("-"))
        {

            holder.YouGaveAmountTv.setText("\u20B9 "+amountModel.getAmount().substring(1,amountModel.getAmount().length()));
        }
        else
        {
            holder.YouGotAmountTv.setText("\u20B9 "+amountModel.getAmount());
        }
        // balance
        if(amountModel.getBalance().contains("-"))
        {

            holder.Balance.setText("\u20B9 "+amountModel.getBalance().substring(1,amountModel.getBalance().length()));
        }
        //date
        else
        {
            holder.Balance.setText("\u20B9 "+amountModel.getBalance());
        }

        holder.DateTv.setText(amountModel.getDate()+ " "+ amountModel.getTime());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView YouGaveAmountTv, YouGotAmountTv, Balance, DateTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            YouGaveAmountTv = itemView.findViewById(R.id.yougaveamountTV);
            YouGotAmountTv = itemView.findViewById(R.id.yougotamountTV);
            Balance = itemView.findViewById(R.id.balanceTV);
            DateTv = itemView.findViewById(R.id.date);
        }
    }
}
