package com.app.superdistributor.RequestService;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.MyAmountAdapter;
import com.app.superdistributor.R;
import com.app.superdistributor.models.AmountModel;

import java.util.ArrayList;

public class MyServiceAdapter extends RecyclerView.Adapter<MyServiceAdapter.MyViewHolder>{
    Context context;
    ArrayList<ServiceModel> list;
    public MyServiceAdapter(Context context, ArrayList<ServiceModel> list) {
        this.context = context;
        this.list = list;
    }

    public void filterList(ArrayList<ServiceModel> filterlist) {
        // below line is to add our filtered
        // list in our course array list.
        list = filterlist;
        // below line is to notify our adapter
        // as change in recycler view data.
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyServiceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.service_report_item,parent,false);
        return  new MyServiceAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyServiceAdapter.MyViewHolder holder, int position) {
        ServiceModel serviceModel = list.get(position);
        holder.CustomerNameTV.setText(serviceModel.getCustomerName());
        holder.DateOfPurchaseTV.setText(serviceModel.getDateOfPurchase());
        holder.ModelNoTV.setText(serviceModel.getModelNumber());
        holder.NewProductSerialNumberTV.setText(serviceModel.getNewProductSerialNumber());
        holder.PhoneNoTV.setText(serviceModel.getPhoneNumber());
        holder.status.setText(serviceModel.getStatus());
        holder.ReportURLTV.setText("Document URL");
        holder.SerialNoTv.setText(serviceModel.getSerialNumber());

        holder.ReportURLTV.setMovementMethod(LinkMovementMethod.getInstance());
        holder.ReportURLTV.setPaintFlags(holder.ReportURLTV.getPaintFlags());
        holder.ReportURLTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(serviceModel.getReportUrl()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView CustomerNameTV, DateOfPurchaseTV, ModelNoTV, NewProductSerialNumberTV, PhoneNoTV, ReportURLTV, SerialNoTv,status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            CustomerNameTV = itemView.findViewById(R.id.servicecustomername);
            DateOfPurchaseTV = itemView.findViewById(R.id.servicedateofpurchase);
            ModelNoTV = itemView.findViewById(R.id.modelno);
            NewProductSerialNumberTV = itemView.findViewById(R.id.newproductserialnumber);
            PhoneNoTV = itemView.findViewById(R.id.servicephonenumber);
            ReportURLTV = itemView.findViewById(R.id.reporturl);
            SerialNoTv = itemView.findViewById(R.id.serviceserialnumber);
            status = itemView.findViewById(R.id.status);
        }
    }
}
