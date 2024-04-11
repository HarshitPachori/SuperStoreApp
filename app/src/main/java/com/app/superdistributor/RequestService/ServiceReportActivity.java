package com.app.superdistributor.RequestService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.superdistributor.MyAmountAdapter;
import com.app.superdistributor.R;
import com.app.superdistributor.models.AmountModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ServiceReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference database;
    MyServiceAdapter myAdapter;
    ArrayList<ServiceModel> list;
    String selectedDate;
    private Button mPickDateButton;
    String username,usertype;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_report);
username = getIntent().getStringExtra("Username");
usertype = getIntent().getType();
        recyclerView = findViewById(R.id.serviceReportRV);
        database = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        myAdapter = new MyServiceAdapter(this, list);
        recyclerView.setAdapter(myAdapter);

        mPickDateButton = findViewById(R.id.filterdateET);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        selectedDate = materialDatePicker.getHeaderText();
                        Toast.makeText(ServiceReportActivity.this, ""+selectedDate, Toast.LENGTH_SHORT).show();

                        filter(selectedDate);
                    }
                });
Log.d("usertype",usertype);
        database.child(usertype).child("RequestServices").child("ReplacementByDealer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                           for(DataSnapshot snapshot1 : dataSnapshot.getChildren()){
                               ServiceModel serviceModel = snapshot1.getValue(ServiceModel.class);
                               Log.d("servicemodel",serviceModel.toString());
                               list.add(serviceModel);
                           }
                        }

                        myAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void filter(String selectedDate) {
        ArrayList<ServiceModel> filteredlist = new ArrayList<ServiceModel>();

        // running a for loop to compare elements.
        for (ServiceModel item : list) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getDateOfPurchase().toLowerCase().contains(selectedDate.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            myAdapter.filterList(filteredlist);
        }
    }
}