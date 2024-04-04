package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.app.superdistributor.sr.complaint.ComplaintAdapter;
import com.app.superdistributor.sr.dealerorders.DealerOrder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class TechnicianHandleComplaintsActivity extends AppCompatActivity {

    ProgressBar progressBar;
    RecyclerView recyclerView;
    DatabaseReference database;
    TechnicianComplaintsAdapter myAdapter;
    ArrayList<TechnicianComplaints> list, listAll;


    String technicianUsername;
    boolean history;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_technician_handle_complaints);

        technicianUsername = getIntent().getStringExtra("TechnicianUsername");
        history = getIntent().getBooleanExtra("History",false);

        progressBar =  findViewById(R.id.progressBar2);

        recyclerView = findViewById(R.id.dealerorderList);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        listAll = new ArrayList<>();
        if(history) {
            myAdapter = new TechnicianComplaintsAdapter(this, listAll, technicianUsername, true);

        }
        else {
            myAdapter = new TechnicianComplaintsAdapter(this, list, technicianUsername);
        }
        recyclerView.setAdapter(myAdapter);

        database.child("Dealers")
                .child("RequestServices").child("RegisterComplaints")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                TechnicianComplaints technicianComplaints = dataSnapshot.getValue(TechnicianComplaints.class);
                                listAll.add(technicianComplaints);
                                if(technicianComplaints.getStatus().equals("Pending") || technicianComplaints.getStatus().equals("Rescheduled"))
                                    list.add(technicianComplaints);
                                progressBar.setVisibility(View.INVISIBLE);
                        }

                        myAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        myAdapter.notifyDataSetChanged();
    }
}