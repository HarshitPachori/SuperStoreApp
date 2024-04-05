package com.app.superdistributor.sr.reports.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.models.RegisteredComplaintModel;

import java.util.ArrayList;

public class RegisterComplaintAdapter extends RecyclerView.Adapter<RegisterComplaintAdapter.ViewHolder> {

    Context context;
    ArrayList<RegisteredComplaintModel> list;

    public RegisterComplaintAdapter(Context context, ArrayList<RegisteredComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RegisterComplaintAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.register_complaint_report_item, parent, false);
        return new RegisterComplaintAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RegisterComplaintAdapter.ViewHolder holder, int position) {
        RegisteredComplaintModel model = list.get(position);
        holder.CustomerName.setText(model.getCustomerName());
        holder.DateOfPurchase.setText(model.getDateOfPurchase());
        holder.ModelNumber.setText(model.getModelNumber());
        holder.PhoneNumber.setText(model.getPhoneNumber());

        holder.Status.setText(model.getStatus());
        holder.SerialNumber.setText(model.getSerialNumber());
        holder.ReportUrl.setMovementMethod(LinkMovementMethod.getInstance());
        holder.ReportUrl.setPaintFlags(holder.ReportUrl.getPaintFlags());
        holder.ReportUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(model.getReportUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView CustomerName, DateOfPurchase, ModelNumber, PhoneNumber, ReportUrl, SerialNumber, Status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            CustomerName = itemView.findViewById(R.id.complaint_customer_name);
            DateOfPurchase = itemView.findViewById(R.id.complaint_dateofpurchase);
            ModelNumber = itemView.findViewById(R.id.complaint_modelno);
            PhoneNumber = itemView.findViewById(R.id.complaint_phone);
            ReportUrl = itemView.findViewById(R.id.complaint_report);
            Status = itemView.findViewById(R.id.complaint_status);
            SerialNumber = itemView.findViewById(R.id.complaint_serialno);
        }
    }
}
