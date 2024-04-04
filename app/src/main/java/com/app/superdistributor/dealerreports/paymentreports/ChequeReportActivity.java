package com.app.superdistributor.dealerreports.paymentreports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.superdistributor.R;
import com.app.superdistributor.dealerreports.paymentreports.adapter.CashPaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.adapter.ChequePaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.beans.CashPaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.ChequePaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChequeReportActivity extends AppCompatActivity {
    RecyclerView chequepaymentrecyclerView;
    DatabaseReference database;

    ChequePaymentAdapter myAdapter;
    ArrayList<ChequePaymentModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheque_report);

        chequepaymentrecyclerView = findViewById(R.id.chequePaymentRV);

        database = FirebaseDatabase.getInstance().getReference();
        chequepaymentrecyclerView.setHasFixedSize(true);
        chequepaymentrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new ChequePaymentAdapter(this,list);
        chequepaymentrecyclerView.setAdapter(myAdapter);

        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    for(DataSnapshot chequesnapshot: dataSnapshot.child("Payments").child("Cheque").getChildren())
                    {
                        list.add(chequesnapshot.getValue(ChequePaymentModel.class));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}