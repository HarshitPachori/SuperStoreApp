package com.app.superdistributor.payments;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import java.util.Objects;

public class CashPaymentActivity extends AppCompatActivity {

    RadioGroup CashPaymentradioGroup;
    RadioButton CashPaymentRadioButton;

    TextInputEditText CashAmountET, CashUserNameEt;
    Button CashPaymentSubmitBtn;

    String DealerName;

    private DatabaseReference mref;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_payment);

        DealerName = getIntent().getStringExtra("DealerName");

        mref= FirebaseDatabase.getInstance().getReference();

        CashPaymentradioGroup=(RadioGroup)findViewById(R.id.cashpatmentradioGroup);
        CashAmountET = findViewById(R.id.cashAmountET);
        CashUserNameEt = findViewById(R.id.cashUserNameET);
        CashPaymentSubmitBtn = findViewById(R.id.cashSubmitBtn);

        LoadingBar=new ProgressDialog(this);

        CashPaymentSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CashAmountET.getText().toString().equals(""))
                {
                    Toast.makeText(CashPaymentActivity.this, "Please enter cash amount..", Toast.LENGTH_SHORT).show();
                }
                else if(CashUserNameEt.getText().toString().equals(""))
                {
                    Toast.makeText(CashPaymentActivity.this, "Please enter user name..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LoadingBar.setTitle("Please Wait..");
                    LoadingBar.setMessage("Please Wait we are saving your details...");
                    LoadingBar.show();

                    int radioId=CashPaymentradioGroup.getCheckedRadioButtonId();
                    CashPaymentRadioButton=findViewById(radioId);

                    mref.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Map<String, Object> cashMap = new HashMap<>();
                                    cashMap.put("User", CashUserNameEt.getText().toString());
                                    cashMap.put("UserType", CashPaymentRadioButton.getText().toString());
                                    cashMap.put("Amount", CashAmountET.getText().toString());

                                    mref.child("Dealers").child(DealerName).
                                            child("Payments").child("Cash").push().updateChildren(cashMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(CashPaymentActivity.this, "Details Submitted.", Toast.LENGTH_SHORT).show();
                                                    CashUserNameEt.setText("");
                                                    CashAmountET.setText("");
                                                    LoadingBar.dismiss();
                                                    Intent i = new Intent(CashPaymentActivity.this, PaymentMethodActivity.class);
                                                    i.putExtra("DealerName",DealerName);
                                                    startActivity(i);
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