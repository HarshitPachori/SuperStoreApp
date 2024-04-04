package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.admin.AdminPanelActivity;
import com.app.superdistributor.admin.ViewCreditDebitActivity;
import com.app.superdistributor.admin.notification.AdminNotificationActivity;
import com.app.superdistributor.sr.DealerIntentActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SRHomeActivity extends AppCompatActivity {

    TextView SalesDoneTV, RemainingTargetTV;
    Button TotalSROutstandingBtn, DealerIntentBtn, PaymentApproveBtn, ComplaintRaiseBtn, AddPaymentBtn,
    RequestServiceBtn, PostMessageBtn, ExpenseBtn, AddVisitBtn, CreditDebitBtn;

    ImageView myMessagesBtn, NotificationBtn,LogoutBtn;

    String SRUsername;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srhome);

        SalesDoneTV = findViewById(R.id.salesDoneTv);
        RemainingTargetTV = findViewById(R.id.remainingTargetTv);

        SRUsername = getIntent().getStringExtra("SRUsername");
        myMessagesBtn = findViewById(R.id.srMessages);
        NotificationBtn = findViewById(R.id.srNotification);
        LogoutBtn = findViewById(R.id.srLogout);

        database = FirebaseDatabase.getInstance().getReference();
        myMessagesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SRHomeActivity.this, MyMessagesActivity.class).putExtra("Username",SRUsername));
            }
        });
        NotificationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, AdminNotificationActivity.class);
                startActivity(i);
            }
        });
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SRHomeActivity.this, LoginActivity.class);
                Toast.makeText(SRHomeActivity.this, "Logout Successful..", Toast.LENGTH_SHORT).show();

                SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("UserType", "");
                myEdit.commit();

                startActivity(i);
            }
        });
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Integer salesDone = Integer.parseInt(snapshot.child("SRs").child(SRUsername)
                        .child("SRSSalesStatus")
                        .child("SalesDone").getValue().toString());
                Integer remainingTarget = Integer.parseInt(snapshot.child("SRs").child(SRUsername)
                        .child("SRSSalesStatus")
                        .child("TargetAmount").getValue().toString())
                        - salesDone;

                SalesDoneTV.setText("Sales Done \u20B9 "+ salesDone);
                RemainingTargetTV.setText("Remaining Target \u20B9 "+ remainingTarget);
                TotalSROutstandingBtn.setText("Total Outstanding - "+snapshot.child("SRs").child(SRUsername)
                        .child("SRSSalesStatus")
                        .child("TotalOutstanding").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        TotalSROutstandingBtn = findViewById(R.id.totaloutstandingondealersbtn);
        DealerIntentBtn = findViewById(R.id.dealerintentbtn);
        PaymentApproveBtn = findViewById(R.id.paymentapprovebtn);
        ComplaintRaiseBtn = findViewById(R.id.complaintraisebtn);
        AddPaymentBtn = findViewById(R.id.addpaymentbtn);
        RequestServiceBtn = findViewById(R.id.requestservicebtn);
        PostMessageBtn = findViewById(R.id.postMessageBtn);
        ExpenseBtn = findViewById(R.id.expensebtn);
        AddVisitBtn = findViewById(R.id.addvisitbtn);
        CreditDebitBtn = findViewById(R.id.creditdebitbtn);


        database = FirebaseDatabase.getInstance().getReference();

        TotalSROutstandingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SRHomeActivity.this, SRDealersLedgerAccountActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });

        DealerIntentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SRHomeActivity.this, DealerIntentActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });

        PaymentApproveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SRHomeActivity.this, ViewSRPaymentsActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });

        ComplaintRaiseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, SRComplaintActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });

        AddPaymentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, SRAddPaymentActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });
        RequestServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, RequestServiceActivity.class);
                i.setType("viaSr");
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });
        PostMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, SRPostMessageActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });
        ExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, SRExpenseActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });
        AddVisitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, SRAddVisitActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });

        CreditDebitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SRHomeActivity.this, SrCreditDebitActivity.class);
                i.putExtra("SRUsername",SRUsername);
                startActivity(i);
            }
        });

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null)
        {
            actionBar.setTitle("SR Dashboard");
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.srmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_alert)
        {
            Toast.makeText(this, "New Alerts", Toast.LENGTH_SHORT).show();
        }
        if(item.getItemId() == R.id.nav_logout)
        {
            Intent i = new Intent(SRHomeActivity.this, LoginActivity.class);
            Toast.makeText(this, "Logout Successful..", Toast.LENGTH_SHORT).show();
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
    }
}