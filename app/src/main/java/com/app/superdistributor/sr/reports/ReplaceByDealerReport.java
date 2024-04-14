package com.app.superdistributor.sr.reports;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.adapters.RegisterComplaintAdapter;
import com.app.superdistributor.sr.reports.adapters.ReplaceByDealerAdapter;
import com.app.superdistributor.sr.reports.models.RegisteredComplaintModel;
import com.app.superdistributor.sr.reports.models.ReplaceByDealerModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ReplaceByDealerReport extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    ReplaceByDealerAdapter adapter;
    ArrayList<ReplaceByDealerModel> list;
    ProgressBar progressBar;
    private Button mPickDateButton;
    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replace_by_dealer_report);
        recyclerView = findViewById(R.id.replaceByDealerReportRV);
        progressBar = findViewById(R.id.replaceByDealerProgressBar);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        progressBar.setVisibility(View.VISIBLE);


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
                        Toast.makeText(ReplaceByDealerReport.this, "" + selectedDate, Toast.LENGTH_SHORT).show();

                        filter(selectedDate);
                    }
                });




        database.child("Dealers").child("RequestServices").child("ReplacementByDealer").addListenerForSingleValueEvent(new ValueEventListener() {
//        database.child("SRs").child("RequestServices").child("ReplacementByDealer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                   for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                       ReplaceByDealerModel model = snapshot1.getValue(ReplaceByDealerModel.class);
                       if (model.getStatus().equals("Accepted") || model.getStatus().equals("Rejected")) {
                           list.add(model);
                       }
                   }
                }
                adapter = new ReplaceByDealerAdapter(ReplaceByDealerReport.this, list);
                recyclerView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    private void filter(String date){
        if(!date.isEmpty()){
            adapter.filter(date);
        }
    }
}