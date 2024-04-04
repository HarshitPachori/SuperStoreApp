package com.app.superdistributor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.app.superdistributor.RequestService.HandoverDRActivity;
import com.app.superdistributor.RequestService.ServiceReportActivity;
import com.app.superdistributor.RequestService.RaiseServiceConcerActivity;
import com.app.superdistributor.RequestService.RegisterComplaintAcitivty;
import com.app.superdistributor.RequestService.ReplaceByDealerActivity;
import com.app.superdistributor.RequestService.ServiceContactDetailActivity;

public class RequestServiceActivity extends AppCompatActivity {

    String userType, username;
    Button ReplaceByDealerBtn, RegisterComplaintBtn, HandOverBtn, ServiceContactBtn, MaterialPendencyBtn, RaiseConcernBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_service);

        userType = getIntent().getType();
        userType = (userType.equals("viaDealer"))? "Dealers" : "SRs" ;
        username = (userType.equals("viaDealer"))?getIntent().getStringExtra("DealerName"):getIntent().getStringExtra("SRUserame");

        ReplaceByDealerBtn = findViewById(R.id.replacedbydealerbtn);
        RegisterComplaintBtn = findViewById(R.id.registercomplaints);
        HandOverBtn = findViewById(R.id.handoverbtn);
        ServiceContactBtn = findViewById(R.id.servicecontactbtn);
        MaterialPendencyBtn = findViewById(R.id.materialpendencybtn);
        RaiseConcernBtn= findViewById(R.id.raiseconcernbtn);

        ReplaceByDealerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestServiceActivity.this, ReplaceByDealerActivity.class);
                i.setType(userType);
                i.putExtra("Username",username);
                startActivity(i);
            }
        });

        RegisterComplaintBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestServiceActivity.this, RegisterComplaintAcitivty.class);
                i.setType(userType);
                i.putExtra("Username",username);
                startActivity(i);
            }
        });

        HandOverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestServiceActivity.this, HandoverDRActivity.class);
                i.setType(userType);
                i.putExtra("Username",username);
                startActivity(i);
            }
        });

        ServiceContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestServiceActivity.this, ServiceContactDetailActivity.class);
                i.setType(userType);
                i.putExtra("Username",username);
                startActivity(i);
            }
        });

        MaterialPendencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestServiceActivity.this, ServiceReportActivity.class);
                i.setType(userType);
                i.putExtra("Username",username);
                startActivity(i);
            }
        });

        RaiseConcernBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RequestServiceActivity.this, RaiseServiceConcerActivity.class);
                i.setType(userType);
                i.putExtra("Username",username);
                startActivity(i);
            }
        });
    }
}