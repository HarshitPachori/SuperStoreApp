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
import android.widget.ProgressBar;
import android.widget.TextView;
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
    String username, usertype;
    ProgressBar progressBar;

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

        progressBar = findViewById(R.id.serviceReportProgressBar);
progressBar.setVisibility(View.VISIBLE);
        list = new ArrayList<>();
//        myAdapter = new MyServiceAdapter(this, list);
//        recyclerView.setAdapter(myAdapter);

        mPickDateButton = findViewById(R.id.filterdateET);
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("SELECT A DATE");

        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        mPickDateButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!materialDatePicker.isVisible()) {
                            materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                        }
                    }
                });


        materialDatePicker.addOnPositiveButtonClickListener(
                new MaterialPickerOnPositiveButtonClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        selectedDate = materialDatePicker.getHeaderText();
                        TextView selectedDateTv = findViewById(R.id.serviceRepDate);
                        if(!selectedDate.isEmpty()) {
                            selectedDateTv.setText(String.format("Selected Date : %s", selectedDate));
                        }
                        Toast.makeText(ServiceReportActivity.this, "" + selectedDate, Toast.LENGTH_SHORT).show();

                        filter(selectedDate);
                    }
                });
        Log.d("usertype", usertype);
        database.child(usertype).child(username).child("RequestServices").child("ReplacementByDealer")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            for (DataSnapshot snapshot1 : dataSnapshot.getChildren()) {
                                ServiceModel serviceModel = snapshot1.getValue(ServiceModel.class);
                                Log.d("servicemodel", serviceModel.toString());
                                list.add(serviceModel);
                            }
                        }
                        // Initialize the adapter with the complete data list
                        myAdapter = new MyServiceAdapter(ServiceReportActivity.this, list);
                        recyclerView.setAdapter(myAdapter);
//                        myAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void filter(String selectedDate) {
        Log.d("item", selectedDate);
        Log.d("item", list.toString());
        if (!selectedDate.isEmpty()) {

            myAdapter.filter(selectedDate);
        }
    }
}