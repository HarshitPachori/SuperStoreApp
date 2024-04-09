package com.app.superdistributor.admin.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.app.superdistributor.NotificationAdapter;
import com.app.superdistributor.NotificationItemModel;
import com.app.superdistributor.R;
import com.app.superdistributor.sr.dealerorders.DealerOrder;
import com.app.superdistributor.sr.dealerorders.DealerOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminNotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    NotificationAdapter myAdapter;
    ArrayList<NotificationItemModel> list;
    NotificationItemModel notificationItemModel;

    String username;

    ArrayList<String> srNames;
    ArrayList<String> technicianNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        recyclerView = findViewById(R.id.notificationRLV);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        Toast.makeText(this, "Pending reminders are in red", Toast.LENGTH_LONG).show();

        list = new ArrayList<>();
        myAdapter = new NotificationAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        username = getIntent().getStringExtra("username");
        srNames = new ArrayList<>();
        technicianNames = new ArrayList<>();

        database.child("SRs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals("RequestServices")) {
                        srNames.add(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child("Technicians").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if (!dataSnapshot.getKey().equals("RequestServices")) {
                        technicianNames.add(dataSnapshot.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child("Admin").child("Notifications").child("ProductConfirmation").child("SRs")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("Status").getValue().toString().equals("Pending")) {
                                NotificationItemModel notificationItemModel = new NotificationItemModel();
                                notificationItemModel.setNotificationType("SR Product Confirmation");
                                notificationItemModel.setNotificationTag(dataSnapshot.getKey().toString());
                                notificationItemModel.
                                        setNotificationDesc("Name : " + dataSnapshot.child("Name").getValue().toString() +
                                                "\nPrice : " + dataSnapshot.child("Price").getValue().toString() +
                                                "\nProductID : " + dataSnapshot.child("ProductID").getValue().toString() +
                                                "\nQuantity : " + dataSnapshot.child("Qty").getValue().toString() +
                                                "\nPlaced by : " + dataSnapshot.child("PlacedBy").getValue().toString()
                                        );

                                if (dataSnapshot.child("Reminder").exists()) notificationItemModel
                                        .setNotificationPriority(dataSnapshot.child("Reminder").getValue().toString());
                                list.add(notificationItemModel);
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        database.child("Dealers").child("RequestServices").child("RegisterComplaints")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("Status").getValue().toString().equals("Pending")) {
                                notificationItemModel = new NotificationItemModel();
                                notificationItemModel.setNotificationType("Dealer Complaint");
                                notificationItemModel.setNotificationTag(dataSnapshot.child("CustomerName").getValue().toString());
                                notificationItemModel.
                                        setNotificationDesc("Phone : " + dataSnapshot.child("PhoneNumber").getValue().toString() +
                                                "\nModel : " + dataSnapshot.child("ModelNumber").getValue().toString() +
                                                "\nPurchased on : " + dataSnapshot.child("DateOfPurchase").getValue().toString() +
                                                "\nSerial No. : " + dataSnapshot.child("SerialNumber").getValue().toString());
                                Log.d("snapReport", dataSnapshot.child("ReportUrl").getValue(String.class));
                                if (dataSnapshot.child("ReportUrl").exists()) {
                                    notificationItemModel.setReportUrl(dataSnapshot.child("ReportUrl").getValue(String.class));
                                }
                                if (dataSnapshot.child("Reminder").exists()) notificationItemModel
                                        .setNotificationPriority(dataSnapshot.child("Reminder").getValue().toString());
                                list.add(notificationItemModel);
//                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        database.child("Dealers").child("RequestServices").child("ReplacementByDealer")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            if (dataSnapshot.child("Status").getValue().toString().equals("Pending")) {
                                NotificationItemModel notificationItemModel = new NotificationItemModel();
                                notificationItemModel.setNotificationType("Replacement by Dealer");
                                notificationItemModel.setNotificationTag(dataSnapshot.child("CustomerName").getValue().toString());
                                notificationItemModel.
                                        setNotificationDesc("Phone : " + dataSnapshot.child("PhoneNumber").getValue().toString() +
                                                "\nModel : " + dataSnapshot.child("ModelNumber").getValue().toString() +
                                                "\nPurchased on : " + dataSnapshot.child("DateOfPurchase").getValue().toString() +
                                                "\nSerial No. : " + dataSnapshot.child("SerialNumber").getValue().toString() +
                                                "\nNew Serial No. : " + dataSnapshot.child("NewProductSerialNumber").getValue().toString());
                                Log.d("snapReport", dataSnapshot.child("ReportUrl").getValue(String.class));
                                if (dataSnapshot.child("ReportUrl").exists()) {
                                    notificationItemModel.setReportUrl(dataSnapshot.child("ReportUrl").getValue(String.class));
                                }
                                if (dataSnapshot.child("Reminder").exists()) notificationItemModel
                                        .setNotificationPriority(dataSnapshot.child("Reminder").getValue().toString());
                                list.add(notificationItemModel);
                                myAdapter.notifyDataSetChanged();

                            }
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        database.child("Grievances")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            NotificationItemModel notificationItemModel = new NotificationItemModel();
                            notificationItemModel.setNotificationType("Grievance");
                            notificationItemModel.setNotificationTag(dataSnapshot.getKey());
                            notificationItemModel.setNotificationDesc(dataSnapshot.getValue().toString());
                            list.add(notificationItemModel);
                            myAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        database.child("SRs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot srSnapshot : dataSnapshot.getChildren()) {
                    DataSnapshot expensesSnapshot = srSnapshot.child("Expenses");

                    for (DataSnapshot expenseSnapshot : expensesSnapshot.getChildren()) {
                        String status = expenseSnapshot.child("Status").getValue(String.class);

                        if ("Pending".equals(status)) {
                            String expenseType = expenseSnapshot.child("Type").getValue(String.class);
                            String amount = expenseSnapshot.child("Amount").getValue(String.class);
                            String date = expenseSnapshot.child("Date").getValue(String.class);
                            String reminder = expenseSnapshot.child("Reminder").getValue(String.class);
                            String expenseId = expenseSnapshot.child("Id").getValue(String.class);
                            String expenseUsername = expenseSnapshot.child("Name").getValue(String.class);
                            NotificationItemModel notificationItemModel = new NotificationItemModel();
                            notificationItemModel.setNotificationType("Expense");
                            notificationItemModel.setNotificationTag(expenseUsername);
                            notificationItemModel.setNotificationDesc("Expense Type : " + expenseType +
                                    "\nAmount : " + amount +
                                    "\nDate : " + date +
                                    "\nStatus : " + status);

                            if (reminder != null) {
                                notificationItemModel.setNotificationPriority(reminder);
                            }

                            if (username.equals("admin")) list.add(notificationItemModel);
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                }

                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });

        myAdapter.notifyDataSetChanged();
    }

    private int getItemPosition(NotificationItemModel notificationItemModel) {
        int position = 0;
        for (NotificationItemModel item : list) {
            if (item.equals(notificationItemModel)) {
                return position;
            }
            position++;
        }
        return position;
    }
}