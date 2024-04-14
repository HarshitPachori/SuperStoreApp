package com.app.superdistributor.sr.reports;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.DateUtils;
import com.app.superdistributor.R;
import com.app.superdistributor.sr.dealerorders.DealerOrder;
import com.app.superdistributor.sr.dealerorders.DealerOrderAdapter;
import com.app.superdistributor.sr.reports.adapters.ConfirmedOrderAdapter;
import com.app.superdistributor.sr.reports.models.ConfirmedOrderModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ConfirmedOrderReport extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    ConfirmedOrderAdapter adapter;
    ArrayList<ConfirmedOrderModel> list;
    String SRUsername;
    ArrayList<String> myDealerList = new ArrayList<>();
    private Button mPickDateButton;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_order_report);

        SRUsername = getIntent().getStringExtra("SRUsername");

        recyclerView = findViewById(R.id.confirmOrderReportRV);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new ConfirmedOrderAdapter(this,list);
        recyclerView.setAdapter(adapter);




        database.child("SRs").child(SRUsername).child("myDealers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    myDealerList.add(dataSnapshot.child("Name").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mPickDateButton = findViewById(R.id.filterdateET);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!materialDatePicker.isVisible()) {
                            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                        }
                    }
                });


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        selectedDate = materialDatePicker.getHeaderText();
                        TextView selectedDateTv = findViewById(R.id.serviceRepDate);
                        if(!selectedDate.isEmpty()) {
                            selectedDateTv.setText(String.format("Selected Date : %s", selectedDate));
                        }
                        Toast.makeText(ConfirmedOrderReport.this, "" + selectedDate, Toast.LENGTH_SHORT).show();

                        filter(selectedDate);
                    }
                });


        database.child("Dealers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if(dataSnapshot.hasChild("Name")){
                        String dealerName = Objects.requireNonNull(dataSnapshot.child("Name").getValue()).toString();
                        for (int i=0; i<myDealerList.size(); i++)
                        {
                            Log.d("snapshotdata",dealerName );
                            if (myDealerList.get(i).equals(dealerName))
                            {
                                for(DataSnapshot dataSnapshot1: dataSnapshot.child("Orders").getChildren())
                                {
                                    ConfirmedOrderModel model = dataSnapshot1.getValue(ConfirmedOrderModel.class);
                                    Log.d("ordermodel",model.toString());
                                    if(model.getStatus().equals("Accepted") || model.getStatus().equals("Rejected")){
                                        list.add(model);
                                    }
                                }
                            }
                        }
                    }
                }
                adapter = new ConfirmedOrderAdapter(ConfirmedOrderReport.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.notifyDataSetChanged();
    }

    private void filter(String date){
        if(!date.isEmpty()){
            date = DateUtils.formatDate(date);
            adapter.filter(date);
        }
    }
}