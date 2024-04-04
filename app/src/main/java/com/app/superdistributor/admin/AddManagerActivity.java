package com.app.superdistributor.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class AddManagerActivity extends AppCompatActivity {

    TextInputEditText ManagerNameET, ManagerUserNameET, ManagerPhoneET, ManagerEmailET, ManagerPasswordET;
    Button SubmitManagerDetailsBtn;
    DatabaseReference database;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager);

        LoadingBar=new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference();

        ManagerNameET = findViewById(R.id.managerNameET);
        ManagerUserNameET = findViewById(R.id.managerUserNameET);
        ManagerPhoneET  = findViewById(R.id.managerPhoneET);
        ManagerEmailET  = findViewById(R.id.managerEmailET);
        ManagerPasswordET  = findViewById(R.id.managerPasswordET);

        SubmitManagerDetailsBtn = findViewById(R.id.submitmanagerDetailsBtn);

        SubmitManagerDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ManagerNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddManagerActivity.this, "Please enter manager name", Toast.LENGTH_SHORT).show();
                }
                else if(ManagerUserNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddManagerActivity.this, "Please enter manager user name", Toast.LENGTH_SHORT).show();
                }
                else if(ManagerPhoneET.getText().toString().equals(""))
                {
                    Toast.makeText(AddManagerActivity.this, "Please enter manager phone", Toast.LENGTH_SHORT).show();
                }
                else if(ManagerEmailET.getText().toString().equals(""))
                {
                    Toast.makeText(AddManagerActivity.this, "Please enter manager email", Toast.LENGTH_SHORT).show();
                }
                else if(ManagerPasswordET.getText().toString().equals(""))
                {
                    Toast.makeText(AddManagerActivity.this, "Please enter manager password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    LoadingBar.setTitle("Please Wait..");
                    LoadingBar.setMessage("Please Wait while we are checking our database...");
                    LoadingBar.show();

                    String ManagerName = ManagerNameET.getText().toString();
                    String ManagerUsername = ManagerUserNameET.getText().toString();
                    String ManagerPhone = ManagerPhoneET.getText().toString();
                    String ManagerEmail = ManagerEmailET.getText().toString();
                    String ManagerPassword = ManagerPasswordET.getText().toString();

                    createManagerAccount(ManagerName, ManagerUsername, ManagerPhone, ManagerEmail, ManagerPassword);
                }
            }
        });
    }

    private void createManagerAccount(String ManagerName, String ManagerUsername, String ManagerPhone, String ManagerEmail, String ManagerPassword) {

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Managers").child(ManagerUsername).exists()))
                {
                    HashMap<String,Object> managers = new HashMap<>();
                    managers.put("Name", ManagerName);
                    managers.put("UserName", ManagerUsername);
                    managers.put("Phone", ManagerPhone);
                    managers.put("Email", ManagerEmail);
                    managers.put("Password", ManagerPassword);
                    managers.put("CurrentBalance", "0");

                    database.child("Managers").child(ManagerUsername).updateChildren(managers)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    LoadingBar.dismiss();
                                    Toast.makeText(AddManagerActivity.this, "Manager Added.", Toast.LENGTH_SHORT).show();
                                    ManagerNameET.setText("");
                                    ManagerUserNameET.setText("");
                                    ManagerPhoneET.setText("");
                                    ManagerEmailET.setText("");
                                    ManagerPasswordET.setText("");
                                }
                            });
                }
                else {
                    LoadingBar.dismiss();
                    Toast.makeText(AddManagerActivity.this, "Manager with this user name already exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}