package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class SrGiveGetAmountActivity extends AppCompatActivity {
    String username,dealerUsername;
    EditText nameET, amountET;
    Spinner type;
    Button submitPayment;
    DatabaseReference database;
    ArrayList<String> paymentType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr_give_get_amount);
        username = getIntent().getStringExtra("SrUsername");
        dealerUsername = getIntent().getStringExtra("dealerUsername");
        database = FirebaseDatabase.getInstance().getReference();

        amountET = findViewById(R.id.paymentAmountET);
        type = findViewById(R.id.paymentType);
        submitPayment = findViewById(R.id.submitPaymentBtn);

        paymentType = new ArrayList<>();
        paymentType.add("Credit");
        paymentType.add("Debit");
        ArrayAdapter<String> payment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentType);
        type.setAdapter(payment);
        submitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (amountET.getText().toString().equals("")) {
                    Toast.makeText(SrGiveGetAmountActivity.this,"Please add Payment Amount", Toast.LENGTH_SHORT).show();
                }
                else{
                    HashMap<String,Object> payment = new HashMap<>();
                     database.child("SRs")
                             .child(username)
                             .child("myDealers")
                             .child(dealerUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                                 @Override
                                 public void onDataChange(@NonNull DataSnapshot snapshot) {
                                     if(type.getSelectedItem().toString().equals("Credit")){
                                         payment.put("Balance", ( Integer.parseInt(snapshot.child("Balance").getValue().toString()) - Integer.parseInt(amountET.getText().toString()) ));
                                     }
                                     else {
                                         payment.put("Balance", ( Integer.parseInt(snapshot.child("Balance").getValue().toString()) + Integer.parseInt(amountET.getText().toString()) ));
                                     }
                                     database.child("SRs")
                                             .child(username)
                                             .child("myDealers")
                                             .child(dealerUsername).updateChildren(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                 @Override
                                                 public void onSuccess(Void unused) {
                                                     Toast.makeText(SrGiveGetAmountActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                                                     startActivity(new Intent(SrGiveGetAmountActivity.this, SRHomeActivity.class).putExtra("SRUsername",username));
                                                 }
                                             }).addOnFailureListener(new OnFailureListener() {
                                                 @Override
                                                 public void onFailure(@NonNull Exception e) {
                                                     Toast.makeText(SrGiveGetAmountActivity.this, "There was an error", Toast.LENGTH_SHORT).show();
                                                 }
                                             });
                                 }

                                 @Override
                                 public void onCancelled(@NonNull DatabaseError error) {

                                 }
                             });

                }
            }
        });
    }
}