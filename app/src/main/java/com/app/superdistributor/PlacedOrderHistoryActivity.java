package com.app.superdistributor;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.models.PlacedOrderModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PlacedOrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;

    MyPlacedOrderAdapter adapter;

    ArrayList<PlacedOrderModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_placed_order_history);
        String DealerName = getIntent().getStringExtra("DealerName");
        recyclerView = findViewById(R.id.placedOrdersReportRV);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        adapter = new MyPlacedOrderAdapter(this, list);
        recyclerView.setAdapter(adapter);
        database.child("Dealers").child(DealerName).child("Orders").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    PlacedOrderModel placedOrderModel = dataSnapshot.getValue(PlacedOrderModel.class);
                    Log.d("placedorder",placedOrderModel.toString());
                    list.add(placedOrderModel);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}