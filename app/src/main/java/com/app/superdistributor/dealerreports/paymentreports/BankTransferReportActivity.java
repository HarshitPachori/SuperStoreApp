package com.app.superdistributor.dealerreports.paymentreports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.superdistributor.R;
import com.app.superdistributor.dealerreports.paymentreports.adapter.BTPaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.adapter.ChequePaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.beans.BTPaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.ChequePaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class BankTransferReportActivity extends AppCompatActivity {

    RecyclerView BTpaymentrecyclerView;
    DatabaseReference database;

    BTPaymentAdapter myAdapter;
    ArrayList<BTPaymentModel> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_transfer_report);

        BTpaymentrecyclerView = findViewById(R.id.BTPaymentRV);

        database = FirebaseDatabase.getInstance().getReference();
        BTpaymentrecyclerView.setHasFixedSize(true);
        BTpaymentrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new BTPaymentAdapter(this,list);
        BTpaymentrecyclerView.setAdapter(myAdapter);

        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    for(DataSnapshot btsnapshot: dataSnapshot.child("Payments").child("BankTransfer").getChildren())
                    {
                        list.add(btsnapshot.getValue(BTPaymentModel.class));
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