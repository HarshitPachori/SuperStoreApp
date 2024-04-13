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
import com.app.superdistributor.sr.reports.adapters.PaymentReportAdapter;
import com.app.superdistributor.sr.reports.models.PaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApprovedPaymentReport extends AppCompatActivity {

    String SRUsername;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    ArrayList<PaymentModel> list;
    PaymentReportAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_payment_report);
        SRUsername = getIntent().getStringExtra("SRUsername");
        recyclerView = findViewById(R.id.paymentReportRV);
        progressBar = findViewById(R.id.paymentReportProgressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new PaymentReportAdapter(this, list);
        recyclerView.setAdapter(adapter);
        Log.d("sruser", SRUsername);
        progressBar.setVisibility(View.VISIBLE);
        databaseReference.child("SRs").child(SRUsername).child("myDealers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            String dealername = dataSnapshot.getKey();
                            if (dealername != null) {
                                databaseReference.child("Dealers").child(dealername).child("Payments").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot1) {
                                        for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {
                                            String paymentMethod = dataSnapshot1.getKey();
                                            for (DataSnapshot snapshot11 : dataSnapshot1.getChildren()) {
                                                Log.d("dealer", snapshot11.toString());
                                                PaymentModel model = snapshot11.getValue(PaymentModel.class);
                                                model.setPaymentMethod(paymentMethod);
                                               if(!"Pending".equals(model.getStatus())) list.add(model);
                                            }
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}