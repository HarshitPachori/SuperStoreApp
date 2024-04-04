package com.app.superdistributor.dealerreports.paymentreports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.app.superdistributor.admin.paymenthistory.AccountBalanceOverviewAdapter;
import com.app.superdistributor.admin.paymenthistory.AmountOverviewModel;
import com.app.superdistributor.dealerreports.paymentreports.adapter.CashPaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.beans.CashPaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CashReportActivity extends AppCompatActivity {
    RecyclerView cashpaymentrecyclerView;
    DatabaseReference database;

    CashPaymentAdapter myAdapter;
    ArrayList<CashPaymentModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_report);

        cashpaymentrecyclerView = findViewById(R.id.cashPaymentRV);

        database = FirebaseDatabase.getInstance().getReference();
        cashpaymentrecyclerView.setHasFixedSize(true);
        cashpaymentrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new CashPaymentAdapter(this,list);
        cashpaymentrecyclerView.setAdapter(myAdapter);

        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    for(DataSnapshot cashsnapshot: dataSnapshot.child("Payments").child("Cash").getChildren())
                    {
                        list.add(cashsnapshot.getValue(CashPaymentModel.class));
                    }
                }
                myAdapter.notifyDataSetChanged();
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Toast.makeText(this, ""+String.valueOf(list.size()), Toast.LENGTH_SHORT).show();

    }
}