package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class SRAddPaymentActivity extends AppCompatActivity {

    String username;
    EditText amountET;
    Spinner dealerNameDropdown;
    Spinner type;
    Button submitPayment;
    DatabaseReference database;
    ArrayList<String> paymentType, dealerNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sradd_payment);
        username = getIntent().getStringExtra("SRUsername");
        database = FirebaseDatabase.getInstance().getReference();

        dealerNameDropdown = findViewById(R.id.dealerNameDropdown);
        amountET = findViewById(R.id.paymentAmountET);
        type = findViewById(R.id.paymentType);
        submitPayment = findViewById(R.id.submitPaymentBtn);

        paymentType = new ArrayList<>();
        paymentType.add("Cash");
        paymentType.add("Cheque");
        paymentType.add("Bank");
        paymentType.add("UPI");
        ArrayAdapter<String> payment = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, paymentType);
        type.setAdapter(payment);


        dealerNamesList = new ArrayList<>();
        database.child("SRs").child(username).child("myDealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dealerNamesList.add(dataSnapshot.getKey());
                }
                ArrayAdapter<String> dealerNameAdapter = new ArrayAdapter<>(SRAddPaymentActivity.this, android.R.layout.simple_spinner_dropdown_item, dealerNamesList);
                dealerNameDropdown.setAdapter(dealerNameAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        submitPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dealerNameDropdown.getSelectedItem().equals("")) {
                    Toast.makeText(SRAddPaymentActivity.this, "Please add Payment Name", Toast.LENGTH_SHORT).show();
                } else if (amountET.getText().toString().isEmpty()) {
                    Toast.makeText(SRAddPaymentActivity.this, "Please add Payment Amount", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> payment = new HashMap<>();
                    payment.put("Name", dealerNameDropdown.getSelectedItem().toString());
                    payment.put("Amount", amountET.getText().toString());
                    payment.put("Type", type.getSelectedItem().toString());
                    payment.put("Status", "Pending");
                    database.child("SRs")
                            .child(username)
                            .child("myPayments")
                            .child(dealerNameDropdown.getSelectedItem().toString())
                            .updateChildren(payment).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    amountET.setText("");
                                    type.setSelection(0);
                                    Toast.makeText(SRAddPaymentActivity.this, "Your Payment has been submitted", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SRAddPaymentActivity.this, "There was an error submitting your Payment", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

    }
}