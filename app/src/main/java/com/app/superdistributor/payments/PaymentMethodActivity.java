package com.app.superdistributor.payments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.superdistributor.R;

public class PaymentMethodActivity extends AppCompatActivity {

    Button CashPaymentBtn, QRPaymentBtn, ChequePaymentBtn, BankTransferPaymentBtn, MaterialReturnBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);

        String DealerName = getIntent().getStringExtra("DealerName");

        CashPaymentBtn = findViewById(R.id.cashBtn);
        QRPaymentBtn = findViewById(R.id.qrBtn);
        ChequePaymentBtn = findViewById(R.id.chequeBtn);
        BankTransferPaymentBtn = findViewById(R.id.bankTransferBtn);
        MaterialReturnBtn = findViewById(R.id.materialBtn);

        CashPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentMethodActivity.this, CashPaymentActivity.class);
                i.putExtra("DealerName",DealerName);
                startActivity(i);
            }
        });

        QRPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentMethodActivity.this, QRPaymentActivity.class);
                i.putExtra("DealerName",DealerName);
                startActivity(i);
            }
        });

        ChequePaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentMethodActivity.this, ChequePaymentActivity.class);
                i.putExtra("DealerName",DealerName);
                startActivity(i);
            }
        });

        BankTransferPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentMethodActivity.this, BankTransferPaymentActivity.class);
                i.putExtra("DealerName",DealerName);
                startActivity(i);
            }
        });

        MaterialReturnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PaymentMethodActivity.this, MaterialReturnPaymentActivity.class);
                i.putExtra("DealerName",DealerName);
                startActivity(i);
            }
        });
    }
}