package com.app.superdistributor.payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.superdistributor.DealerHomeActivity;
import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MaterialReturnPaymentActivity extends AppCompatActivity {

    TextInputEditText MaterialDescription, MaterialValue;
    Button MaterialSubmitBtn;
    private DatabaseReference mref;
    private ProgressDialog LoadingBar;

    String DealerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material_return_payment);

        DealerName = getIntent().getStringExtra("DealerName");

        MaterialSubmitBtn = findViewById(R.id.materialSubmitBtn);
        MaterialDescription = findViewById(R.id.materialET);
        MaterialValue = findViewById(R.id.materialValueET);

        LoadingBar=new ProgressDialog(this);

        mref= FirebaseDatabase.getInstance().getReference();

        MaterialSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(MaterialDescription.getText().toString().equals(""))
                {
                    Toast.makeText(MaterialReturnPaymentActivity.this, "Please enter material description..", Toast.LENGTH_SHORT).show();
                }
                else if(MaterialValue.getText().toString().equals(""))
                {
                    Toast.makeText(MaterialReturnPaymentActivity.this, "Please enter material value..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LoadingBar.setTitle("Please Wait..");
                    LoadingBar.setMessage("Please Wait we submitting your details...");
                    LoadingBar.show();

                    Map<String, Object> materialDetails = new HashMap<String, Object>();
                    materialDetails.put("Description", MaterialDescription.getText().toString());
                    materialDetails.put("Value", MaterialValue.getText().toString());

                    mref.child("Dealers").child(DealerName).child("Payments").child("MaterialReturn")
                            .push().updateChildren(materialDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MaterialReturnPaymentActivity.this, "Material Details Submitted..", Toast.LENGTH_SHORT).show();
                                    MaterialDescription.setText("");
                                    MaterialValue.setText("");
                                    Intent i = new Intent(MaterialReturnPaymentActivity.this, PaymentMethodActivity.class);
                                    startActivity(i);
                                }
                            });
                }
            }
        });
    }
}