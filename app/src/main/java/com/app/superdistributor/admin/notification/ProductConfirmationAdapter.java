package com.app.superdistributor.admin.notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.SRHomeActivity;
import com.app.superdistributor.sr.dealerorders.DealerOrder;
import com.app.superdistributor.sr.dealerorders.DealerOrderAdapter;
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

public class ProductConfirmationAdapter extends RecyclerView.Adapter<ProductConfirmationAdapter.MyViewHolder>{

    Context context;
    ArrayList<ProductConfirmationModel> list;
    DatabaseReference database;

    public ProductConfirmationAdapter(Context context, ArrayList<ProductConfirmationModel> list) {
        this.context = context;
        this.list = list;
        database = FirebaseDatabase.getInstance().getReference();
    }
    @NonNull
    @Override
    public ProductConfirmationAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.product_confirmation_notification_item,parent,false);
        return  new ProductConfirmationAdapter.MyViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull ProductConfirmationAdapter.MyViewHolder holder, int position) {

        ProductConfirmationModel productConfirmationModel = list.get(position);
        holder.ProductConfirmationName.setText("Product Name - "+productConfirmationModel.getName());
        holder.ProductConfirmationPlacedBy.setText("Order Placed By - "+productConfirmationModel.getPlacedBy());
        holder.ProductConfirmationPrice.setText("Product Price - "+productConfirmationModel.getPrice());
        holder.ProductConfirmationProductID.setText("Product ID - "+productConfirmationModel.getProductID());
        holder.ProductConfirmationQty.setText("Product Qty - "+productConfirmationModel.getQty());

        holder.ProductConfirmationAcceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("Dealers").child(productConfirmationModel.getPlacedBy()).child("Orders").
                        child(productConfirmationModel.getProductID()).child("Status").setValue("Accepted").
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.child("Admin").child("Notifications").child("ProductConfirmation").child("SRs")
                                        .child(productConfirmationModel.getPlacedBy()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context.getApplicationContext(), "Order Accepted...", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        });
            }
        });

        holder.ProductConfirmationRejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.child("Dealers").child(productConfirmationModel.getPlacedBy()).child("Orders").
                        child(productConfirmationModel.getProductID()).child("Status").setValue("Rejected").
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                database.child("Admin").child("Notifications").child("ProductConfirmation").child("SRs")
                                        .child(productConfirmationModel.getPlacedBy()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(context.getApplicationContext(), "Order Rejected...", Toast.LENGTH_SHORT).show();
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

        TextView ProductConfirmationName,ProductConfirmationPlacedBy,ProductConfirmationPrice,
                ProductConfirmationProductID,ProductConfirmationQty, ProductConfirmationAcceptBtn, ProductConfirmationRejectBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ProductConfirmationName = itemView.findViewById(R.id.productConfirmationName);
            ProductConfirmationPlacedBy = itemView.findViewById(R.id.productConfirmationPlacedBy);
            ProductConfirmationPrice = itemView.findViewById(R.id.productConfirmationPrice);
            ProductConfirmationProductID = itemView.findViewById(R.id.productConfirmationProductID);
            ProductConfirmationQty = itemView.findViewById(R.id.productConfirmationQty);
            ProductConfirmationAcceptBtn = itemView.findViewById(R.id.productConfirmationAcceptBtn);
            ProductConfirmationRejectBtn = itemView.findViewById(R.id.productConfirmationRejectBtn);


        }
    }
}
