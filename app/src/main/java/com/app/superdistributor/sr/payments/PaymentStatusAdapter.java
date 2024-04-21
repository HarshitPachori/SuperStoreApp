package com.app.superdistributor.sr.payments;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PaymentStatusAdapter extends RecyclerView.Adapter<PaymentStatusAdapter.MyViewHolder> {

    DatabaseReference database;
    String username = "";
    Context context;
    ArrayList<PaymentStatusModel> list;


    public PaymentStatusAdapter(Context context, ArrayList<PaymentStatusModel> list) {
        this.context = context;
        this.list = list;
    }

    public PaymentStatusAdapter(Context context, ArrayList<PaymentStatusModel> list, String username) {
        this.context = context;
        this.list = list;
        this.username = username;
    }

    @NonNull
    @Override
    public PaymentStatusAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sr_payment_status_item, parent, false);
        return new PaymentStatusAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PaymentStatusAdapter.MyViewHolder holder, int position) {
        database = FirebaseDatabase.getInstance().getReference();
        PaymentStatusModel paymentStatusModel = list.get(position);
        holder.roundedTv.setText(paymentStatusModel.getName().substring(0, 1).toUpperCase());
        holder.name.setText(paymentStatusModel.getName());
        holder.type.setText(paymentStatusModel.getType());
        holder.amount.setText(paymentStatusModel.getAmount());
        holder.status.setText(paymentStatusModel.getStatus());
        if (paymentStatusModel.getStatus().equals("Approved")) {
            holder.status.setBackgroundColor(Color.parseColor("#43A047"));
        } else {
            holder.status.setBackgroundColor(Color.parseColor("#FFB300"));
        }
        if (!username.equals("")) {
            holder.status.setClickable(true);
            holder.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String, Object> temp = new HashMap<>();
                    temp.put("Status", "Approved");
                    database.child("SRs").child(username)
                            .child("myPayments").child(paymentStatusModel.getName())
                            .updateChildren(temp)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    holder.status.setBackgroundColor(Color.parseColor("#43A047"));
                                    holder.status.setText("Approved");
                                }
                            });

                    database.child("SRs").child(username).child("SRSSalesStatus").child("SalesDone").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("dataa", snapshot.toString());
                            Integer currentValue = snapshot.getValue(Integer.class);
                            if (currentValue == null) {
                                currentValue = 0;
                            }
                            database.child("SRs").child(username).child("SRSSalesStatus").child("SalesDone")
                                    .setValue(currentValue + Integer.parseInt(paymentStatusModel.getAmount()));
                            int salesDone = currentValue + Integer.parseInt(paymentStatusModel.getAmount());
                            database.child("SRs").child(username).child("SRSSalesStatus").child("TargetAmount").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.d("dataa", snapshot.toString());
                                    int targetAmount = snapshot.getValue(Integer.class);
                                    database.child("SRs").child(username).child("SRSSalesStatus").child("RemainingTarget").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            Log.d("dataa", snapshot.toString());
                                            Integer currentValue = snapshot.getValue(Integer.class);
                                            if (currentValue == null) {
                                                currentValue = 0;
                                            }
                                            database.child("SRs").child(username).child("SRSSalesStatus").child("RemainingTarget")
                                                    .setValue(targetAmount - salesDone);
                                        }


                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }


                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, type, amount, roundedTv;
        Button status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.paymentNameTv);
            type = itemView.findViewById(R.id.typeTv);
            amount = itemView.findViewById(R.id.amountTv);
            roundedTv = itemView.findViewById(R.id.roundedNameTv);
            status = itemView.findViewById(R.id.statusBtn);
        }
    }

}
