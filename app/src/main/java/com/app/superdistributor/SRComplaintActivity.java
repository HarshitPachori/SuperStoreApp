package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

public class SRComplaintActivity extends AppCompatActivity {

    EditText complaintTagET, complaintBodyET;
    Spinner chooseDealerSpinner;
    Button submitComplaintBtn;
    String username;
    DatabaseReference database;
    private ProgressDialog LoadingBar;
    private ArrayList<String> dealerUserNamesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srcomplaint);

        username = getIntent().getStringExtra("SRUsername");
        database = FirebaseDatabase.getInstance().getReference();
        LoadingBar = new ProgressDialog(this);

        dealerUserNamesList = new ArrayList<>();
        dealerUserNamesList.add("--Select a dealer--");

        complaintTagET = findViewById(R.id.complaintTagET);
        complaintBodyET = findViewById(R.id.complaintBodyET);
        chooseDealerSpinner = findViewById(R.id.selectDealerToComplain);

        database.child("SRs").child(username).child("myDealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snap : snapshot.getChildren()) {

                    Log.d("Check",snap.getValue().toString());
                    if (snap.getValue() != null)
                        dealerUserNamesList.add(snap.getValue().toString());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        Log.d("Dealers" , dealerUserNamesList.toString());
        ArrayAdapter<String> dealerUserNameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dealerUserNamesList);
        chooseDealerSpinner.setAdapter(dealerUserNameAdapter);

        submitComplaintBtn = findViewById(R.id.submitComplaintBtn);

        username = getIntent().getStringExtra("SRUsername");
        
        submitComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complaintTagET.getText().toString().equals("")){
                    Toast.makeText(SRComplaintActivity.this, "Please add a tag for your complaint",Toast.LENGTH_SHORT).show();
                } else if (complaintBodyET.getText().toString().equals("")) {
                    Toast.makeText(SRComplaintActivity.this, "Please Describe your complaint",Toast.LENGTH_SHORT).show();
                } else if (chooseDealerSpinner.getSelectedItem().toString().equals("--Select a dealer--")) {
                    Toast.makeText(SRComplaintActivity.this, "Please Choose a dealer concerning your complaint",Toast.LENGTH_SHORT).show();
                } else {
                        HashMap<String,Object> complaint = new HashMap<>();
                        String complainTag = complaintTagET.getText().toString();
                        String complainBody = complaintBodyET.getText().toString();
                        String dealer = chooseDealerSpinner.getSelectedItem().toString();
                        complaint.put("Dealer",dealer);
                        complaint.put("Tag",complainTag);
                        complaint.put("Description",complainBody);

                        database.child("SRs").child(username).child("Complaints").child(dealer)
                                .updateChildren(complaint).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        complaintTagET.setText("");
                                        complaintBodyET.setText("");
                                        chooseDealerSpinner.setSelection(0);
                                        Toast.makeText(SRComplaintActivity.this,"Your Complaint has been submitted",Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SRComplaintActivity.this,"There was an error submitting your Complaint",Toast.LENGTH_SHORT).show();
                                    }
                                });

                }
            }
        });
    }
}