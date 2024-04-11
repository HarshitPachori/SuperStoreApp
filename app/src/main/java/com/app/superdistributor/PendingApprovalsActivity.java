package com.app.superdistributor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PendingApprovalsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    NotificationAdapter myAdapter;
    ArrayList<NotificationItemModel> list;
    NotificationItemModel notificationItemModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_approvals);

        recyclerView = findViewById(R.id.notificationRLV);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        myAdapter = new NotificationAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        database.child("Admin").child("Notifications").child("ProductConfirmation").child("SRs")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot notificationSnapshot : dataSnapshot.getChildren()) {
                                String status = notificationSnapshot.child("Status").getValue(String.class);
                                if ("Pending".equals(status)) {
                                    NotificationItemModel notificationItemModel = new NotificationItemModel();
                                    notificationItemModel.setNotificationType("SR Product Confirmation");
                                    notificationItemModel.setNotificationTag(dataSnapshot.getKey());
                                    notificationItemModel.setNotificationId(notificationSnapshot.getKey());
                                    notificationItemModel.
                                            setNotificationDesc("Product Name : " + notificationSnapshot.child("Name").getValue().toString() +
                                                    "\nPrice : " + notificationSnapshot.child("Price").getValue().toString() +
                                                    "\nProductID : " + notificationSnapshot.child("ProductID").getValue().toString() +
                                                    "\nQuantity : " + notificationSnapshot.child("Qty").getValue().toString() +
                                                    "\nPlaced by : " + notificationSnapshot.child("PlacedBy").getValue().toString()
                                            );

                                    if (notificationSnapshot.child("Reminder").exists())
                                        notificationItemModel
                                                .setNotificationPriority(notificationSnapshot.child("Reminder").getValue().toString());
                                    Log.d("adminnn", notificationItemModel.toString());
                                    list.add(notificationItemModel);
                                }
                            }
                        }
                        myAdapter.notifyDataSetChanged();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        database.child("Dealers").child("RequestServices").child("RegisterComplaints")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                if (snapshot1.child("Status").getValue().toString().equals("Pending")) {
                                    notificationItemModel = new NotificationItemModel();
                                    notificationItemModel.setNotificationType("Dealer Complaint");
                                    notificationItemModel.setNotificationTag(snapshot1.child("CustomerName").getValue().toString());
                                    notificationItemModel.setNotificationId(snapshot1.getKey());
                                    notificationItemModel.
                                            setNotificationDesc("Phone : " + snapshot1.child("PhoneNumber").getValue().toString() +
                                                    "\nModel : " + snapshot1.child("ModelNumber").getValue().toString() +
                                                    "\nPurchased on : " + snapshot1.child("DateOfPurchase").getValue().toString() +
                                                    "\nSerial No. : " + snapshot1.child("SerialNumber").getValue().toString());
                                    Log.d("snapReport", snapshot1.child("ReportUrl").getValue(String.class));
                                    if (snapshot1.child("ReportUrl").exists()) {
                                        notificationItemModel.setReportUrl(snapshot1.child("ReportUrl").getValue(String.class));
                                    }
                                    if (snapshot1.child("Reminder").exists()) notificationItemModel
                                            .setNotificationPriority(snapshot1.child("Reminder").getValue().toString());
                                    list.add(notificationItemModel);
//                                myAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        database.child("Dealers").child("RequestServices").child("ReplacementByDealer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                                if (snapshot1.child("Status").getValue().toString().equals("Pending")) {
                                    NotificationItemModel notificationItemModel = new NotificationItemModel();
                                    notificationItemModel.setNotificationType("Replacement by Dealer");
                                    notificationItemModel.setNotificationTag(snapshot1.child("CustomerName").getValue().toString());
                                    notificationItemModel.setNotificationId(snapshot1.getKey());
                                    notificationItemModel.
                                            setNotificationDesc("Phone : " + snapshot1.child("PhoneNumber").getValue().toString() +
                                                    "\nModel : " + snapshot1.child("ModelNumber").getValue().toString() +
                                                    "\nPurchased on : " + snapshot1.child("DateOfPurchase").getValue().toString() +
                                                    "\nSerial No. : " + snapshot1.child("SerialNumber").getValue().toString() +
                                                    "\nNew Serial No. : " + snapshot1.child("NewProductSerialNumber").getValue().toString());
                                    Log.d("snapReport", snapshot1.child("ReportUrl").getValue(String.class));
                                    if (snapshot1.child("ReportUrl").exists()) {
                                        notificationItemModel.setReportUrl(snapshot1.child("ReportUrl").getValue(String.class));
                                    }
                                    if (snapshot1.child("Reminder").exists()) notificationItemModel
                                            .setNotificationPriority(snapshot1.child("Reminder").getValue().toString());
                                    list.add(notificationItemModel);
                                    myAdapter.notifyDataSetChanged();

                                }
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
        myAdapter.notifyDataSetChanged();
    }
}