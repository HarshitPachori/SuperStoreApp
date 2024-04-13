package com.app.superdistributor.sr.reports;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.MyPlacedOrderAdapter;
import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.adapters.RegisterComplaintAdapter;
import com.app.superdistributor.sr.reports.models.RegisteredComplaintModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisteredComplaintReport extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    RegisterComplaintAdapter adapter;
    ArrayList<RegisteredComplaintModel> list;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_complaint_report);
        recyclerView = findViewById(R.id.registeredComplaintReportRV);
        progressBar = findViewById(R.id.registerComplaintProgressBar);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new RegisterComplaintAdapter(this, list);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        database.child("Dealers").child("RequestServices").child("RegisterComplaints").addListenerForSingleValueEvent(new ValueEventListener() {
//        database.child("SRs").child("RequestServices").child("RegisterComplaints").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                  for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                      RegisteredComplaintModel model = snapshot1.getValue(RegisteredComplaintModel.class);
                      if (model.getStatus().equals("Accepted") || model.getStatus().equals("Rejected")) {
                          list.add(model);
                      }
                  }
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressBar.setVisibility(View.GONE);

            }
        });
    }
}