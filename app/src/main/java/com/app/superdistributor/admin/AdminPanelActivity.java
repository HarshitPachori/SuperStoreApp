package com.app.superdistributor.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.superdistributor.AdminViewSRComplaintsActivity;
import com.app.superdistributor.AdminViewSRPaymentsActivity;
import com.app.superdistributor.LoginActivity;
import com.app.superdistributor.MyMessagesActivity;
import com.app.superdistributor.MyProducts.ViewProductList;
import com.app.superdistributor.R;
import com.app.superdistributor.admin.notification.AdminNotificationActivity;
import com.app.superdistributor.admin.paymenthistory.AmountOverviewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminPanelActivity extends AppCompatActivity {

    ImageView AddCreditDebitBtn, ViewCreditDebitBtn, AddUserBtn, ViewUserBtn, AddProductBtn, AddOfferBtn, srInfoBtn;
    private ProgressDialog LoadingBar;
    DatabaseReference database;
    ArrayList<AmountOverviewModel> list;
    TextView Head;
    ImageView myMessages, AdminNotification, AdminLogout;

    String Username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        Username = getIntent().getStringExtra("Username");

        AdminNotification = findViewById(R.id.adminNotification);
        AdminLogout = findViewById(R.id.adminLogout);
        myMessages = findViewById(R.id.my_messages);

        database = FirebaseDatabase.getInstance().getReference();

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("Admin").child("Notifications").child("ProductConfirmation").exists())
                {
                    AdminNotification.setImageResource(R.drawable.baseline_notifications_active_24);
                }
                else
                {
                    AdminNotification.setImageResource(R.drawable.baseline_notifications_24);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AdminNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminPanelActivity.this, AdminNotificationActivity.class);
                i.putExtra("username",Username);
                startActivity(i);
            }
        });

        AdminLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AdminPanelActivity.this, LoginActivity.class);

                SharedPreferences sharedPreferences = getSharedPreferences("LoginSharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("UserType", "");
                myEdit.commit();

                startActivity(i);
            }
        });

        Head = findViewById(R.id.adminhead);

        Head.setText("Welcome "+Username);

        AddCreditDebitBtn = findViewById(R.id.addcreditdebitbtn);
        //ViewCreditDebitBtn = findViewById(R.id.viewcreditdebitbtn);

        AddProductBtn = findViewById(R.id.addProductBtn);


        AddUserBtn = findViewById(R.id.adduserbtn);
        ViewUserBtn = findViewById(R.id.viewUserbtn);
        AddOfferBtn = findViewById(R.id.addofferbtn);
        srInfoBtn = findViewById(R.id.sr_info_btn);

        LoadingBar=new ProgressDialog(this);
        AddProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelActivity.this);

                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.product_dialog, null);
                builder.setView(customLayout);

                // add a button
                builder.setPositiveButton("Add Product", (dialog, which) -> {
                    // send data from the AlertDialog to the Activity
                    EditText productName = customLayout.findViewById(R.id.productNameET);
                    EditText productID = customLayout.findViewById(R.id.productIDET);

                    if(productName.getText().toString().equals(""))
                    {
                        Toast.makeText(AdminPanelActivity.this, "Please enter product name", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        sendDialogDataToActivity(productName.getText().toString(), productID.getText().toString());
                    }

                });
                builder.setNegativeButton("View Products", (dialog, which) -> {
                    Intent i = new Intent(AdminPanelActivity.this, ViewProductList.class);
                    startActivity(i);
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }

            // Do something with the data coming from the AlertDialog
            private void sendDialogDataToActivity(String newproductName, String newproductID) {
                //Toast.makeText(AdminPanelActivity.this, ""+data, Toast.LENGTH_SHORT).show();

                LoadingBar.setTitle("Adding Product");
                LoadingBar.setMessage("Please wait we are adding product in our database..");
                LoadingBar.setCanceledOnTouchOutside(false);
                LoadingBar.show();



                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        database.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                HashMap<String,Object> map = new HashMap<>();
                                map.put("ProductID",newproductID);
                                map.put("Name",newproductName);

                                database.child("Products").child(newproductID).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        LoadingBar.dismiss();
                                        Toast.makeText(AdminPanelActivity.this, "Product Added!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

        });
        list = new ArrayList<>();
        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    if(!dataSnapshot.getKey().toString().equals("RequestServices")) {
                        AmountOverviewModel amountOverviewModel = new AmountOverviewModel();
                        amountOverviewModel.setName(dataSnapshot.child("Name").getValue().toString());
                        amountOverviewModel.setUserName(dataSnapshot.child("UserName").getValue().toString());
                        amountOverviewModel.setCurrentBalance(dataSnapshot.child("CurrentBalance").getValue().toString());
//                                list.add(dataSnapshot.getValue(AmountOverviewModel.class));
                        list.add(amountOverviewModel);
                        Log.d("Added to list", amountOverviewModel.toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
        AddCreditDebitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, AddDebitCreditActivity.class);
                intent.putExtra("Username",Username);
                intent.setType("viaAdmin");
                if(list.isEmpty()){
                    Toast.makeText(AdminPanelActivity.this, "No data available currently", Toast.LENGTH_SHORT).show();
                }
                else {
                    startActivity(intent);
                }
            }
        });


        /*ViewCreditDebitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPanelActivity.this, ViewCreditDebitActivity.class);
                startActivity(intent);
            }
        });*/

        AddUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String[] users = {"Dealer", "SR", "Technician", "Manager", "Accountant"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelActivity.this);
                builder.setTitle("Select User Type");
                builder.setItems(users, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(users[i].equals("Dealer"))
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, AddDealerActivity.class);
                            intent.putExtra("task","addDealer");
                            intent.putExtra("username","");
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                        else if(users[i].equals("SR"))
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, AddSRActivity.class);
                            intent.putExtra("task","addSR");
                            intent.putExtra("Username",Username);
                            intent.putExtra("username","");
                            startActivity(intent);
                        }
                        else if(users[i].equals("Manager"))
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, AddManagerActivity.class);
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                        else if(users[i].equals("Accountant"))
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, AddAccountantActivity.class);
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, AddTechnicianActivity.class);
                            intent.putExtra("task","addTechnician");
                            intent.putExtra("username","");
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();

            }
        });

        myMessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminPanelActivity.this, MyMessagesActivity.class).putExtra("Username",Username));
            }
        });

        AddOfferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String[] users = {"Offer", "Scheme"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelActivity.this);
                builder.setTitle("Select Type");
                builder.setItems(users, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(users[i].equals("Offer"))
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, AddOfferActivity.class);
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, AddSchemeActivity.class);
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();

            }
        });

        ViewUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String[] users = {"Dealer", "SR", "Technician"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelActivity.this);
                builder.setTitle("Select User Type");
                builder.setItems(users, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(users[i].equals("Dealer"))
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, ViewDealerActivity.class);
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                        else if(users[i].equals("SR"))
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, ViewSRActivity.class);
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent = new Intent(AdminPanelActivity.this, ViewTechnicianActivity.class);
                            intent.putExtra("Username",Username);
                            startActivity(intent);
                        }
                    }
                });
                builder.show();
            }
        });

        srInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] infoType = {"Complaints", "Payments"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelActivity.this);
                builder.setTitle("Select info type");

                List<String> srNames = new ArrayList<>();
                List<String> srUsernames = new ArrayList<>();
                database.child("SRs").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            if(!snap.getKey().toString().equals("RequestServices")) {
                                srUsernames.add(snap.getKey().toString());
                                srNames.add(snap.child("Name").getValue().toString());
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AdminPanelActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setItems(infoType, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        selectSrsDialogBox(infoType[i], srNames, srUsernames);
                        Log.d("fetch from db", infoType[i]+"    "+srNames.toString() + "    "+ srUsernames.toString());

                    }
                });
                builder.show();
            }
        });
    }

    private void selectSrsDialogBox(String infoType, List<String> srNames, List<String> srUsernames) {
        String[] srNamesArr = new String[srNames.size()];
        int i = 0;
        for(String srName: srNames){
            srNamesArr[i++] = srName;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminPanelActivity.this);
        builder.setTitle("Select SR");
        builder.setItems(srNamesArr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(AdminPanelActivity.this, AdminViewSRComplaintsActivity.class);

                if (infoType.equals("Payments")) {
                    intent = new Intent(AdminPanelActivity.this, AdminViewSRPaymentsActivity.class);
                }
                intent.putExtra("SRUsername", srUsernames.get(which));
                startActivity(intent);
            }
        });
        builder.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}