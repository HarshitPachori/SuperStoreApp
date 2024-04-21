package com.app.superdistributor.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.superdistributor.MyAmountAdapter;
import com.app.superdistributor.R;
import com.app.superdistributor.models.AmountModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GiveGetAmountActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    MyAmountAdapter myAdapter;
    ArrayList<AmountModel> list;

    String DealerUsername, DealerName, Username,usertype;

    TextView GiveGetUsername,getGiveGetRoundedUsername;

    Button YouGaveBtn, YouGotButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_get_amount);

        DealerUsername = getIntent().getStringExtra("dealerUsername");
        DealerName = getIntent().getStringExtra("dealerName");
        usertype = getIntent().getType();
        Username = getIntent().getStringExtra("Username");

        YouGaveBtn = findViewById(R.id.yougaveBtn);
        YouGotButton = findViewById(R.id.yougotBtn);

       if(usertype.equals("viaDealer")){
           YouGaveBtn.setVisibility(View.GONE);
           YouGotButton.setVisibility(View.GONE);
       }

        YouGaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GiveGetAmountActivity.this, AddCreditDebitActivity.class);
                i.putExtra("DealerUsername",DealerUsername);
                i.putExtra("TransactionType","Debit");
                i.putExtra("DealerName",DealerName);
                i.putExtra("Username",Username);
                startActivity(i);
            }
        });

        YouGotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GiveGetAmountActivity.this, AddCreditDebitActivity.class);
                i.putExtra("DealerUsername",DealerUsername);
                i.putExtra("TransactionType","Credit");
                i.putExtra("DealerName",DealerName);
                i.putExtra("Username",Username);
                startActivity(i);
            }
        });



        getGiveGetRoundedUsername = findViewById(R.id.roundedNameTv);
        GiveGetUsername = findViewById(R.id.roundedUserNameTv);

        recyclerView = findViewById(R.id.accountBalanceRL);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyAmountAdapter(this,list);
        recyclerView.setAdapter(myAdapter);

        database.child("Dealers").child(DealerUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snap: snapshot.child("Credit").getChildren())
                {
                    AmountModel amountModel = snap.getValue(AmountModel.class);
                    list.add(amountModel);

                    list.sort((o1,o2)->o1.getTime().compareToIgnoreCase(o2.getTime()));
                }
                for(DataSnapshot snap: snapshot.child("Debit").getChildren())
                {
                    AmountModel amountModel1 = snap.getValue(AmountModel.class);
                    String amount = amountModel1.getAmount();
                    String date = amountModel1.getDate();
                    String balance = amountModel1.getBalance();
                    String time = amountModel1.getTime();

                    AmountModel amountModel2 = new AmountModel();
                    amountModel2.setAmount("-"+amount);
                    amountModel2.setDate(date);
                    amountModel2.setBalance(balance);
                    amountModel2.setTime(time);
                    list.add(amountModel2);

                    list.sort((o1,o2)->o1.getTime().compareToIgnoreCase(o2.getTime()));
                }


                getGiveGetRoundedUsername.setText(DealerName.substring(0,1).toUpperCase());
                GiveGetUsername.setText(DealerName);

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}