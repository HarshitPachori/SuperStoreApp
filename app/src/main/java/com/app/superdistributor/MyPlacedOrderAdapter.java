package com.app.superdistributor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.models.PlacedOrderModel;

import java.util.ArrayList;

public class MyPlacedOrderAdapter extends RecyclerView.Adapter<MyPlacedOrderAdapter.MyViewHolder> {

    Context context;
    ArrayList<PlacedOrderModel> list;

    public MyPlacedOrderAdapter(Context context, ArrayList<PlacedOrderModel> list) {
        this.context = context;
        this.list = list;
    }


    @NonNull
    @Override
    public MyPlacedOrderAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.placed_order_item, parent, false);
        return new MyPlacedOrderAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyPlacedOrderAdapter.MyViewHolder holder, int position) {
        PlacedOrderModel orderModel = list.get(position);
        holder.DealerNameTv.setText(orderModel.getDealerName());
        holder.ProductNameTv.setText(orderModel.getName());
        holder.ProductIDTv.setText(orderModel.getProductID());
        holder.ProductQtyTv.setText(orderModel.getProductQty());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView DealerNameTv, ProductNameTv, ProductIDTv, ProductQtyTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            DealerNameTv = itemView.findViewById(R.id.orderDealerName);
            ProductNameTv = itemView.findViewById(R.id.orderProductName);
            ProductIDTv = itemView.findViewById(R.id.orderProductID);
            ProductQtyTv = itemView.findViewById(R.id.orderProductQty);
        }

    }
}
