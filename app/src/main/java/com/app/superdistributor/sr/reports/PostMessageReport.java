package com.app.superdistributor.sr.reports;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.adapters.ExpenseAdapter;
import com.app.superdistributor.sr.reports.adapters.PostMessageAdapter;
import com.app.superdistributor.sr.reports.models.ExpenseModel;
import com.app.superdistributor.sr.reports.models.PostMessageModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostMessageReport extends AppCompatActivity {

    PostMessageAdapter adapter;
    ArrayList<PostMessageModel> list;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    String SRUsername;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_message_report);
        SRUsername = getIntent().getStringExtra("SRUsername");
        recyclerView = findViewById(R.id.postMessageReportRV);
        progressBar = findViewById(R.id.postMessageProgressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new PostMessageAdapter(this, list);
        recyclerView.setAdapter(adapter);
        Log.d("sruser1",SRUsername);
        progressBar.setVisibility(View.VISIBLE);

        databaseReference.child("SRs").child(SRUsername).child("MessageToDealers").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot srSnapshot : dataSnapshot.getChildren()) {
                    Log.d("msgdata",srSnapshot.getValue().toString());
                    PostMessageModel model = srSnapshot.getValue(PostMessageModel.class);
                    Log.d("modelmsg",model.toString());
                    list.add(model);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}