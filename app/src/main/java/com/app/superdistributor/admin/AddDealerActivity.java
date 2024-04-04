package com.app.superdistributor.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddDealerActivity extends AppCompatActivity {

    TextInputEditText DealerNameET, DealerUserNameET, DealerPhoneET, DealerEmailET, DealerCityET, DealerPasswordET;
    Button SubmitDealerDetailsBtn;
    DatabaseReference database;
    private ProgressDialog LoadingBar;

    String Task, Username,CurrentController;
    TextView AddDealerHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_dealer);

        AddDealerHead = findViewById(R.id.addDealerHead);

        Task = getIntent().getStringExtra("task");
        Username = getIntent().getStringExtra("username");
        CurrentController = getIntent().getStringExtra("Username");

        LoadingBar=new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference();

        DealerNameET = findViewById(R.id.dealerNameET);
        DealerUserNameET = findViewById(R.id.dealerUserNameET);
        DealerPhoneET  = findViewById(R.id.dealerPhoneET);
        DealerEmailET  = findViewById(R.id.dealerEmailET);
        DealerCityET  = findViewById(R.id.dealerCityET);
        DealerPasswordET  = findViewById(R.id.dealerPasswordET);

        SubmitDealerDetailsBtn = findViewById(R.id.submitDealerDetailsBtn);


        if(Task.equals("viewDealer"))
        {
            SubmitDealerDetailsBtn.setText("Update Dealer");
            AddDealerHead.setText("Update Dealer");
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    DealerNameET.setText(snapshot.child("Dealers").child(Username).child("Name").getValue().toString());
                    DealerUserNameET.setText(snapshot.child("Dealers").child(Username).child("UserName").getValue().toString());
                    DealerPhoneET.setText(snapshot.child("Dealers").child(Username).child("Phone").getValue().toString());
                    DealerEmailET.setText(snapshot.child("Dealers").child(Username).child("Email").getValue().toString());
                    DealerCityET.setText(snapshot.child("Dealers").child(Username).child("City").getValue().toString());
                    DealerPasswordET.setText(snapshot.child("Dealers").child(Username).child("Password").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        SubmitDealerDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(DealerNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddDealerActivity.this, "Please enter dealer name", Toast.LENGTH_SHORT).show();
                }
                else if(DealerUserNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddDealerActivity.this, "Please enter dealer user name", Toast.LENGTH_SHORT).show();
                }
                else if(DealerPhoneET.getText().toString().equals(""))
                {
                    Toast.makeText(AddDealerActivity.this, "Please enter dealer phone", Toast.LENGTH_SHORT).show();
                }
                else if(DealerEmailET.getText().toString().equals(""))
                {
                    Toast.makeText(AddDealerActivity.this, "Please enter dealer email", Toast.LENGTH_SHORT).show();
                }
                else if(DealerCityET.getText().toString().equals(""))
                {
                    Toast.makeText(AddDealerActivity.this, "Please enter dealer city", Toast.LENGTH_SHORT).show();
                }
                else if(DealerPasswordET.getText().toString().equals(""))
                {
                    Toast.makeText(AddDealerActivity.this, "Please enter dealer password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    LoadingBar.setTitle("Please Wait..");
                    LoadingBar.setMessage("Please Wait while we are checking our database...");
                    LoadingBar.show();

                    String dealerName = DealerNameET.getText().toString();
                    String dealerUsername = DealerUserNameET.getText().toString();
                    String dealerPhone = DealerPhoneET.getText().toString();
                    String dealerEmail = DealerEmailET.getText().toString();
                    String dealerCity = DealerCityET.getText().toString();
                    String dealerPassword = DealerPasswordET.getText().toString();

                    createDealerAccount(dealerName, dealerUsername, dealerPhone, dealerEmail, dealerCity, dealerPassword);

                }
            }
        });
    }

    private void createDealerAccount(String dealerName, String dealerUsername, String dealerPhone, String dealerEmail, String dealerCity, String dealerPassword) {

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(Task.equals("addDealer"))
                {
                    if (!(snapshot.child("Dealers").child(dealerUsername).exists()))
                    {
                        HashMap<String,Object> dealers = new HashMap<>();
                        dealers.put("Name", dealerName);
                        dealers.put("UserName", dealerUsername);
                        dealers.put("Phone", dealerPhone);
                        dealers.put("Email", dealerEmail);
                        dealers.put("City", dealerCity);
                        dealers.put("Password", dealerPassword);
                        dealers.put("CurrentBalance", "0");

                        database.child("Dealers").child(dealerUsername).updateChildren(dealers)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        LoadingBar.dismiss();
                                        Toast.makeText(AddDealerActivity.this, "Dealer Added.", Toast.LENGTH_SHORT).show();
                                        DealerNameET.setText("");
                                        DealerUserNameET.setText("");
                                        DealerPhoneET.setText("");
                                        DealerEmailET.setText("");
                                        DealerCityET.setText("");
                                        DealerPasswordET.setText("");
                                        Intent i = new Intent(AddDealerActivity.this, AdminPanelActivity.class);
                                        i.putExtra("Username",CurrentController);
                                        startActivity(i);
                                    }
                                });
                    }
                    else {
                        LoadingBar.dismiss();
                        Toast.makeText(AddDealerActivity.this, "Dealer with this user name already exist.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    HashMap<String,Object> dealers = new HashMap<>();
                    dealers.put("Name", dealerName);
                    dealers.put("UserName", dealerUsername);
                    dealers.put("Phone", dealerPhone);
                    dealers.put("Email", dealerEmail);
                    dealers.put("City", dealerCity);
                    dealers.put("Password", dealerPassword);

                    database.child("Dealers").child(dealerUsername).updateChildren(dealers)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    LoadingBar.dismiss();
                                    Toast.makeText(AddDealerActivity.this, "Dealer Updated..", Toast.LENGTH_SHORT).show();
                                    DealerNameET.setText("");
                                    DealerUserNameET.setText("");
                                    DealerPhoneET.setText("");
                                    DealerEmailET.setText("");
                                    DealerCityET.setText("");
                                    DealerPasswordET.setText("");
                                    Intent i = new Intent(AddDealerActivity.this, AdminPanelActivity.class);
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