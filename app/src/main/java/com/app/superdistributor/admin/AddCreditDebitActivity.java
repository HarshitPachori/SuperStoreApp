package com.app.superdistributor.admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddCreditDebitActivity extends AppCompatActivity {

    String Username;
    DatabaseReference database;
    ArrayList<String> nameArrayList = new ArrayList<>();

    EditText ParticularET, DocNoET, AmountET, NoteET;
    Button AddAmountBtn;

    private TextView mShowSelectedDateText;

    private Button mPickDateButton;

    String selectedDate, DealerUsername, TransactionType, DealerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_credit_debit);

        DealerUsername = getIntent().getStringExtra("DealerUsername");
        TransactionType = getIntent().getStringExtra("TransactionType");
        DealerName = getIntent().getStringExtra("DealerName");
        Username = getIntent().getStringExtra("Username");

        ParticularET = findViewById(R.id.particularET);
        //DateET = findViewById(R.id.dateET);
        DocNoET = findViewById(R.id.docnoET);
        AmountET = findViewById(R.id.amountET);
        NoteET = findViewById(R.id.noteET);

        AddAmountBtn = findViewById(R.id.addAmountBtn);
        mShowSelectedDateText = findViewById(R.id.show_selected_date);

        mPickDateButton = findViewById(R.id.dateET);
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
                        mShowSelectedDateText.setText("Selected Date is : " + materialDatePicker.getHeaderText());
                        selectedDate = materialDatePicker.getHeaderText();
                    }
                });

        database = FirebaseDatabase.getInstance().getReference();

        AddAmountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ParticularET.getText().toString().equals(""))
                {
                    Toast.makeText(AddCreditDebitActivity.this, "Please enter particular..", Toast.LENGTH_SHORT).show();
                }
                else if(DocNoET.getText().toString().equals(""))
                {
                    Toast.makeText(AddCreditDebitActivity.this, "Please enter doc no..", Toast.LENGTH_SHORT).show();
                }
                else if(AmountET.getText().toString().equals(""))
                {
                    Toast.makeText(AddCreditDebitActivity.this, "Please enter credit..", Toast.LENGTH_SHORT).show();
                }
                else if(NoteET.getText().toString().equals(""))
                {
                    Toast.makeText(AddCreditDebitActivity.this, "Please enter note..", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Toast.makeText(AddExpenseActivity.this, ""+dropdown.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();

                    database.child("Dealers").child(DealerUsername).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String currentBalance = snapshot.child("CurrentBalance").getValue().toString();

                            //Toast.makeText(AddExpenseActivity.this, "currentBalance"+currentBalance, Toast.LENGTH_SHORT).show();

                            String transactionType = "";
                            int balance=0;
                            if(TransactionType.equals("Credit"))
                            {
                                balance = Integer.parseInt(currentBalance) + Integer.parseInt(AmountET.getText().toString());
                                transactionType = "Credit";
                            }
                            else {
                                balance = Integer.parseInt(currentBalance) - Integer.parseInt(AmountET.getText().toString());
                                transactionType = "Debit";
                            }

                            Date date = new Date();
                            SimpleDateFormat formatTime = new SimpleDateFormat("hh.mm.ss aa");
                            String time = formatTime.format(date);

                            HashMap<String,String> creditData = new HashMap<>();
                            creditData.put("Particular", ParticularET.getText().toString());
                            creditData.put("Date", selectedDate);
                            creditData.put("Time", time);
                            creditData.put("DocNo", DocNoET.getText().toString());
                            creditData.put("Name", DealerName);
                            creditData.put("Amount", AmountET.getText().toString());
                            creditData.put("Balance", String.valueOf(balance));
                            creditData.put("Note", NoteET.getText().toString());
                            database.child("Dealers").child(DealerUsername).child(transactionType).push().setValue(creditData);

                            database.child("Dealers").child(DealerUsername).child("CurrentBalance").
                                    setValue(String.valueOf(balance)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(AddCreditDebitActivity.this, "Amount Added", Toast.LENGTH_SHORT).show();
                                            ParticularET.setText("");
                                            mShowSelectedDateText.setText("Selected Date is : ");
                                            DocNoET.setText("");
                                            AmountET.setText("");
                                            NoteET.setText("");
                                            Intent i = new Intent(AddCreditDebitActivity.this, AddDebitCreditActivity.class);
                                            i.putExtra("Username",Username);
                                            startActivity(i);
                                        }
                                    });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });


    }
}