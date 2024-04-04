package com.app.superdistributor.RequestService;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.superdistributor.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RaiseServiceConcerActivity extends AppCompatActivity {

    EditText messageEt;
    Button submitBtn;
    DatabaseReference databaseReference;
    String userType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raise_service_concer);

        userType = getIntent().getType();
        messageEt = findViewById(R.id.raiseConcernMessageET);
        submitBtn = findViewById(R.id.submitBtn);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(messageEt.getText().toString().equals("")){
                    Toast.makeText(RaiseServiceConcerActivity.this, "Please add a message", Toast.LENGTH_SHORT).show();
                }
                else {
                    HashMap<String,Object> concern = new HashMap<>();
                    concern.put(databaseReference.push().getKey(),messageEt.getText().toString());
                    databaseReference.child("Grievances").updateChildren(concern).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(RaiseServiceConcerActivity.this, "Your concern has been raised", Toast.LENGTH_SHORT).show();
                            messageEt.setText("");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RaiseServiceConcerActivity.this, "There was an error, please try again later", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}