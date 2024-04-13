package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.models.RaiseComplaintModel;

import java.util.ArrayList;

public class RaiseComplaintReportAdapter extends RecyclerView.Adapter<RaiseComplaintReportAdapter.ViewHolder> {

    Context context;
    ArrayList<RaiseComplaintModel> list;

    public RaiseComplaintReportAdapter(Context context, ArrayList<RaiseComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RaiseComplaintReportAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.raise_complaint_report_item, parent, false);
        return new RaiseComplaintReportAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RaiseComplaintReportAdapter.ViewHolder holder, int position) {
        RaiseComplaintModel model = list.get(position);
        holder.description.setText(model.getDescription());
        holder.tag.setText(model.getTag());
        holder.dealer.setText(model.getDealer());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dealer, description, tag;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dealer = itemView.findViewById(R.id.raise_Complaint_dealer);
            description = itemView.findViewById(R.id.raise_Complaint_description);
            tag = itemView.findViewById(R.id.raise_Complaint_tag);
        }
    }
}
