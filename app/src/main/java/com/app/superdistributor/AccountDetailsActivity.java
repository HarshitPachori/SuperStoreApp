package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.app.superdistributor.models.AmountModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AccountDetailsActivity extends AppCompatActivity {

    String dealerName, transactionType;
    TextView CurrentBalanceTv;
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAmountAdapter myAdapter;
    ArrayList<AmountModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);

        Intent intent = getIntent();
        dealerName = intent.getStringExtra("Dealer");
        transactionType = intent.getStringExtra("TransactionType");

        CurrentBalanceTv = findViewById(R.id.currentbalancetxt);

        recyclerView = findViewById(R.id.amountlist);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAmountAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        database.child("Dealers").child(dealerName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(transactionType.equals("All"))
                {
                    for (DataSnapshot dataSnapshot : snapshot.child("Credit").getChildren()){

                        AmountModel amountModel = dataSnapshot.getValue(AmountModel.class);
                        list.add(amountModel);

                    }
                    for (DataSnapshot dataSnapshot : snapshot.child("Debit").getChildren()){

                        AmountModel amountModel = dataSnapshot.getValue(AmountModel.class);
                        list.add(amountModel);
                    }
                }
                else
                {
                    for (DataSnapshot dataSnapshot : snapshot.child(transactionType).getChildren()){

                        AmountModel amountModel = dataSnapshot.getValue(AmountModel.class);
                        list.add(amountModel);
                    }
                }
                myAdapter.notifyDataSetChanged();

                //Toast.makeText(CustomerAccountDetails.this, "list"+snapshot, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child("Dealers").child(dealerName).child("CurrentBalance").
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CurrentBalanceTv.setText("Current Balance : "+snapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}