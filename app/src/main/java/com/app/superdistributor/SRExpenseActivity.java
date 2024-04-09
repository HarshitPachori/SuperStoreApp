package com.app.superdistributor;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.UUID;

public class SRExpenseActivity extends AppCompatActivity {
    String username;
    DatabaseReference database;
    EditText expenseTypeEt , expenseAmtEt;
    DatePicker expenseDateDp;
    private Button addExpenseBtn , mPickDateButton;;
    boolean dateChanged = false;
    private TextView mShowSelectedDateText;

    String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srexpense);
        username = getIntent().getStringExtra("SRUsername");
        database = FirebaseDatabase.getInstance().getReference();

        expenseTypeEt = findViewById(R.id.expenseTypeET);
        expenseAmtEt = findViewById(R.id.expenseAmountET);
        addExpenseBtn = findViewById(R.id.addExpenseBtn);
        mPickDateButton = findViewById(R.id.dateET);
        mShowSelectedDateText = findViewById(R.id.show_selected_date);

        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker();

        materialDateBuilder.setTitleText("Select a date :");

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
                        dateChanged = true;
                        mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        selectedDate = materialDatePicker.getHeaderText();
                    }
                });

        addExpenseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expenseTypeEt.getText().toString().equals("")) {
                    Toast.makeText(SRExpenseActivity.this, "Please enter type of expense", Toast.LENGTH_SHORT).show();
                } else if (expenseAmtEt.getText().toString().equals("")) {
                    Toast.makeText(SRExpenseActivity.this, "Please select amount", Toast.LENGTH_SHORT).show();
                } else if (!dateChanged) {
                    Toast.makeText(SRExpenseActivity.this, "Please select a date", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> expense = new HashMap<>();
                    String expenseType = expenseTypeEt.getText().toString();
                    String expenseAmt = expenseAmtEt.getText().toString();
String expenseId = UUID.randomUUID().toString();
                    expense.put("Id", expenseId);
                    expense.put("Type", expenseType);
                    expense.put("Amount", expenseAmt);
                    expense.put("Date", selectedDate);
                    expense.put("Status","Pending");
                    expense.put("Reminder","No");
                    expense.put("Name",username);
                    database.child("SRs").child(username)
                            .child("Expenses").child(expenseId)
                            .updateChildren(expense).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    expenseTypeEt.setText("");
                                    expenseAmtEt.setText("");
                                    mShowSelectedDateText.setText("Select a date :");
                                    dateChanged = false;
                                    materialDatePicker.dismiss();
                                    Toast.makeText(SRExpenseActivity.this, "Added Expense", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SRExpenseActivity.this, "Error adding Expense", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
    }
}