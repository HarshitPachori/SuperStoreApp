package com.app.superdistributor;

import android.app.DatePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.sr.dealerorders.DealerOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TechnicianComplaintsAdapter extends RecyclerView.Adapter<TechnicianComplaintsAdapter.MyViewHolder>{
    Context context;
    ArrayList<TechnicianComplaints> list;
    DatabaseReference databaseReference;
    String technicianUsername;
    boolean history = false;

    public TechnicianComplaintsAdapter(Context context, ArrayList<TechnicianComplaints> list, String technicianUsername){
        this.context = context;
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.technicianUsername = technicianUsername;
    }
    public TechnicianComplaintsAdapter(Context context, ArrayList<TechnicianComplaints> list, String technicianUsername, boolean history){
        this.context = context;
        this.list = list;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.technicianUsername = technicianUsername;
        this.history = history;
    }
    @NonNull
    @Override
    public TechnicianComplaintsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TechnicianComplaintsAdapter.MyViewHolder(
                LayoutInflater.from(context).inflate(R.layout.technician_complaint_item, parent, false)
        );
    }
    @Override
    public void onBindViewHolder(@NonNull TechnicianComplaintsAdapter.MyViewHolder holder, int position) {
        if (list.get(0).getCustomerName().isEmpty()){
            Toast.makeText(context, "No Complaints", Toast.LENGTH_LONG).show();
        }
        if(history) {
            holder.statusTv.setVisibility(View.VISIBLE);
            holder.statusCv.setVisibility(View.INVISIBLE);
        }
            TechnicianComplaints technicianComplaints = list.get(position);
            databaseReference.child("Dealers").child("RequestServices").child("RegisterComplaints")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            holder.customerNameEt.setText(technicianComplaints.getCustomerName());
                            holder.dateOfPurchaseEt.setText("Purchased : " + technicianComplaints.getDateOfPurchase());
                            holder.modelNumberEt.setText("Model : " + technicianComplaints.getModelNumber());
                            holder.phoneNumberEt.setText("Phone : " + technicianComplaints.getPhoneNumber());
                            holder.serialNumberEt.setText("Serial : " + technicianComplaints.getSerialNumber());
                            holder.statusTv.setText(technicianComplaints.getStatus());
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> temp = new HashMap<>();
                temp.put("Status","Accepted");
                databaseReference.child("Dealers").child("RequestServices").child("RegisterComplaints")
                        .child(technicianComplaints.getCustomerName()).updateChildren(temp);
                Toast.makeText(context, "The complaint has been accepted", Toast.LENGTH_SHORT).show();
            }
        });
        holder.rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> temp = new HashMap<>();
                temp.put("Status","Rejected");
                databaseReference.child("Dealers").child("RequestServices").child("RegisterComplaints")
                        .child(technicianComplaints.getCustomerName()).updateChildren(temp);
                Toast.makeText(context, "The complaint has been rejected", Toast.LENGTH_SHORT).show();
            }
        });
        holder.rescheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*              java.util.Calendar calendar = java.util.Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                // Handle the selected date here
                                // The year, monthOfYear, and dayOfMonth parameters give you the selected date
                            }
                        },
                        year, month, dayOfMonth);
                datePickerDialog.show();*/
                Map<String,Object> temp = new HashMap<>();
                temp.put("Status","Rescheduled");
                databaseReference.child("Dealers").child("RequestServices").child("RegisterComplaints")
                        .child(technicianComplaints.getCustomerName()).updateChildren(temp);
                Toast.makeText(context, "The complaint has been rescheduled", Toast.LENGTH_SHORT).show();
            }
        });
        holder.closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> temp = new HashMap<>();
                temp.put("Status","Closed");
                databaseReference.child("Dealers").child("RequestServices").child("RegisterComplaints")
                        .child(technicianComplaints.getCustomerName()).updateChildren(temp);
                Toast.makeText(context, "The complaint has been closed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameEt, dateOfPurchaseEt, modelNumberEt, phoneNumberEt, serialNumberEt, statusTv;
        CardView  statusCv;
        Button acceptBtn, rejectBtn, rescheduleBtn, closeBtn;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusCv = itemView.findViewById(R.id.status_cardview);
            statusTv = itemView.findViewById(R.id.status_tv);
            customerNameEt = itemView.findViewById(R.id.customer_name);
            dateOfPurchaseEt = itemView.findViewById(R.id.purchase_date);
            modelNumberEt = itemView.findViewById(R.id.model_number);
            phoneNumberEt = itemView.findViewById(R.id.phone_number);
            serialNumberEt = itemView.findViewById(R.id.serial_number);
            acceptBtn = itemView.findViewById(R.id.accept_button);
            rejectBtn = itemView.findViewById(R.id.reject_button);
            rescheduleBtn = itemView.findViewById(R.id.reschedule_button);
            closeBtn = itemView.findViewById(R.id.close_button);

        }
    }
}
