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

public class AddAccountantActivity extends AppCompatActivity {

    TextInputEditText AccountantNameET, AccountantUserNameET, AccountantPhoneET, AccountantEmailET, AccountantPasswordET;
    Button SubmitAccountantDetailsBtn;
    DatabaseReference database;
    private ProgressDialog LoadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_accountant);

        LoadingBar=new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference();

        AccountantNameET = findViewById(R.id.accountantNameET);
        AccountantUserNameET = findViewById(R.id.accountantUserNameET);
        AccountantPhoneET  = findViewById(R.id.accountantPhoneET);
        AccountantEmailET  = findViewById(R.id.accountantEmailET);
        AccountantPasswordET  = findViewById(R.id.accountantPasswordET);

        SubmitAccountantDetailsBtn = findViewById(R.id.submitaccountantDetailsBtn);

        SubmitAccountantDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AccountantNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddAccountantActivity.this, "Please enter accountant name", Toast.LENGTH_SHORT).show();
                }
                else if(AccountantUserNameET.getText().toString().equals(""))
                {
                    Toast.makeText(AddAccountantActivity.this, "Please enter accountant user name", Toast.LENGTH_SHORT).show();
                }
                else if(AccountantPhoneET.getText().toString().equals(""))
                {
                    Toast.makeText(AddAccountantActivity.this, "Please enter accountant phone", Toast.LENGTH_SHORT).show();
                }
                else if(AccountantEmailET.getText().toString().equals(""))
                {
                    Toast.makeText(AddAccountantActivity.this, "Please enter accountant email", Toast.LENGTH_SHORT).show();
                }
                else if(AccountantPasswordET.getText().toString().equals(""))
                {
                    Toast.makeText(AddAccountantActivity.this, "Please enter accountant password", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    LoadingBar.setTitle("Please Wait..");
                    LoadingBar.setMessage("Please Wait while we are checking our database...");
                    LoadingBar.show();

                    String AccountantName = AccountantNameET.getText().toString();
                    String AccountantUsername = AccountantUserNameET.getText().toString();
                    String AccountantPhone = AccountantPhoneET.getText().toString();
                    String AccountantEmail = AccountantEmailET.getText().toString();
                    String AccountantPassword = AccountantPasswordET.getText().toString();

                    createAccountantAccount(AccountantName, AccountantUsername, AccountantPhone, AccountantEmail, AccountantPassword);
                }
            }
        });
    }

    private void createAccountantAccount(String AccountantName, String AccountantUsername, String AccountantPhone, String AccountantEmail, String AccountantPassword) {

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!(snapshot.child("Accountants").child(AccountantUsername).exists()))
                {
                    HashMap<String,Object> accountants = new HashMap<>();
                    accountants.put("Name", AccountantName);
                    accountants.put("UserName", AccountantUsername);
                    accountants.put("Phone", AccountantPhone);
                    accountants.put("Email", AccountantEmail);
                    accountants.put("Password", AccountantPassword);
                    accountants.put("CurrentBalance", "0");

                    database.child("Accountants").child(AccountantUsername).updateChildren(accountants)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    LoadingBar.dismiss();
                                    Toast.makeText(AddAccountantActivity.this, "Accountant Added.", Toast.LENGTH_SHORT).show();
                                    AccountantNameET.setText("");
                                    AccountantUserNameET.setText("");
                                    AccountantPhoneET.setText("");
                                    AccountantEmailET.setText("");
                                    AccountantPasswordET.setText("");
                                }
                            });
                }
                else {
                    LoadingBar.dismiss();
                    Toast.makeText(AddAccountantActivity.this, "Accountant with this user name already exist.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}