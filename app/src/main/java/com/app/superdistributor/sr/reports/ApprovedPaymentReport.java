package com.app.superdistributor.sr.reports;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.DateUtils;
import com.app.superdistributor.R;
import com.app.superdistributor.sr.reports.adapters.ExpenseAdapter;
import com.app.superdistributor.sr.reports.adapters.PaymentReportAdapter;
import com.app.superdistributor.sr.reports.models.PaymentModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApprovedPaymentReport extends AppCompatActivity {

    String SRUsername;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    DatabaseReference databaseReference;
    ArrayList<PaymentModel> list;
    PaymentReportAdapter adapter;
    private Button mPickDateButton;
    String selectedDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approved_payment_report);
        SRUsername = getIntent().getStringExtra("SRUsername");
        recyclerView = findViewById(R.id.paymentReportRV);
        progressBar = findViewById(R.id.paymentReportProgressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();

        Log.d("sruser", SRUsername);
        progressBar.setVisibility(View.VISIBLE);

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
                        Toast.makeText(ApprovedPaymentReport.this, "" + selectedDate, Toast.LENGTH_SHORT).show();

                        filter(selectedDate);
                    }
                });



        databaseReference.child("SRs").child(SRUsername).child("myPayments")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            PaymentModel model = dataSnapshot.getValue(PaymentModel.class);
                            if (!"Pending".equals(model.getStatus())) list.add(model);
                        }

                        adapter = new PaymentReportAdapter(ApprovedPaymentReport.this, list);
                        recyclerView.setAdapter(adapter);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void filter(String date){
        if(!date.isEmpty()){
            date = DateUtils.formatDate(date);
            adapter.filter(date);
        }
    }
}