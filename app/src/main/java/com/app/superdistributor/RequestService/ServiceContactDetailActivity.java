package com.app.superdistributor.RequestService;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.app.superdistributor.R;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

public class ServiceContactDetailActivity extends AppCompatActivity {

    private ListView ContactDetailsDataLV, EmailDetailsDataLV;
    ArrayList<String> contactDetailsDataList, emailDetailsDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_contact_detail);


        ContactDetailsDataLV = findViewById(R.id.contactDetailsListView);
        contactDetailsDataList = new ArrayList<String>();

        EmailDetailsDataLV = findViewById(R.id.emailDetailsListView);
        emailDetailsDataList = new ArrayList<String>();

        contactDetailsDataList.add("TITAN SOLOR\nComplaint Number - 919-310-9310");
        contactDetailsDataList.add("EXIDE AUTOMOTIVE COMPLAINT\nComplaint Number - 18001035454");
        contactDetailsDataList.add("EXIDE SOLOR & INDUSTRIAL\nComplaint Number - 18002045758");

        contactDetailsDataList.add("POWERZONE\nContact Number - 9999999999");
        contactDetailsDataList.add("WAREE CUSTOMER CARE\nContact Number - 9999999999");
        contactDetailsDataList.add("WAACAB CUSTOMER CARE\nComplaint Number - 9999999999");


        emailDetailsDataList.add("TITAN SOLAR\ntitansolar.alg@gmail.com");
        emailDetailsDataList.add("TATA POWER SOLUTIONS\ntatapowersolutions@gmail.com");
        emailDetailsDataList.add("EXCIDE\nemail@gmail.com");
        emailDetailsDataList.add("WAAREE\ncustomercare@waaree.com");

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(ServiceContactDetailActivity.this, android.R.layout.simple_list_item_1, contactDetailsDataList);
        adapter.notifyDataSetChanged();
        ContactDetailsDataLV.setAdapter(adapter);

        final ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(ServiceContactDetailActivity.this, android.R.layout.simple_list_item_1, emailDetailsDataList);
        adapter2.notifyDataSetChanged();
        EmailDetailsDataLV.setAdapter(adapter2);
    }
}