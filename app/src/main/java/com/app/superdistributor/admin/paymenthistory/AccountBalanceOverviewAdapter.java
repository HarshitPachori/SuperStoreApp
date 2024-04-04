package com.app.superdistributor.admin.paymenthistory;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.MyAmountAdapter;
import com.app.superdistributor.R;
import com.app.superdistributor.SrCreditDebitActivity;
import com.app.superdistributor.SrGiveGetAmountActivity;
import com.app.superdistributor.admin.AddDebitCreditActivity;
import com.app.superdistributor.admin.GiveGetAmountActivity;

import java.util.ArrayList;

public class AccountBalanceOverviewAdapter extends RecyclerView.Adapter<AccountBalanceOverviewAdapter.MyViewHolder>{
    Context context;
    ArrayList<AmountOverviewModel> list;

    public AccountBalanceOverviewAdapter(Context context, ArrayList<AmountOverviewModel> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public AccountBalanceOverviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.balance_overview_item,parent,false);
        return  new AccountBalanceOverviewAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountBalanceOverviewAdapter.MyViewHolder holder, int position) {
        AmountOverviewModel amountOverviewModel = list.get(position);
        holder.NameTv.setText(amountOverviewModel.getName());
        holder.CurrentBalanceTv.setText("\u20B9 " + Integer.parseInt(amountOverviewModel.getCurrentBalance()));
        holder.RoundedTv.setText(amountOverviewModel.getName().substring(0,1).toUpperCase());
        holder.UserNameTv.setText(amountOverviewModel.getUserName());

        if(Integer.parseInt(amountOverviewModel.getCurrentBalance()) < 0)
        {
            holder.CurrentBalanceTv.setTextColor(Color.parseColor("#FF0000"));
        }
        else if(Integer.parseInt(amountOverviewModel.getCurrentBalance()) == 0)
        {
            holder.CurrentBalanceTv.setTextColor(Color.parseColor("#808080"));
        }
        else
        {
            holder.CurrentBalanceTv.setTextColor(Color.parseColor("#3CB043"));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context,context.toString().substring(25,46), Toast.LENGTH_SHORT).show();
                Intent i;
                if (context.toString().substring(25,46).equals("SrCreditDebitActivity")){
                    i = new Intent(context.getApplicationContext(), SrGiveGetAmountActivity.class);
                    i.putExtra("SrUsername", SrCreditDebitActivity.Username);
                    i.putExtra("dealerUsername", amountOverviewModel.getUserName());
                }
                else {
                    i = new Intent(context.getApplicationContext(), GiveGetAmountActivity.class);
                    i.putExtra("dealerUsername", amountOverviewModel.getUserName());
                    i.putExtra("dealerName", amountOverviewModel.getName());
                    i.putExtra("Username", AddDebitCreditActivity.Username);
                }
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView NameTv, UserNameTv, CurrentBalanceTv, RoundedTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            NameTv = itemView.findViewById(R.id.balanaceOverviewName);
            UserNameTv = itemView.findViewById(R.id.balanceOverviewUsername);
            CurrentBalanceTv = itemView.findViewById(R.id.overviewOutstandingBalance);
            RoundedTv = itemView.findViewById(R.id.roundedNameTv);
        }
    }
}
