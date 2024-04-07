package com.app.superdistributor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.app.superdistributor.sr.reports.ApprovedPaymentReport;
import com.app.superdistributor.sr.reports.ConfirmedOrderReport;
import com.app.superdistributor.sr.reports.ExpenseReport;
import com.app.superdistributor.sr.reports.PostMessageReport;
import com.app.superdistributor.sr.reports.RegisteredComplaintReport;
import com.app.superdistributor.sr.reports.ReplaceByDealerReport;

public class SrReportsActivity extends AppCompatActivity {

    Button RegisterComplaint, ReplaceByDealer, ConfirmOrder, ApprovePayment, PostMessage, RaiseComplaint, Expense;
    String SRUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sr_reports);
        SRUsername = getIntent().getStringExtra("SRUsername");
        RegisterComplaint = findViewById(R.id.registerComplaintReportBtn);
        ReplaceByDealer = findViewById(R.id.replaceByDealerReportBtn);
        ConfirmOrder = findViewById(R.id.confirmedOrderReportBtn);
        ApprovePayment = findViewById(R.id.paymentReportBtn);
        PostMessage = findViewById(R.id.postMessageReportBtn);
        RaiseComplaint = findViewById(R.id.raiseComplaintReportBtn);
        Expense = findViewById(R.id.expenseReportBtn);
        Log.d("sruser",SRUsername);
        RegisterComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrReportsActivity.this, RegisteredComplaintReport.class);
                startActivity(intent);
            }
        });
        ReplaceByDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrReportsActivity.this, ReplaceByDealerReport.class);
                startActivity(intent);
            }
        });
        ConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrReportsActivity.this, ConfirmedOrderReport.class);
                intent.putExtra("SRUsername",SRUsername);
                startActivity(intent);
            }
        });
        ApprovePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrReportsActivity.this, ApprovedPaymentReport.class);
                startActivity(intent);
            }
        });
        PostMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrReportsActivity.this, PostMessageReport.class);
                startActivity(intent);
            }
        });
        RaiseComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrReportsActivity.this, RegisteredComplaintReport.class);
                startActivity(intent);
            }
        });
        Expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SrReportsActivity.this, ExpenseReport.class);
                intent.putExtra("SRUsername",SRUsername);
                startActivity(intent);
            }
        });
    }
}