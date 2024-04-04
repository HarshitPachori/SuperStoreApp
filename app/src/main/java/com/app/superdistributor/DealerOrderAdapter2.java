package com.app.superdistributor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.sr.dealerorders.DealerOrder;
import com.app.superdistributor.sr.dealerorders.DealerOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DealerOrderAdapter2 extends RecyclerView.Adapter<DealerOrderAdapter2.MyViewHolder>{

    Context context;
    ArrayList<DealerOrder> list;
    DatabaseReference databaseReference;
    String technicianUsername;

    public DealerOrderAdapter2(Context context, ArrayList<DealerOrder> list, String technicianUsername){
        this.context = context;
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.technicianUsername = technicianUsername;
    }

    @NonNull
    @Override
    public DealerOrderAdapter2.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.dealer_order_item_static, parent, false );
        return new DealerOrderAdapter2.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DealerOrderAdapter2.MyViewHolder holder, int position) {
        DealerOrder dealerOrder = list.get(position);

        databaseReference.child("Dealers").child(dealerOrder.getDealerName())
                .child("Orders").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                holder.orderIdEt.setText(dealerOrder.getProductID());
                                holder.orderNameEt.setText(dealerOrder.getName());
                                holder.dealerNameEt.setText("by " + dealerOrder.getDealerName());
                                holder.quantityEt.setText("Quantity : " + dealerOrder.getProductQty());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {}
                        }
                );
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView orderIdEt, orderNameEt, dealerNameEt, quantityEt;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdEt = itemView.findViewById(R.id.order_id);
            orderNameEt = itemView.findViewById(R.id.order_name);
            dealerNameEt = itemView.findViewById(R.id.dealer_name);
            quantityEt = itemView.findViewById(R.id.qty);
        }
    }
}
