package com.app.superdistributor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.app.superdistributor.admin.AddAccountantActivity;
import com.app.superdistributor.admin.AddDealerActivity;
import com.app.superdistributor.admin.AddManagerActivity;
import com.app.superdistributor.admin.AddSRActivity;
import com.app.superdistributor.admin.AddTechnicianActivity;
import com.app.superdistributor.admin.AdminPanelActivity;
import com.app.superdistributor.dealerreports.paymentreports.BankTransferReportActivity;
import com.app.superdistributor.dealerreports.paymentreports.CashReportActivity;
import com.app.superdistributor.dealerreports.paymentreports.ChequeReportActivity;
import com.app.superdistributor.dealerreports.paymentreports.MaterialReturnReportActivity;
import com.app.superdistributor.dealerreports.paymentreports.QRReportActivity;

public class ReportsActivity extends AppCompatActivity {

    Button ReportPaymentBtn,PlacedOrderHistoryBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        String DealerName = getIntent().getStringExtra("DealerName");
        ReportPaymentBtn = findViewById(R.id.reportPaymentBtn);
        PlacedOrderHistoryBtn = findViewById(R.id.placedOrderReportsBtn);

        ReportPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] paymentType = {"Cash", "QR", "Cheque", "Bank Transfer", "Material Return"};
                AlertDialog.Builder builder = new AlertDialog.Builder(ReportsActivity.this);
                builder.setTitle("Select Payment Type");
                builder.setItems(paymentType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(paymentType[i].equals("Cash"))
                        {
                            Intent intent = new Intent(ReportsActivity.this, CashReportActivity.class);
                            startActivity(intent);
                        }
                        else if(paymentType[i].equals("QR"))
                        {
                            Intent intent = new Intent(ReportsActivity.this, QRReportActivity.class);
                            startActivity(intent);
                        }
                        else if(paymentType[i].equals("Cheque"))
                        {
                            Intent intent = new Intent(ReportsActivity.this, ChequeReportActivity.class);
                            startActivity(intent);
                        }
                        else if(paymentType[i].equals("Bank Transfer"))
                        {
                            Intent intent = new Intent(ReportsActivity.this, BankTransferReportActivity.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(ReportsActivity.this, MaterialReturnReportActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

        PlacedOrderHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ReportsActivity.this,PlacedOrderHistoryActivity.class);
                i.putExtra("DealerName",DealerName);
                startActivity(i);
            }
        });

    }
}