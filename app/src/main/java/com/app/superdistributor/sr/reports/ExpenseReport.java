package com.app.superdistributor.sr.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
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
import com.app.superdistributor.sr.reports.adapters.ExpenseAdapter;
import com.app.superdistributor.sr.reports.adapters.ReplaceByDealerAdapter;
import com.app.superdistributor.sr.reports.models.ExpenseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ExpenseReport extends AppCompatActivity {

    ArrayList<ExpenseModel> list;
    RecyclerView recyclerView;
    ExpenseAdapter adapter;
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    String SRUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_report);
        SRUsername = getIntent().getStringExtra("SRUsername");
        recyclerView = findViewById(R.id.expenseReportRV);
        progressBar = findViewById(R.id.expenseProgressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new ExpenseAdapter(this, list);
        recyclerView.setAdapter(adapter);
        Log.d("sruser",SRUsername);
        progressBar.setVisibility(View.VISIBLE);

       databaseReference.child("SRs").child(SRUsername).child("Expenses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot srSnapshot : dataSnapshot.getChildren()) {
                    Log.d("expense",srSnapshot.toString());
                    ExpenseModel model = srSnapshot.getValue(ExpenseModel.class);
                    if(!model.getStatus().equals("Pending"))list.add(model);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}