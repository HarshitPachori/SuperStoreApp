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

public class ViewTechnicianActivity extends AppCompatActivity {

    private ListView TechnicianDataLV;

    ArrayList<String> techniciansList, technicianNamesList;
    DatabaseReference database;
    String Username;

    ArrayList<String> techniciansArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_technician);

        Username = getIntent().getStringExtra("Username");

        TechnicianDataLV = findViewById(R.id.viewTechnicianlv);
        techniciansList = new ArrayList<String>();
        technicianNamesList = new ArrayList<>();
        initializeListView();
    }

    private void initializeListView() {
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, techniciansList);

        database = FirebaseDatabase.getInstance().getReference();

        //techniciansList.clear();
        technicianNamesList.clear();
        techniciansList.clear();
        techniciansArrayList.clear();
        database.child("Technicians").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot snap : snapshot.getChildren()) {
                    techniciansArrayList.add(snap.getKey());
                    technicianNamesList.add(snap.child("Name").getValue().toString());
                }

                for(int i=0; i<techniciansArrayList.size(); i++)
                {
                    techniciansList.add("Technician "+String.valueOf(i+1)+" - "+technicianNamesList.get(i));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        TechnicianDataLV.setAdapter(adapter);

        TechnicianDataLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(ViewSRActivity.this, ""+srsList.get(i).split("-")[1].trim(), Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(ViewTechnicianActivity.this, AddTechnicianActivity.class);
                intent.putExtra("task","viewTechnician");
                intent.putExtra("username",techniciansArrayList.get(i).trim());
                intent.putExtra("Username",Username);
                startActivity(intent);
            }
        });
        TechnicianDataLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(ViewTechnicianActivity.this);
                builder.setTitle("Action!");
                builder.setMessage("Do You want to delete technician?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        database.child("Technicians").child(techniciansArrayList.remove(i)).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(ViewTechnicianActivity.this, AdminPanelActivity.class);
                                        adapter.notifyDataSetChanged();
                                        Toast.makeText(ViewTechnicianActivity.this, "Technician - "+techniciansList.get(i)+" is deleted successfully from system.", Toast.LENGTH_SHORT).show();
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