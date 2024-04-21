package com.app.superdistributor.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.DealerHomeActivity;
import com.app.superdistributor.MyAmountAdapter;
import com.app.superdistributor.R;
import com.app.superdistributor.admin.paymenthistory.AccountBalanceOverviewAdapter;
import com.app.superdistributor.admin.paymenthistory.AmountOverviewModel;
import com.app.superdistributor.models.AmountModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddDebitCreditActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    int giveAmount, getAmount;
    TextView YougiveBalance, YougetBalance, TotalBalance;
    ArrayList<String> giveAmountArrayList, getAmmountArrayList;

    AccountBalanceOverviewAdapter myAdapter;
    ArrayList<AmountOverviewModel> list;

    public static String Username;
    String userType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debit_credit);

        Username = getIntent().getStringExtra("Username");
        userType = getIntent().getType();
        recyclerView = findViewById(R.id.accountBalanceOverviewRL);

        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new AccountBalanceOverviewAdapter(this, list,userType);
        recyclerView.setAdapter(myAdapter);

        giveAmountArrayList = new ArrayList<>();
        getAmmountArrayList = new ArrayList<>();

        YougiveBalance = findViewById(R.id.giveAmountTxt);
        YougetBalance = findViewById(R.id.getAmountTxt);
        TotalBalance = findViewById(R.id.balanceAmountTxt);



        if (userType.equals("viaDealer")) {
            database.child("Dealers").child(Username).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.d("creditt", dataSnapshot.toString());
                        if (!dataSnapshot.getKey().equals("RequestServices")) {
                            if (dataSnapshot.getKey().equals("Credit")) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    Log.d("credittt", snap.toString());

                                    giveAmountArrayList.add(snap.child("Amount").getValue().toString());
                                }
                            }
                            if (dataSnapshot.getKey().equals("Debit")) {
                                for (DataSnapshot snap : dataSnapshot.getChildren()) {
                                    Log.d("credittt", snap.toString());

                                    getAmmountArrayList.add(snap.child("Amount").getValue().toString());
                                }
                            }
                        }
                    }
                    giveAmount = 0;
                    getAmount = 0;

                    for (int i = 0; i < giveAmountArrayList.size(); i++) {
                        giveAmount = giveAmount + Integer.parseInt(giveAmountArrayList.get(i));
                    }
                    for (int i = 0; i < getAmmountArrayList.size(); i++) {
                        getAmount = getAmount + Integer.parseInt(getAmmountArrayList.get(i));
                    }

                    YougiveBalance.setText("\u20B9 " + String.valueOf(giveAmount));
                    YougetBalance.setText("\u20B9 " + String.valueOf(getAmount));
                    TotalBalance.setText("\u20B9 " + String.valueOf(getAmount - giveAmount));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

//
                        if(dataSnapshot.getKey().equals(Username)){
                            Log.d("dataaaaa",dataSnapshot.toString());
                            list.add(dataSnapshot.getValue(AmountOverviewModel.class));
                        }
                    }
                    myAdapter.notifyDataSetChanged();
//                    if (list.get(0).getName() == null) {
//                        Toast.makeText(AddDebitCreditActivity.this, "No data available currently", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        } else {
            database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                        if (!dataSnapshot.getKey().equals("RequestServices")) {
                            for (DataSnapshot snap : dataSnapshot.child("Credit").getChildren()) {
                                giveAmountArrayList.add(snap.child("Amount").getValue().toString());
                            }
                            for (DataSnapshot snap : dataSnapshot.child("Debit").getChildren()) {

                                getAmmountArrayList.add(snap.child("Amount").getValue().toString());
                            }
                        }
                    }
                    giveAmount = 0;
                    getAmount = 0;

                    for (int i = 0; i < giveAmountArrayList.size(); i++) {
                        giveAmount = giveAmount + Integer.parseInt(giveAmountArrayList.get(i));
                    }
                    for (int i = 0; i < getAmmountArrayList.size(); i++) {
                        getAmount = getAmount + Integer.parseInt(getAmmountArrayList.get(i));
                    }

                    YougiveBalance.setText("\u20B9 " + String.valueOf(giveAmount));
                    YougetBalance.setText("\u20B9 " + String.valueOf(getAmount));
                    TotalBalance.setText("\u20B9 " + String.valueOf(getAmount - giveAmount));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        if (!dataSnapshot.getKey().equals("RequestServices"))
                            list.add(dataSnapshot.getValue(AmountOverviewModel.class));
                    }
                    if (list.get(0).getName() == null) {
                        Toast.makeText(AddDebitCreditActivity.this, "No data available currently", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i;
//        if (userType.equals("viaDealer")) {
//            i = new Intent(AddDebitCreditActivity.this, DealerHomeActivity.class);
//        } else {
//            i = new Intent(AddDebitCreditActivity.this, AdminPanelActivity.class);
//        }
//        i.putExtra("Username", Username);
//        startActivity(i);
    }
}