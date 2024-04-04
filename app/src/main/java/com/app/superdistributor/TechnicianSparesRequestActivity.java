package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class TechnicianSparesRequestActivity extends AppCompatActivity {
    String technicianUsername;
    EditText materialDescEt, materialQtyEt;
    Button submitBtn;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_spares_request);
        technicianUsername = getIntent().getStringExtra("TechnicianUsername");

        databaseReference = FirebaseDatabase.getInstance().getReference();

        materialDescEt = findViewById(R.id.spareTagET);
        materialQtyEt = findViewById(R.id.spareQtyET);
        submitBtn = findViewById(R.id.submitBtn);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(materialDescEt.getText().toString().equals("")) {
                    Toast.makeText(TechnicianSparesRequestActivity.this, "Please add Spare material description", Toast.LENGTH_SHORT).show();
                }
                    else if (materialQtyEt.getText().toString().equals("")) {
                    Toast.makeText(TechnicianSparesRequestActivity.this, "Please add spare material quantity", Toast.LENGTH_SHORT).show();
                }
                    else {
                    Map<String,Object> spareRequest = new HashMap<>();
                    spareRequest.put("Material",materialDescEt.getText().toString());
                    spareRequest.put("Quantity",materialQtyEt.getText().toString());
                    databaseReference.child("Technicians").child(technicianUsername)
                            .child("Spares").child(materialDescEt.getText().toString())
                            .updateChildren(spareRequest).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    materialDescEt.setText("");
                                    materialQtyEt.setText("");
                                    Toast.makeText(TechnicianSparesRequestActivity.this, "Spare requested!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(TechnicianSparesRequestActivity.this, "Couldn't process?!?!", Toast.LENGTH_SHORT).show();
                                }
                            })
                    ;
                }
            }
        });
    }
}