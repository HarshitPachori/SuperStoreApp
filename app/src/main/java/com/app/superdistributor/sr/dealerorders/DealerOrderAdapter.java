package com.app.superdistributor.sr.dealerorders;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.MyProducts.MyProductAdapter;
import com.app.superdistributor.MyProducts.Products;
import com.app.superdistributor.R;
import com.app.superdistributor.SRHomeActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DealerOrderAdapter extends RecyclerView.Adapter<DealerOrderAdapter.MyViewHolder>{

    Context context;
    ArrayList<DealerOrder> list;
    DatabaseReference database;
    String SRUsername;

    public DealerOrderAdapter(Context context, ArrayList<DealerOrder> list, String SRUsername) {
        this.context = context;
        this.list = list;
        database = FirebaseDatabase.getInstance().getReference();
        this.SRUsername = SRUsername;
    }

    @NonNull
    @Override
    public DealerOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.dealer_order_item,parent,false);
        return  new DealerOrderAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull DealerOrderAdapter.MyViewHolder holder, int position) {

        DealerOrder dealerOrder = list.get(position);

        database.child("Dealers").child(dealerOrder.getDealerName()).child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(snapshot.child(dealerOrder.getProductID()).child("Status").getValue().toString().equals("Open"))
                {
                        holder.DealerOrderProductName.setText(dealerOrder.getName());
                        holder.OrderPlaceByName.setText("Order Placed by - " + dealerOrder.getDealerName());
                        holder.DealerOrderQty.setText(dealerOrder.getProductQty());
                }
                else if(snapshot.child(dealerOrder.getProductID()).child("Status").getValue().toString().equals("Accepted"))
                {
                    holder.DealerOrderProductName.setText(dealerOrder.getName());
                    holder.OrderPlaceByName.setText("Order Placed by - " + dealerOrder.getDealerName()+"\nOrder Confirmed");
                    holder.DealerOrderQty.setEnabled(false);
                    holder.DealerOrderPrice.setEnabled(false);
                    holder.SubmitToAdminBtn.setEnabled(false);
                }
                else if(snapshot.child(dealerOrder.getProductID()).child("Status").getValue().toString().equals("Rejected"))
                {
                    holder.DealerOrderProductName.setText(dealerOrder.getName());
                    holder.OrderPlaceByName.setText("Order Placed by - "+dealerOrder.getDealerName()+"\nOrder Rejected");
                    holder.DealerOrderQty.setEnabled(false);
                    holder.DealerOrderPrice.setEnabled(false);
                    holder.SubmitToAdminBtn.setEnabled(false);
                }
                else
                {
                        holder.DealerOrderProductName.setText(dealerOrder.getName());
                        holder.OrderPlaceByName.setText("Order Placed by - "+dealerOrder.getDealerName()+"\nConfirmation Pending from Admin Side");
                        holder.DealerOrderQty.setEnabled(false);
                        holder.DealerOrderPrice.setEnabled(false);
                        holder.SubmitToAdminBtn.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        holder.SubmitToAdminBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Map<String, Object> orderConfirmationMap = new HashMap<>();
                orderConfirmationMap.put("Name", dealerOrder.getName());
                orderConfirmationMap.put("PlacedBy", dealerOrder.getDealerName());
                orderConfirmationMap.put("Qty", dealerOrder.getProductQty());
                orderConfirmationMap.put("Price", holder.DealerOrderPrice.getText().toString());
                orderConfirmationMap.put("ProductID", dealerOrder.getProductID());
                orderConfirmationMap.put("Reminder",false);
                orderConfirmationMap.put("Status","Pending");

                Map<String, Object> orderStatusMap = new HashMap<>();

                orderStatusMap.put("Status","Pending");



                database.child("Admin").child("Notifications").child("ProductConfirmation")
                        .child("SRs").child(SRUsername).updateChildren(orderConfirmationMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.child("Dealers").child(dealerOrder.getDealerName()).child("Orders")
                                        .child(dealerOrder.getProductID()).updateChildren(orderStatusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context, "Order Submitted to Admin", Toast.LENGTH_SHORT).show();
//                                                holder.OrderPlaceByName.setText("Order Placed by - "+dealerOrder.getDealerName()+"\nConfirmation Pending from Admin Side");
//                                                holder.DealerOrderQty.setEnabled(false);
//                                                holder.DealerOrderPrice.setEnabled(false);
//                                                holder.SubmitToAdminBtn.setEnabled(false);
                                                Intent i = new Intent(context.getApplicationContext(), SRHomeActivity.class);
                                                i.putExtra("SRUsername",SRUsername);
                                                context.startActivity(i);
                                            }
                                        });

                            }
                        });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView DealerOrderProductName, OrderPlaceByName;
        Button SubmitToAdminBtn;

        TextInputEditText DealerOrderQty, DealerOrderPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            OrderPlaceByName = itemView.findViewById(R.id.placedByName);
            DealerOrderProductName = itemView.findViewById(R.id.dealerOrderProductName);
            SubmitToAdminBtn = itemView.findViewById(R.id.submitToAdmin);
            DealerOrderQty = itemView.findViewById(R.id.dealerOrderQty);
            DealerOrderPrice = itemView.findViewById(R.id.dealerOrderPrice);

        }
    }
}
