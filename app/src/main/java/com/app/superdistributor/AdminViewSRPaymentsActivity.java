package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.sr.payments.PaymentStatusAdapter;
import com.app.superdistributor.sr.payments.PaymentStatusModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminViewSRPaymentsActivity extends AppCompatActivity {

    String username;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    DatabaseReference database;
    ArrayList<PaymentStatusModel> list = new ArrayList<>();
    PaymentStatusAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_srpayments);
        username = getIntent().getStringExtra("SRUsername");
        database = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.srPaymentStatusRL);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new PaymentStatusAdapter(this,list,username);
        recyclerView.setAdapter(myAdapter);

        ((TextView)findViewById(R.id.sr_payments_header)).setText("Payments: " + username);

        database.child("SRs").child(username)
                .child("myPayments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (snapshot.getChildrenCount() == 0) {
                            Toast.makeText(AdminViewSRPaymentsActivity.this, "No Pending Payments", Toast.LENGTH_LONG).show();
                            finish();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            PaymentStatusModel psm = new PaymentStatusModel();
                            psm.setName(dataSnapshot.child("Name").getValue().toString());
                            psm.setAmount(dataSnapshot.child("Amount").getValue().toString());
                            psm.setType(dataSnapshot.child("Type").getValue().toString());
                            psm.setStatus(dataSnapshot.child("Status").getValue().toString());
                            list.add(psm);
                            Log.d("Values : ", dataSnapshot.child("Name").getValue().toString() + dataSnapshot.child("Amount").getValue().toString() );
                            myAdapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}