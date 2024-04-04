package com.app.superdistributor;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SRAddVisitActivity extends AppCompatActivity {
    String username;
    Spinner visitType;
    Button selectDealersBtn , submitVisitBtn;
    DatabaseReference database;
    boolean[] choosenDealers;
    ArrayList<String> visitTypeList, dealerUserNamesList , dealerNamesList ;
    HashMap<String,String> selectedDealers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sradd_visit);

        username = getIntent().getStringExtra("SRUsername");
        database = FirebaseDatabase.getInstance().getReference();

        visitType = findViewById(R.id.selectVisitType);

        selectDealersBtn = findViewById(R.id.selectDealersBtn);
        submitVisitBtn = findViewById(R.id.submitVisitBtn);

        visitTypeList = new ArrayList<>();
        visitTypeList.add("General");
        visitTypeList.add("Product Demonstration");
        visitTypeList.add("Sales Visit");
        visitTypeList.add("Technical Support");
        visitTypeList.add("Feedback and Relationship Building");
        ArrayAdapter<String> visits = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, visitTypeList);
        visitType.setAdapter(visits);

        dealerUserNamesList = new ArrayList<>();
        dealerNamesList = new ArrayList<>();
        selectedDealers = new HashMap<>();

        database.child("SRs")
                .child(username).child("myDealers")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if (snap.getValue() != null) {
                                dealerUserNamesList.add((snap.getKey().toString()));
                                dealerNamesList.add(snap.getValue().toString());
                            }
                        }
                        Log.d("Dealers" , dealerUserNamesList.toString());
                        Log.d("Deal",dealerNamesList.toString());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });


        selectDealersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedDealers.clear();
                database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        String[] allDealers = dealerUserNamesList.toArray(new String[0]);
                        choosenDealers = new boolean[dealerUserNamesList.size()];
                        for (int i = 0; i < dealerUserNamesList.size(); i++) {
                            allDealers[i] = dealerNamesList.get(i)+" : "+ dealerUserNamesList.get(i);
                        }
                        Log.d("Dealer Names" , Arrays.toString(allDealers));


                        AlertDialog.Builder builder = new AlertDialog.Builder(SRAddVisitActivity.this);

                        // set title
                        builder.setTitle("Select Dealers");

                        // set dialog non cancelable
                        builder.setCancelable(false);
                        builder.setMultiChoiceItems(allDealers, choosenDealers, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                // check condition
                                if (b) {
                                    // when checkbox selected
                                    // Add position  in lang list
                                    selectedDealers.put(dealerUserNamesList.get(i),dealerNamesList.get(i));
                                    // Sort array list
                                } else {
                                    // when checkbox unselected
                                    // Remove position from langList
                                    selectedDealers.remove(dealerUserNamesList.get(i));
                                }
                            }
                        });
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Snackbar.make(getWindow().getDecorView().getRootView(), "Dealers Selected :\n"+selectedDealers.values().toString(), 8000).setTextMaxLines(10).show();
                            }
                        });

                        builder.setNeutralButton("Select All", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                selectedDealers.clear();
                                for (int i = 0 ; i < dealerUserNamesList.size() ; i++) {
                                    selectedDealers.put(dealerUserNamesList.get(i),dealerNamesList.get(i));
                                }
                                Snackbar.make(getWindow().getDecorView().getRootView(), "All Dealers Selected", 1000).setTextMaxLines(10).show();
//                                Snackbar.make(getWindow().getDecorView().getRootView(), "Dealers Selected :\n"+selectedDealers.values().toString(), 8000).setTextMaxLines(10).show();
                            }
                        });

                        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // dismiss dialog
                                selectedDealers.clear();
                                Snackbar.make(getWindow().getDecorView().getRootView(), "No Dealers Selected", 8000).setTextMaxLines(10).show();
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        submitVisitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedDealers.size() == 0){
                    Toast.makeText(SRAddVisitActivity.this, "Please select dealers",Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String,Object> visitDetails = new HashMap<>();
                    visitDetails.put("To Dealers",selectedDealers);
                    visitDetails.put("Visit Type", visitType.getSelectedItem().toString());

                    database.child("SRs").child(username)
                            .child("Visits")
                            .child(getDate())
                            .updateChildren(visitDetails)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    visitType.setSelection(0);
                                    selectedDealers.clear();
                                    Toast.makeText(SRAddVisitActivity.this, "Added Today's visit!",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SRAddVisitActivity.this, "There was an error adding the visit...",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }
    public String getDate() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy");
        return sdf.format(cal.getTime());
    }
}