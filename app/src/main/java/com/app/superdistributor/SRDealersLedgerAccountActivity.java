package com.app.superdistributor;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.admin.paymenthistory.AccountBalanceOverviewAdapter;
import com.app.superdistributor.admin.paymenthistory.AmountOverviewModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SRDealersLedgerAccountActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    int giveAmount, getAmount;
    TextView YougiveBalance, YougetBalance, TotalBalance;
    ArrayList<String> giveAmountArrayList, getAmmountArrayList, myDealers;

    AccountBalanceOverviewAdapter myAdapter;
    ArrayList<AmountOverviewModel> list;

    public static String SRUsername;
    String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srdealers_ledger_account);
        SRUsername = getIntent().getStringExtra("SRUsername");
        usertype = getIntent().getType();

        recyclerView = findViewById(R.id.accountBalanceOverviewRL);

        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new AccountBalanceOverviewAdapter(this,list,usertype);
        recyclerView.setAdapter(myAdapter);

        giveAmountArrayList = new ArrayList<>();
        getAmmountArrayList = new ArrayList<>();
        myDealers = new ArrayList<>();

        YougiveBalance = findViewById(R.id.giveAmountTxt);
        YougetBalance = findViewById(R.id.getAmountTxt);
        TotalBalance = findViewById(R.id.balanceAmountTxt);

        database.child("SRs").child(SRUsername).child("myDealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren() ) {
                    myDealers.add(dataSnapshot.getKey().toString());
                }
                Log.d("My Dealers" ,myDealers.toString() );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(!dataSnapshot.getKey().equals("RequestServices")){
                    if (myDealers.contains(dataSnapshot.child("UserName").getValue().toString())) {
                        list.add(dataSnapshot.getValue(AmountOverviewModel.class));
                        myAdapter.notifyItemInserted(list.size() - 1);
                        for (DataSnapshot snap : dataSnapshot.child("Credit").getChildren()) {
                            giveAmountArrayList.add(snap.child("Amount").getValue().toString());
                        }
                        for (DataSnapshot snap : dataSnapshot.child("Debit").getChildren()) {
                            getAmmountArrayList.add(snap.child("Amount").getValue().toString());
                        }
                    }
                }
                }
                giveAmount = 0;
                getAmount = 0;

                for (int i=0; i<giveAmountArrayList.size(); i++)
                {
                    giveAmount = giveAmount + Integer.parseInt(giveAmountArrayList.get(i));
                }
                for (int i=0; i<getAmmountArrayList.size(); i++)
                {
                    getAmount = getAmount + Integer.parseInt(getAmmountArrayList.get(i));
                }

                YougiveBalance.setText("\u20B9 "+String.valueOf(giveAmount));
                YougetBalance.setText("\u20B9 "+String.valueOf(getAmount));
                Map<String,Object> map = new HashMap<>();
                map.put("TotalOutstanding",String.valueOf(getAmount));
                database.child("SRs").child(SRUsername)
                        .child("SRSSalesStatus")
                                .updateChildren(map);
                TotalBalance.setText("\u20B9 "+String.valueOf(getAmount-giveAmount));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        myAdapter.notifyDataSetChanged();

    }

}