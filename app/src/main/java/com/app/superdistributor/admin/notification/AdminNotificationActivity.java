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
import com.app.superdistributor.payments.DealerPaymentModel;
import com.app.superdistributor.sr.dealerorders.DealerOrder;
import com.app.superdistributor.sr.dealerorders.DealerOrderAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AdminNotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    NotificationAdapter myAdapter;
    ArrayList<NotificationItemModel> list;
    NotificationItemModel notificationItemModel;

    String username;
    String usertype;
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
        usertype = getIntent().getType();
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
                                    if (username.equals("admin") || srNames.contains(username)) {
                                        list.add(notificationItemModel);
                                    }
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
                                    if ("admin".equals(username) || srNames.contains(username)) {
                                        list.add(notificationItemModel);
                                    }
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
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
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
                                    if ("admin".equals(username) || srNames.contains(username)) {
                                        list.add(notificationItemModel);
                                    }
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
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            NotificationItemModel notificationItemModel = new NotificationItemModel();
                            notificationItemModel.setNotificationType("Grievance");
                            notificationItemModel.setNotificationTag(dataSnapshot.getKey());
                            notificationItemModel.setNotificationDesc(dataSnapshot.getValue().toString());
                            if ("admin".equals(username) || srNames.contains(username)) {
                                list.add(notificationItemModel);
                            }
                            myAdapter.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
        database.child("SRs").addListenerForSingleValueEvent(new ValueEventListener() {
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
                            notificationItemModel.setNotificationTag(expenseId);
                            notificationItemModel.setNotificationDesc("Expense Type : " + expenseType +
                                    "\nAmount : " + amount +
                                    "\nDate : " + date +
                                    "\nStatus : " + status);

                            if (reminder != null) {
                                notificationItemModel.setNotificationPriority(reminder);
                            }

                            if ("admin".equals(username)) {
                                list.add(notificationItemModel);
                            }
                            myAdapter.notifyDataSetChanged();
                        }
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
//        database.child("Dealers").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if (!Objects.equals(dataSnapshot.getKey(), "RequestServices")) {
//                        DataSnapshot dealerSnapshot = dataSnapshot.child("Payments");
//                        String paymentMethod;
//
////                      Log.d("snapshot",dealerSnapshot.toString());
//                        for (DataSnapshot snapshot1 : dealerSnapshot.getChildren()) {
//                            paymentMethod = snapshot1.getKey();
//                            for (DataSnapshot dataSnapshot1 : snapshot1.getChildren()) {
//                                NotificationItemModel model = new NotificationItemModel();
//                                model.setNotificationType("Dealer Payment");
//                                model.setNotificationTag(paymentMethod);
//                                String status = dataSnapshot1.child("Status").getValue(String.class);
//                                model.setNotificationDesc("Payment from User : " + dataSnapshot1.child("User").getValue(String.class) + "\nAmount : " + dataSnapshot1.child("Amount").getValue(String.class) + "\nTo User : " + dataSnapshot1.child("UserType").getValue(String.class) + "\nStatus : " + status);
//                                if (status.equals("Pending")) list.add(model);
//                            }
//                        }
//                    }
//                }
//                myAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        database.child("SRs").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String srname = dataSnapshot.getKey();
                    for (DataSnapshot snapshot1 : dataSnapshot.child("MessageToDealers").getChildren()) {
                        String id = snapshot1.getKey();

                        String dealersString = snapshot1.child("Dealers").getValue(String.class);

                        ArrayList<String> dealersArr = parseDealersString(dealersString);
                      if(dealersArr.contains(username)){
                          String msg = snapshot1.child("Message").getValue(String.class);
                          String audioUrl = snapshot1.child("AudioUrl").getValue(String.class);
                          String date = snapshot1.child("Date").getValue(String.class);
                          NotificationItemModel model = new NotificationItemModel();
                          model.setNotificationId(id);
                          model.setNotificationType("Message To Dealer");
                          model.setNotificationTag(username);
                          model.setNotificationDesc("Message from Sr: " + srname + "\nMessage : " + msg + "\nTo Dealer : " + username + "\nDate : " + date);
                          model.setReportUrl(audioUrl);
                          list.add(model);
                      }
                    }
                }
                myAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        myAdapter.notifyDataSetChanged();
    }

    private ArrayList<String> parseDealersString(String dealersString) {
        ArrayList<String> dealersArr = new ArrayList<>();
        if (dealersString != null && dealersString.length() > 2) {
            dealersString = dealersString.substring(1, dealersString.length() - 1);
            String[] dealers = dealersString.split(", ");
            dealersArr.addAll(Arrays.asList(dealers));
        }
        Log.d("msgdeal",dealersArr.toString());
        return dealersArr;
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