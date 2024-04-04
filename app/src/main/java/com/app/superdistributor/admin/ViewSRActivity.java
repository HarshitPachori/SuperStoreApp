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

public class ViewSRActivity extends AppCompatActivity {

    private ListView SRDataLV;

    ArrayList<String> srsList, srNamesList;
    DatabaseReference database;

    ArrayList<String> srsArrayList = new ArrayList<>();
    String Username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sractivity);

        Username = getIntent().getStringExtra("Username");
        SRDataLV = findViewById(R.id.viewSRlv);
        srsList = new ArrayList<String>();
        srNamesList = new ArrayList<>();
        initializeListView();
    }

    private void initializeListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, srsList);

        database = FirebaseDatabase.getInstance().getReference();

        srsList.clear();
        srNamesList.clear();
        srsArrayList.clear();
        database.child("SRs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    srsArrayList.add(snap.getKey());
                    srNamesList.add(snap.child("Name").getValue().toString());
                }

                for(int i=0; i<srsArrayList.size(); i++)
                {
                    srsList.add("SR "+String.valueOf(i+1)+" - "+srNamesList.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        SRDataLV.setAdapter(adapter);

        SRDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ViewSRActivity.this, ""+srsList.get(i).split("-")[1].trim(), Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ViewSRActivity.this, AddSRActivity.class);
                intent.putExtra("task","viewSR");
                intent.putExtra("username",srsArrayList.get(i).trim());
                intent.putExtra("Username",Username);
                startActivity(intent);
            }
        });
        SRDataLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(ViewSRActivity.this);
                builder.setTitle("Action!");
                builder.setMessage("Do You want to delete SR?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child("SRs").child(srsArrayList.remove(i)).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(ViewSRActivity.this, AdminPanelActivity.class);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(ViewSRActivity.this, "SR - "+srsList.get(i)+" is deleted successfully from system.", Toast.LENGTH_SHORT).show();
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