package com.app.superdistributor;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.superdistributor.admin.AdminPanelActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {

    private EditText AccountId,Password;
    private Button Login;
    private ProgressDialog LoadingBar;
    String mypassword, myaccountid;
    private FirebaseAuth mAuth;
    TextView ForgetPassword;
    DatabaseReference database;
    Spinner userTypeDropdown;

    CheckBox RemeberPasswordCB;

    ArrayList<String> userTypeArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        userTypeDropdown = findViewById(R.id.loginUserTypeDropDown);

        RemeberPasswordCB = findViewById(R.id.rememberPasswordCB);

        database = FirebaseDatabase.getInstance().getReference();

        AccountId=(EditText)findViewById(R.id.accountid);
        Password=(EditText)findViewById(R.id.loginpassword);
        Login=(Button)findViewById(R.id.loginbutton);
        ForgetPassword = findViewById(R.id.forgetPassword);

        userTypeArrayList.add("Select User Type");
        userTypeArrayList.add("Admin");
        userTypeArrayList.add("Manager");
        userTypeArrayList.add("Accountant");
        userTypeArrayList.add("Dealer");
        userTypeArrayList.add("S.R.");
        userTypeArrayList.add("Technician");

        ArrayAdapter<String> usersTypeadapter = new ArrayAdapter<>(this, R.layout.simple_spinner_dropdown_item_colored , userTypeArrayList);
        userTypeDropdown.setAdapter(usersTypeadapter);

        ForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(i);
            }
        });


        LoadingBar=new ProgressDialog(this);
        mypassword=Password.getText().toString();
        myaccountid="+91"+AccountId.getText().toString().trim();



        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(TextUtils.isEmpty(AccountId.getText().toString()))
                {
                    Toast.makeText(LoginActivity.this, "Please enter your account id..", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(Password.getText().toString()))
                {
                    Toast.makeText(LoginActivity.this, "Please enter your password...", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    LoginUser();
                }
            }
        });
    }

    private void LoginUser() {

        LoadingBar.setTitle("Login Account");
        LoadingBar.setMessage("Please wait while we are checking our credentials..");
        LoadingBar.setCanceledOnTouchOutside(false);
        LoadingBar.show();

        myaccountid = AccountId.getText().toString();
        mypassword = Password.getText().toString();

        AllowAccessToUser(myaccountid,mypassword);
    }

    private void AllowAccessToUser(final String myaccountid, final String mypassword) {


        String userType;

        if(userTypeDropdown.getSelectedItem().toString().equals("Dealer"))
        {
            userType = "Dealers";
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child(userType).child(myaccountid).exists()))
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Account with this id doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                    else if(snapshot.child(userType).child(myaccountid).child("Password").getValue().toString().equals(mypassword))
                    {

                        if(RemeberPasswordCB.isChecked())
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);

                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString("UserType", userType);
                            myEdit.putString("UserName", myaccountid);
                            myEdit.putString("UserPassword", mypassword);

                            myEdit.commit();
                        }

                        LoadingBar.dismiss();
                        Intent i = new Intent(LoginActivity.this, DealerHomeActivity.class);
                        i.putExtra("DealerName",myaccountid);
                        startActivity(i);
                    }
                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(userTypeDropdown.getSelectedItem().toString().equals("S.R."))
        {
            userType = "SRs";
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child(userType).child(myaccountid).exists()))
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Account with this id doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                    else if(snapshot.child(userType).child(myaccountid).child("Password").getValue().toString().equals(mypassword))
                    {

                        if(RemeberPasswordCB.isChecked())
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);

                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString("UserType", userType);
                            myEdit.putString("UserName", myaccountid);
                            myEdit.putString("UserPassword", mypassword);

                            myEdit.commit();
                        }

                        LoadingBar.dismiss();
                        Intent i = new Intent(LoginActivity.this, SRHomeActivity.class);
                        i.putExtra("SRUsername", myaccountid);
                        startActivity(i);
                    }
                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(userTypeDropdown.getSelectedItem().toString().equals("Admin"))
        {
            userType = "Admin";
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if(snapshot.child(userType).child("Username").getValue().toString().equals(myaccountid))
                    {
                        if(snapshot.child(userType).child("Password").getValue().toString().equals(mypassword))
                        {

                            if(RemeberPasswordCB.isChecked())
                            {
                                SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);

                                SharedPreferences.Editor myEdit = sharedPreferences.edit();

                                myEdit.putString("UserType", userType);
                                myEdit.putString("UserName", myaccountid);
                                myEdit.putString("UserPassword", mypassword);

                                myEdit.commit();
                            }


                            LoadingBar.dismiss();
                            Intent i = new Intent(LoginActivity.this, AdminPanelActivity.class);
                            i.putExtra("Username","admin");
                            startActivity(i);
                        }
                        else
                        {
                            LoadingBar.dismiss();
                            Toast.makeText(LoginActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Please check your username", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(userTypeDropdown.getSelectedItem().toString().equals("Manager"))
        {
            userType = "Managers";
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child(userType).child(myaccountid).exists()))
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Account with this id doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                    else if(snapshot.child(userType).child(myaccountid).child("Password").getValue().toString().equals(mypassword))
                    {

                        if(RemeberPasswordCB.isChecked())
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);

                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString("UserType", userType);
                            myEdit.putString("UserName", myaccountid);
                            myEdit.putString("UserPassword", mypassword);

                            myEdit.commit();
                        }

                        LoadingBar.dismiss();
                        Intent i = new Intent(LoginActivity.this, AdminPanelActivity.class);
                        i.putExtra("Username",snapshot.child(userType).child(myaccountid).child("Name").getValue().toString());
                        startActivity(i);
                    }
                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else if(userTypeDropdown.getSelectedItem().toString().equals("Accountant"))
        {
            userType = "Accountants";
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child(userType).child(myaccountid).exists()))
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Account with this id doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                    else if(snapshot.child(userType).child(myaccountid).child("Password").getValue().toString().equals(mypassword))
                    {

                        if(RemeberPasswordCB.isChecked())
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);

                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString("UserType", userType);
                            myEdit.putString("UserName", myaccountid);
                            myEdit.putString("UserPassword", mypassword);

                            myEdit.commit();
                        }


                        LoadingBar.dismiss();
                        Intent i = new Intent(LoginActivity.this, AdminPanelActivity.class);
                        i.putExtra("Username",snapshot.child(userType).child(myaccountid).child("Name").getValue().toString());
                        startActivity(i);
                    }
                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            userType = "Technicians";
            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child(userType).child(myaccountid).exists()))
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Account with this id doesn't exist.", Toast.LENGTH_SHORT).show();
                    }
                    else if(snapshot.child(userType).child(myaccountid).child("Password").getValue().toString().equals(mypassword))
                    {

                        if(RemeberPasswordCB.isChecked())
                        {
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);

                            SharedPreferences.Editor myEdit = sharedPreferences.edit();

                            myEdit.putString("UserType", userType);
                            myEdit.putString("UserName", myaccountid);
                            myEdit.putString("UserPassword", mypassword);

                            myEdit.commit();
                        }


                        LoadingBar.dismiss();
                        Intent i = new Intent(LoginActivity.this, TechnicianHomeActivity.class);
                        i.putExtra("Username",snapshot.child(userType).child(myaccountid).child("UserName").getValue().toString());
                        startActivity(i);
                    }
                    else
                    {
                        LoadingBar.dismiss();
                        Toast.makeText(LoginActivity.this, "Please check your password", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }

    @Override
    public void onBackPressed() {

    }
}