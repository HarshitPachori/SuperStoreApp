package com.app.superdistributor.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class AddSRActivity extends AppCompatActivity {

    TextInputEditText SRNameET, SRUserNameET, SRPhoneET, SREmailET, SRPasswordET, SRTargetAmtET;
    Button SubmitSRDetailsBtn , SelectDealersBtn;
    DatabaseReference database;
    private ProgressDialog LoadingBar;
    String Task, Username, CurrentController;
    TextView AddSRHead;
    boolean[] choosenDealers;

    ArrayList<String> dealerUserNames , dealerNames;
    HashMap<String,Object> selectedDealers, dealerDetails;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sractivity);

        AddSRHead = findViewById(R.id.addSRHead);

        Task = getIntent().getStringExtra("task");
        Username = getIntent().getStringExtra("username");
        CurrentController = getIntent().getStringExtra("Username");

        LoadingBar=new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference();

        SRNameET = findViewById(R.id.srNameET);
        SRUserNameET = findViewById(R.id.srUserNameET);
        SRPhoneET  = findViewById(R.id.srPhoneET);
        SREmailET  = findViewById(R.id.srEmailET);
        SRPasswordET = findViewById(R.id.srPasswordET);
        SRTargetAmtET = findViewById(R.id.srTargetET);

        SubmitSRDetailsBtn = findViewById(R.id.submitSRDetailsBtn);
        SelectDealersBtn = findViewById(R.id.selectDealersBtn);

        dealerUserNames = new ArrayList<>();
        dealerNames = new ArrayList<>();
        selectedDealers = new HashMap<>();

        if(Task.equals("viewSR")) //TODO : check for changes to be made
        {
            SubmitSRDetailsBtn.setText("Update SR");
            AddSRHead.setText("Update SR");
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    SRNameET.setText(snapshot.child("SRs").child(Username).child("Name").getValue().toString());
                    SRUserNameET.setText(snapshot.child("SRs").child(Username).child("UserName").getValue().toString());
                    SRPhoneET.setText(snapshot.child("SRs").child(Username).child("Phone").getValue().toString());
                    SREmailET.setText(snapshot.child("SRs").child(Username).child("Email").getValue().toString());
                    SRPasswordET.setText(snapshot.child("SRs").child(Username).child("Password").getValue().toString());
                    SRTargetAmtET.setText(snapshot.child("SRs").child(Username).child("SRSSalesStatus").child("TargetAmount").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }

        SelectDealersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize alert dialog
                database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        dealerUserNames.clear();
                        dealerNames.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if(snap.child("UserName").getValue() != null ) {
                                dealerUserNames.add(snap.child("UserName").getValue().toString());
                                dealerNames.add(snap.child("Name").getValue().toString());
                            }
                        }
                        String[] allDealers = dealerUserNames.toArray(new String[0]);
                        choosenDealers = new boolean[dealerUserNames.size()];
                        for (int i = 0; i < dealerUserNames.size(); i++) {
                            allDealers[i] = dealerNames.get(i)+" - "+ dealerUserNames.get(i);
                        }
                        Log.d("Dealer Names" , Arrays.toString(allDealers));


                        AlertDialog.Builder builder = new AlertDialog.Builder(AddSRActivity.this);

                        // set title
                        builder.setTitle("Select Dealer");

                        // set dialog non cancelable
                        builder.setCancelable(false);
                        builder.setMultiChoiceItems(allDealers, choosenDealers, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                // check condition
                                if (b) {
                                    // when checkbox selected
                                    // Add position  in lang list
                                    dealerDetails = new HashMap<>();
                                    dealerDetails.put("Name",dealerNames.get(i));
                                    dealerDetails.put("Balance",0);
                                    selectedDealers.put(dealerUserNames.get(i),dealerDetails);
//                                    dealerDetails.clear();
                                    // Sort array list
                                } else {
                                    // when checkbox unselected
                                    // Remove position from langList
                                    selectedDealers.remove(dealerUserNames.get(i));
                                }
                            }
                        });
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                Snackbar.make(getWindow().getDecorView().getRootView(), "Dealers Selected :\n"+ selectedDealers.values().toString(), 8000).setTextMaxLines(10).show();
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
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        SubmitSRDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SRNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddSRActivity.this, "Please enter SR name", Toast.LENGTH_SHORT).show();
                }
                else if(SRUserNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddSRActivity.this, "Please enter SR user name", Toast.LENGTH_SHORT).show();
                }
                else if(SRPhoneET.getText().toString().equals(""))
                {
                    Toast.makeText(AddSRActivity.this, "Please enter SR phone", Toast.LENGTH_SHORT).show();
                }
                else if(SREmailET.getText().toString().equals(""))
                {
                    Toast.makeText(AddSRActivity.this, "Please enter SR email", Toast.LENGTH_SHORT).show();
                }
                else if(SRPasswordET.getText().toString().equals(""))
                {
                    Toast.makeText(AddSRActivity.this, "Please enter SR password", Toast.LENGTH_SHORT).show();
                }
                else if(selectedDealers.size() == 0)
                {
                    Toast.makeText(AddSRActivity.this, "Please select dealers", Toast.LENGTH_SHORT).show();
                }
                else if(SRTargetAmtET.getText().toString().equals(""))
                {
                    Toast.makeText(AddSRActivity.this, "Please enter SR Target Amount", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    LoadingBar.setTitle("Please Wait..");
                    LoadingBar.setMessage("Please Wait while we are checking our database...");
                    LoadingBar.show();

                    String srName = SRNameET.getText().toString();
                    String srUserName = SRUserNameET.getText().toString();
                    String srPhone = SRPhoneET.getText().toString();
                    String srEmail = SREmailET.getText().toString();
                    String srPassword = SRPasswordET.getText().toString();
                    String srTargetAmt = SRTargetAmtET.getText().toString();

                    createSRAccount(srName, srPhone, srEmail, srPassword, srUserName, selectedDealers, srTargetAmt );
                }
            }
        });
    }

    private void createSRAccount(String srName, String srPhone, String srEmail, String srPassword, String srUserName , HashMap<String,Object> selectedDealers, String srTargetAmt) {
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(Task.equals("addSR"))
                {
                    if (!(snapshot.child("SRs").child(srUserName).exists()))
                    {
                        HashMap<String,Object> srs = new HashMap<>();
                        srs.put("Name", srName);
                        srs.put("UserName", srUserName);
                        srs.put("Phone", srPhone);
                        srs.put("Email", srEmail);
                        srs.put("Password", srPassword);
                        srs.put("myDealers" , selectedDealers);

                        Toast.makeText(AddSRActivity.this,selectedDealers.values().toString(), Toast.LENGTH_SHORT).show();

                        HashMap<String,Object> srsSalesStatus = new HashMap<>();
                        srsSalesStatus.put("SalesDone",0);
                        srsSalesStatus.put("RemainingTarget",0); // TODO : Target Amount
                        srsSalesStatus.put("TotalOutstanding",0);
                        srsSalesStatus.put("TargetAmount",Integer.parseInt(srTargetAmt));

                        srs.put("SRSSalesStatus",srsSalesStatus);

                        database.child("SRs").child(srUserName).updateChildren(srs)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        LoadingBar.dismiss();
                                        Toast.makeText(AddSRActivity.this, "SR Added.", Toast.LENGTH_SHORT).show();
                                        SRNameET.setText("");
                                        SRUserNameET.setText("");
                                        SRPhoneET.setText("");
                                        SREmailET.setText("");
                                        SRPasswordET.setText("");
                                        Intent i = new Intent(AddSRActivity.this, AdminPanelActivity.class);
                                        i.putExtra("Username",CurrentController);
                                        startActivity(i);
                                    }
                                });
                    }
                    else {
                        LoadingBar.dismiss();
                        Toast.makeText(AddSRActivity.this, "SR with this user name already exist.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    HashMap<String,Object> srs = new HashMap<>();
                    srs.put("Name", srName);
                    srs.put("UserName", srUserName);
                    srs.put("Phone", srPhone);
                    srs.put("Email", srEmail);
                    srs.put("Password", srPassword);
                    srs.put("myDealers" , selectedDealers);

                    database.child("SRs").child(srUserName).updateChildren(srs)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    LoadingBar.dismiss();
                                    Toast.makeText(AddSRActivity.this, "SR Updated.", Toast.LENGTH_SHORT).show();
                                    SRNameET.setText("");
                                    SRUserNameET.setText("");
                                    SRPhoneET.setText("");
                                    SREmailET.setText("");
                                    SRPasswordET.setText("");
                                    Intent i = new Intent(AddSRActivity.this, AdminPanelActivity.class);
                                    i.putExtra("Username",CurrentController);
                                    startActivity(i);
                                }
                            });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}