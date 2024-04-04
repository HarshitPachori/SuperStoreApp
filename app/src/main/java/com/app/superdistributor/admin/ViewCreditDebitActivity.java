package com.app.superdistributor.admin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.superdistributor.AccountDetailsActivity;
import com.app.superdistributor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewCreditDebitActivity extends AppCompatActivity {
    Button ViewDetailsBtn;
    ArrayList<String> transactionTypeArrayList = new ArrayList<>();
    ArrayList<String> dealerArrayList = new ArrayList<>();
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credit_debit);

        database = FirebaseDatabase.getInstance().getReference();

        Spinner dealerDropdown = findViewById(R.id.selectDealerDropDown);

        dealerArrayList.add("Select Dealer");
        ArrayAdapter<String> dealeradapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dealerArrayList);

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.child("Dealers").getChildren()) {
                    dealerArrayList.add(snap.getKey());
                    Log.d("Test",dealerArrayList.toString());
                }
                dealerDropdown.setAdapter(dealeradapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Spinner transactionTypeSpinner = findViewById(R.id.selelctTransactionType);

        transactionTypeArrayList.add("All");
        transactionTypeArrayList.add("Credit");
        transactionTypeArrayList.add("Debit");

        ArrayAdapter<String> transactionAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, transactionTypeArrayList);
        transactionTypeSpinner.setAdapter(transactionAdapter);

        ViewDetailsBtn = findViewById(R.id.viewDetailsBtn);

        ViewDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ViewCreditDebitActivity.this, AccountDetailsActivity.class);
                i.putExtra("Dealer",dealerDropdown.getSelectedItem().toString());
                i.putExtra("TransactionType",transactionTypeSpinner.getSelectedItem().toString());
                if(dealerDropdown.getSelectedItem().toString().equals("Select Dealer"))
                    Toast.makeText(ViewCreditDebitActivity.this,"Please select a dealer",Toast.LENGTH_SHORT).show();
                else
                    startActivity(i);
            }
        });
    }
}