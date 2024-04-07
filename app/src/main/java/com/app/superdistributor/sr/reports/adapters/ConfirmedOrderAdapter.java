package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.models.ConfirmedOrderModel;

import java.util.ArrayList;

public class ConfirmedOrderAdapter extends RecyclerView.Adapter<ConfirmedOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<ConfirmedOrderModel> list;

    public ConfirmedOrderAdapter(Context context, ArrayList<ConfirmedOrderModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ConfirmedOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_order_item, parent, false);
        return new ConfirmedOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmedOrderAdapter.ViewHolder holder, int position) {
        ConfirmedOrderModel model = list.get(position);
        holder.CustomerName.setText(model.getName());
        holder.Status.setText(model.getStatus());
        holder.PlacedBy.setText(model.getDealerName());
        holder.ProductId.setText(model.getProductID());
        holder.Qty.setText(model.getProductQty());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView CustomerName,  PlacedBy, Status, Qty, ProductId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CustomerName = itemView.findViewById(R.id.confirm_order_name);
            Status = itemView.findViewById(R.id.confirm_order_status);
            PlacedBy = itemView.findViewById(R.id.confimorderplacedby);
            Qty = itemView.findViewById(R.id.confirm_order_qty);
            ProductId = itemView.findViewById(R.id.confirmorderProductID);
        }
    }
}
