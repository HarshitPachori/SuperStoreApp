package com.app.superdistributor.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewDealerActivity extends AppCompatActivity {

    private ListView DealerDataLV;

    ArrayList<String> dealersList, dealerNamesList;
    DatabaseReference database;

    ArrayList<String> dealersArrayList;
    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dealer);

        Username = getIntent().getStringExtra("Username");

        DealerDataLV = findViewById(R.id.viewDealerlv);

        dealersList = new ArrayList<String>();
        dealersArrayList = new ArrayList<>();
        dealerNamesList = new ArrayList<>();

        initializeListView();
    }

    private void initializeListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dealersList);

        database = FirebaseDatabase.getInstance().getReference();

        dealerNamesList.clear();
        dealersList.clear();
        dealersArrayList.clear();
        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    if(!snap.getKey().toString().equals("RequestServices")) {
                        dealersArrayList.add(snap.getKey());
                        dealerNamesList.add(snap.child("Name").getValue().toString());
                    }
                }

                for(int i=0; i<dealersArrayList.size(); i++)
                {
                    dealersList.add("Dealer "+String.valueOf(i+1)+" - "+dealerNamesList.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DealerDataLV.setAdapter(adapter);

        DealerDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ViewDealerActivity.this, ""+dealersList.get(i).split("-")[1].trim(), Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ViewDealerActivity.this, AddDealerActivity.class);
                intent.putExtra("task","viewDealer");
                intent.putExtra("username",dealersArrayList.get(i).trim());
                intent.putExtra("Username",Username);
                startActivity(intent);
            }
        });

        DealerDataLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(ViewDealerActivity.this);
                builder.setTitle("Action!");
                builder.setMessage("Do You want to delete dealer?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child("Dealers").child(dealersArrayList.remove(i)).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(ViewDealerActivity.this, AdminPanelActivity.class);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(ViewDealerActivity.this, "Dealer - "+dealersList.get(i)+" is deleted successfully from system.", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                });
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });
                builder.show();

                return false;
            }
        });
    }
}