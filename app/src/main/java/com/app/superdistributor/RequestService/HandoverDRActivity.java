package com.app.superdistributor.RequestService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class HandoverDRActivity extends AppCompatActivity {

    boolean[] selectedLanguage;
    ArrayList<Integer> langList = new ArrayList<>();
    String[] serialNumberArray = {};

    Button HandoverAddProductBtn;

    private ListView HandoverDataLV;

    DatabaseReference database;

    ArrayList<String> handoverDataList, SelectedItems;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handover_dractivity);

        userType = getIntent().getType();

        HandoverAddProductBtn = findViewById(R.id.handoverAddProductBtn);

        SelectedItems = new ArrayList<>();

        HandoverDataLV = findViewById(R.id.handOverItemListView);
        handoverDataList = new ArrayList<String>();

        database = FirebaseDatabase.getInstance().getReference();

        HandoverAddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize alert dialog
                database.child("Dealers").child("RequestServices").child("ReplacementByDealer").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        handoverDataList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if(snap.child("Status").getValue().toString().equals("Accepted")) {
                                handoverDataList.add(snap.child("SerialNumber").getValue().toString() );
                            }
                        }
                        serialNumberArray = handoverDataList.toArray(new String[0]);

                        AlertDialog.Builder builder = new AlertDialog.Builder(HandoverDRActivity.this);

                        // set title
                        builder.setTitle("Select Product Serial Number");

                        // set dialog non cancelable
                        builder.setCancelable(false);

                        builder.setMultiChoiceItems(serialNumberArray, selectedLanguage, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                // check condition
                                if (b) {
                                    // when checkbox selected
                                    // Add position  in lang list
                                    langList.add(i);
                                    // Sort array list
                                    Collections.sort(langList);
                                } else {
                                    // when checkbox unselected
                                    // Remove position from langList
                                    langList.remove(Integer.valueOf(i));
                                }
                            }
                        });

                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // Initialize string builder
                                StringBuilder stringBuilder = new StringBuilder();
                                // use for loop
                                for (int j = 0; j < langList.size(); j++) {
                                    // concat array value
                                    stringBuilder.append(serialNumberArray[langList.get(j)]);
                                    // check condition
                                    SelectedItems.add(serialNumberArray[langList.get(j)]);
                                    final ArrayAdapter<String> adapter = new ArrayAdapter<String>(HandoverDRActivity.this, android.R.layout.simple_list_item_1, SelectedItems);
                                    adapter.notifyDataSetChanged();
                                    HandoverDataLV.setAdapter(adapter);
                                }
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // dismiss dialog
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        Toast.makeText(HandoverDRActivity.this, "These are the approved products", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });
    }
}