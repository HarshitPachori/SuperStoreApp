package com.app.superdistributor.dealerreports.paymentreports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.superdistributor.R;
import com.app.superdistributor.dealerreports.paymentreports.adapter.ChequePaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.adapter.QRPaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.beans.ChequePaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.QRPaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class QRReportActivity extends AppCompatActivity {
    RecyclerView qrpaymentrecyclerView;
    DatabaseReference database;

    QRPaymentAdapter myAdapter;
    ArrayList<QRPaymentModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrreport);

        qrpaymentrecyclerView = findViewById(R.id.qrPaymentRV);

        database = FirebaseDatabase.getInstance().getReference();
        qrpaymentrecyclerView.setHasFixedSize(true);
        qrpaymentrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new QRPaymentAdapter(this,list);
        qrpaymentrecyclerView.setAdapter(myAdapter);

        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    for(DataSnapshot qrsnapshot: dataSnapshot.child("Payments").child("QR").getChildren())
                    {
                        list.add(qrsnapshot.getValue(QRPaymentModel.class));
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