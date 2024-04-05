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

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.adapters.RegisterComplaintAdapter;
import com.app.superdistributor.sr.reports.adapters.ReplaceByDealerAdapter;
import com.app.superdistributor.sr.reports.models.RegisteredComplaintModel;
import com.app.superdistributor.sr.reports.models.ReplaceByDealerModel;
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
        adapter = new ReplaceByDealerAdapter(this, list);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.VISIBLE);
        database.child("Dealers").child("RequestServices").child("ReplacementByDealer").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    ReplaceByDealerModel model = dataSnapshot.getValue(ReplaceByDealerModel.class);
                    if (model.getStatus().equals("Accepted") || model.getStatus().equals("Rejected")) {
                        list.add(model);
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