package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.ConfirmedOrderReport;
import com.app.superdistributor.sr.reports.models.ConfirmedOrderModel;
import com.app.superdistributor.sr.reports.models.ReplaceByDealerModel;

import java.util.ArrayList;

public class ConfirmedOrderAdapter extends RecyclerView.Adapter<ConfirmedOrderAdapter.ViewHolder> {
    Context context;
    ArrayList<ConfirmedOrderModel> list,filterList;

    public ConfirmedOrderAdapter(Context context, ArrayList<ConfirmedOrderModel> list) {
        this.context = context;
        this.list = list;
        this.filterList = new ArrayList<>(list);
    }

    public void filter(String date) {
        filterList.clear();
        for (ConfirmedOrderModel item : list) {
            Log.d("item",item.getTimestamp());
            Log.d("item",date);
            if (item.getTimestamp().split(" ")[0].toLowerCase().contains(date.toLowerCase())) {
                filterList.add(item);
            }
        }
        Log.d("item",filterList.toString());
        Log.d("item",list.toString());
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConfirmedOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.confirm_order_item, parent, false);
        return new ConfirmedOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConfirmedOrderAdapter.ViewHolder holder, int position) {
        ConfirmedOrderModel model = filterList.get(position);
        holder.CustomerName.setText(model.getName());
        holder.Status.setText(model.getStatus());
        holder.PlacedBy.setText(model.getDealerName());
        holder.ProductId.setText(model.getProductID());
        holder.Qty.setText(model.getProductQty());
        holder.date.setText(model
                .getTimestamp().split(" ")[0]);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView CustomerName,  PlacedBy, Status, Qty, ProductId,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CustomerName = itemView.findViewById(R.id.confirm_order_name);
            Status = itemView.findViewById(R.id.confirm_order_status);
            PlacedBy = itemView.findViewById(R.id.confimorderplacedby);
            Qty = itemView.findViewById(R.id.confirm_order_qty);
            ProductId = itemView.findViewById(R.id.confirmorderProductID);
            date = itemView.findViewById(R.id.confimorderplaceddate);
        }
    }
}
