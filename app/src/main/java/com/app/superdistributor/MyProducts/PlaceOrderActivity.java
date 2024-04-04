package com.app.superdistributor.MyProducts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.CheckoutActivity;
import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PlaceOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyProductAdapter myAdapter;
    ArrayList<Products> list;

    Button PlaceFinalOrderButton;

    public static Map<String,Object> orderMap;

    String DealerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        DealerName = getIntent().getStringExtra("DealerName");

        PlaceFinalOrderButton = findViewById(R.id.placefinalorderbtn);

        recyclerView = findViewById(R.id.productList);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyProductAdapter(this,list, DealerName);
        recyclerView.setAdapter(myAdapter);

        orderMap = new HashMap<>();

        PlaceFinalOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(orderMap.size() == 0)
                {
                    Toast.makeText(PlaceOrderActivity.this, "Please add products to place order..", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    database.child("Dealers").child(DealerName).child("Orders").updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    orderMap.clear();
                                    Intent i = new Intent(PlaceOrderActivity.this, CheckoutActivity.class);
                                    i.putExtra("DealerName",DealerName);
                                    startActivity(i);
                                }
                            });
                }
                //Toast.makeText(PlaceOrderActivity.this, ""+String.valueOf(checkArray.size()), Toast.LENGTH_SHORT).show();
            }
        });
        database.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){


                    Products products = dataSnapshot.getValue(Products.class);

                    // Toast.makeText(BuyProductActivity.this, name.toString(), Toast.LENGTH_SHORT).show();
                    list.add(products);


                }
                myAdapter.notifyDataSetChanged();

                //Toast.makeText(BuyProductActivity.this, list.get(0).getProductName().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}