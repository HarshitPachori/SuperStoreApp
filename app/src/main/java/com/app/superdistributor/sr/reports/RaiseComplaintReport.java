package com.app.superdistributor.sr.reports;

import android.os.Bundle;
import android.util.Log;
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

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.adapters.ExpenseAdapter;
import com.app.superdistributor.sr.reports.adapters.RaiseComplaintReportAdapter;
import com.app.superdistributor.sr.reports.models.RaiseComplaintModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RaiseComplaintReport extends AppCompatActivity {

    String SRUsername;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    ArrayList<RaiseComplaintModel>list;
    RaiseComplaintReportAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_complaint_report);
        SRUsername = getIntent().getStringExtra("SRUsername");
        recyclerView = findViewById(R.id.raiseComplaintReportRV);
        progressBar = findViewById(R.id.raiseComplaintReportProgressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new RaiseComplaintReportAdapter(this, list);
        recyclerView.setAdapter(adapter);
        Log.d("sruser",SRUsername);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("SRs").child(SRUsername).child("Complaints").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               for(DataSnapshot dataSnapshot :snapshot.getChildren()){
                  for(DataSnapshot snapshot1 :dataSnapshot.getChildren()){
                      Log.d("sruser",snapshot1.toString());
                      RaiseComplaintModel model = snapshot1.getValue(RaiseComplaintModel.class);
                      list.add(model);
                  }
               }
                if (!list.isEmpty()) {
                    adapter.notifyItemChanged(list.size() - 1);
                } else {
                    adapter.notifyDataSetChanged();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}