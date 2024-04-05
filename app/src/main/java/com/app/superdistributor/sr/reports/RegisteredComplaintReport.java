package com.app.superdistributor.sr.reports;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.superdistributor.R;
import com.google.firebase.database.DatabaseReference;

public class RegisteredComplaintReport extends AppCompatActivity {
    RecyclerView recyclerView;
    DatabaseReference database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_complaint_report);

    }
}