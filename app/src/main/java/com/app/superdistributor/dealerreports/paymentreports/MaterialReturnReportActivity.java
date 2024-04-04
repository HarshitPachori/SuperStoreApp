package com.app.superdistributor.dealerreports.paymentreports;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.app.superdistributor.R;
import com.app.superdistributor.dealerreports.paymentreports.adapter.BTPaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.adapter.MaterialReturnPaymentAdapter;
import com.app.superdistributor.dealerreports.paymentreports.beans.BTPaymentModel;
import com.app.superdistributor.dealerreports.paymentreports.beans.MaterialReturnPaymentModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MaterialReturnReportActivity extends AppCompatActivity {

    RecyclerView MaterialReturnPaymentrecyclerView;
    DatabaseReference database;

    MaterialReturnPaymentAdapter myAdapter;
    ArrayList<MaterialReturnPaymentModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_return_report);

        MaterialReturnPaymentrecyclerView = findViewById(R.id.MaterialReturnPaymentRV);

        database = FirebaseDatabase.getInstance().getReference();
        MaterialReturnPaymentrecyclerView.setHasFixedSize(true);
        MaterialReturnPaymentrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MaterialReturnPaymentAdapter(this,list);
        MaterialReturnPaymentrecyclerView.setAdapter(myAdapter);

        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren())
                {
                    for(DataSnapshot materialreturnsnapshot: dataSnapshot.child("Payments").child("MaterialReturn").getChildren())
                    {
                        list.add(materialreturnsnapshot.getValue(MaterialReturnPaymentModel.class));
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