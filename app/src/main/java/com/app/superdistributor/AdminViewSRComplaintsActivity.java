package com.app.superdistributor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.sr.complaint.ComplaintAdapter;
import com.app.superdistributor.sr.complaint.ComplaintModel;
import com.app.superdistributor.sr.payments.PaymentStatusAdapter;
import com.app.superdistributor.sr.payments.PaymentStatusModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminViewSRComplaintsActivity extends AppCompatActivity {

    String username;
    ProgressBar progressBar;
    RecyclerView recyclerView;
    DatabaseReference database;
    ArrayList<ComplaintModel> list = new ArrayList<>();
    ComplaintAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view_srcomplaints);
        username = getIntent().getStringExtra("SRUsername");
        database = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.sr_complaint_rl);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new ComplaintAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        database.child("SRs").child(username)
                .child("Complaints")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        progressBar.setVisibility(View.INVISIBLE);
                            if (snapshot.getChildrenCount() == 0) {
                                Toast.makeText(AdminViewSRComplaintsActivity.this, "No Pending Complaints", Toast.LENGTH_LONG).show();
                                finish();
                            }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            list.add(new ComplaintModel(
                                    dataSnapshot.child("Tag").getValue().toString(),
                                    dataSnapshot.child("Description").getValue().toString(),
                                    dataSnapshot.child("Dealer").getValue().toString()
                            ));
                            Log.d("Values : ", dataSnapshot.child("Tag").getValue().toString() + dataSnapshot.child("Dealer").getValue().toString() );
                            myAdapter.notifyDataSetChanged();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }
}