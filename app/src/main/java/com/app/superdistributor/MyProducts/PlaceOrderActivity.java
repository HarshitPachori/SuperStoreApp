package com.app.superdistributor.MyProducts;

import static android.app.ProgressDialog.show;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.CheckoutActivity;
import com.app.superdistributor.R;
import com.app.superdistributor.SRHomeActivity;
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
import java.util.UUID;

public class PlaceOrderActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyProductAdapter myAdapter;
    ArrayList<Products> list;

    Button PlaceFinalOrderButton;
    Map<String, Object> orderStatusMap;
    public static Map<String, Object> orderMap;

    String SRUsername, DealerName;
    ArrayList<String> dealersNames;
    Spinner placeOrderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);
        dealersNames = new ArrayList<>();

        SRUsername = getIntent().getStringExtra("SRUsername");

        PlaceFinalOrderButton = findViewById(R.id.placefinalorderbtn);

        recyclerView = findViewById(R.id.productList);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();


        orderMap = new HashMap<>();
        orderStatusMap = new HashMap<>();

        orderStatusMap.put("Status", "Pending");
        placeOrderSpinner = findViewById(R.id.placeOrderSpinner);
        database.child("SRs").child(SRUsername).child("myDealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dealersNames.add(dataSnapshot.getKey());
                }
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(PlaceOrderActivity.this, android.R.layout.simple_spinner_item, dealersNames);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                placeOrderSpinner.setAdapter(spinnerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        placeOrderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                DealerName = parent.getItemAtPosition(position).toString();
                Log.d("dataaaa", DealerName);
                myAdapter = new MyProductAdapter(PlaceOrderActivity.this, list, DealerName);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        String uniqueId = UUID.randomUUID().toString();

        PlaceFinalOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (orderMap.size() == 0) {
                    Toast.makeText(PlaceOrderActivity.this, "Please add products to place order..", Toast.LENGTH_SHORT).show();
                } else if (DealerName.isEmpty() || DealerName == null) {
                    Toast.makeText(PlaceOrderActivity.this, "Please select dealer name..", Toast.LENGTH_SHORT).show();
                } else {
                    database.child("Dealers").child(DealerName).child("Orders").child(uniqueId).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            orderMap.clear();
                            Intent i = new Intent(PlaceOrderActivity.this, CheckoutActivity.class);
                            i.putExtra("SRUsername", SRUsername);
                            startActivity(i);
                        }
                    });

                    database.child("Admin").child("Notifications").child("ProductConfirmation").child("SRs").child(SRUsername).child(uniqueId).updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {


//                orderStatusMap.put("Status","Pending");

                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            database.child("Dealers").child(DealerName).child("Orders").child(uniqueId).updateChildren(orderStatusMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(PlaceOrderActivity.this, "Order submitted to admin...", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(PlaceOrderActivity.this, SRHomeActivity.class);
                                    i.putExtra("SRUsername", SRUsername);
                                    startActivity(i);
                                }
                            });
                        }

                    });
                }
                //Toast.makeText(PlaceOrderActivity.this, ""+String.valueOf(checkArray.size()), Toast.LENGTH_SHORT).show();
            }
        });

        database.child("Products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    Products products = dataSnapshot.getValue(Products.class);

                    // Toast.makeText(BuyProductActivity.this, name.toString(), Toast.LENGTH_SHORT).show();
                    list.add(products);


                }
                if (myAdapter == null) {
                    myAdapter = new MyProductAdapter(PlaceOrderActivity.this, list, DealerName);
                    recyclerView.setAdapter(myAdapter);
                } else {
                    myAdapter.notifyDataSetChanged();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}